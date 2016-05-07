package starvationevasion.server;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.model.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;

/**
 * Worker used to persist a connection for socket connetion.
 */
public class Persistent extends Connector
{
  
  public Persistent (Socket client, Server server)
  {
    super(client, server);
  }

  public void run ()
  {

    while(isRunning())
    {
      try
      {
        // need to refactor to be able to replace abstract our the
        Object _raw = getReader().read();

        Request request = null;

        if (_raw == null || getReader() == null || _raw.equals("\u0003�"))
        {
          // lost the client
          shutdown();
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
            send(new ResponseFactory().build(getServer().uptime(), null, Type.ERROR, "'" + _raw + "' is an invalid command"));
            continue;
          }

          request = new Request(arr[0], arr[1], string);
          if (request.getDestination() == Endpoint.NOT_FOUND)
          {
            send(new ResponseFactory().build(getServer().uptime(), null, Type.NOT_FOUND, "Not found", request));
            continue;
          }
        }
        // still need to be able to call handle
        getHandler().handle(request);
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
        send(new ResponseFactory().build(getServer().uptime(), null, Type.ERROR, "Invalid Class"));
      }
      catch(BadPaddingException e)
      {
        System.out.println("Error padding encryption");
        send(new ResponseFactory().build(getServer().uptime(), null, Type.ERROR, "Invalid padding"));
      }
      catch(IllegalBlockSizeException e)
      {
        System.out.println("Block is too large");
        send(new ResponseFactory().build(getServer().uptime(), null, Type.ERROR, "Invalid Encryption"));
      }
      catch(InvalidKeyException e)
      {
        System.out.println("Key is incorrect.");
        send(new ResponseFactory().build(getServer().uptime(), null, Type.ERROR, "Invalid Key"));
      }
    }
  }



}
