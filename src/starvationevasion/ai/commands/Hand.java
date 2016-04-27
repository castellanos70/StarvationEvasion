package starvationevasion.ai.commands;


import starvationevasion.ai.AI;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.RequestFactory;

public class Hand extends AbstractCommand
{

  private int tries = 3;
  public Hand (AI client)
  {
    super(client);
  }
  @Override
  public String commandString()
  {
    return "Hand";
  }
  @Override
  public boolean run ()
  {
    if (tries == 0)
    {
      return false;
    }

    if (getClient().getUser().getHand() == null || getClient().getUser().getHand().size() <= 6)
    {
      tries--;
      getClient().send(new RequestFactory().build(getClient().getStartNanoSec(),
                                            Endpoint.HAND_READ));
      return true;
    }
    return false;

  }
}
