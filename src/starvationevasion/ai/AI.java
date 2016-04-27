package starvationevasion.ai;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import starvationevasion.ai.commands.*;
import starvationevasion.common.Constant;
import starvationevasion.common.Util;
import starvationevasion.common.WorldData;
import starvationevasion.common.policies.PolicyCard;
import starvationevasion.server.model.*;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

import starvationevasion.ai.commands.Command;
import starvationevasion.ai.commands.Draft;
import starvationevasion.ai.commands.GameState;
import starvationevasion.ai.commands.Login;
import starvationevasion.ai.commands.Uptime;
import starvationevasion.ai.commands.Vote;
import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.common.RegionData;
import starvationevasion.common.SpecialEventData;
import starvationevasion.common.Util;
import starvationevasion.common.WorldData;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.RequestFactory;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.State;
import starvationevasion.server.model.Type;
import starvationevasion.server.model.User;

public class AI
{
  private Socket clientSocket;

  private DataInputStream reader;
  private DataOutputStream writer;

  private User u;
  private ArrayList<User> users = new ArrayList<>();

  private State state = null;

  public ArrayList<WorldData> worldData;

  private List<PolicyCard> ballot;

  // time of server start
  private double startNanoSec = 0;

  private StreamListener listener;
  private volatile boolean isRunning = true;
  public int numTurns = 0;
  private Stack<Command> commands = new Stack<>();

  /*
   * Factors used as map keys to keep track of and store events that are
   * happening in the world and in the region that this AI represents.
   */
  public enum WorldFactors
  {
    SEALEVEL, REVENUEBALANCE, POPULATION, UNDERNOURISHED, HDI, FOODPRODUCED, FOODINCOME, FOODIMPORTED, FOODEXPORTED, ETHANOLTAXCREDIT, FOODPRICE
  }

  // This map is used to store information about the world and region that will
  // be used in selecting
  // cards to play on each turn.
  public Map<WorldFactors, ArrayList<Object[]>> factorMap = new EnumMap<WorldFactors, ArrayList<Object[]>>(
      WorldFactors.class);

  // The AI has a copy of the list of special events, if any occurred during the
  // last turn.
  public ArrayList<SpecialEventData> eventList = new ArrayList<>();

  // List of pairs of cards played in previous hands.
  public ArrayList<ArrayList<PolicyCard>> draftedCards = new ArrayList<>();

  // The region that this AI represents.
  String region = "";

  private SecretKey serverKey;
  private Cipher aesCipher;
  private KeyPair rsaKey;

  private AI(String host, int portNumber)
  {
    setupSecurity();
    while (!openConnection(host, portNumber))
    {
    }
    listener = new StreamListener();
    System.out.println("AI: Starting listener = : " + listener);
    listener.start();
    commands.add(new GameState(this));
    commands.add(new Uptime(this));
    commands.add(new Login(this));
    listenToUserRequests();

  }

  /**
   * Jeffrey McCall Create the EnumMap that will be passed to Draft.java which
   * will aid the AI in making decisions about which card to draft.
   */
  private void createEnumMap()
  {
    for (int i = 0; i < WorldFactors.values().length; i++)
    {
      factorMap.put(WorldFactors.values()[i], new ArrayList<Object[]>());
    }
  }

