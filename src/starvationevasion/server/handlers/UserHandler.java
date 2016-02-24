package starvationevasion.server.handlers;



import starvationevasion.server.*;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.User;

public class UserHandler extends AbstractHandler
{

  public UserHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {

    if (request.getDestination().equals(Endpoint.USER_CREATE))
    {
      if (server.addUser(new User(request.getData())))
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
    else if (request.getDestination().equals(Endpoint.USERS))
    {
      StringBuilder stringBuilder = new StringBuilder();
      for (User user : server.getUserList())
      {
        stringBuilder.append(user.toString()).append(" ");
      }
      m_response = new Response(server.timeDiff(), stringBuilder.toString());
      getClient().send(m_response.toString());
      return true;
    }

    return false;
  }


}
