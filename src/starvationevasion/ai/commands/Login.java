package starvationevasion.ai.commands;

import starvationevasion.ai.AI;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.RequestFactory;

import static starvationevasion.common.Constant.AI_NAMES;

public class Login extends AbstractCommand
{

  private String chosenUsername = "";
  private boolean requestSent = false;

  public Login (AI client)
  {
    super(client);
  }

  @Override
  public boolean run ()
  {
    if (!requestSent && getClient().getUsers().size() == 0)
    {
      getClient().send(new RequestFactory().build(getClient().getStartNanoSec(),
                                            Endpoint.USERS_LOGGED_IN));
      requestSent = true;
      return true;
    }
    if (chosenUsername.isEmpty())
    {
      for (String aiName : AI_NAMES)
      {
        if (!getClient().getUsers().stream().anyMatch(user -> user.getUsername().equals(aiName)))
        {
          chosenUsername = aiName;
          break;
        }
      }

      return true;
    }

    if (getClient().getUser() == null)
    {

      getClient().send(new RequestFactory().login(getClient().getStartNanoSec(),
                                            chosenUsername,
                                            "bot",
                                            null));
      return true;
    }
    return false;

  }
}
