package starvationevasion.communication.commands;

import starvationevasion.ai.AI;
import starvationevasion.common.*;
import starvationevasion.common.gamecards.*;
import starvationevasion.communication.AITest;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.server.model.RequestFactory;
import starvationevasion.server.model.State;

import java.util.ArrayList;


public class Draft extends AbstractCommand
{
  private boolean draftedCard = false;
  private boolean discarded = false;
  private boolean drawn = false;
  private GameCard cardDrafted;
  private int tries = 2;

  public Draft (AITest client)
  {
    super(client);
  }

  @Override
  public boolean run ()
  {
//    System.out.println("Drafted: " + draftedCard +
//                               "\nDiscarded: " + discarded +
//                               "\nDrawn: " + drawn);

    if (!getClient().getState().equals(State.DRAFTING) && tries > 0)
    {
      tries--;
      if (tries <= 0)
      {
        return false;
      }
    }



    if (getClient().getState().equals(State.DRAFTING))
    {

      if (!draftedCard)
      {
        randomlySetDraftedCard();
        return true;
      }

      if (!discarded && getClient().getUser().getHand().size() > 0)
      {
        if (!Util.rand.nextBoolean())
        {
          randomlyDiscard();
        }

        return true;
      }
      //getClient().getCommModule().send(new RequestFactory().build(getClient().getStartNanoSec(), new Payload(), Endpoint.DONE));
      getClient().getCommModule().send(Endpoint.DONE, new Payload(), null);

//      if (!drawn && getClient().getUser().getHand().size() < 7)
//      {
//        Request request = new Request(getClient().getStartNanoSec(), Endpoint.DRAW_CARD);
//        getClient().send(request);
//        drawn = true;
//        return true;
//      }

    }
    return false;
  }

  private void randomlyDiscard ()
  {
    EnumPolicy discard = null;

    for (EnumPolicy policy : getClient().getUser().getHand())
    {
      GameCard _card = GameCard.create(getClient().getUser().getRegion(), policy);
      // dont remove the card we just drafted!!!
      if (cardDrafted != null && policy != cardDrafted.getCardType() && Util.rand.nextBoolean())
      {
        discard = policy;
        break;
      }
    }
    int idx = getClient().getUser().getHand().indexOf(discard);
    if(idx >= 0)
    {
      getClient().getCommModule().send(Endpoint.DELETE_CARD, discard, null);
    }
    discarded = true;
  }

  private void randomlySetDraftedCard ()
  {
    GameCard card = null;

    int i = 0;
    boolean draftSent = false;
    for (EnumPolicy policy : getClient().getUser().getHand())
    {
      card = GameCard.create(getClient().getUser().getRegion(), policy);

        if (card.votesRequired() == 0 || !draftSent)
        {
          setupCard(card);
          getClient().getCommModule().send(Endpoint.DRAFT_CARD, card, null);
          //getClient().send(new RequestFactory().build(getClient().getStartNanoSec(), card, Endpoint.DRAFT_CARD));
          draftedCard = true;
          draftSent = true;
          if (i == 0)
          {
            continue;
          }
          if ( i == getClient().getUser().getHand().size()-1)
          {
            return;
          }

        }
      i++;
    }
    setupCard(card);

    if (card == null)
    {
      return;
    }

    new RequestFactory().build(getClient().getStartNanoSec(),
                         card,
                         Endpoint.DRAFT_CARD);
    cardDrafted = card;
    draftedCard = true;
    tries = 2;
  }

  private void setupCard(GameCard card)
  {
    if (card == null)
    {
      return;
    }

    ArrayList<Integer> legalValueList = card.getOptionsOfVariable();
    EnumFood[] food=card.getValidTargetFoods();
    EnumRegion[] regions=card.getValidTargetRegions();

    if (legalValueList == null) card.setX(0);
    else
    {
      card.setX(legalValueList.get(Util.rand.nextInt(legalValueList.size())));
    }


    if(food!=null)
    {
      card.setTargetFood(food[Util.rand.nextInt(food.length)]);
    }
    if(regions!=null)
    {
      card.setTargetRegion(regions[Util.rand.nextInt(regions.length)]);
    }
  }
}
