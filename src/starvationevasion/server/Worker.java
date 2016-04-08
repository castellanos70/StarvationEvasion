package starvationevasion.server;


/**
 * @author Javier Chavez (javierc@cs.unm.edu)
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
 *  Worker that holds connection, writer, reader, and user information
 */
public class Worker extends Thread
{
  private Socket client;
  private String username = "ANON";
  private volatile boolean isRunning = true;
  private final Server server;
  private Handler handler;

  private WriteStrategy writer;
  private ReadStrategy reader;

  private final DataOutputStream outStream;
  private final DataInputStream inStream;
  private boolean isLoggedIn = false;


  public Worker (Socket client, Server server)
  {
    this.writer = new SocketWriteStrategy(client);
    this.reader = new SocketReadStrategy(client);
    this.outStream = writer.getStream();
    this.inStream = reader.getStream();


    this.client = client;
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
  public synchronized <T extends Sendable> void send (T data)
  {
    try
    {
      writer.write(data);
    }
    catch(IOException e)
    {
      String u = "";
//      if (getUser() != null)
//      {
//        u = getUser().toString() + "\n";
//      }
      System.out.println("There was an error trying to write to:\n\t"
                                 + getName() + "\n\t"
                                 + u);
      shutdown();
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
    getUser().setLoggedIn(false);
    isLoggedIn = false;
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
            send(new Response(server.uptime(), "Invalid number of arguments"));
            continue;
          }

          request = new Request(arr[0], arr[1], string);
        }


        handler.handle(request);

      }
      catch(EndpointException e)
      {
        System.out.println("Invalid endpoint!");
        send(new Response(server.uptime(), e.getMessage()));
      }
      catch(IOException e)
      {
        isRunning = false;
        String u = "";
//        if (getUser() != null)
//        {
//          u = getUser().toString() + "\n";
//        }
        System.out.println("There was an error reading:\n\t"
                                   + getName() + "\n\t"
                                   + u);
        return;
      }
      catch(ClassNotFoundException e)
      {
        System.out.println("Invalid Class was received");
        send(new Response(server.uptime(), e.getMessage()));
      }
    }
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

  public WriteStrategy getWriter ()
  {
    return writer;
  }


  public String getUsername ()
  {
    return username;
  }

  public void setUsername (String username)
  {
    this.username = username;
    if (!username.equals("ANON"))
    {
      isLoggedIn = true;
    }
  }

  public boolean loggedIn ()
  {
    return isLoggedIn;
  }

  public User getUser()
  {
    return server.getUserByUsername(getUsername());
  }
}