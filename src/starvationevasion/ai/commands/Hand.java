package starvationevasion.ai.commands;


import starvationevasion.ai.AI;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;
import starvationevasion.common.policies.CovertIntelligencePolicy;
import starvationevasion.common.policies.EthanolTaxCreditChangePolicy;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.State;

import java.util.Random;

public class Hand extends AbstractCommand
{
  public Hand (AI client)
  {
    super(client);
  }

  @Override
  public boolean run ()
  {
    if (getClient().getUser().getHand() == null || getClient().getUser().getHand().size() <= 6)
    {
      getClient().send(new Request(getClient().getStartNanoSec(), Endpoint.HAND_READ));

      return true;
    }


    // Discard a card
    System.out.println("Checking phase");
    if (!getClient().getState().equals(State.DRAFTING))
    {
      return true;
    }

    if (getClient().getState().equals(State.DRAFTING))
    {

      System.out.println("drafting card");
      Request r = new Request(getClient().getStartNanoSec(), Endpoint.DRAFT_CARD);
      Payload data = new Payload();
      PolicyCard card = PolicyCard.create(getClient().getUser().getRegion(), getClient().getUser().getHand().get(0));

      for (EnumPolicy policy : getClient().getUser().getHand())
      {
        card = PolicyCard.create(getClient().getUser().getRegion(), policy);

        if (card.votesRequired() == 0)
        {
          break;
        }
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
        if(card.getRequiredVariables(variable) != null)
        {
          int amt = 10;
          if(card.getRequiredVariables(variable).equals(PolicyCard.EnumVariableUnit.MILLION_DOLLAR))
          {
            amt = 1;
          }
          if(card.getRequiredVariables(variable).equals(PolicyCard.EnumVariableUnit.PERCENT))
          {
            amt = 10;
          }
          if(card.getRequiredVariables(variable).equals(PolicyCard.EnumVariableUnit.UNIT))
          {
            amt = 30;
          }

          if (variable.name().equals("X"))
          {
            card.setX(amt);
            if (card instanceof CovertIntelligencePolicy)
            {
              card.setX(2);
              break;
            }
          }
          else if (variable.name().equals("Y"))
          {
            card.setX(amt);
          }
          else if (variable.name().equals("Z"))
          {
            card.setZ(amt);
          }
        }

      }
      data.putData(card);
      r.setData(data);
      getClient().send(r);
      return true;
    }

    if (getClient().getUser().getHand().size() == 7)
    {
      System.out.println("discarding");
      Request r = new Request(getClient().getStartNanoSec(), Endpoint.DELETE_CARD);
      Payload data = new Payload();

      EnumPolicy card = getClient().getUser().getHand().remove(3);
      data.putData(card);

      r.setData(data);
      getClient().send(r);

      return false;
    }

    return false;

  }
}
