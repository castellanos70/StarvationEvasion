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

      if (getClient().getUser() == null)
      {
        getClient().send(new Response(server.uptime(),"Error"));
        return true;
      }
      String to = ((String) request.getPayload().get("to")).toUpperCase();
      String from = getClient().getUser().getRegion().name();

      request.getPayload().remove("to");
      request.getPayload().put("from", from);

      if (to.equals("ALL"))
      {
        server.broadcast(new Response(server.uptime(), request.getPayload()));
      }
      else
      {
        EnumRegion destination = EnumRegion.valueOf(to);
        Worker worker = server.getWorkerByRegion(destination);
        worker.send(new Response(server.uptime(), request.getPayload()));
      }

      return true;
    }
    return false;
  }
}
