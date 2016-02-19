package starvationevasion.server;

/**
 * @author Javier Chavez
 */


import starvationevasion.server.handlers.Handler;
import starvationevasion.server.model.Data;
import starvationevasion.sim.Simulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ServerWorker
 */
public class Worker extends Thread
{

  private Socket client;
  private PrintWriter clientWriter;
  private BufferedReader clientReader;
  private boolean isRunning = true;
  private final Server server;
  private final Simulator simulator;
  private Handler handler;
  private long serverStartTime;
  private boolean sent = false;

  public Worker (Socket client, Server server)
  {
    this.client = client;
    this.simulator = server.getSimulator();
    this.server = server;
    this.handler = new Handler(server, this);


    try
    {
      clientWriter = new PrintWriter(client.getOutputStream(), true);
    }
    catch(IOException e)
    {
      System.err.println("Server Worker: Could not open output stream");
      e.printStackTrace();
    }

    try
    {
      clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }
    catch(IOException e)
    {
      System.err.println("Server Worker: Could not open input stream");
      e.printStackTrace();
    }
  }

  /**
   * Check to see if worker is holding a connection to client.
   *
   * @return true if worker is running
   */
  public boolean isRunning ()
  {
    return isRunning;
  }


  /**
   * Send message to client.
   *
   * @param msg string containing message to be sent.
   */
  public void send (String msg)
  {
    System.out.println("ServerWorker.send(" + msg + ")");

    clientWriter.println(msg);

  }


  public void run ()
  {

    while(isRunning)
    {
      try
      {
        String s = clientReader.readLine();

        if (s == null || clientReader == null)
        {
          // lost the client
          client.close();
          isRunning = false;
          break;
        }

        System.out.println(s);

        Data r = new Data();
//
//        if (r.getRequest() == ActionType.QUIT)
//        {
//          // client gracefully closed.
//          client.close();
//          isRunning = false;
//          break;
//        }

        // handler.handle(r);

      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  public void setServerStartTime (long serverStartTime)
  {
    this.serverStartTime = serverStartTime;
  }


  public BufferedReader getClientReader ()
  {
    return clientReader;
  }

  public PrintWriter getClientWriter ()
  {
    return clientWriter;
  }


  private String decodeWebsocket () throws IOException
  {
    int len = 0;
    byte[] b = new byte[140];
    len = client.getInputStream().read(b);

    if (len != -1)
    {

      byte rLength = 0;
      int rMaskIndex = 2;
      int rDataStart = 0;
      //b[0] is always text in my case so no need to check;
      byte data = b[1];
      byte op = (byte) 127;
      rLength = (byte) (data & op);

      if (rLength == (byte) 126)
      {
        rMaskIndex = 4;
      }
      if (rLength == (byte) 127)
      {
        rMaskIndex = 10;
      }

      byte[] masks = new byte[4];

      int j = 0;
      int i = 0;
      for (i = rMaskIndex; i < (rMaskIndex + 4); i++)
      {
        masks[j] = b[i];
        j++;
      }

      rDataStart = rMaskIndex + 4;

      int messLen = len - rDataStart;

      byte[] message = new byte[messLen];

      for (i = rDataStart, j = 0; i < len; i++, j++)
      {
        message[j] = (byte) (b[i] ^ masks[j % 4]);
      }

      return new String(message);
    }

    return "";
  }
}