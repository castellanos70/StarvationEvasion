package starvationevasion.client.Networking;

import starvationevasion.client.GUI.GUI;
import starvationevasion.client.Logic.ChatManager;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.common.gamecards.GameCard;
import starvationevasion.communication.Communication;
import starvationevasion.server.model.State;

import java.util.ArrayList;

/**
 * This interface is primarily to allow multiple versions of Client to be implemented without
 * causing issues with registering them with the GUI.
 *
 * A class that implements Client is not required to be thread-safe. This is left up to individual
 * implementations to decide if it's necessary or not.
 *
 * @author Justin Hall, George Boujaoude
 */
public interface Client
{
  /**
   * Gets the region associated with this client.
   *
   * @return region
   */
  EnumRegion getRegion();

  /**
   * Gets the hand maintained by this client at the present time.
   *
   * @return array list containing hand data.
   */
  ArrayList<EnumPolicy> getHand();

  /**
   * Returns the chat manager maintained by this client. A chat manager should interface
   * with this client's communication module behind the scenes.
   *
   * @return valid chat manager
   */
  ChatManager getChatManager();

  /**
   * Gets the communication module.
   *
   * @return communication module
   */
  Communication getCommunicationModule();

  /**
   * This should return a valid state for this client (as received from server).
   *
   * @return last-valid state sent to the client
   */
  State getState();

  /**
   * Gets a list of policy cards that this client currently has.
   *
   * @return list of policy cards
   */
  ArrayList<GameCard> getVotingCards();

  /**
   * Gets the GUI object that was registered with this client.
   *
   * @return gui
   */
  GUI getGui();

  /**
   * Sets the GUI for this client.
   *
   * @param gui valid gui reference
   */
  void setGUI(GUI gui);

  /**
   * This should send a request to the server to login. The return statement only reflects
   * whether the statement was sent or not, rather than whether the login was successful.
   *
   * @param username username to use
   * @param password password to try
   * @param region region for the user
   * @return true if the request was sent and false if the send failed
   */
  boolean loginToServer(String username, String password, EnumRegion region);

  /**
   * Attempts to register a new account with the server.
   *
   * @param username username to create
   * @param password password to associate
   * @param region region tied to this user
   * @return true if the request was sent and false if the send failed
   */
  boolean createUser(String username, String password, EnumRegion region);

  /**
   * Asks the client to vote the given card up.
   *
   * @param card card to vote up
   * @return true if the vote up request was sent and false if the send failed
   */
  boolean voteUp(GameCard card);

  /**
   * Asks the client to vote the given card down.
   *
   * @param card card to vote down
   * @return true if the vote up request was sent and false if the send failed
   */
  boolean voteDown(GameCard card);

  /**
   * Tells the client to send a ready request.
   *
   * @return true if the ready request was sent and false if the send failed
   */
  boolean ready();

  /**
   * Tells the client to send a done request to the server so it can move forward.
   *
   * @return true if the done request was sent and false if the send failed
   */
  boolean done();

  /**
   * Tries to draft the given card (interfaces with server).
   *
   * @param card card to draft
   * @return true if the draft request was sent and false if the send failed
   */
  boolean draftCard(GameCard card);

  /**
   * Tries to discard the given card (interfaces with server).
   *
   * @param card card to discard
   * @return true if the discard request was sent and false if the send failed
   */
  boolean discardCard(GameCard card);

  /**
   * Checks to see if the client is still running. This might return false if something
   * like the server disconnected.
   *
   * @return true if running and false if not
   */
  boolean isRunning();

  /**
   * Shuts down the client. It is not meant to be used at all after this is called.
   */
  void shutdown();

  /**
   * When called, this should perform all necessary updates to keep the client up to date with
   * the current state of the game. This function should only ever be called from one thread.
   *
   * @param deltaSeconds change in seconds since the last time this function was called
   */
  void update(double deltaSeconds);
}

