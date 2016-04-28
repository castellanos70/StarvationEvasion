package starvationevasion.communication.ClientTest;

import starvationevasion.client.Logic.*;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.WorldData;
import starvationevasion.common.policies.EnumPolicy;
import starvationevasion.common.policies.PolicyCard;
import starvationevasion.communication.CommModule;
import starvationevasion.server.model.*;

import java.util.ArrayList;
import java.util.List;

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
  private volatile boolean called = false;
  private ChatManager chatManager;

  //---- GUI stuff
  private GUI gui;
  private LocalDataContainer container;
  private EnumRegion region;
  private ArrayList<EnumRegion> availableRegion;

  public Client(UpdateLoop loop, String host, int port)
  {
    // Sets up the comm module to be used by the client
    COMM = new CommModule(host, port);
    isRunning = COMM.isConnected();

    // Create the ChatManager
    chatManager = new ChatManager(COMM);

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
    COMM.setResponseListener(Type.GAME_STATE, (type, data) ->
    {
      System.out.println("called");
      state = (State)data;
      updateState();
    });

    // Create the LocalDataContainer
    container = new LocalDataContainer(this);
  }

  public boolean isRunning()
  {
    return isRunning;
  }

  public void sendReady()
  {
    COMM.send(Endpoint.READY, null, null);
  }

  public ArrayList<EnumPolicy> getHand()
  {
    return null;
  }

  public void draftCard(PolicyCard card)
  {

  }

  public void discardCard(PolicyCard card)
  {

  }

  public void voteUp(PolicyCard card)
  {
    Payload data = new Payload();
    data.putData(card);
    data.put("card", card);
    COMM.send(Endpoint.VOTE_UP, data, null);
  }

  public void voteDown(PolicyCard card)
  {
    Payload data = new Payload();
    data.putData(card);
    data.put("card", card);
    COMM.send(Endpoint.VOTE_DOWN, data, null);
  }

  public void done()
  {
    COMM.send(Endpoint.DONE, null, null);
  }

  public ChatManager getChatManager()
  {
    return chatManager;
  }

  public State getState()
  {
    return state;
  }

  public ArrayList<PolicyCard> getVotingCards()
  {
    return null;
  }

  public void update()
  {
    isRunning = COMM.isConnected(); // Check the state of the comm module
    if (!isRunning) return;

    COMM.pushResponseEvents(); // Push responses received from server since last call to this function
  }

  public GUI getGUI()
  {
    return gui;
  }

  public EnumRegion getRegion()
  {
    return region;
  }

  public void setGUI(GUI gui)
  {
    called = true;
    this.gui = gui;
  }

  public LocalDataContainer getLocalDataContainer()
  {
    return container;
  }

  public void setRegion(EnumRegion region)
  {
    this.region = region;
  }

  public boolean login(String username, String password, EnumRegion region)
  {
    // Try to create the user first - this should fail if it already exists, which
    // is exactly what we want
    Payload data = new Payload();
    data.put("username", username);
    data.put("password", password);
    data.put("region", region);
    COMM.send(Endpoint.USER_CREATE, data, null);
    // Return the result of sending the login request
    return COMM.login(username, password, region);
  }

  public void shutdown()
  {
    COMM.dispose();
  }

  private void updateState()
  {
    switch (state)
    {
      case DRAWING:
        if(!gui.isDraftingPhase())
        {
          gui.resetVotingPhase();
          gui.switchScenes();
        }
        break;
      case VOTING:
        if(gui.isDraftingPhase())
        {
          gui.resetDraftingPhase();
          gui.switchScenes();
        }
        break;
    }
  }
}
