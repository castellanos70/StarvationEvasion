package starvationevasion.server.handlers;


import starvationevasion.server.Connector;
import starvationevasion.server.Server;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.ResponseFactory;
import starvationevasion.server.model.Type;

public class PermissionFilter extends AbstractHandler
{
  public PermissionFilter (Server server, Connector client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    // No command will pass permission gate.
    boolean cannotPass = !getClient().getUser().isLoggedIn();
    if (cannotPass)
    {
      getClient().send(new ResponseFactory().build(server.uptime(),
                                                   null,
                                                   Type.AUTH,
                                                   "You must be logged in.",
                                                   request));
    }
    return cannotPass;
  }
}
