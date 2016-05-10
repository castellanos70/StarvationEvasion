package starvationevasion.client.Networking;

import starvationevasion.client.GUI.GUI;
import starvationevasion.client.Logic.ChatManager;
import starvationevasion.client.Logic.LocalDataContainer;
import starvationevasion.client.UpdateLoop;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.WorldData;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.common.gamecards.GameCard;
import starvationevasion.communication.Communication;
import starvationevasion.communication.ConcurrentCommModule;
import starvationevasion.server.model.*;

import java.util.ArrayList;

/**
 * Proof of concept.
 */
public class ClientTest implements Client
{
  private final UpdateLoop GAME_LOOP;
  private final Communication COMM;
  private final LocalDataContainer CONTAINER;
  private final ChatManager CHAT;
  private boolean isRunning = false;
  private GUI gui;
  private EnumRegion region;
  private ArrayList<GameCard> votingCards;
  private ArrayList<EnumPolicy> hand;
  private State state;

  public ClientTest(UpdateLoop gameLoop, String host, int port)
  {
    GAME_LOOP = gameLoop;
    COMM = new ConcurrentCommModule(host, port);
    CONTAINER = new LocalDataContainer(this);
    CHAT = new ChatManager(this);
    // Try to connect and then see how it went
    COMM.connect();
    isRunning = COMM.isConnected();

    CONTAINER.init();
  }

  /**
   * Gets the region associated with this client.
   *
   * @return region
   */
  @Override
  public EnumRegion getRegion()
  {
    return region;
  }

  /**
   * Gets the hand maintained by this client at the present time.
   *
   * @return array list containing hand data.
   */
  @Override
  public ArrayList<EnumPolicy> getHand()
  {
    return hand;
  }

  /**
   * Returns the chat manager maintained by this client. A chat manager should interface
   * with this client's communication module behind the scenes.
   *
   * @return valid chat manager
   */
  @Override
  public ChatManager getChatManager()
  {
    return CHAT;
  }

  /**
   * Gets the communication module.
   *
   * @return communication module
   */
  @Override
  public Communication getCommunicationModule()
  {
    return COMM;
  }

  /**
   * This should return a valid state for this client (as received from server).
   *
   * @return last-valid state sent to the client
   */
  @Override
  public State getState()
  {
    return state;
  }

  /**
   * Gets a list of policy cards that this client currently has.
   *
   * @return list of policy cards
   */
  @Override
  public ArrayList<GameCard> getVotingCards()
  {
    return votingCards;
  }

  /**
   * Gets the GUI object that was registered with this client.
   *
   * @return gui
   */
  @Override
  public GUI getGui()
  {
    return gui;
  }

  /**
   * Sets the GUI for this client.
   *
   * @param gui valid gui reference
   */
  @Override
  public void setGUI(GUI gui)
  {
    this.gui = gui;
  }

  /**
   * This should send a request to the server to login. The return statement only reflects
   * whether the statement was sent or not, rather than whether the login was successful.
   *
   * @param username username to use
   * @param password password to try
   * @return true if the request was sent and false if the send failed
   */
  @Override
  public boolean loginToServer(String username, String password, EnumRegion region)
  {
    return COMM.login(username, password, region);
  }

  /**
   * Attempts to register a new account with the server.
   *
   * @param username username to create
   * @param password password to associate
   * @param region   region tied to this user
   * @return true if the request was sent and false if the send failed
   */
  @Override
  public boolean createUser(String username, String password, EnumRegion region)
  {
    Payload data = new Payload();
    data.put("username", username);
    data.put("password", password);
    data.put("region", region);
    return COMM.send(Endpoint.USER_CREATE, data, null);
  }

  /**
   * Asks the client to vote the given card up.
   *
   * @param card card to vote up
   * @return true if the vote up request was sent and false if the send failed
   */
  @Override
  public boolean voteUp(GameCard card)
  {
    Payload data = new Payload();
    data.putData(card);
    data.put("card", card);
    return COMM.send(Endpoint.VOTE_UP, data, null);
  }

  /**
   * Asks the client to vote the given card down.
   *
   * @param card card to vote down
   * @return true if the vote up request was sent and false if the send failed
   */
  @Override
  public boolean voteDown(GameCard card)
  {
    Payload data = new Payload();
    data.putData(card);
    data.put("card", card);
    return COMM.send(Endpoint.VOTE_DOWN, data, null);
  }

