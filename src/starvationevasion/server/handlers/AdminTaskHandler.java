package starvationevasion.server.handlers;


import starvationevasion.server.Server;
import starvationevasion.server.Worker;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Request;

public class AdminTaskHandler extends AbstractHandler
{
  public AdminTaskHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    // first check the credentials
    if(getClient().getUser() != null && getClient().getUser().getUsername().equals("admin"))
    {

      if (request.getDestination().equals(Endpoint.KILL))
      {
        server.killServer();
        return true;
      }


    }

    return false;
  }
}
