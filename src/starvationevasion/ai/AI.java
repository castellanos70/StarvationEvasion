package starvationevasion.ai;


import starvationevasion.ai.commands.*;
import starvationevasion.common.PolicyCard;
import starvationevasion.common.WorldData;
import starvationevasion.server.model.*;
import starvationevasion.util.SocketConnection;
import starvationevasion.util.listeners.SocketEventListener;
import starvationevasion.util.events.AuthenticationEvent;
import starvationevasion.util.events.Event;
import starvationevasion.util.events.SocketConnectionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class AI implements SocketEventListener
{

  private User u;
  private ArrayList<User> users = new ArrayList<>();

  private State state = null;

  private ArrayList<WorldData> worldData;

  private List<PolicyCard> ballot;

  // time of server start
  private double startNanoSec = 0;

//  private StreamListener listener;
  private volatile boolean isRunning = true;

  private volatile boolean isConnected = false;
  private final SocketConnection connection;

  private Stack<Command> commands = new Stack<>();



  private AI (String host, int portNumber)
  {
    connection = new SocketConnection("localhost", 5555, this);

//    commands.add(new GameState(this));
//    commands.add(new Uptime(this));
    commands.add(new Login(this));


    listenToUserRequests();

  }



  private void listenToUserRequests ()
  {
    while(isRunning)
    {
      try
      {
        // if commands is empty check again
        if (commands.size() == 0 || !isConnected)
        {
          continue;
        }

        // take off the top of the stack
        Command c = commands.peek();

        boolean runAgain = c.run();

        // if it does not need to run again pop
        if (!runAgain)
        {
          commands.pop();
        }
        // wait a little
        Thread.sleep(1000);
      }
      catch(InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }

  public ArrayList<WorldData> getWorldData ()
  {
    return worldData;
  }

  public double getStartNanoSec ()
  {
    return startNanoSec;
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

  public List<PolicyCard> getBallot ()
  {
    return ballot;
  }

  public ArrayList<User> getUsers ()
  {
    return users;
  }

  public void send(Request request)
  {
    System.out.println(request);
    connection.send(request);
  }

  @Override
  public void onReceive (Event e)
  {

    System.out.println("e = " + e);

    if (e instanceof AuthenticationEvent.Success)
    {
      u = ((AuthenticationEvent.Success) e).getData();
      System.out.println(u.toJSON().toJSON());
    }
    if (e instanceof AuthenticationEvent.Fail)
    {
      System.out.println("wrong");
    }
    else if (e instanceof SocketConnectionEvent.Success)
    {
      isConnected = true;
    }
    else if (e instanceof SocketConnectionEvent.Fail)
    {
      isConnected = false;
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
      if (port < 1)
      {
        throw new Exception();
      }
    }
    catch(Exception e)
    {
      System.exit(0);
    }
    new AI(host, port);

  }

}