  /**
   * Tells the client to send a ready request.
   *
   * @return true if the ready request was sent and false if the send failed
   */
  @Override
  public boolean ready()
  {
    return COMM.send(Endpoint.READY, null, null);
  }

  /**
   * Tells the client to send a done request to the server so it can move forward.
   *
   * @return true if the done request was sent and false if the send failed
   */
  @Override
  public boolean done()
  {
    return COMM.send(Endpoint.DONE, null, null);
  }

  /**
   * Tries to draft the given card (interfaces with server).
   *
   * @param card card to draft
   * @return true if the draft request was sent and false if the send failed
   */
  @Override
  public boolean draftCard(GameCard card)
  {
    Payload data = new Payload();
    data.putData(card.getCardType());
    return COMM.send(Endpoint.DELETE_CARD, data, null);
  }

  /**
   * Tries to discard the given card (interfaces with server).
   *
   * @param card card to discard
   * @return true if the discard request was sent and false if the send failed
   */
  @Override
  public boolean discardCard(GameCard card)
  {
    return false;
  }

  /**
   * Checks to see if the client is still running. This might return false if something
   * like the server disconnected.
   *
   * @return true if running and false if not
   */
  @Override
  public boolean isRunning()
  {
    return isRunning;
  }

  /**
   * Shuts down the client. It is not meant to be used at all after this is called.
   */
  @Override
  public void shutdown()
  {
    COMM.dispose();
    isRunning = false;
  }

  /**
   * When called, this should perform all necessary updates to keep the client up to date with
   * the current state of the game. This function should only ever be called from one thread.
   *
   * @param deltaSeconds change in seconds since the last time this function was called
   */
  @Override
  public void update(double deltaSeconds)
  {
    isRunning = COMM.isConnected();
    if (!isRunning) return;

    // Check to see if any new server responses have come in since the last iteration of this loop
    ArrayList<Response> responses = COMM.pollMessages();
    processServerInput(responses);
  }

  private void respondToStateChange()
  {
    if (state == State.DRAWING && !gui.isDraftingPhase())
    {
      gui.resetVotingPhase();
      gui.switchScenes();
    }
    else if (state == State.VOTING && gui.isDraftingPhase())
    {
      gui.resetDraftingPhase();
      gui.switchScenes();
    }
  }

  private void readHand()
  {
    COMM.send(Endpoint.HAND_READ, null, null);
  }

  private void processServerInput(ArrayList<Response> responses)
  {
    for (Response response : responses)
    {
      Type type = response.getType();
      Object data = response.getPayload().getData();

      if (type == Type.AUTH_SUCCESS) GAME_LOOP.notifyOfSuccessfulLogin();
      else if (type == Type.USER)
      {
        System.out.println("Received user information " + data);
        region = ((User)data).getRegion();
        hand = ((User)data).getHand();
        gui.setAssignedRegion(region);
        gui.setCardsInHand(getHand());
        gui.getDraftLayout().getHand().setPolicies(getHand().toArray(new EnumPolicy[hand.size()]));
      }
      else if (type == Type.WORLD_DATA_LIST)
      {
        System.out.println("Received world data list");
        ArrayList<WorldData> world = (ArrayList<WorldData>)data;
        for (WorldData worldData : world) CONTAINER.updateGameState(worldData);
      }
      else if (type == Type.WORLD_DATA)
      {
        System.out.println("Received single piece of world data");
        CONTAINER.updateGameState((WorldData)data);
      }
      else if (type == Type.USER_HAND)
      {
        System.out.println("Received user hand");
        hand = (ArrayList<EnumPolicy>)data;
      }
      //else if (type == Type.USERS_LOGGED_IN_LIST) users = (ArrayList<User>)data;
      else if (type == Type.VOTE_BALLOT)
      {
        System.out.println("Received voting cards");
        votingCards = (ArrayList<GameCard>)data;
        gui.getVotingLayout().getVotingHand().setVotingCards(votingCards);
      }
      else if (type == Type.GAME_STATE)
      {
        state = (State)data;
        System.out.println("Received game state update " + state);
        if(state.equals(State.DRAWING)) readHand();
        respondToStateChange();
      }
    }
  }
}
