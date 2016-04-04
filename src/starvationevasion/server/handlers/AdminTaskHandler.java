package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.Server;
import starvationevasion.server.Worker;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Request;

public class AdminTaskHandler extends AbstractHandler
{
  public AdminTaskHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    // first check the credentials
    if(getClient().getUser() != null && getClient().getUser().getUsername().equals("admin"))
    {

      if (request.getDestination().equals(Endpoint.KILL))
      {
        server.killServer();
        return true;
      }
      else if (request.getDestination().equals(Endpoint.STOP_GAME))
      {
        server.stopGame();
        return true;
      }
      else if (request.getDestination().equals(Endpoint.RESTART_GAME))
      {
        server.restartGame();
        return true;
      }

    }

    return false;
  }
}
