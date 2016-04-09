package starvationevasion.ai;


import starvationevasion.ai.commands.*;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.PolicyCard;
import starvationevasion.common.WorldData;
import starvationevasion.server.model.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class AI
{
  private Socket clientSocket;

  private DataInputStream reader;
  private DataOutputStream writer;

  private User u;
  private ArrayList<User> users = new ArrayList<>();

  private State state = null;

  private ArrayList<WorldData> worldData;

  private ArrayList<PolicyCard> ballot;

  // time of server start
  private double startNanoSec = 0;

  private StreamListener listener;
  private volatile boolean isRunning = true;

  private Stack<Command> commands = new Stack<>();


  private AI (String host, int portNumber)
  {

    openConnection(host, portNumber);
    listener = new StreamListener();
    System.out.println("AI: Starting listener = : " + listener);
    listener.start();
    commands.add(new GameState(this));
    commands.add(new Uptime(this));
    commands.add(new Login(this));


    listenToUserRequests();

  }

  private boolean openConnection (String host, int portNumber)
  {

    try
    {
      clientSocket = new Socket(host, portNumber);
    }
    catch(Exception e)
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
      writer.write("JavaClient\n".getBytes());
      writer.flush();
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }


    try
    {
      reader = new DataInputStream(clientSocket.getInputStream());
    }
    catch(IOException e)
    {
      e.printStackTrace();
      return false;
    }
    isRunning = true;

    return true;

  }

  private void listenToUserRequests ()
  {
    while(isRunning)
    {
      try
      {
        // if commands is empty check again
        if (commands.size() == 0)
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
      }
      catch(InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }

  public ArrayList<WorldData> getWorldData ()
  {
    return worldData;
  }

  public double getStartNanoSec ()
  {
    return startNanoSec;
  }

  public State getState ()
  {
    return state;
  }

  public User getUser ()
  {
    return u;
  }

  public Stack<Command> getCommands ()
  {
    return commands;
  }

  public ArrayList<PolicyCard> getBallot ()
  {
    return ballot;
  }

  public ArrayList<User> getUsers ()
  {
    return users;
  }

  /**
   * StreamListener
   *
   * Handles reading stream from socket. The data is then outputted
   * to the console for user.
   */
  private class StreamListener extends Thread
  {

    public void run ()
    {

      while(isRunning)
      {
        read();
      }
    }

    private void read ()
    {
      try
      {
        Response response = readObject();

        if (response.getType().equals(Type.AUTH_SUCCESS))
        {
          u = (User) response.getPayload().getData();

          send(RequestFactory.chat(startNanoSec,
                                   "ALL",
                                   "Hi, I am " + u.getUsername() + ". I'll be playing using (crappy) AI.",
                                   null));

          System.out.println("Hi, I am " + u.getUsername() + ". I'll be playing using (crappy) AI.");
        }
        else if (response.getType().equals(Type.AUTH_ERROR))
        {
          System.out.println("Error logging in");
          isRunning = false;
        }
        else if (response.getType().equals(Type.USER))
        {
          u = (User) response.getPayload().getData();
        }
        else if (response.getType().equals(Type.TIME))
        {
          startNanoSec = (double) response.getPayload().get("data");
        }
        else if (response.getType().equals(Type.WORLD_DATA_LIST))
        {
          // System.out.println("Getting a list of WorldData's");
          worldData = (ArrayList<WorldData>) response.getPayload().getData();
        }
        else if (response.getType().equals(Type.USERS_LOGGED_IN_LIST))
        {
          // System.out.println("Getting a list of ready users");
          users = (ArrayList<User>) response.getPayload().getData();
        }
        else if (response.getType().equals(Type.WORLD_DATA))
        {
          // System.out.println("Getting a list of WorldData's");
          worldData.add((WorldData) response.getPayload().getData());
        }
        else if (response.getType().equals(Type.GAME_STATE))
        {
          // System.out.println("Getting state of server");
          state = (starvationevasion.server.model.State) response.getPayload().getData();
          if (state == starvationevasion.server.model.State.VOTING)
          {
            AI.this.commands.add(new Vote(AI.this));
          }
          else if (state == starvationevasion.server.model.State.DRAFTING)
          {
            AI.this.commands.add(new Draft(AI.this));
          }
          else if (state == starvationevasion.server.model.State.DRAWING)
          {
            // AI.this.commands.add(new Draft(AI.this));
          }
        }
        else if (response.getType().equals(Type.DRAFTED) || response.getType().equals(Type.DRAFTED_INTO_VOTE))
        {
          String msg = response.getPayload().getMessage();
          if (msg == null)
          {
            // PolicyCard card = (PolicyCard) response.getPayload().getData();
            // getUser().getHand().remove(card.getCardType());
          }
          else
          {
            System.out.println(msg);
          }
        }
        else if (response.getType().equals(Type.VOTE_BALLOT))
        {
          ballot = (ArrayList<PolicyCard>) response.getPayload().getData();
        }

      }
      catch(EOFException e)
      {
        isRunning = false;
        System.out.println("Lost server, press enter to shutdown.");
        return;
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  private Response readObject () throws Exception
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

    byte[] object = new byte[size];

    reader.readFully(object);

    ByteArrayInputStream in = new ByteArrayInputStream(object);
    ObjectInputStream is = new ObjectInputStream(in);

    return (Response) is.readObject();
  }

  public void send (Request request)
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(request);
      oos.close();


      byte[] bytes = baos.toByteArray();

      writer.writeInt(bytes.length);
      writer.write(bytes);
      writer.flush();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }


  public static void main (String[] args)
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
      System.exit(0);
    }
    new AI(host, port);

  }

}

