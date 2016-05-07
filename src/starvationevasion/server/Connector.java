package starvationevasion.server;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 * TODO Rename to Worker
 */

import starvationevasion.common.Util;
import starvationevasion.server.handlers.Handler;
import starvationevasion.server.io.ReadStrategy;
import starvationevasion.server.io.WriteStrategy;
import starvationevasion.server.io.strategies.SocketReadStrategy;
import starvationevasion.server.io.strategies.SocketWriteStrategy;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.User;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Worker that defines the most basic socket connection.
 */
public abstract class Connector extends Thread
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

  private final static Logger LOG = Logger.getGlobal(); // getLogger(Server.class.getName());
  private final static Level logLevel = Level.FINEST;


  public Connector (Socket client, Server server)
  {
    LOG.setLevel(logLevel);
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
      LOG.info("Contains incorrect padding");
      shutdown();
    }
    catch(IllegalBlockSizeException e)
    {
      LOG.info("Block is incorrect");
      shutdown();
    }
    catch(InvalidKeyException e)
    {
      LOG.info("Key is invalid");
      shutdown();
    }
    catch(IOException e)
    {
      LOG.info("Error writing to stream");
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
      LOG.info("There was an error shutting down");
    }
    if (user != null)
    {
      user.setLoggedIn(false);
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

  Class getConnectionType(){
    return this.getClass();
  }

  Handler getHandler ()
  {
    return handler;
  }

  Server getServer ()
  {
    return server;
  }
}
