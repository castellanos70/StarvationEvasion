package starvationevasion.server.handlers;


import starvationevasion.server.Server;
import starvationevasion.server.Worker;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.ResponseFactory;
import starvationevasion.server.model.Type;

public class PermissionFilter extends AbstractHandler
{
  public PermissionFilter (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    // No command will pass permission gate.
    boolean isLoggedIn = !getClient().getUser().isLoggedIn();
    if (isLoggedIn)
    {
      getClient().send(new ResponseFactory().build(server.uptime(), null, Type.ERROR, "You must be logged in."));
    }
    return isLoggedIn;
  }
}
