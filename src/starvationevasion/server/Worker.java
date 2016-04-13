package starvationevasion.server;


/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.handlers.Handler;
import starvationevasion.server.io.*;
import starvationevasion.server.io.strategies.SocketReadStrategy;
import starvationevasion.server.io.strategies.SocketWriteStrategy;
import starvationevasion.server.model.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;

/**
 *  Worker that holds connection, writer, reader, and user information
 */
public class Worker extends Thread
{
  private Socket client;
  private String username = "ANON";
  private volatile boolean isRunning = true;
  private final Server server;
  private final Handler handler;

  private WriteStrategy writer;
  private ReadStrategy reader;

  private final DataOutputStream outStream;
  private final DataInputStream inStream;
  private boolean isLoggedIn = false;
  private User user;


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
    catch(BadPaddingException e)
    {
      System.out.println("Contains incorrect padding");
      shutdown();
    }
    catch(IllegalBlockSizeException e)
    {
      System.out.println("Block is incorrect");
      shutdown();
    }
    catch(InvalidKeyException e)
    {
      System.out.println("Key is invalid");
      shutdown();
    }
    catch(IOException e)
    {
      System.out.println("Error writing to stream");
      shutdown();
    }

  }


  void shutdown()
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
      System.out.println("There was an error shutting down");
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
            send(new ResponseFactory().build(server.uptime(), null, Type.BROADCAST, "Invalid command args"));
            continue;
          }

          request = new Request(arr[0], arr[1], string);
        }

        handler.handle(request);
      }
      catch(EndpointException e)
      {
        System.out.println("Invalid endpoint!");
        send(new ResponseFactory().build(server.uptime(), null, Type.BROADCAST, "Invalid endpoint"));
      }
      catch(IOException e)
      {
        // isRunning = false;
        System.out.println("There was an error reading");
      }
      catch(ClassNotFoundException e)
      {
        System.out.println("Invalid Class was received");
        send(new ResponseFactory().build(server.uptime(), null, Type.BROADCAST, "Invalid Class"));
      }
      catch(BadPaddingException e)
      {
        System.out.println("Error padding encryption");
      }
      catch(IllegalBlockSizeException e)
      {
        System.out.println("Block is too large");
      }
      catch(InvalidKeyException e)
      {
        System.out.println("Key is incorrect.");
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
    if (isLoggedIn && user == null)
    {
      this.user = server.getUserByUsername(getUsername());
    }
    return user;
  }
}