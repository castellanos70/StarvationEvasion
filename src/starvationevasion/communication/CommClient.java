package starvationevasion.communication;

import starvationevasion.common.WorldData;
import starvationevasion.server.model.State;
import starvationevasion.server.model.User;

import java.util.Collection;

/**
 * This interface MUST be implemented by any client that communicates with the server, this includes
 * the game player as well as any AI objects. This will create a uniform way that
 * the server knows how it can interact with any of its clients.
 */
public interface CommClient
{
  void setStartNanoSec(double nanoSec);

  void setWorldData(Collection<WorldData> data);

  void setState(State state);

  void setUser(User user);

  void setUsers(Collection<User> users);
}
