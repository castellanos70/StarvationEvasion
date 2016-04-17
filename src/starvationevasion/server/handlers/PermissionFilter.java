package starvationevasion.server.handlers;


import starvationevasion.server.Server;
import starvationevasion.server.Worker;
import starvationevasion.server.model.Request;

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
    return !getClient().getUser().isLoggedIn();
  }
}