  /**
   * Jeffrey McCall Collect all of the important data about the world and region
   * that has been updated since the last turn. Put this data in the factorMap
   * which is used in decision making by the AI regarding which cards it wants
   * to draft.
   */
  private void aggregateData()
  {
    region = u.region.name();
    factorMap.clear();
    createEnumMap();
    for(int i=0;i<worldData.size();i++)
    {
      Double[] seaLevel =
        { worldData.get(i).seaLevel };
      factorMap.get(WorldFactors.SEALEVEL).add(seaLevel);
      eventList.clear();
      if (worldData.get(i).eventList.size() > 0)
      {
        for (SpecialEventData event : worldData.get(numTurns).eventList)
        {
          eventList.add(event);
        }
      }
      RegionData thisRegion = null;
      for (RegionData data : worldData.get(i).regionData)
      {
        if (data.region.name().equals(u.region.name()))
        {
          thisRegion = data;
        }
      }
      Integer[] revenueBalance =
      { thisRegion.revenueBalance };
      factorMap.get(WorldFactors.REVENUEBALANCE).add(revenueBalance);
      Integer[] population =
      { thisRegion.population };
      factorMap.get(WorldFactors.POPULATION).add(population);
      Double[] undernourished =
      { thisRegion.undernourished };
      factorMap.get(WorldFactors.UNDERNOURISHED).add(undernourished);
      Double[] hdi =
      { thisRegion.humanDevelopmentIndex };
      factorMap.get(WorldFactors.HDI).add(hdi);
      Long[] foodProduced = new Long[EnumFood.SIZE];
      for (int h = 0; h < EnumFood.SIZE; h++)
      {
        foodProduced[h] = thisRegion.foodProduced[h];
      }
      // Food produced over the last year in metric tons.
      factorMap.get(WorldFactors.FOODPRODUCED).add(foodProduced);
      Integer[] foodIncome = new Integer[EnumFood.SIZE];
      for (int h = 0; h < EnumFood.SIZE; h++)
      {
        foodIncome[h] = thisRegion.foodIncome[h];
      }
      factorMap.get(WorldFactors.FOODINCOME).add(foodIncome);
      Long[] foodImported = new Long[EnumFood.SIZE];
      for (int h = 0; h < EnumFood.SIZE; h++)
      {
        foodImported[h] = thisRegion.foodImported[h];
      }
      factorMap.get(WorldFactors.FOODIMPORTED).add(foodImported);
      Long[] foodExported = new Long[EnumFood.SIZE];
      for (int h = 0; h < EnumFood.SIZE; h++)
      {
        foodExported[h] = thisRegion.foodExported[h];
      }
      factorMap.get(WorldFactors.FOODEXPORTED).add(foodExported);
      Integer[] ethanolCredit =
      { thisRegion.ethanolProducerTaxCredit };
      factorMap.get(WorldFactors.ETHANOLTAXCREDIT).add(ethanolCredit);
      Integer[] foodPrice = new Integer[EnumFood.SIZE];
      for (int h = 0; h < EnumFood.SIZE; h++)
      {
        foodPrice[h] = worldData.get(i).foodPrice[h];
      }
      factorMap.get(WorldFactors.FOODPRICE).add(foodPrice);
    }
  }

  private boolean openConnection(String host, int portNumber)
  {

    try
    {
      clientSocket = new Socket(host, portNumber);
    } catch (Exception e)
    {
      System.err.println("Client Error: Could not open connection to " + host
          + " on port " + portNumber);
      e.printStackTrace();
      isRunning = false;
      return false;
    }

    try
    {
      writer = new DataOutputStream(clientSocket.getOutputStream());
    } catch (Exception e)
    {
      e.printStackTrace();
      return false;
    }

    try
    {
      reader = new DataInputStream(clientSocket.getInputStream());
    } catch (IOException e)
    {
      e.printStackTrace();
      return false;
    }
    isRunning = true;

    Util.startServerHandshake(clientSocket, rsaKey, DataType.POJO);

    return true;

  }

