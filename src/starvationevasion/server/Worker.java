package starvationevasion.server;

/**
 * @author Javier Chavez
 */


import starvationevasion.server.handlers.Handler;
import starvationevasion.server.io.*;
import starvationevasion.server.io.strategies.SocketReadStrategy;
import starvationevasion.server.io.strategies.SocketWriteStrategy;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.Sendable;
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
  private volatile boolean isRunning = true;
  private final Server server;
  private final Simulator simulator;
  private Handler handler;
  private long serverStartTime;
  private boolean sent = false;

  private WriteStrategy writer;
  private ReadStrategy reader;

  private final DataOutputStream outStream;
  private final DataInputStream inStream;


  public Worker (Socket client, Server server)
  {

    this.writer = new SocketWriteStrategy(client);
    this.reader = new SocketReadStrategy(client);
    this.outStream = writer.getStream();
    this.inStream = reader.getStream();


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
   */
  public <T extends Sendable> void send (T data)
  {
    try
    {
      writer.write(data);
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }


  public void shutdown()
  {
    isRunning = false;
    try
    {
      reader.close();
      writer.close();
      client.close();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
    
  }
  
  public void run ()
  {

    while(isRunning)
    {
      try
      {

        Object _raw = reader.read();

        Request request = null;

        if (_raw == null || reader == null || _raw.equals("\u0003�"))
        {
          // lost the client
          isRunning = false;
          return;
        }

        if (_raw instanceof Request)
        {
          request = (Request) _raw;
        }
        else if (_raw instanceof String)
        {
          String string = ((String) _raw);
          // notice I am expecting only requests from a client... Not supporting responses from client.
          String[] arr = string.split("\\s+");
          if (arr.length < 2)
          {
            send(new Response(server.uptime(), "invalid"));
          }

          request = new Request(arr[0], arr[1], string);
        }


        handler.handle(request);

      }
      catch(Exception e)
      {
        isRunning = false;
        e.printStackTrace();
        return;
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
    this.reader.setStream(inStream);
  }

  public void setWriter (WriteStrategy writer)
  {
    this.writer = writer;
    this.writer.setStream(outStream);
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
