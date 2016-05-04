package starvationevasion.communication.AITest.commands;

import starvationevasion.communication.AITest.AI;
import starvationevasion.server.model.Endpoint;

public class Uptime extends AbstractCommand
{

  public Uptime(AI client)
  {
    super(client);
  }

  @Override
  public boolean run()
  {

    if (getClient().getStartNanoSec() == 0)
    {
      getClient().getCommModule().send(Endpoint.SERVER_UPTIME, null, null);
      // getClient().send(new
      // RequestFactory().build(getClient().getStartNanoSec(),
      // Endpoint.SERVER_UPTIME));
      return true;
    }
    return false;

  }

  @Override
  public String commandString()
  {
    // TODO Auto-generated method stub
    return "Uptime";
  }
}