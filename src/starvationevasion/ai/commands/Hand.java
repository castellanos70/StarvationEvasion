package starvationevasion.ai.commands;

import starvationevasion.ai.AI;
import starvationevasion.server.model.Endpoint;

public class Hand extends AbstractCommand
{

  private int tries = 3;

  public Hand(AI client)
  {
    super(client);
  }

  @Override
  public boolean run()
  {
    System.out.println("Initial hand size:"+getClient().getUser().getHand().size());
    if (tries == 0)
    {
      return false;
    }

    if (getClient().getUser().getHand() == null
        || getClient().getUser().getHand().size() <= 6)
    {
      tries--;
      getClient().getCommModule().send(Endpoint.HAND_READ, null, null);
      // getClient().send(new
      // RequestFactory().build(getClient().getStartNanoSec(),
      // Endpoint.HAND_READ));
      return true;
    }
    return false;

  }

  @Override
  public String commandString()
  {
    return "Hand";
  }
}
