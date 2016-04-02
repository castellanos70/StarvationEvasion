package starvationevasion.server.handlers;


import starvationevasion.common.EnumRegion;
import starvationevasion.server.Server;
import starvationevasion.server.Worker;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.Response;

public class DataHandler extends AbstractHandler
{
  public DataHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    if (request.getDestination().equals(Endpoint.GAME_STATE))
    {
      Payload data = new Payload();
      data.putData(server.getGameState());
      m_response = new Response(server.uptime(), data);
      getClient().send(m_response);
      return true;
    }
    else if (request.getDestination().equals(Endpoint.SERVER_UPTIME))
    {
      return true;
    }
    else if (request.getDestination().equals(Endpoint.AVAILABLE_REGIONS))
    {
      return true;
    }

    return false;
  }
}
