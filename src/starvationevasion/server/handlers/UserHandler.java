package starvationevasion.server.handlers;


import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.*;
import starvationevasion.sim.Simulator;

public class UserHandler extends AbstractHandler
{

  public UserHandler (ServerMaster server, ClientWorker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {

    if (request.getPath().contains("user/login"))
    {
      // get the user info
      if (request.getRequest() == ActionType.GET)
      {
        System.out.println("getting");
        m_response = new Response(ActionType.SUCCESS,
                                  serverMaster.uptime(),
                                  serverMaster.getUser(getClient().getName()).toJson());
        return true;
      }
      // update a user
      else if (request.getRequest() == ActionType.PUT)
      {
        System.out.println("updated user");
      }

    }
    else if (request.getPath().contains("user/hand"))
    {
      // get the user info
      if (request.getRequest() == ActionType.GET)
      {
        // serverMaster.getHand()
        // m_response = new Response(ActionType.SUCCESS, serverMaster.uptime(), new JSONDocument(JSONDocument.Type.OBJECT));
        // parse the hand and create rsponse
        return true;
      }
      // create a user hand
      else if (request.getRequest() == ActionType.POST)
      {

      }
      // update a user hand
      else if (request.getRequest() == ActionType.PUT)
      {

      }
    }
    else if (request.getPath().contains("users"))
    {
      // create a user
      if (request.getRequest() == ActionType.POST)
      {
        System.out.println("new user");
        if (serverMaster.addUser(new User(request.getPayload()), getClient()))
        {

          m_response = new Response(ActionType.SUCCESS, serverMaster.uptime(), getUser());
        }
        else
        {
          m_response = new Response(ActionType.FAIL, serverMaster.uptime(), getUser());

        }
        return true;
      }
    }
    return false;
  }

  JSONDocument getUser ()
  {
    JSONDocument obj = new JSONDocument(JSONDocument.Type.OBJECT);
    obj.setString("username", "admin");
    obj.setString("password", "hi");
    obj.setString("region", "Eastern");

    return obj;
  }
}
