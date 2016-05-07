package starvationevasion.server;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.io.HttpParse;
import starvationevasion.server.model.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Worker used to handle HTTP requests.
 */
public class Temporary extends Connector
{
  private final Queue<HttpParse> requests;

  public Temporary (Socket client, Server server)
  {
    super(client, server);
    requests = new LinkedList<>();
  }


  public void run ()
  {
    while(!requests.isEmpty())
    {
      String destination;
      HttpParse httpRequest = requests.poll();
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

        request = new Request(getServer().uptimeString(), destination, httpRequest.getMessageBody());
        if (request.getDestination() == Endpoint.NOT_FOUND)
        {
          send(new ResponseFactory().build(getServer().uptime(), null, Type.NOT_FOUND, "Not found", request));
          break;
        }

        getHandler().handle(request);
      }
      break;

    }
    shutdown();
  }


  public Class getConnectionType(){
    return this.getClass();
  }

  void handleDirectly (HttpParse paresr)
  {
    requests.add(paresr);
  }
}
