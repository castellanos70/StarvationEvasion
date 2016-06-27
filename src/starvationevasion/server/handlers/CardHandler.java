package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.EnumRegion;
import starvationevasion.common.GameState;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.PolicyCard;
import starvationevasion.server.Connector;
import starvationevasion.server.Server;
import starvationevasion.server.model.*;

import java.util.ArrayList;
import java.util.Collections;

public class CardHandler extends AbstractHandler
{
  public CardHandler (Server server, Connector client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    User user = getClient().getUser();
    if (!server.getGameState().equals(GameState.DRAFTING))
    {
      return false;
    }

    // need to disable --- This was used for testing
    if (request.getDestination().equals(Endpoint.DRAW_CARD))
    {
      // server.drawByUser(getClient().getUser());
      // getClient().send(new ResponseFactory().build(server.uptime(), getClient().getUser(), Type.D));

      return true;
    }
    else if (request.getDestination().equals(Endpoint.DELETE_CARD))
    {
      EnumPolicy card = (EnumPolicy) request.getPayload().get("data");
      if (user.policyCardsDiscarded == 0 || getClient().getUser().actionPointsRemaining >= 1)
      {
        if (user.getHand().contains(card))
        {
          server.discard(user, card);


          if (user.policyCardsDiscarded == 1)
          {
            user.actionPointsRemaining--;
          }
          else
          {
            user.policyCardsDiscarded++;
          }

          // getClient().send(new ResponseFactory().build(server.uptime(), getClient().getUser(), Type.USER));
          server.broadcast(new ResponseFactory().build(server.uptime(), user, Type.DRAFTED, "Discarded a card."));
        }
      }
      else
      {
        getClient().send(new ResponseFactory().build(server.uptime(), null, Type.ERROR, "You do not have enough action points!"));
      }

      return true;
    }
    else if (request.getDestination().equals(Endpoint.DELETE_AND_DRAW_CARDS))
    {
      ArrayList<EnumPolicy> cards = (ArrayList<EnumPolicy>) request.getPayload().get("data");
      if (getClient().getUser().actionPointsRemaining >= 1)
      {
        boolean isSubset = user.getHand().containsAll(cards);

        if (isSubset)
        {
          EnumRegion region = user.getRegion();

          for (EnumPolicy card : cards)
          {
            server.discard(user, card);
            EnumPolicy[] newCards = server.getSimulator().drawCards(region);
            Collections.addAll(user.getHand(), newCards);
          }


          getClient().getUser().actionPointsRemaining--;
          server.broadcast(new ResponseFactory().build(server.uptime(), user, Type.DRAFTED, "Discard and Drew"));
          getClient().send(new ResponseFactory().build(server.uptime(), user, Type.USER));
        }
      }
      else
      {
        getClient().send(new ResponseFactory().build(server.uptime(), null, Type.ERROR, "You do not have enough action points!"));
      }

      return true;
    }
    else if (request.getDestination().equals(Endpoint.DRAFT_CARD))
    {
      PolicyCard policyCard = (PolicyCard) request.getPayload().getData();

      if (user.actionPointsRemaining >= policyCard.getActionPointCost() && getClient().getUser().drafts < 2)
      {

        if (policyCard.getOwner().equals(user.getRegion()))
        {
          String validation = policyCard.validate();
          if (validation == null)
          {
            // int drafts = getClient().getUser().drafts;
            synchronized(server)
            {
              server.draftCard(policyCard, getClient().getUser());
            }

            if (policyCard.votesRequired() > 0 && getClient().getUser().draftVoteCard < 1)
            {
              user.getHand().remove(policyCard.getCardType());
              server.broadcast(new ResponseFactory().build(server.uptime(), user, Type.DRAFTED_INTO_VOTE, "Drafted a card into vote."));
              user.draftVoteCard++;
              getClient().send(new ResponseFactory().build(server.uptime(), user, Type.USER));
            }
            else if (policyCard.votesRequired() > 0 && user.draftVoteCard == 1)
            {
              server.broadcast(new ResponseFactory().build(server.uptime(), user, Type.ERROR, "Only one vote card please."));
            }
            else
            {
              user.getHand().remove(policyCard.getCardType());
              server.broadcast(new ResponseFactory().build(server.uptime(), user, Type.DRAFTED, "Drafted a card."));
              getClient().send(new ResponseFactory().build(server.uptime(), user, Type.USER));
            }
            user.drafts++;
            user.actionPointsRemaining -= policyCard.getActionPointCost();
            return true;
          }
          else
          {
            getClient().send(new ResponseFactory().build(server.uptime(), user, Type.DRAFTED, validation));
            return true;
          }
        }
      }
      else
      {
        getClient().send(new ResponseFactory().build(server.uptime(), null, Type.ERROR, "You do not have enough action points!"));
      }


      return true;
    }
    else if (request.getDestination().equals(Endpoint.HAND_READ))
    {
      getClient().send(new ResponseFactory().build(server.uptime(), getClient().getUser(),Type.USER));
      return true;
    }

    return false;
  }
}
