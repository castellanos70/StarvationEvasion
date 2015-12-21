package starvationevasion.client.MegaMawile.controller;


import starvationevasion.client.MegaMawile.model.*;
import starvationevasion.client.MegaMawile.net.AbstractClient;
import starvationevasion.client.MegaMawile.model.GameStateData;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;
import starvationevasion.common.messages.*;
import starvationevasion.server.ServerState;

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
   * Returns the current {@link Ballot} owned by the {@link Player} controlled by this controller.
   *
   * @return the player's Ballot
   */
  public Ballot getBallot()
  {
    return player.getBallot();
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
   * Sets the {@link Ballot} owned by the {@link Player} controlled by this controller.
   *
   * @param ballot the player's Ballot.
   */
  public void setBallot(Ballot ballot)
  {
    player.setBallot(ballot);
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

  /**
   * Submit a PolicyCard to draft.
   *
   * @param policyCard the PolicyCard to submit for drafting.
   * @return <code>true</code> if the card is valid to draft and was drafted successfully.
   */
  public boolean draftPolicy(PolicyCard policyCard)
  {
    if (gameState.getServerState() == ServerState.DRAFTING)
    {
      if (player.getStatus() != NetworkStatus.LOGGED_IN)
      {
        //error
        System.out.println("Player is not connected to the Server");
        return false;
      }
      else
      {
        client.send(new DraftCard(policyCard));
      }
      return true;
    }
    return false;
  }

  /**
   * Checks to see if an array of cards is eligible to be discarded and drawn this turn.
   *
   * @param policyCards
   * @return <code>true</code> if there is an action to use and there are no more than 3 cards to discard/draw.
   */
  public boolean discardDraw(PolicyCard[] policyCards)
  {
    if (gameState.getServerState() == ServerState.DRAFTING)
    {
      if (player.getStatus() != NetworkStatus.LOGGED_IN)
      {
        // error
        System.out.println("Player is not connected to the Server");
        return false;
      }
      else
      {
        EnumPolicy[] draftCards = new EnumPolicy[3];
        for (int i = 0; i < draftCards.length; i++)
        {
          if(policyCards[i] != null) draftCards[i] = policyCards[i].getCardType();
          else draftCards[i] = null;
        }
        client.send(new Discard(draftCards[0], draftCards[1], draftCards[2]));
      }
      return true;
    }
    return false;
  }

  /**
   * Discards a card this turn. DOES NOT draw immediately.
   *
   * @param policyCard a PolicyCard to discard.
   * @return <code>true</code> if there is a discard action to use.
   */
  public boolean discard(PolicyCard policyCard)
  {
    if ( gameState.getServerState() == ServerState.DRAFTING)
    {
      if (player.getStatus() != NetworkStatus.LOGGED_IN)
      {
        // error
        System.out.println("Player is not connected to the Server");
        return false;
      }
      else
      {
        client.send(new Discard(policyCard.getCardType()));
      }
      return true;
    }
    return false;
  }

  /**
   * Submits a yes vote for the passed {@link PolicyCard} to the server, as well as updating the player's {@link Ballot}
   * accordingly.
   *
   * @param policyCard a PolicyCard to vote yes on.
   */
  public void voteYes(PolicyCard policyCard)
  {
    if (gameState.getServerState() == ServerState.VOTING)
    {
      player.setVote(policyCard,  VoteType.FOR);
      client.send(new Vote(policyCard.getOwner(), VoteType.FOR));
    }
    else
    {
      System.out.println("Not voting yet!");
    }
  }

  /**
   * Submits a no vote to the server for the passed {@link PolicyCard}, as well as updating the player's {@link Ballot}
   * accordingly.
   *
   * @param policyCard a PolicyCard to vote no on.
   */
  public void voteNo(PolicyCard policyCard)
  {
    if (gameState.getServerState() == ServerState.VOTING)
    {
      player.setVote(policyCard, VoteType.AGAINST);
      client.send(new Vote(policyCard.getOwner(), VoteType.AGAINST));
    }
    else
    {
      System.out.println("Not voting yet!");
    }
  }

  /**
   * Submits an abstain vote to the server for the passed {@link PolicyCard}, as well as updating it in the player's
   * current {@link Ballot}.
   *
   * @param policyCard a PolicyCard to vote abstain on.
   */
  public void voteAbstain(PolicyCard policyCard)
  {
    if (gameState.getServerState() == ServerState.VOTING)
    {
      player.setVote(policyCard,VoteType.ABSTAIN);
      client.send(new Vote(policyCard.getOwner(), VoteType.ABSTAIN));
    }
    else
    {
      System.out.println("Not voting yet!");
    }
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
