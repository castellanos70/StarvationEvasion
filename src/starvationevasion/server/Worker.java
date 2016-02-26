package starvationevasion.server;

/**
 * @author Javier Chavez
 */


import starvationevasion.server.handlers.Handler;
import starvationevasion.server.io.*;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.User;
import starvationevasion.sim.Simulator;

import java.io.*;
import java.net.Socket;

/**
 * ServerWorker
 */
public class Worker extends Thread
{
  private User cred;
  private Socket client;
  private boolean isRunning = true;
  private final Server server;
  private final Simulator simulator;
  private Handler handler;
  private long serverStartTime;
  private boolean sent = false;

  private WriteStrategy writer;
  private ReadStrategy reader;

  public Worker (Socket client, Server server)
  {
    this.writer = new SocketWriteStrategy(client);
    this.reader = new SocketReadStrategy(client);

    this.client = client;
    this.simulator = server.getSimulator();
    this.server = server;
    this.handler = new Handler(server, this);

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
    System.out.println("STRING ServerWorker.send(" + msg + ")");
    try
    {
      writer.write(msg);
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Send message to client.
   *
   */
  public <T extends JSON> void send (T data)
  {
    System.out.println("JSON ServerWorker.send(" + data.toJSON().toJSON() + ")");
    try
    {
      writer.write(data.toJSONString());
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }


  /**
   * Send message to client.
   *
   */
//  public <T extends Serializable> void send (T data)
//  {
//    System.out.println("ServerWorker.send(" + data.toString() + ")");
//    try
//    {
//      clientObjectWriter.writeObject(data);
//    }
//    catch(IOException e)
//    {
//      e.printStackTrace();
//    }
//  }


  public void run ()
  {

    while(isRunning)
    {
      try
      {
        String s = reader.read();
        // System.out.println(s);

        if (s == null || reader == null || s.equals("\u0003�"))
        {
          // lost the client
          client.close();
          isRunning = false;
          break;
        }

        // notice I am expecting only requests from a client... Not supporting responses from client.
        String[] arr = s.split("\\s+");
        if (arr.length < 2)
        {
          System.exit(1);
          throw new Exception("Not enough data");
        }

        Request r = new Request(arr[0], arr[1], s);
        handler.handle(r);

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

  public ReadStrategy getReader ()
  {
    return reader;
  }

  public void setReader (ReadStrategy reader)
  {
    this.reader = reader;
  }

  public void setWriter (WriteStrategy writer)
  {
    this.writer = writer;
  }

  public User getUser ()
  {
    return cred;
  }

  public void setUser (User user)
  {
    this.cred = user;
  }
}