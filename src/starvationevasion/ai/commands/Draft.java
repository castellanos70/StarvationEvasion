package starvationevasion.ai.commands;

import starvationevasion.ai.AI;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.Util;
import starvationevasion.common.policies.EnumPolicy;
import starvationevasion.common.policies.PolicyCard;
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
  private PolicyCard cardDrafted;
  private int tries = 2;

  public Draft (AI client)
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
      getClient().send(new RequestFactory().build(getClient().getStartNanoSec(), new Payload(), Endpoint.DONE));

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
      PolicyCard _card = PolicyCard.create(getClient().getUser().getRegion(), policy);
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

      getClient().send(new RequestFactory().build(getClient().getStartNanoSec(),
                                            discard,
                                            Endpoint.DELETE_CARD));

    }
    discarded = true;
  }

  private void randomlySetDraftedCard ()
  {
    PolicyCard card = null;

    int i = 0;
    boolean draftSent = false;
    for (EnumPolicy policy : getClient().getUser().getHand())
    {
      card = PolicyCard.create(getClient().getUser().getRegion(), policy);

        if (card.votesRequired() == 0 || !draftSent)
        {
          setupCard(card);
          getClient().send(new RequestFactory().build(getClient().getStartNanoSec(), card, Endpoint.DRAFT_CARD));
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

  private void setupCard(PolicyCard card)
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
