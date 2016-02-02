package starvationevasion.server.handlers;

import starvationevasion.server.Worker;
import starvationevasion.server.Request;
import starvationevasion.server.Server;

public class ChatHandler extends AbstractHandler
{
  public ChatHandler (Server server, Worker client)
  {
    super(server, client);
  }
  @Override
  protected boolean handleRequestImpl (Request request)
  {
    return false;
  }
}
