package starvationevasion.server;

import starvationevasion.common.WorldData;
import starvationevasion.server.model.*;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;


class JavaSocketClient
{
  private Socket clientSocket;

  private DataInputStream reader;
  private DataOutputStream writer;

  // time of server start
  private long startNanoSec;
  private Scanner keyboard;
  // writes to user
  private ClientListener listener;
  private volatile boolean isRunning = true;


  private JavaSocketClient (String host, int portNumber)
  {

    keyboard = new Scanner(System.in);

    openConnection(host, portNumber);
    listener = new ClientListener();
    System.out.println("JavaClient: Starting listener = : " + listener);
    listener.start();

    listenToUserRequests();

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

        data.setType("user");

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

        System.out.println("Received a Response object.");
        if (response.getData().get("data") instanceof User)
        {
          System.out.println("Response.data = User object.");

          System.out.println(((User) response.getData().get("data")).getRegion());
        }
        else if (response.getData().get("data") instanceof WorldData)
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
      catch(IOException e)
      {
        e.printStackTrace();
      }
      catch(ClassNotFoundException e)
      {
        e.printStackTrace();
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
    new JavaSocketClient(host, port);

  }


}

