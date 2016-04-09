package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;
import starvationevasion.server.Server;
import starvationevasion.server.Worker;
import starvationevasion.server.model.*;

import java.util.Collections;

public class CardHandler extends AbstractHandler
{
  public CardHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    if (!server.getGameState().equals(State.DRAFTING))
    {
      return false;
    }


    if (request.getDestination().equals(Endpoint.DRAW_CARD))
    {
      server.drawByUser(getClient().getUser());
      getClient().send(ResponseFactory.build(server.uptime(), getClient().getUser(), Type.USER));

      return true;
    }
    else if (request.getDestination().equals(Endpoint.DELETE_CARD))
    {
      EnumPolicy card = (EnumPolicy) request.getPayload().get("data");
      if (getClient().getUser().policyCardsDiscarded <= 2)
      {
        if (getClient().getUser().getHand().contains(card))
        {
          EnumRegion region = getClient().getUser().getRegion();

          server.getSimulator().discard(region, card);
          getClient().getUser().getHand().remove(card);

          server.drawByUser(getClient().getUser());

          getClient().send(ResponseFactory.build(server.uptime(), getClient().getUser(), Type.USER));

        }
      }

      return true;
    }
    else if (request.getDestination().equals(Endpoint.DRAFT_CARD))
    {
      PolicyCard policyCard = (PolicyCard) request.getPayload().getData();



      if (policyCard.getOwner() == getClient().getUser().getRegion())
      {
        String validation = policyCard.validate();
        if (validation == null)
        {

          server.getDraftedPolicyCards().add(policyCard);

          if (policyCard.votesRequired() > 0)
          {
            getClient().getUser().getHand().remove(policyCard.getCardType());
            getClient().send(ResponseFactory.build(server.uptime(), getClient().getUser(), Type.DRAFTED_INTO_VOTE));
            return true;
          }
          else
          {
            getClient().getUser().getHand().remove(policyCard.getCardType());
            getClient().send(ResponseFactory.build(server.uptime(), getClient().getUser(), Type.DRAFTED));
            return true;
          }
        }
        else
        {
          getClient().send(ResponseFactory.build(server.uptime(), policyCard, validation, Type.DRAFTED));
          return true;
        }

      }


      return true;
    }
    else if (request.getDestination().equals(Endpoint.HAND_READ))
    {
      getClient().send(ResponseFactory.build(server.uptime(), getClient().getUser(),Type.USER));
      return true;
    }

    return false;
  }
}
