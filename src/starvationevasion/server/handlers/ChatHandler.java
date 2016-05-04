package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.EnumRegion;
import starvationevasion.server.Connector;
import starvationevasion.server.Server;
import starvationevasion.server.model.*;


public class ChatHandler extends AbstractHandler
{
  public ChatHandler (Server server, Connector client)
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

      request.getPayload().put("from", getClient().getUser().getUsername());

      if (to.equals("ALL"))
      {
        server.broadcast(new ResponseFactory().build(server.uptime(),
                                               request.getPayload(),
                                               Type.CHAT));
      }
      else if (!isRegion)
      {
        User u = server.getUserByUsername(to);
        if (u != null)
        {
          u.getWorker().send(new ResponseFactory().build(server.uptime(),
                                                   request.getPayload(),
                                                   Type.CHAT));
        }
      }
      else
      {
        EnumRegion destination = EnumRegion.valueOf(to);
        for (User _user : server.getLoggedInUsers())
        {
          if (_user.getRegion().equals(destination))
          {
            _user.getWorker().send(new ResponseFactory().build(server.uptime(),
                                                         request.getPayload(),
                                                         Type.CHAT));
          }
        }
      }

      return true;
    }
    return false;
  }
}
