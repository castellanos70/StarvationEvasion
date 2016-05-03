package starvationevasion.communication.AITest;

import starvationevasion.common.gamecards.GameCard;
import starvationevasion.common.WorldData;
import starvationevasion.communication.Communication;
import starvationevasion.communication.AITest.commands.Command;
import starvationevasion.communication.AITest.commands.Draft;
import starvationevasion.communication.AITest.commands.GameState;
import starvationevasion.communication.AITest.commands.Login;
import starvationevasion.communication.AITest.commands.Uptime;
import starvationevasion.communication.AITest.commands.Vote;
import starvationevasion.communication.ConcurrentCommModule;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.State;
import starvationevasion.server.model.Type;
import starvationevasion.server.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This is just a test/proof-of-concept for the CommModule implementation of the Communication interface.
 */
public class AI
{
  private final Communication COMM;
  private User u;
  private ArrayList<User> users = new ArrayList<>();
  private State state = null;
  private ArrayList<WorldData> worldData;
  private List<GameCard> ballot;
  private Stack<Command> commands = new Stack<>();
  private volatile boolean isRunning = true;

  public AI(String host, int port)
  {
    COMM = new ConcurrentCommModule(host, port);
    // Try to connect and then see how it went
    COMM.connect();
    isRunning = COMM.isConnected();

    // Add the starting commands
    commands.add(new GameState(this));
    commands.add(new Uptime(this));
    commands.add(new Login(this));

    aiLoop();
    COMM.dispose();
  }

  private void aiLoop()
  {
    while(isRunning)
    {
      try
      {
        isRunning = COMM.isConnected();

        // Ask the communication module to give us any server response events it has received
        // since the last call
        ArrayList<Response> responses = COMM.pollMessages();
        processServerInput(responses);

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

  private void processServerInput(ArrayList<Response> responses)
  {
    for (Response response : responses)
    {
      Type type = response.getType();
      Object data = response.getPayload().getData();

      if (type == Type.AUTH_SUCCESS)
      {
        u = (User)data;
        COMM.sendChat("ALL", "Hi, I am " + u.getUsername() + ". I'll be playing using (crappy) AI.", null);
      }
      else if (type == Type.USER)
      {
        u = (User)data;
        COMM.sendChat("ALL", "User updated: " + u.getUsername(), null);
      }
      else if (type == Type.WORLD_DATA_LIST) worldData = (ArrayList<WorldData>)data;
      else if (type == Type.WORLD_DATA) worldData.add((WorldData)data);
      else if (type == Type.USERS_LOGGED_IN_LIST) users = (ArrayList<User>)data;
      else if (type == Type.VOTE_BALLOT) ballot = (List<GameCard>)data;
      else if (type == Type.GAME_STATE)
      {
        state = (State)data;
        if (state == starvationevasion.server.model.State.VOTING)
        {
          AI.this.commands.add(new Vote(AI.this));
        }
        else if (state == starvationevasion.server.model.State.DRAFTING)
        {
          AI.this.commands.add(new Draft(AI.this));
        }
        else if (state == starvationevasion.server.model.State.DRAWING)
        {
          // AI.this.commands.add(new Draft(AI.this));
          commands.clear();
        }
      }
    }
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

    new AI(host, port);
  }
}
