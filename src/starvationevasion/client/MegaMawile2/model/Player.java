package starvationevasion.client.MegaMawile2.model;

import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;
import starvationevasion.common.messages.VoteType;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Generic player this is responsible for holding relevant attributes that
 * apply to both Human and Computer.
 */
public class Player
{
  private EnumRegion region;
  private float hdi;
  private int money;
  private NetworkStatus status = NetworkStatus.NOT_CONNECTED;
  private String username = "";
  private String password = "";


  //The player's hand
  private ArrayList<PolicyCard> hand = new ArrayList<>();

  public Player(String value, EnumRegion region)
  {
    this.region = region;
    this.username = value;
  }

  public Player()
  {

  }

  /**
   * Returns the player's region
   *
   * @return the player's EnumRegion
   */
  public EnumRegion getRegion()
  {
    return region;
  }

  /**
   * Returns the player's current human development index (score)
   *
   * @return a float representing the player's hdi
   */
  public float getHdi()
  {
    return hdi;
  }

  /**
   * Does not actually give money
   *
   * @return the amount of money that this player owns
   */
  public int getMoney()
  {
    return money;
  }

  /**
   * Returns the player's hand.
   *
   * @return an Iterator to iterate through the player's current hand.
   */
  public Iterator<PolicyCard> getHand()
  {
    return hand.iterator();
  }

  public ArrayList<PolicyCard> getHandList()
  {
    return hand;
  }

  /**
   * Assign the player's region. This should only be called once, at the beginning of the game.
   *
   * @param region the EnumRegion to assign to this player.
   */
  public void setRegion(EnumRegion region)
  {
    this.region = region;
  }

  /**
   * Returns the number of cards in the player's hand
   *
   * @return an Integer to represent the number of cards
   */
  public int getHandSize()
  {
    return hand.size();
  }

  /*
  public int setVote(int cardIndex, int vote)
  {
    return ballot.getBallotItems().uhh(i dunno);
  }
  */

  public NetworkStatus getStatus()
  {
    return status;
  }

  public void setStatus(NetworkStatus status)
  {
    this.status = status;
  }


  /**
   * Sets the player's hand using an array of policies received from the server.
   *
   * @param hand the policies to assign to this player's hand.
   */
  public void setHand(EnumPolicy[] hand)
  {
    this.hand.clear();
    EnumRegion region = getRegion();
    for (EnumPolicy policy : hand)
    {
      PolicyCard policyCard = PolicyCard.create(region, policy);
//      System.out.println(policyCard.toString());
      this.hand.add(policyCard);
    }
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }
}
