package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.EnumRegion;
import starvationevasion.server.Server;
import starvationevasion.server.Worker;
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
    if (request.getDestination().equals(Endpoint.DONE))
    {
      getClient().getUser().isDone = true;
      return true;
    }
    if (request.getDestination().equals(Endpoint.USER_NEW))
    {
      getClient().send(new ResponseFactory().build(server.uptime(),
                                                   null,
                                                   Type.USER,
                                                   "",
                                                   request));
      return true;
    }
    if (request.getDestination().equals(Endpoint.USER_CREATE))
    {

      String uname = (String) request.getPayload().get("username");
      String pwd = (String) request.getPayload().get("password");
      EnumRegion region = null;

      if (request.getPayload().keySet().contains("region"))
      {
        try
        {
          region = (EnumRegion) request.getPayload().get("region");
        }
        catch(Exception e)
        {
          getClient().send(new ResponseFactory().build(server.uptime(),
                                                       null,
                                                       Type.CREATE_ERROR,
                                                       "Not a valid region. Remove completely to get a better result"));
          return true;
        }
      }
      if (server.createUser(new User(uname, pwd, region, new ArrayList<>())))
      {
        getClient().send(new ResponseFactory().build(server.uptime(),
                                               null,
                                               Type.CREATE_SUCCESS, "User created."
        ));
      }
      else
      {
        getClient().send(new ResponseFactory().build(server.uptime(),
                                               null,
                                               Type.CREATE_ERROR, "Username taken"
        ));
      }
      return true;
    }
    else if (request.getDestination().equals(Endpoint.USERS))
    {
      getClient().send(new ResponseFactory().build(server.uptime(),
                                             new Payload(server.getUserList()),
                                             Type.USERS, "", request));
      return true;
    }
    else if (request.getDestination().equals(Endpoint.USERS_LOGGED_IN))
    {
      getClient().send(new ResponseFactory().build(server.uptime(),
                                             new Payload(server.getLoggedInUsers()),
                                             Type.USERS_LOGGED_IN_LIST));
      return true;
    }
    else if (request.getDestination().equals(Endpoint.USERS_READY))
    {
      getClient().send(new ResponseFactory().build(server.uptime(),
                                             new Payload(server.getPlayers()),
                                             Type.USERS_READY_LIST));
      return true;
    }
    else if (request.getDestination().equals(Endpoint.READY) && getClient().getUser().isLoggedIn())
    {
      if (server.getGameState() == State.LOGIN)
      {
        User _u = getClient().getUser();

        if (_u != null)
        {
          _u.setPlaying(true);

          // addPlayer() will set the region if not already set.
          boolean isPlaying = server.addPlayer(_u);
          if (isPlaying)
          {
            getClient().send(new ResponseFactory().build(server.uptime(),
                                                         null,
                                                         Type.BROADCAST, "Success"
            ));
          }
          else
          {
            getClient().send(new ResponseFactory().build(server.uptime(),
                                                         null,
                                                         Type.BROADCAST,
                                                         "Sorry, " + _u.getUsername() +
                                                                 " there was an error." +
                                                                 " " + _u.getRegion().name()
            ));
          }

        }
      }
      else
      {
        getClient().send(new ResponseFactory().build(server.uptime(),
                                                     null,
                                                     Type.BROADCAST, "Game has already started"));
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


      getClient().send(new ResponseFactory().build(server.uptime(),
                                             data,
                                             Type.USER));
      return true;
    }

    return false;
  }


}
