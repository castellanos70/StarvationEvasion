package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.Worker;
import starvationevasion.server.Server;
import starvationevasion.server.model.*;

public class LoginHandler extends AbstractHandler
{
  public LoginHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    if (request.getDestination().equals(Endpoint.LOGIN))
    {
      if (getClient().loggedIn())
      {
        getClient().send(new ResponseFactory().build(server.uptime(),
                                                     getClient().getUser(),
                                                     Type.ERROR, "Already logged in"));
        return true;
      }
      String uname = (String) request.getPayload().get("username");
      String pwd = (String) request.getPayload().get("password");

      boolean auth = authenticate(uname, pwd);
      if (auth)
      {
        getClient().send(new ResponseFactory().build(server.uptime(),
                                               server.getUserByUsername(uname),
                                               Type.AUTH_SUCCESS, "SUCCESS"
        ));
      }
      else
      {
        getClient().send(new ResponseFactory().build(server.uptime(),
                                               null,
                                               Type.AUTH_ERROR, "Username or password incorrect."
        ));
      }

      return true;
    }

    return false;
  }

  private boolean authenticate(String username, String password)
  {
    User s = server.getUserByUsername(username);

    if (s != null)
    {
      String salt = s.getSalt();
      String hash = Encryptable.bytesToHex(Encryptable.generateHashedPassword(salt.getBytes(),
                                                                              password.getBytes()));
      if (s.getPassword().equals(hash))
      {
        s.setLoggedIn(true);
        getClient().setUsername(s.getUsername());
        s.setWorker(getClient());
        return true;
      }
    }

    return false;
  }
}
