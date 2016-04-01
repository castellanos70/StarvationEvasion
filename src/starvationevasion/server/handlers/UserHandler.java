package starvationevasion.server.handlers;



import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.*;
import starvationevasion.server.model.*;

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

      String uname = (String) request.getData().get("username");
      String pwd = (String) request.getData().get("password");

      if (server.addUser(new User(uname, pwd, null, new ArrayList<>())))
      {
        m_response = new Response(server.uptime(), "SUCCESS");
      }
      else
      {
        m_response = new Response(server.uptime(), "Authentication failed.");
      }
      getClient().send(m_response);
      return true;
    }
    else if (request.getDestination().equals(Endpoint.USERS))
    {
//      JSONDocument doc = JSONDocument.createArray(server.getUserCount());

//      for (User user : server.getUserList())
//      {
//        doc.array().add(user.toJSON());
//      }

      Payload data = new Payload();
      data.put("data", server.getUserList());
      // data.put("message", "SUCCESS");

      m_response = new Response(server.uptime(), data);
      getClient().send(m_response);
      return true;
    }
    else if (request.getDestination().equals(Endpoint.USERS_LOGGED_IN))
    {
      ArrayList list = new ArrayList<User>();

      // JSONDocument doc = JSONDocument.createArray(server.getActiveCount());
      for (User user : server.getActiveUserList())
      {
        list.add(user);
      }

      Payload data = new Payload();
      data.put("data", server.getActiveUserList());

      m_response = new Response(server.uptime(), data);
      getClient().send(m_response);
      return true;
    }

    return false;
  }


}
