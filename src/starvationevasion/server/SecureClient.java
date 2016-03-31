package starvationevasion.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Scanner;


class SecureClient extends Client
{
  private Socket clientSocket;

  // write to socket
  private PrintWriter write;
  // read the socket
  private BufferedReader reader;

  // time of server start
  private long startNanoSec;
  private volatile long lastInventoryUpdate;
  private Scanner keyboard;
  // writes to user
  private ClientListener listener;
  private volatile boolean isRunning = true;

  private String serverKey = "";


  public SecureClient (String host, int portNumber)
  {
    super(host, portNumber);
    try
    {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
    }
    catch(NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }
    catch(NoSuchProviderException e)
    {
      e.printStackTrace();
    }
  }

  private boolean openConnection(String host, int portNumber)
  {

    try
    {
      clientSocket = new Socket(host, portNumber);
    }
    catch (UnknownHostException e)
    {
      System.err.println("Client Error: Unknown Host " + host);
      e.printStackTrace();
      isRunning = false;
      return false;
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open connection to " + host
                                 + " on port " + portNumber);
      e.printStackTrace();
      isRunning = false;
      return false;
    }

    try
    {
      write = new PrintWriter(clientSocket.getOutputStream(), true);
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open output stream");
      e.printStackTrace();
      return false;
    }
    try
    {
      reader = new BufferedReader(new InputStreamReader(
              clientSocket.getInputStream()));
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open input stream");
      e.printStackTrace();
      return false;
    }
    isRunning = true;
    // write.println("Sec-Socket-Key:" + );
    return true;

  }


  /**
   * ClientListener
   *
   * Handles reading stream from socket. The data is then outputted
   * to the console for user.
   */
  class ClientListener extends Thread
  {

    public void run()
    {
      while (isRunning)
      {
        read();
      }
    }

    private void read()
    {
      try
      {
        String msg = reader.readLine();
        if (msg == null)
        {
          System.out.println("Lost server, press enter to shutdown.");
          isRunning = false;
          return;
        }

        if (msg.contains("Key: "))
        {
          serverKey = msg.replace("Key: ", "");
          return;
        }
        System.out.println(msg);

      }
      catch (IOException e)
      {
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
  }

}
