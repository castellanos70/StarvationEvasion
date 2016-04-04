package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

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

      String uname = (String) request.getPayload().get("username");
      String pwd = (String) request.getPayload().get("password");

      if (server.createUser(new User(uname, pwd, null, new ArrayList<>())))
      {
        m_response = new Response(server.uptime(), "SUCCESS");
      }
      else
      {
        m_response = new Response(server.uptime(), "Could not create user");
      }
      getClient().send(m_response);
      return true;
    }
    else if (request.getDestination().equals(Endpoint.USERS))
    {
      Payload data = new Payload();
      data.putData(server.getUserList());

      m_response = new Response(server.uptime(), data);
      getClient().send(m_response);
      return true;
    }
    else if (request.getDestination().equals(Endpoint.USERS_LOGGED_IN))
    {
      ArrayList list = new ArrayList<User>();
      for (User user : server.getLoggedInUsers())
      {
        list.add(user);
      }

      Payload data = new Payload();
      data.putData(server.getLoggedInUsers());

      m_response = new Response(server.uptime(), data);
      getClient().send(m_response);
      return true;
    }
    else if (request.getDestination().equals(Endpoint.READY))
    {
      User _u = getClient().getUser();
      if (_u != null && _u.getRegion() != null)
      {
        _u.setPlaying(true);
        server.addPlayer(_u);
        m_response = new Response(server.uptime(), "SUCCESS");
        getClient().send(m_response);
      }

      return true;
    }
    else if (request.getDestination().equals(Endpoint.USER_READ))
    {
      Payload data = new Payload();

      if (request.getPayload().containsKey("username"))
      {
        data.putData(server.getUserByUsername((String) request.getPayload().get("username")));
      }
      else if (request.getPayload().containsKey("region"))
      {
        ArrayList<User> _users = new ArrayList<>();
        for (User user : server.getUserList())
        {
          _users.add(user);
        }
        data.putData(_users);
      }


      m_response = new Response(data);
      getClient().send(m_response);

      return true;
    }

    return false;
  }


}
