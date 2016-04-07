package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.EnumRegion;
import starvationevasion.common.Tuple;
import starvationevasion.server.Worker;
import starvationevasion.server.Server;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.User;

import java.util.concurrent.BlockingQueue;


public class ChatHandler extends AbstractHandler
{
  public ChatHandler (BlockingQueue<Tuple<User, Request>> server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    if (request.getDestination().equals(Endpoint.CHAT))
    {

      return true;
    }
    return false;
  }
}
