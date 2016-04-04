package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.EnumRegion;
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
      Payload data = new Payload();
      data.putData(server.getUserList());

      m_response = new Response(server.uptime(), data);
      getClient().send(m_response);
      return true;
    }
    else if (request.getDestination().equals(Endpoint.USERS_LOGGED_IN))
    {
      ArrayList list = new ArrayList<User>();
      for (User user : server.getActiveUserList())
      {
        list.add(user);
      }

      Payload data = new Payload();
      data.putData(server.getActiveUserList());

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
        m_response = new Response(server.uptime(), "SUCCESS");
        getClient().send(m_response);
      }

      return true;
    }
    else if (request.getDestination().equals(Endpoint.USER_READ))
    {
      User u = null;
      if (request.getPayload().containsKey("username"))
      {
        u = server.getUserByUsername((String) request.getPayload().get("username"));
      }
      else if (request.getPayload().containsKey("region"))
      {
        u = server.getWorkerByRegion((EnumRegion) request.getPayload().get("region")).getUser();
      }

      Payload data = new Payload();
      data.putData(u);

      m_response = new Response(data);
      getClient().send(m_response);

      return true;
    }

    return false;
  }


}
