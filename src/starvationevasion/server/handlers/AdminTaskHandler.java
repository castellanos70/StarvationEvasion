package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.Server;
import starvationevasion.server.Worker;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.Response;

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
    if(getClient().getUser().getUsername().equals("admin"))
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
      else if (request.getDestination().equals(Endpoint.AI))
      {
        server.startAI();
        return true;
      }
      else if (request.getDestination().equals(Endpoint.KILL_AI))
      {
        server.killAI();
        return true;
      }

      else if (request.getDestination().equals(Endpoint.TOTAL_PLAYERS))
      {
        long num = (long) request.getPayload().getData();
        Server.TOTAL_PLAYERS = (int) num;
        return true;
      }

    }

    return false;
  }
}
