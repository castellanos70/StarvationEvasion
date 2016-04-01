package starvationevasion.server;

import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.server.model.Request;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
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


  public JavaSocketClient (String host, int portNumber)
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
      writer = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
      writer.writeBytes("JavaClient\n");
      writer.flush();
    }
    catch(IOException e)
    {
      e.printStackTrace();
      return false;
    }
    try
    {
      reader = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
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

        System.out.println("logging in");
        Request f = new Request(startNanoSec, Endpoint.LOGIN);

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
          System.out.println(bytes.length);
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
        // need to fix this so that objects can be read in....
        int mc = reader.read();
        while(mc > -1)
        {
          System.out.println(mc);
          mc = reader.read();
        }
        Object msg = "";

        int sz = reader.readInt();
        if (sz > 0)
        {
          byte[] obj = new byte[sz];
          reader.readFully(obj);
          System.out.println("read");
        }
        else
        {
          System.out.println("!read");
        }

      }
      catch(EOFException e)
      {
        e.printStackTrace();
        isRunning = false;
        System.out.println("Lost server, press enter to shutdown.");
        return;
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }
    }
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

