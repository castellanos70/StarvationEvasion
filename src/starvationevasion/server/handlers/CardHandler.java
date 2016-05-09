package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.common.gamecards.GameCard;
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
    if (!server.getGameState().equals(State.DRAFTING))
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
      if (getClient().getUser().policyCardsDiscarded == 0 || getClient().getUser().actionPointsRemaining >= 1)
      {
        if (getClient().getUser().getHand().contains(card))
        {
          server.discard(getClient().getUser(), card);


          if (getClient().getUser().policyCardsDiscarded == 1)
          {
            getClient().getUser().actionPointsRemaining--;
          }
          else
          {
            getClient().getUser().policyCardsDiscarded++;
          }

          // getClient().send(new ResponseFactory().build(server.uptime(), getClient().getUser(), Type.USER));
          server.broadcast(new ResponseFactory().build(server.uptime(), getClient().getUser(), Type.DRAFTED, "Discarded a card."));
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
        boolean isSubset = getClient().getUser().getHand().containsAll(cards);

        if (isSubset)
        {
          EnumRegion region = getClient().getUser().getRegion();

          for (EnumPolicy card : cards)
          {
            server.discard(getClient().getUser(), card);
            EnumPolicy[] newCards = server.getSimulator().drawCards(region);
            Collections.addAll(getClient().getUser().getHand(), newCards);
          }


          getClient().getUser().actionPointsRemaining--;
          server.broadcast(new ResponseFactory().build(server.uptime(), getClient().getUser(), Type.DRAFTED, "Discard and Drew"));
          getClient().send(new ResponseFactory().build(server.uptime(), getClient().getUser(), Type.USER));
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
      GameCard policyCard = (GameCard) request.getPayload().getData();

      if (getClient().getUser().actionPointsRemaining >= policyCard.actionPointCost(policyCard.getCardType()) && getClient().getUser().drafts < 2)
      {

        if (policyCard.getOwner().equals(getClient().getUser().getRegion()))
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
              getClient().getUser().getHand().remove(policyCard.getCardType());
              server.broadcast(new ResponseFactory().build(server.uptime(), getClient().getUser(), Type.DRAFTED_INTO_VOTE, "Drafted a card into vote."));
              getClient().getUser().draftVoteCard++;
              getClient().send(new ResponseFactory().build(server.uptime(), getClient().getUser(), Type.USER));
            }
            else if (policyCard.votesRequired() > 0 && getClient().getUser().draftVoteCard == 1)
            {
              server.broadcast(new ResponseFactory().build(server.uptime(), getClient().getUser(), Type.ERROR, "Only one vote card please."));
            }
            else
            {
              getClient().getUser().getHand().remove(policyCard.getCardType());
              server.broadcast(new ResponseFactory().build(server.uptime(), getClient().getUser(), Type.DRAFTED, "Drafted a card."));
              getClient().send(new ResponseFactory().build(server.uptime(), getClient().getUser(), Type.USER));
            }
            getClient().getUser().drafts++;
            getClient().getUser().actionPointsRemaining -= policyCard.actionPointCost(policyCard.getCardType());
            return true;
          }
          else
          {
            getClient().send(new ResponseFactory().build(server.uptime(), getClient().getUser(), Type.DRAFTED, validation));
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
