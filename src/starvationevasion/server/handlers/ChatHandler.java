package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.EnumRegion;
import starvationevasion.server.Worker;
import starvationevasion.server.Server;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.User;


public class ChatHandler extends AbstractHandler
{
  public ChatHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    if (request.getDestination().equals(Endpoint.CHAT))
    {
      boolean isRegion = false;
      String to;
      if (request.getPayload().containsKey("to-region"))
      {
        to = ((String) request.getPayload().get("to-region")).toUpperCase();
        request.getPayload().remove("to-region");
        isRegion = true;
      }
      else {
        to = (String) request.getPayload().get("to-username");
        request.getPayload().remove("to-username");
      }

      request.getPayload().put("from", getClient().getUsername());

      if (to.equals("ALL"))
      {
        server.broadcast(new Response(server.uptime(), request.getPayload()));
      }
      else if (!isRegion)
      {
        User u = server.getUserByUsername(to);
        if (u != null)
        {
          u.getWorker().send(new Response(server.uptime(), request.getPayload()));
        }
      }
      else
      {
        EnumRegion destination = EnumRegion.valueOf(to);
        for (User _user : server.getLoggedInUsers())
        {
          if (_user.getRegion().equals(destination))
          {
            _user.getWorker().send(new Response(server.uptime(), request.getPayload()));
          }
        }
      }

      return true;
    }
    return false;
  }
}