  private void listenToUserRequests()
  {
    while (isRunning)
    {
      try
      {
        // if commands is empty check again
        if (commands.size() == 0 || serverKey == null)
        {
          continue;
        }

        // take off the top of the stack
        Command c = commands.peek();

        boolean runAgain = c.run();

        // if it does not need to run again pop
        if (!runAgain)
        {
          commands.pop();
        }
        // wait a little
        Thread.sleep(1000);
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }

  public ArrayList<WorldData> getWorldData()
  {
    return worldData;
  }

  public double getStartNanoSec()
  {
    return startNanoSec;
  }

  public State getState()
  {
    return state;
  }

  public User getUser()
  {
    return u;
  }

  public Stack<Command> getCommands()
  {
    return commands;
  }

  public List<PolicyCard> getBallot()
  {
    return ballot;
  }

  public ArrayList<User> getUsers()
  {
    return users;
  }

  /**
   * StreamListener
   *
   * Handles reading stream from socket. The data is then outputted to the
   * console for user.
   */
  private class StreamListener extends Thread
  {

    public void run()
    {
      serverKey = Util.endServerHandshake(clientSocket, rsaKey);
      while (isRunning)
      {
        read();
      }
    }

    @SuppressWarnings("unchecked")
    private void read()
    {
      try
      {
        Response response = readObject();

        if (response.getType().equals(Type.AUTH_SUCCESS))
        {
          u = (User) response.getPayload().getData();

          send(new RequestFactory().chat(startNanoSec, "ALL", "Hi, I am "
              + u.getUsername() + ". I'll be playing using (crappy) AI.",
              null));

          System.out.println("Hi, I am " + u.getUsername()
              + ". I'll be playing using (crappy) AI.");
        } else if (response.getType().equals(Type.AUTH_ERROR))
        {
          System.out.println("Error logging in");
          isRunning = false;
        } else if (response.getType().equals(Type.USER))
        {
          if (u != null && u.getUsername()
              .equals(((User) response.getPayload().getData()).getUsername()))
          {
            u = (User) response.getPayload().getData();
          }

        } else if (response.getType().equals(Type.TIME))
        {
          startNanoSec = (double) response.getPayload().get("data");
        } else if (response.getType().equals(Type.WORLD_DATA_LIST))
        {
          // System.out.println("Getting a list of WorldData's");
          worldData = (ArrayList<WorldData>) response.getPayload().getData();
        } else if (response.getType().equals(Type.USERS_LOGGED_IN_LIST))
        {
          // System.out.println("Getting a list of ready users");
          users = (ArrayList<User>) response.getPayload().getData();
        } else if (response.getType().equals(Type.WORLD_DATA))
        {
          worldData.add((WorldData) response.getPayload().getData());
        } else if (response.getType().equals(Type.GAME_STATE))
        {
          // System.out.println("Getting state of server");
          state = (starvationevasion.server.model.State) response.getPayload()
              .getData();
          if (state == starvationevasion.server.model.State.VOTING)
          {
            AI.this.commands.add(new Vote(AI.this));
          } else if (state == starvationevasion.server.model.State.DRAFTING)
          {
            aggregateData();
            draftedCards.add(new ArrayList<PolicyCard>());
            Draft newDraft = new Draft(AI.this,region);
            AI.this.commands.add(newDraft);
            numTurns++;
          } else if (state == starvationevasion.server.model.State.DRAWING)
          {
            // AI.this.commands.add(new Draft(AI.this));
            commands.clear();
          }
        } else if (response.getType().equals(Type.DRAFTED)
            || response.getType().equals(Type.DRAFTED_INTO_VOTE))
        {
          String msg = response.getPayload().getMessage();
          if (msg == null)
          {
            // PolicyCard card = (PolicyCard) response.getPayload().getData();
            // getUser().getHand().remove(card.getCardType());
          } else
          {
            System.out.println(msg);
          }
        } else if (response.getType().equals(Type.VOTE_BALLOT))
        {
          ballot = (List<PolicyCard>) response.getPayload().getData();
        }

      } catch (EOFException e)
      {
        isRunning = false;
        System.out.println("Lost server, press enter to shutdown.");

      } catch (SocketException e)
      {
        isRunning = false;
        System.out.println("Lost server");
        System.out.println("Shutting down...");

      } catch (NoSuchAlgorithmException e)
      {
        isRunning = false;
        System.out.println("Will not be able to decrypt");
        System.exit(1);
      } catch (IOException e)
      {
        isRunning = false;
        System.out.println("Error reading");
      } catch (InvalidKeyException e)
      {
        System.out.println("Key is not valid");
      } catch (ClassNotFoundException e)
      {
        isRunning = false;
        System.out.println("Class not found!");
      }
    }
  }

 private Response readObject () throws IOException,
                                        ClassNotFoundException,
                                        InvalidKeyException,
                                        NoSuchAlgorithmException
  {
    int ch1 = reader.read();
    int ch2 = reader.read();
    int ch3 = reader.read();
    int ch4 = reader.read();

    if ((ch1 | ch2 | ch3 | ch4) < 0)
    {
      throw new EOFException();
    }
    int size = ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));

    byte[] encObject = new byte[size];

    reader.readFully(encObject);
    ByteArrayInputStream in = new ByteArrayInputStream(encObject);
    ObjectInputStream is = new ObjectInputStream(in);
    SealedObject sealedObject = (SealedObject) is.readObject();
    Response response = (Response) sealedObject.getObject(serverKey);
    is.close();
    in.close();

    return response;
  }

  public void send(Request request)
  {
    try
    {
      aesCipher.init(Cipher.ENCRYPT_MODE, serverKey);
      SealedObject sealedObject = new SealedObject(request, aesCipher);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(sealedObject);
      oos.close();

      writer.flush();
      byte[] bytes = baos.toByteArray();
      writer.writeInt(bytes.length);
      writer.write(bytes);
      writer.flush();
      baos.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    } catch (InvalidKeyException e)
    {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e)
    {
      e.printStackTrace();
    }
  }

  public static void main(String[] args)
  {

    String host = null;
    int port = 0;

    try
    {
      host = args[0];
      port = Integer.parseInt(args[1]);
      if (port < 1)
      {
        throw new Exception();
      }
    } 
    catch(Exception e)
    {
      System.out.println("Usage: hostname port");
      System.exit(0);
    }
    new AI(host, port);

  }

  private void setupSecurity()
  {
    try
    {
      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

      keyGen.initialize(1024);
      rsaKey = keyGen.generateKeyPair();
      aesCipher = Cipher.getInstance(Constant.DATA_ALGORITHM);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
