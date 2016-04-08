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
    Response m_response = new Response("");
    if (request.getDestination().equals(Endpoint.LOGIN))
    {
      String uname = (String) request.getPayload().get("username");
      String pwd = (String) request.getPayload().get("password");
      Payload data = new Payload();

      boolean auth = authenticate(uname, pwd);
      if (auth)
      {
        data.putData(server.getUserByUsername(uname));
        data.putMessage("SUCCESS");
      }
      else
      {
        data.putMessage("FAIL");
      }
      m_response = new Response(server.uptime(), data);
      m_response.setType(Type.AUTH);
      getClient().send(m_response);

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
      String hash = Encryptable.generateHashedPassword(salt, password);
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
