package starvationevasion.server.handlers;

import starvationevasion.server.Worker;
import starvationevasion.server.Server;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Request;

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

      String destination = request.getData()[0];
      String data = request.getData()[1];
      String from = getClient().getUser().getRegion().name();

      // Determine if the destination is by username or by region

      // get the worker for that destination
      // send data.

      return true;
    }
    return false;
  }
}
