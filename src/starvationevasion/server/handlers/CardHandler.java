package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.EnumPolicy;
import starvationevasion.server.Server;
import starvationevasion.server.Worker;
import starvationevasion.server.model.*;

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
      server.drawByWorker(getClient());
      Payload data = new Payload();

      data.putData(getClient().getUser().getHand());
      data.putMessage("SUCCESS");
      m_response = new Response(server.uptime(), data);
      getClient().send(m_response);

      return true;
    }
    else if (request.getDestination().equals(Endpoint.DELETE_CARD))
    {
      EnumPolicy card = (EnumPolicy) request.getPayload().get("data");
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
      m_response.setType(Type.USER_HAND);

      getClient().send(m_response);

      return true;
    }

    return false;
  }
}
