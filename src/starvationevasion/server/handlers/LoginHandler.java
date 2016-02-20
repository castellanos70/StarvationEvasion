package starvationevasion.server.handlers;


import starvationevasion.server.Worker;
import starvationevasion.server.Server;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.User;

public class LoginHandler extends AbstractHandler
{
  public LoginHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    if (request.getDestination() == Endpoint.LOGIN)
    {
      boolean auth = authenticate(request.getData()[0], request.getData()[1]);
      if (auth)
      {
        m_response = new Response(server.timeDiff(), "SUCCESS");
      }
      else
      {
        m_response = new Response(server.timeDiff(), "FAIL");
      }
      getClient().send(m_response.toString());

      return true;
    }

    return false;
  }

  private boolean authenticate(String username, String password)
  {
    User s = server.getUserByUsername(username);
    return s != null && s.getPassword().equals(password);

  }
}
