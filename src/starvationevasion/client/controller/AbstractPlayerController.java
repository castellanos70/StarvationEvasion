package starvationevasion.client.controller;

import starvationevasion.client.model.GameOptions;
import starvationevasion.client.net.common.NetworkStatus;
import starvationevasion.client.model.Player;
import starvationevasion.client.net.AbstractClient;
import starvationevasion.client.model.GameStateData;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;
import starvationevasion.common.messages.*;

import java.util.Iterator;

/**
 * Class that contains methods that are relevant/used for both Computer and Human player.
 */
public abstract class AbstractPlayerController implements GameController
{
  private final GameOptions options;
  private AbstractClient client;
  private Player player;

  protected GameStateData gameState;

  /**
   * Creates a new AbstractPlayerController to be used by both human and computer players.
   *
   * @param gameState a {@link GameStateData} which we'll use to retrieve information about the server/game phase.
   * @param player    a {@link Player} model to control.
   * @param options   {@link GameOptions} which are used to store username/password credentials/available regions for play.
   */
  public AbstractPlayerController(GameStateData gameState, Player player, GameOptions options)
  {
    this.gameState = gameState;
    this.player = player;
    this.options = options;
  }


  /**
   * Returns the hand of PolicyCards owned by the {@link Player} controlled by this controller.
   *
   * @return the player's hand.
   */
  public Iterator<PolicyCard> getHand()
  {
    return player.getHand();
  }

  /**
   * Returns the {@link EnumRegion} owned by the {@link Player} controlled by this controller.
   *
   * @return the player's region.
   */
  public EnumRegion getRegion()
  {
    return player.getRegion();
  }

  /**
   * Returns the network status of the {@link Player} controlled by this controller.
   *
   * @return the player's {@link NetworkStatus}
   */
  public NetworkStatus getPlayerStatus()
  {
    return player.getStatus();
  }

  /**
   * Returns the name of the {@link Player} controlled by this controller.
   *
   * @return the name of the player, as a String.
   */
  public String getName()
  {
    return player.getUsername();
  }

  /**
   * Sets the {@link EnumRegion} owned by the {@link Player} controlled by this controller.
   *
   * @param region the player's assigned EnumRegion.
   */
  public void setRegion(EnumRegion region)
  {
    this.player.setRegion(region);
  }

  /**
   * Sets the {@link NetworkStatus} of the {@link Player} controlled by this controller.
   *
   * @param playerStatus the player's NetworkStatus.
   */
  public void setPlayerStatus(NetworkStatus playerStatus)
  {
    player.setStatus(playerStatus);
  }

  /**
   * Sets the name of the {@link Player} controlled by this controller.
   *
   * @param name the player's name, as a String.
   */
  public void setName(String name)
  {
    player.setUsername(name);
  }

  /**
   * Assigns the passed {@link AbstractClient}to this player controller, used for sending messages to the server.
   *
   * @param client the AbstractClient to communicate with the server through.
   */
  public void setClient(AbstractClient client)
  {
    this.client = client;
  }


  @Override
  public void update(float deltaTime)
  {

  }

  /**
   * Selects a region from the server, if it's not taken.
   *
   * @param region the EnumRegion to select and assign to the player.
   */
  public void selectRegion(EnumRegion region)
  {
    client.send(new RegionChoice(region));
    player.setRegion(region);
  }

  public Player getPlayer()
  {
    return player;
  }


  public AbstractClient getClient()
  {
    return client;
  }
}
