package starvationevasion.server.handlers;


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
      String uname = (String) request.getData().get("username");
      String pwd = (String) request.getData().get("password");

      boolean auth = authenticate(uname, pwd);
      if (auth)
      {
        Payload data = new Payload();
        data.put("data", getClient().getUser());
        data.put("message", "SUCCESS");
        m_response = new Response(server.uptime(), data);
      }
      else
      {
        m_response = new Response(server.uptime(), "FAIL");
      }
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
        s.setActive(true);
        getClient().setUser(s);
        return true;
      }
    }

    return false;
  }
}
