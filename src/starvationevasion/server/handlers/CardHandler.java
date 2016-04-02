package starvationevasion.server.handlers;


import starvationevasion.common.EnumPolicy;
import starvationevasion.server.Server;
import starvationevasion.server.Worker;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.Response;

public class CardHandler extends AbstractHandler
{
  public CardHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    if (request.getDestination().equals(Endpoint.HAND_CREATE))
    {
      server.draw(getClient());
      Payload data = new Payload();

      data.putData(getClient().getUser().getHand());
      data.putMessage("SUCCESS");
      m_response = new Response(server.uptime(), data);
      getClient().send(m_response);

      return true;
    }
    else if (request.getDestination().equals(Endpoint.DELETE_CARD))
    {
      EnumPolicy card = (EnumPolicy) request.getData().get("data");
      if (getClient().getUser().getHand().contains(card))
      {
        server.getSimulator().discard(getClient().getUser().getRegion(), card);
      }
      return true;
    }
    else if (request.getDestination().equals(Endpoint.DRAFT_CARD))
    {

      return true;
    }
    else if (request.getDestination().equals(Endpoint.HAND_READ))
    {
      Payload data = new Payload();

      data.putData(getClient().getUser().getHand());
      m_response = new Response(server.uptime(), data);

      getClient().send(m_response);

      return true;
    }

    return false;
  }
}
