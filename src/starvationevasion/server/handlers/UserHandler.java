package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.EnumRegion;
import starvationevasion.common.Tuple;
import starvationevasion.common.Util;
import starvationevasion.server.*;
import starvationevasion.server.model.*;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class UserHandler extends AbstractHandler
{

  public UserHandler (BlockingQueue<Tuple<User, Request>> server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {

    if (request.getDestination().equals(Endpoint.USER_CREATE))
    {


      return true;
    }
    else if (request.getDestination().equals(Endpoint.USERS))
    {

      return true;
    }
    else if (request.getDestination().equals(Endpoint.USERS_LOGGED_IN))
    {

      return true;
    }
    else if (request.getDestination().equals(Endpoint.USERS_READY))
    {

      return true;
    }
    else if (request.getDestination().equals(Endpoint.READY))
    {


      return true;
    }
    else if (request.getDestination().equals(Endpoint.USER_READ))
    {

      return true;
    }

    return false;
  }


}
