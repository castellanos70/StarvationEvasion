package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.Tuple;
import starvationevasion.server.Worker;
import starvationevasion.server.Server;
import starvationevasion.server.model.*;

import java.util.concurrent.BlockingQueue;

public class LoginHandler extends AbstractHandler
{
  public LoginHandler (BlockingQueue<Tuple<User, Request>> server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {


    return false;
  }

  private boolean authenticate(String username, String password)
  {
//    User s = server.getUserByUsername(username);
//
//    if (s != null)
//    {
//      String salt = s.getSalt();
//      String hash = Encryptable.generateHashedPassword(salt, password);
//      if (s.getPassword().equals(hash))
//      {
//        s.setLoggedIn(true);
//        getClient().setUsername(s.getUsername());
//        s.setWorker(getClient());
//        return true;
//      }
//    }

    return false;
  }
}
