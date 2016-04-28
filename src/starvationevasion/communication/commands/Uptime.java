package starvationevasion.communication.commands;


import starvationevasion.ai.AI;
import starvationevasion.communication.AITest;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.RequestFactory;

public class Uptime extends AbstractCommand
{

  public Uptime (AITest client)
  {
    super(client);
  }

  @Override
  public boolean run ()
  {

    if (getClient().getStartNanoSec() == 0)
    {
      getClient().getCommModule().send(Endpoint.SERVER_UPTIME, null, null);
      //getClient().send(new RequestFactory().build(getClient().getStartNanoSec(), Endpoint.SERVER_UPTIME));
      return true;
    }
    return false;

  }
}