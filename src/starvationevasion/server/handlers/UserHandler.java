package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.EnumRegion;
import starvationevasion.common.Util;
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
    Response m_response = new Response("");
    if (request.getDestination().equals(Endpoint.USER_CREATE))
    {

      String uname = (String) request.getPayload().get("username");
      String pwd = (String) request.getPayload().get("password");
      EnumRegion region=(EnumRegion)request.getPayload().get("region");

      if (server.createUser(new User(uname, pwd, region, new ArrayList<>())))
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
      Payload data = new Payload();
      data.putData(server.getLoggedInUsers());

      m_response = new Response(server.uptime(), data);
      m_response.setType(Type.USERS_LOGGED_IN_LIST);
      getClient().send(m_response);
      return true;
    }
    else if (request.getDestination().equals(Endpoint.USERS_READY))
    {
      Payload data = new Payload();
      data.putData(server.getPlayers());

      m_response = new Response(server.uptime(), data);
      m_response.setType(Type.USERS_READY_LIST);
      getClient().send(m_response);
      return true;
    }
    else if (request.getDestination().equals(Endpoint.READY))
    {
      User _u = getClient().getUser();

      Payload payload = new Payload();
      m_response = new Response(server.uptime(), payload);

      if (_u != null)
      {
        _u.setPlaying(true);

        // addPlayer() will set the region if not already set.
        boolean isPlaying = server.addPlayer(_u);
        if (isPlaying)
        {
          payload.putData(_u);
          payload.putMessage("SUCCESS");
          m_response.setType(Type.USER);
        }
        else
        {
          m_response = new Response(server.uptime(), "Sorry, " + _u.getUsername() + " there was an error.");
        }

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
