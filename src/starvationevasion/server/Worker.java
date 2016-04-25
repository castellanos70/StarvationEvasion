package starvationevasion.server;


/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.Util;
import starvationevasion.server.handlers.Handler;
import starvationevasion.server.handlers.LoginHandler;
import starvationevasion.server.io.*;
import starvationevasion.server.io.strategies.HTTPWriteStrategy;
import starvationevasion.server.io.strategies.SocketReadStrategy;
import starvationevasion.server.io.strategies.SocketWriteStrategy;
import starvationevasion.server.model.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.StringTokenizer;

/**
 *  Worker that holds connection, writer, reader, and user information
 */
public class Worker extends Thread
{
  private Socket client;
  private String username = Util.generateName();
  private volatile boolean isRunning = true;
  private final Server server;
  private final Handler handler;

  private WriteStrategy writer;
  private ReadStrategy reader;

  private final DataOutputStream outStream;
  private final DataInputStream inStream;
  private User user = new User(username);
  private HttpParse httpRequest;


  public Worker (Socket client, Server server)
  {
    this.user.setAnonymous(true);
    this.writer = new SocketWriteStrategy(client);
    this.reader = new SocketReadStrategy(client);
    this.outStream = writer.getStream();
    this.inStream = reader.getStream();


    this.client = client;
    this.server = server;
    this.handler = new Handler(server, this);

  }

  @Override
  public synchronized void start ()
  {
    isRunning = true;
    super.start();
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
  public synchronized void send (Response data)
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
    catch(NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }
    catch(NoSuchPaddingException e)
    {
      e.printStackTrace();
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
    if (!username.equals("ANON"))
    {
      user.setLoggedIn(false);
    }
  }
  
  public void run ()
  {
    if (writer instanceof HTTPWriteStrategy)
    {
      String destination;
      destination = httpRequest.getRequestLine().replace("GET", "");
      destination = destination.replace("PUT", "");
      destination = destination.replace("POST", "");

      destination = destination.replace("HTTP/1.1", "");
      destination = destination.replace("/", "").trim();

      // not supported
//      if (false && httpRequest.getHeaderParam("Authorization") != null)
//      {
//        httpRequest.getHeaderParam("Authorization");
//        String _rawAuth = httpRequest.getHeaderParam("Authorization").replace("Basic", "").trim();
//        _rawAuth = new String(Base64.getDecoder().decode(_rawAuth));
//        StringTokenizer tokenizer = new StringTokenizer(_rawAuth, ":");
//
//        if (tokenizer.countTokens() <= 1 || !LoginHandler.authenticate(server, this, tokenizer.nextToken(), tokenizer.nextToken()))
//        {
//          send(new ResponseFactory().build(server.uptime(), null, Type.AUTH_ERROR, "Incorrect username or password"));
//          shutdown();
//          return;
//        }
//      }

      if (!destination.isEmpty())
      {

        Request request = null;

        request = new Request(server.uptimeString(), destination, httpRequest.getMessageBody());
        if (request.getDestination() == Endpoint.NOT_FOUND)
        {
          send(new ResponseFactory().build(server.uptime(), null, Type.NOT_FOUND, "Not found", request));
          shutdown();
          return;
        }

        handler.handle(request);
      }

      shutdown();
      return;
    }


    while(isRunning)
    {
      try
      {
        // need to refactor to be able to replace abstract our the
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
            send(new ResponseFactory().build(server.uptime(), null, Type.ERROR, "'" + _raw + "' is an invalid command"));
            continue;
          }

          request = new Request(arr[0], arr[1], string);
          if (request.getDestination() == Endpoint.NOT_FOUND)
          {
            send(new ResponseFactory().build(server.uptime(), null, Type.NOT_FOUND, "Not found", request));
            continue;
          }
        }
        // still need to be able to call handle
        handler.handle(request);
      }
      catch(IOException e)
      {
        System.out.println("There was an error reading");
        System.out.println("Shutting down");
        shutdown();
      }
      catch(ClassNotFoundException e)
      {
        System.out.println("Invalid Class was received");
        send(new ResponseFactory().build(server.uptime(), null, Type.ERROR, "Invalid Class"));
      }
      catch(BadPaddingException e)
      {
        System.out.println("Error padding encryption");
        send(new ResponseFactory().build(server.uptime(), null, Type.ERROR, "Invalid padding"));
      }
      catch(IllegalBlockSizeException e)
      {
        System.out.println("Block is too large");
        send(new ResponseFactory().build(server.uptime(), null, Type.ERROR, "Invalid Encryption"));
      }
      catch(InvalidKeyException e)
      {
        System.out.println("Key is incorrect.");
        send(new ResponseFactory().build(server.uptime(), null, Type.ERROR, "Invalid Key"));
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


  public void setUser (User user)
  {
    this.user = user;
  }

  public User getUser()
  {
    return user;
  }

  public void handleDirectly (HttpParse paresr)
  {
    this.httpRequest = paresr;
  }
}