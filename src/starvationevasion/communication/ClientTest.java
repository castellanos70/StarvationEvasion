package starvationevasion.communication;

import starvationevasion.common.Constant;
import starvationevasion.common.WorldData;
import starvationevasion.common.policies.PolicyCard;
import starvationevasion.communication.CommModule;
import starvationevasion.communication.commands.Command;
import starvationevasion.communication.commands.Draft;
import starvationevasion.communication.commands.Vote;
import starvationevasion.server.model.State;
import starvationevasion.server.model.Type;
import starvationevasion.server.model.User;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * This Client class is a re-write of Client.java.
 * The major difference is that this class has been designed to use the communication package, and its interfaces
 * within.
 *
 * @author Justin Hall, George Boujaoude
 */
public class ClientTest
{
  //---- Code from communication\AITest.java
  private final CommModule COMM;
  private User u;
  private ArrayList<User> users = new ArrayList<>();
  private State state = null;
  private ArrayList<WorldData> worldData;
  private List<PolicyCard> ballot;
  private Stack<Command> commands = new Stack<>();
  private volatile boolean isRunning = true;

  //---- Code from server\Client.java
  private Socket clientSocket;
  private PrintWriter write;
  private BufferedReader reader;
  private Scanner keyboard;
  //private ClientListener listener;
  private SecretKey serverKey;
  private Cipher aesCipher;
  private KeyPair rsaKey;
  //private volatile boolean isRunning = true; *** Already from AITest.java

  ClientTest(String host, int port)
  {
    // Sets up the comm module to be used by the client
    COMM = new CommModule(host, port);
    isRunning = COMM.isConnected();

    // Set up the response listeners
    COMM.setResponseListener(Type.AUTH_SUCCESS, (type, data) ->
    {
      u = (User)data;
      COMM.sendChat("ALL", "Human user: " + u.getUsername() + " just connected to the game", null);
    });
    COMM.setResponseListener(Type.USER, (type, data) ->
    {
      u = (User)data;
      COMM.sendChat("ALL", "Human user updated: " + u.getUsername(), null);
    });


    COMM.setResponseListener(Type.WORLD_DATA_LIST, (type, data) -> worldData = (ArrayList<WorldData>)data);
    COMM.setResponseListener(Type.USERS_LOGGED_IN_LIST, (type, data) -> users = (ArrayList<User>)data);
    COMM.setResponseListener(Type.WORLD_DATA, (type, data) -> worldData.add((WorldData)data));
    COMM.setResponseListener(Type.VOTE_BALLOT, (type, data) -> ballot = (List<PolicyCard>)data);
    COMM.setResponseListener(Type.GAME_STATE, (type, data) -> state = (State)data);

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


}
