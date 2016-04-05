package starvationevasion.client;

import starvationevasion.client.Logic.ChatManager;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;
import starvationevasion.common.WorldData;
import starvationevasion.server.model.*;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;


public class Client
{
  private Socket clientSocket;

  private DataInputStream reader;
  private DataOutputStream writer;

  // time of server start
  private long startNanoSec;
  private Scanner keyboard;
  // writes to user
  private ClientListener listener;

  private EnumRegion region;
  private volatile boolean isRunning = true;
  private ArrayList<User> allUsers=new ArrayList<>();
  private ChatManager chatManager;
  private ArrayList<EnumPolicy> hand;
  private User user;
  private String userName;
  private boolean loginSuccessful;
  private boolean recivedMessege=false;
  public Client (String host, int portNumber)
  {
    chatManager=new ChatManager(this);
    keyboard = new Scanner(System.in);

    openConnection(host, portNumber);
    listener = new ClientListener();
    System.out.println("JavaClient: Starting listener = : " + listener);
    listener.start();

   // listenToUserRequests();

  }
  public ChatManager getChatManager(){return  chatManager;}
  //TODO
  public EnumRegion getRegion(){return region;}
  public boolean loginToServer(String userName,String pass)
  {
    this.userName=userName;
    // Create a request to login
    Request f = new Request(startNanoSec, Endpoint.LOGIN);
    // Create a payload (this is the class that stores Sendable information)
    Payload data = new Payload();

    data.putData("user");

    data.put("username", userName);
    data.put("password", pass);

    f.setData(data);
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(f);
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
    createHand();
    getGameState();
    ready();

//    while(!recivedMessege){
//      System.out.println(recivedMessege);
//      //Wait tell a message has been recieved
//      }
    return true;
  }
  public void createHand()
  {
    Request f = new Request(startNanoSec, Endpoint.HAND_CREATE);
    // Create a payload (this is the class that stores Sendable information)
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(f);
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
    readHand();
  }
  public void readHand()
  {
    Request f = new Request(startNanoSec, Endpoint.HAND_READ);
    // Create a payload (this is the class that stores Sendable information)
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(f);
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
  public void ready()
  {
    Request f = new Request(startNanoSec, Endpoint.READY);
    // Create a payload (this is the class that stores Sendable information)
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(f);
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
  public void getUsers()
  {
    Request f = new Request(startNanoSec, Endpoint.USERS);
    // Create a payload (this is the class that stores Sendable information)
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(f);
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
  public ArrayList<EnumPolicy> getHand()
  {
    return hand;
  }
  public void getGameState()
  {
    Request f = new Request(startNanoSec, Endpoint.GAME_STATE);
    // Create a payload (this is the class that stores Sendable information)
    try
    {

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(f);
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
  public void drawCard()
  {
    Request f = new Request(startNanoSec, Endpoint.DRAW_CARD);
    // Create a payload (this is the class that stores Sendable information)
    try
    {

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(f);
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

  public void createUser(String user,String pass)
  {
    // Create a request to login
    Request f = new Request(startNanoSec, Endpoint.USER_CREATE);
    // Create a payload (this is the class that stores Sendable information)
    Payload data = new Payload();

    data.putData("user");

    data.put("username", user);
    data.put("password", pass);

    f.setData(data);
    try
    {

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(f);
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
  public void draftCard(PolicyCard card)
  {
    Request f = new Request(startNanoSec, Endpoint.DRAFT_CARD);
    Payload data = new Payload();

    data.putData("user");
    data.put("card", card);
    // Create a payload (this is the class that stores Sendable information)
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(f);
      oos.close();
      byte[] bytes = baos.toByteArray();
      writer.writeInt(bytes.length);
      writer.write(bytes);
      writer.flush();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  public void discardCard(PolicyCard card)
  {
    Request f = new Request(startNanoSec, Endpoint.DELETE_CARD);
    Payload data = new Payload();

    data.putData("user");
    data.put("card", card);
    // Create a payload (this is the class that stores Sendable information)
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(f);
      oos.close();
      byte[] bytes = baos.toByteArray();
      writer.writeInt(bytes.length);
      writer.write(bytes);
      writer.flush();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  public void sendChatMessage(String message,EnumRegion toRegion)
  {
    // Create a request to login
    Request f = new Request(startNanoSec, Endpoint.CHAT);
    // Create a payload (this is the class that stores Sendable information)
    Payload data = new Payload();
    data.putData("chat");
    data.put("to-region",toRegion.name());
    data.put("card", EnumPolicy.Clean_River_Incentive);
    data.put("text",message);
    f.setData(data);
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(f);
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
  public boolean writeToServer(String message){


//    currentOutput="";
//    write.println(System.nanoTime() + " " + message);
//
//    System.out.println((System.nanoTime() + " " + message));
//    //System.out.println(currentOutput);
//    while(message.contains("login"))
//    {
//      if (currentOutput.contains("SUCCESS"))return true;
//      if(currentOutput.contains("FAIL"))return false;
//    }

    return false;
  }
  private boolean openConnection (String host, int portNumber)
  {

    try
    {
      clientSocket = new Socket(host, portNumber);
    }
    catch(UnknownHostException e)
    {

      isRunning = false;
      return false;
    }
    catch(IOException e)
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
    catch(IOException e)
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
  private void updateUser(User user)
  {
    this.user = user;
    region = user.getRegion();

  }
  public void closeAll()
  {
    isRunning=false;
    try
    {
      writer.close();
      reader.close();
      clientSocket.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }

  }
  private void listenToUserRequests ()
  {
    while(isRunning)
    {

      String cmd = keyboard.nextLine();


      if (cmd == null || cmd.length() < 1)
      {
        continue;
      }

      if (cmd.charAt(0) == 'q')
      {
        isRunning = false;
      }
      if (cmd.equals("login"))
      {
        // Create a request to login
        Request f = new Request(startNanoSec, Endpoint.LOGIN);
        // Create a payload (this is the class that stores Sendable information)
        Payload data = new Payload();

        data.putData("user");

        data.put("username", "admin");
        data.put("password", "admin");

        f.setData(data);

        try
        {

          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ObjectOutputStream oos = new ObjectOutputStream(baos);
          oos.writeObject(f);
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
    }
  }


  /**
   * ClientListener
   *
   * Handles reading stream from socket. The data is then outputted
   * to the console for user.
   */
  private class ClientListener extends Thread
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
        System.out.println(response.getPayload());
        if (response.getPayload().get("data") instanceof User)
        {
          if(response.getPayload().get("message")!=null&&response.getPayload().get("message").equals("SUCCESS"))
          {
            loginSuccessful=true;
            recivedMessege=true;

          }else if(response.getPayload().get("message")=="FAIL")
          {
            loginSuccessful=false;
            recivedMessege=true;
          }
          System.out.println("Response.data = User object.");
          updateUser((User)response.getPayload().get("data"));

        }

        else if (response.getPayload().get("data") instanceof WorldData)
        {
          System.out.println("Response.data = WorldData object.");
        }
        else if(response.getPayload().get("data")instanceof ArrayList)
        {
          hand=((ArrayList)response.getPayload().get("data"));
        }
        else if(response.getPayload().get("data")instanceof String)
        {
          chatManager.sendChatToClient((String)response.getPayload().get("text"));
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

  private Response readObject() throws Exception
  {
    int ch1 = reader.read();
    int ch2 = reader.read();
    int ch3 = reader.read();
    int ch4 = reader.read();

    if ((ch1 | ch2 | ch3 | ch4) < 0)
    {
      throw new EOFException();
    }
    int size  = ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));

    byte[] object = new byte[size];

    reader.readFully(object);

    ByteArrayInputStream in = new ByteArrayInputStream(object);
    ObjectInputStream is = new ObjectInputStream(in);

    return (Response) is.readObject();
  }

  private String timeDiff ()
  {
    long nanoSecDiff = System.nanoTime() - startNanoSec;
    double secDiff = (double) nanoSecDiff / 1000000000.0;
    return String.format("%.3f", secDiff);
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
    new Client(host, port);

  }


}

