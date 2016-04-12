package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import java.util.ArrayList;
import java.util.Collections;

import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
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
    if (!server.getGameState().equals(State.DRAFTING))
    {
      return false;
    }

    // need to disable --- This was used for testing
    if (request.getDestination().equals(Endpoint.DRAW_CARD))
    {
      // server.drawByUser(getClient().getUser());
      // getClient().send(ResponseFactory.build(server.uptime(), getClient().getUser(), Type.D));

      return true;
    }
    else if (request.getDestination().equals(Endpoint.DELETE_CARD))
    {
      EnumPolicy card = (EnumPolicy) request.getPayload().get("data");
      if (getClient().getUser().policyCardsDiscarded == 0 || getClient().getUser().actionsRemaining >= 1)
      {
        if (getClient().getUser().getHand().contains(card))
        {
          EnumRegion region = getClient().getUser().getRegion();

          server.getSimulator().discard(region, card);
          getClient().getUser().getHand().remove(card);


          if (getClient().getUser().policyCardsDiscarded == 1)
          {
            getClient().getUser().actionsRemaining--;
          }
          else
          {
            getClient().getUser().policyCardsDiscarded++;
          }

          // getClient().send(ResponseFactory.build(server.uptime(), getClient().getUser(), Type.USER));
          server.broadcast(ResponseFactory.build(server.uptime(), getClient().getUser(), Type.DRAFTED, "Discarded a card."));
        }
      }
      else
      {
        getClient().send(ResponseFactory.build(server.uptime(), null, Type.ERROR, "You're out of actions!"));
      }

      return true;
    }
    else if (request.getDestination().equals(Endpoint.DELETE_AND_DRAW_CARDS))
    {
      ArrayList<EnumPolicy> cards = (ArrayList<EnumPolicy>) request.getPayload().get("data");
      if (getClient().getUser().actionsRemaining >= 1)
      {
        boolean isSubset = getClient().getUser().getHand().containsAll(cards);

        if (isSubset)
        {
          EnumRegion region = getClient().getUser().getRegion();

          for (EnumPolicy card : cards)
          {
            server.getSimulator().discard(region, card);
            getClient().getUser().getHand().remove(card);
            EnumPolicy[] newCards = server.getSimulator().drawCards(region);
            Collections.addAll(getClient().getUser().getHand(), newCards);
          }


          getClient().getUser().actionsRemaining--;
          server.broadcast(ResponseFactory.build(server.uptime(), getClient().getUser(), Type.DRAFTED, "Discard and Drew"));

          // getClient().send(ResponseFactory.build(server.uptime(), getClient().getUser(), Type.USER));

        }
      }
      else
      {
        getClient().send(ResponseFactory.build(server.uptime(), null, Type.ERROR, "You're out of actions!"));
      }

      return true;
    }
    else if (request.getDestination().equals(Endpoint.DRAFT_CARD))
    {
      PolicyCard policyCard = (PolicyCard) request.getPayload().getData();

      if (getClient().getUser().actionsRemaining >= 1)
      {

        if (policyCard.getOwner() == getClient().getUser().getRegion())
        {
          String validation = policyCard.validate();
          if (validation == null)
          {

            server.getDraftedPolicyCards().add(policyCard);

            if (policyCard.votesRequired() > 0)
            {
              getClient().getUser().getHand().remove(policyCard.getCardType());
              server.broadcast(ResponseFactory.build(server.uptime(), getClient().getUser(), Type.DRAFTED_INTO_VOTE, "Drafted a card into vote."));
            }
            else
            {
              getClient().getUser().getHand().remove(policyCard.getCardType());
              server.broadcast(ResponseFactory.build(server.uptime(), getClient().getUser(), Type.DRAFTED, "Drafted a card."));
            }
            getClient().getUser().actionsRemaining--;
            return true;
          }
          else
          {
            getClient().send(ResponseFactory.build(server.uptime(), getClient().getUser(), Type.DRAFTED, validation));
            return true;
          }
        }
      }
      else
      {
        getClient().send(ResponseFactory.build(server.uptime(), null, Type.ERROR, "You're out of actions!"));
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
