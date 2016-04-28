package starvationevasion.communication.commands;


import starvationevasion.ai.AI;
import starvationevasion.communication.AITest;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.RequestFactory;

public class Hand extends AbstractCommand
{

  private int tries = 3;
  public Hand (AITest client)
  {
    super(client);
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
      getClient().getCommModule().send(Endpoint.HAND_READ, null, null);
      //getClient().send(new RequestFactory().build(getClient().getStartNanoSec(),
                                            //Endpoint.HAND_READ));
      return true;
    }
    return false;

  }
}
