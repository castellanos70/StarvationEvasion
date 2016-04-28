package starvationevasion.communication;

import starvationevasion.common.gamecards.GameCard;
import starvationevasion.common.WorldData;
import starvationevasion.communication.commands.Command;
import starvationevasion.communication.commands.Draft;
import starvationevasion.communication.commands.GameState;
import starvationevasion.communication.commands.Login;
import starvationevasion.communication.commands.Uptime;
import starvationevasion.communication.commands.Vote;
import starvationevasion.server.model.State;
import starvationevasion.server.model.Type;
import starvationevasion.server.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This is just a test/proof-of-concept for the CommModule implementation of the Communication interface.
 */
public class AITest
{
  private final CommModule COMM;
  private User u;
  private ArrayList<User> users = new ArrayList<>();
  private State state = null;
  private ArrayList<WorldData> worldData;
  private List<GameCard> ballot;
  private Stack<Command> commands = new Stack<>();
  private volatile boolean isRunning = true;

  public AITest(String host, int port)
  {
    COMM = new CommModule(host, port);
    isRunning = COMM.isConnected();

    // Add the starting commands
    commands.add(new GameState(this));
    commands.add(new Uptime(this));
    commands.add(new Login(this));

    // Set up the response listeners
    COMM.setResponseListener(Type.AUTH_SUCCESS, (type, data) ->
    {
      u = (User)data;
      COMM.sendChat("ALL", "Hi, I am " + u.getUsername() + ". I'll be playing using (crappy) AI.", null);
    });
    COMM.setResponseListener(Type.USER, (type, data) ->
    {
      u = (User)data;
      COMM.sendChat("ALL", "User updated: " + u.getUsername(), null);
    });
    COMM.setResponseListener(Type.WORLD_DATA_LIST, (type, data) -> worldData = (ArrayList<WorldData>)data);
    COMM.setResponseListener(Type.USERS_LOGGED_IN_LIST, (type, data) -> users = (ArrayList<User>)data);
    COMM.setResponseListener(Type.WORLD_DATA, (type, data) -> worldData.add((WorldData)data));
    COMM.setResponseListener(Type.VOTE_BALLOT, (type, data) -> ballot = (List<GameCard>)data);
    COMM.setResponseListener(Type.GAME_STATE, (type, data) ->
    {
      state = (State)data;
      if (state == starvationevasion.server.model.State.VOTING)
      {
        AITest.this.commands.add(new Vote(AITest.this));
      }
      else if (state == starvationevasion.server.model.State.DRAFTING)
      {
        AITest.this.commands.add(new Draft(AITest.this));
      }
      else if (state == starvationevasion.server.model.State.DRAWING)
      {
        // AI.this.commands.add(new Draft(AI.this));
        commands.clear();
      }
    });

    listenToUserRequests();
    COMM.dispose();
  }

  private void listenToUserRequests ()
  {
    while(isRunning)
    {
      try
      {
        isRunning = COMM.isConnected();

        // Ask the communication module to push any server response events it has received
        // since the last call
        COMM.pushResponseEvents();

        // if commands is empty check again
        if (commands.size() == 0) continue;

        // take off the top of the stack
        Command c = commands.peek();

        boolean runAgain = c.run();

        // if it does not need to run again pop
        if (!runAgain) commands.pop();
        // wait a little
        Thread.sleep(1000);
      }
      catch(InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }

  public Communication getCommModule()
  {
    return COMM;
  }

  public ArrayList<WorldData> getWorldData ()
  {
    return worldData;
  }

  public double getStartNanoSec ()
  {
    return COMM.getStartNanoTime();
  }

  public State getState ()
  {
    return state;
  }

  public User getUser ()
  {
    return u;
  }

  public Stack<Command> getCommands ()
  {
    return commands;
  }

  public List<GameCard> getBallot ()
  {
    return ballot;
  }

  public ArrayList<User> getUsers ()
  {
    return users;
  }

  public static void main (String[] args)
  {

    String host = null;
    int port = 0;

    try
    {
      host = args[0];
      port = Integer.parseInt(args[1]);
      if (port < 1) throw new Exception();
    }
    catch(Exception e)
    {
      System.exit(0);
    }

    new AITest(host, port);
  }
}
