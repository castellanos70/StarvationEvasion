package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.EnumPolicy;
import starvationevasion.common.PolicyCard;
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
      if (getClient().getUser().policyCardsDiscarded <= 2)
      {
        if (getClient().getUser().getHand().contains(card))
        {
          server.getSimulator().discard(getClient().getUser().getRegion(), card);
          server.getSimulator().drawCards(getClient().getUser().getRegion());
        }
      }

      return true;
    }
    else if (request.getDestination().equals(Endpoint.DRAFT_CARD))
    {
      PolicyCard policyCard = (PolicyCard) request.getPayload().getData();
      Payload data = new Payload();
      m_response = new Response(server.uptime(), data);


      if (policyCard.getOwner() == getClient().getUser().getRegion())
      {
        String validation = policyCard.validate();
        if (validation == null)
        {
          server.addDraftedCard(policyCard);
          if (policyCard.votesRequired() > 0)
          {
            getClient().getUser().getHand().remove(policyCard);
            m_response.setType(Type.DRAFTED_INTO_VOTE);
            data.putData(policyCard);
            getClient().send(m_response);
            return true;
          }
          else
          {
            getClient().getUser().getHand().remove(policyCard);
            m_response.setType(Type.DRAFTED);
            data.putData(policyCard);
            getClient().send(m_response);
            return true;
          }
        }
        else
        {
          m_response.setType(Type.DRAFTED);
          data.putData(policyCard);
          data.putMessage(validation);
          getClient().send(m_response);
          return true;
        }

      }


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
