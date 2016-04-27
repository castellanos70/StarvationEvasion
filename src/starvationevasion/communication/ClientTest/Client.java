package starvationevasion.communication.ClientTest;

import javafx.stage.Stage;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.Logic.ChatManager;
//import starvationevasion.client.Logic.LocalDataContainer;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.WorldData;
import starvationevasion.common.policies.PolicyCard;
import starvationevasion.communication.AITest.commands.Command;
import starvationevasion.communication.CommModule;
import starvationevasion.server.model.State;
import starvationevasion.server.model.Type;
import starvationevasion.server.model.User;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
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
public class Client
{
  //---- Code from communication\AI.java
  private final CommModule COMM;
  private User u;
  private ArrayList<User> users = new ArrayList<>();
  private State state = null;
  private ArrayList<WorldData> worldData;
  private List<PolicyCard> ballot;
  private volatile boolean isRunning = true;

  //---- GUI stuff
  private GUI gui;
  private ArrayList<EnumRegion> availableRegion;

  public Client(UpdateLoop loop, String host, int port)
  {
    // Sets up the comm module to be used by the client
    COMM = new CommModule(host, port);
    isRunning = COMM.isConnected();

    // Set up the response listeners
    COMM.setResponseListener(Type.AUTH_SUCCESS, (type, data) ->
    {
      loop.notifyOfLoginSuccess(); // Let the main loop know we logged in
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
  }

  public boolean isRunning()
  {
    return isRunning;
  }

  public void update()
  {
    isRunning = COMM.isConnected(); // Check the state of the comm module

    COMM.pushResponseEvents(); // Push responses received from server since last call to this function
  }

  public boolean login(String username, String password, EnumRegion region)
  {
    return COMM.login(username, password, region);
  }

  public void shutdown()
  {
    COMM.dispose();
  }
}
