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
 * This interface MUST be implemented by any client that communicates with the server, this includes
 * the game player as well as any AI objects. This will create a uniform way that
 * the server knows how it can interact with any of its clients.
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
