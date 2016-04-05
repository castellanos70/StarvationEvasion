package starvationevasion.ai.commands;


import starvationevasion.ai.AI;
import starvationevasion.common.EnumPolicy;
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
    if (getClient().getUser().getHand() == null || getClient().getUser().getHand().size() == 0)
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
