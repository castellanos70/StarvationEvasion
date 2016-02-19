package starvationevasion.server.handlers;


import starvationevasion.server.Worker;
import starvationevasion.server.Server;

public class LoginHandler extends AbstractHandler
{
  public LoginHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {

    return false;
  }
}
