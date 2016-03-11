package starvationevasion.server.handlers;



import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.*;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.User;

import java.util.ArrayList;

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

      String uname = request.chomp();
      String pwd = request.chomp();

      if (server.addUser(new User(uname, pwd, null, new ArrayList<>())))
      {
        m_response = new Response(server.uptime(), "", "SUCCESS");
      }
      else
      {
        m_response = new Response(server.uptime(), "", "Authentication failed.");
      }
      getClient().send(m_response);
      return true;
    }
    else if (request.getDestination().equals(Endpoint.USERS))
    {
      JSONDocument doc = JSONDocument.createArray(server.getUserCount());

      for (User user : server.getUserList())
      {
        doc.array().add(user.toJSON());
      }
      m_response = new Response(server.uptime(), doc);
      getClient().send(m_response);
      return true;
    }
    else if (request.getDestination().equals(Endpoint.USERS_LOGGED_IN))
    {
      JSONDocument doc = JSONDocument.createArray(server.getActiveCount());
      for (User user : server.getActiveUserList())
      {
        doc.array().add(user.toJSON());
      }
      m_response = new Response(server.uptime(), doc);
      getClient().send(m_response);
      return true;
    }

    return false;
  }


}
