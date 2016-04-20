package starvationevasion.communication;

import starvationevasion.common.PolicyCard;
import starvationevasion.common.WorldData;
import starvationevasion.ai.commands.Command;
import starvationevasion.server.model.State;
import starvationevasion.server.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * I tried to make a good comment here but it failed really hard.
 */
public interface CommClient
{
  Collection<WorldData> getWorldData();

  double getStartNanoSec();

  State getState();

  User getUser();

  CommModule getCommModule();

  List<PolicyCard> getBallot();

  Collection<User> getUsers();

  Stack<Command> getCommands();

  void setWorldData(Collection<WorldData> data);

  void setState(State state);

  void setUser(User user);

  void setUsers(Collection<User> users);

  void setCommands(Stack<Command> commands);
}
