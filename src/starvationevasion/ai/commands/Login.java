package starvationevasion.ai.commands;

import starvationevasion.ai.AI;
import starvationevasion.common.EnumRegion;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.RequestFactory;

public class Login extends AbstractCommand
{
  private static final String[] AI_NAMES =
          {"Emma", "Olivia", "Noah", "Sophia", "Liam", "Mason", "Isabella", "Jacob", "William", "Ethan"};

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
      getClient().send(RequestFactory.build(getClient().getStartNanoSec(),
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

      getClient().send(RequestFactory.login(getClient().getStartNanoSec(),
                                            chosenUsername,
                                            "bot",
                                            null));
      return true;
    }
    return false;

  }
}
