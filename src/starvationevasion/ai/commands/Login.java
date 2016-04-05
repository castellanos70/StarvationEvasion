package starvationevasion.ai.commands;

import starvationevasion.ai.AI;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.server.model.Request;

public class Login extends AbstractCommand
{

  public Login (AI client)
  {
    super(client);
  }

  @Override
  public boolean run ()
  {
    if (getClient().getUser() == null)
    {
      Request loginRequest = new Request(getClient().getStartNanoSec(), Endpoint.LOGIN);
      Payload data = new Payload();

      data.put("username", "admin");
      data.put("password", "admin");

      loginRequest.setData(data);
      getClient().send(loginRequest);
      return true;
    }
    return false;

  }
}
