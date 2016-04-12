package starvationevasion.ai.commands;

import starvationevasion.ai.AI;
import starvationevasion.common.*;
import starvationevasion.common.policies.CovertIntelligencePolicy;
import starvationevasion.common.policies.EfficientIrrigationIncentivePolicy;
import starvationevasion.common.policies.EthanolTaxCreditChangePolicy;
import starvationevasion.common.policies.FertilizerSubsidyPolicy;
import starvationevasion.server.model.*;

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

        return false;
      }

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
      if (policy != cardDrafted.getCardType() && _card.votesRequired() >= Util.randInt(0,2))
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

        if (card.votesRequired() == 0 && !draftSent)
        {
          setupCard(card);
          getClient().send(new RequestFactory().build(getClient().getStartNanoSec(), card, Endpoint.DRAFT_CARD));
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
    EnumFood[] foods = card.getValidTargetFoods();
    EnumRegion[] regions = card.getValidTargetRegions();

    if (foods != null)
    {
      card.setTargetFood(foods[0]);
    }
    if (regions != null)
    {
      card.setTargetRegion(regions[0]);
    }

    for (PolicyCard.EnumVariable variable : PolicyCard.EnumVariable.values())
    {
      if (card.getRequiredVariables(variable) != null)
      {
        int amt = 0;
        // Contextually setting variable
        if (card.getRequiredVariables(variable).equals(PolicyCard.EnumVariableUnit.MILLION_DOLLAR))
        {
          amt = Util.randInt(1, 20);
        }
        else if (card.getRequiredVariables(variable).equals(PolicyCard.EnumVariableUnit.PERCENT))
        {
          if (card instanceof EfficientIrrigationIncentivePolicy)
          {
            amt = Util.randInt(50, 80);
          }
          else if (card instanceof EthanolTaxCreditChangePolicy || card instanceof FertilizerSubsidyPolicy)
          {
            amt = Util.randInt(20, 40);
          }
          else
          {
            amt = Util.randInt(1, 100);
          }
        }
        else if (card.getRequiredVariables(variable).equals(PolicyCard.EnumVariableUnit.UNIT))
        {
          amt = Util.randInt(1, 10);
        }

        if (variable.name().equals("X"))
        {
          card.setX(amt);
          if (card instanceof CovertIntelligencePolicy)
          {
            if (Util.rand.nextBoolean())
            {
              card.setX(2);
            }
            else
            {
              card.setX(7);
            }
            break;
          }
        }
        else if (variable.name().equals("Y"))
        {
          card.setY(amt);
        }
        else if (variable.name().equals("Z"))
        {
          card.setZ(amt);
        }
      }

    }
  }
}
