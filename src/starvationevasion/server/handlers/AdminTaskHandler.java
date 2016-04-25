package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.Connector;
import starvationevasion.server.Server;
import starvationevasion.server.model.*;

public class AdminTaskHandler extends AbstractHandler
{
  public AdminTaskHandler (Server server, Connector client)
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
        boolean started = false;
        if (server.getGameState() != State.LOGIN)
        {
          // send out notification?
          server.stopGame();
          started = true;
        }
        Server.TOTAL_PLAYERS = (int) num;

        server.broadcast(new ResponseFactory().build(server.uptime(),
                                                     null,
                                                     Type.SUCCESS, "Total players: " + Server.TOTAL_PLAYERS));
        if (started)
        {
          server.restartGame();
        }

        return true;
      }

    }
    else
    {
      getClient().send(new ResponseFactory().build(server.uptime(),
                                                   null,
                                                   Type.ERROR,
                                                   "You do not have permission to do this."));
    }

    return false;
  }
}
