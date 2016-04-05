package starvationevasion.server;

import starvationevasion.common.WorldData;
import starvationevasion.server.model.*;

import java.io.*;
import java.net.Socket;


class AI
{
  private Socket clientSocket;

  private DataInputStream reader;
  private DataOutputStream writer;

  private User u;

  private State state;

  private WorldData worldData;

  // time of server start
  private long startNanoSec;

  private StreamListener listener;
  private volatile boolean isRunning = true;


  private AI (String host, int portNumber)
  {

    openConnection(host, portNumber);
    listener = new StreamListener();
    System.out.println("AI: Starting listener = : " + listener);
    listener.start();

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
        Thread.sleep(1000);

        requestGameState();

        requestTime();

        login();

      }
      catch(InterruptedException e)
      {
        e.printStackTrace();
      }
    }
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

        System.out.println("Received a Response object.");
        if (response.getPayload().get("data") instanceof User)
        {
          System.out.println("Response.data = User object.");

          System.out.println(((User) response.getPayload().get("data")).getRegion());
        }
        else if (response.getPayload().get("data") instanceof WorldData)
        {
          System.out.println("Response.data = WorldData object.");
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

  private void send(Request request)
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


  private void requestGameState()
  {
    if (state == null)
    {
      send(new Request((double) System.currentTimeMillis(), Endpoint.GAME_STATE));
    }
  }

  private void requestTime()
  {
    if (startNanoSec == 0)
    {
      send(new Request((double) System.currentTimeMillis(), Endpoint.SERVER_UPTIME));
    }
  }


  private void login()
  {
    if (u == null)
    {
      Request loginRequest = new Request(startNanoSec, Endpoint.LOGIN);
      Payload data = new Payload();

      data.put("username", "admin");
      data.put("password", "admin");

      loginRequest.setData(data);
      send(loginRequest);
    }
  }


}

