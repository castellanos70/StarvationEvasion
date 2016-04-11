package starvationevasion.ai.commands;


import starvationevasion.ai.AI;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.RequestFactory;

public class Uptime extends AbstractCommand
{

  public Uptime (AI client)
  {
    super(client);
  }

  @Override
  public boolean run ()
  {

    if (getClient().getStartNanoSec() == 0)
    {
      getClient().send(RequestFactory.build(getClient().getStartNanoSec(), Endpoint.SERVER_UPTIME));
      return true;
    }
    return false;

  }
}