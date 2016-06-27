package starvationevasion.common;

import java.util.ArrayList;
import java.util.EnumSet;

import com.oracle.javafx.jmx.json.JSONDocument;


import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;

/**
 * GameCard is the structure used by the Client, Server and Simulator.
 * It holds all the data needed by the Simulator to implement the effects
 * of a a policy drafted and enacted by a player.<br><br>
 *
 * The game client's might want to extend this a class adding,
 * for example, an image, a status (in deck, in hand, proposed, voting,
 * enacted, rejected) and other data. <br><br>
 *
 * If the client does extend this class, DO NOT send the extended class
 * to the Server. This will cause the transmission of unused data causing
 * needed network lag. Rather, use the clone(GameCard source) method to
 * create a new GameCard that will copy the parts of your extended
 * gameCard that actually need to be sent.<br><br>
 *
 * Use the validate() method to verify all needed parameters of the policy
 * are defined in range.
 */
public class PolicyCard implements Sendable
{
  private final EnumPolicy type;

  //=========================================================================================
  /**
   * Every game card must have an owner. This is the player region from who's deck the
   * card was drawn. Note all automatic policy cards are enacted by the owner and only
   * the owner (enactingRegionBits is ignored).
   */
  private final EnumRegion owner;



  //=========================================================================================
  /**
   * This is a bitwise int with a 1 in the position corresponding to
   * each region that has approved the policy (EnumRegion.getBit()).<br>
   * Note: This field is ignored for all automatic policy cards.<br>
   * Note: The player who owns a policy always has an automatic approve vote.
   * and does not actually get to submit a vote.<br>
   */
  private int approvedRegionBits = 0;




  //=========================================================================================
  /**
   * Some cards require a target Food.
   * This field is ignored if this policy does not require a target food.
   */
  private EnumFood targetFood;



  //=========================================================================================
  /**
   * Some cards require a target region.
   * This field is ignored if this policy does not require a target region.
   */
  private EnumRegion targetRegion;




  //=========================================================================================
  /**
   * Some game cards require quantity X.
   * The units of this value depend on the particular policy.
   * Use {@link #getValidTargetRegions() validTargetRegions()} method to
   * get the valid target regions of this policy card.<br><br>.
   * This field is ignored if not require by this policy.
   */
  private int varX;

  public PolicyCard(EnumPolicy type, EnumRegion owner)
  {
    this.type = type;
    this.owner = owner;
  }

  //=========================================================================================

  /**
   * @return the owner of this game card
   */
  public EnumRegion getOwner()
  {
    return owner;
  }


  //=========================================================================================
  /**
   * @return The title text for this policy.
   */
  public String getTitle() {return type.getTitle();}



  //=========================================================================================
  /**
   * @return The game text for this policy.
   */
  public String getGameText() {return type.getGameText();}



  //=========================================================================================
  /**
   * In the abstract GameCard class, this method returns the default
   * behavior of a gamecard card (votesRequired() = 0).
   * If a class extending GameCard is not automatic, then it must override this method.
   * @return 0 if the policy is automatic. Otherwise, returns the number of
   * votes required for the policy to be enacted.<br>
   * Note: This will always return a number that is
   *   no smaller than the number of regions returned by getRegionsEligibleToVote().
   */
  public int votesRequired() {return 0;}
  
//=========================================================================================
  /**
   * In the abstract GameCard class, this method returns the action point
   * cost of a given policy type.
   */

  public int getActionPointCost() {return type.getActionPointCost();}
  
  // =========================================================================================
  /**
   * Note: Not every card has flavor text, so this may return null.
   */
  public String getFlavorText() 
  {
    return type.getFlavorText();
  }
  
  // =========================================================================================
  /**
   * Note: Not every card's flavor text will have a source, so this may return null.
   */
  public String getFlavorTextAuthor()
  {
    return type.getFlavorTextAuthor();
  }

  //=========================================================================================
  /**
   * At least in the current set of cards (and this may always be true),
   * waitForAll is true if and only if the number of votes required is 1.
   *
   * @return true if voting should continue until all eligible players
   * have voted on this policy. Return false if voting should stop as soon as
   * the required number of votes have been reached.
   */
  public boolean voteWaitForAll()
  {
    if (type.getVotesRequired() == 1) return true;
    return false;
  }



  //=========================================================================================
  /**
   * In the abstract GameCard class, this method returns the default
   * behavior of a policy card (isEligibleToVote() = false if the policy is
   * automatic and true for any region if not automatic).
   * If a class extending GameCard requires different behavior, then it must override this method.
   * @param playerRegion being queried.
   * @return true if and only if the given playerRegion is eligible to vote on this
   * particular policy
   */
  public boolean isEligibleToVote(EnumRegion playerRegion)
  {
    if (votesRequired() == 0) return false;
    return true;
  }


  //=========================================================================================
  /**
   * @return The policy card type for this policy.
   */
  public EnumPolicy getCardType() {return type;}



  //=========================================================================================
  /**
   * @return the value of quantity X.
   */
  public int getX() {return varX;}





  //=========================================================================================
  /**
   * @param x value to be set to quantity X.
   */
  public void setX(int x) {varX = x;}

  
  
  //=========================================================================================
  /**
   * @return the game states in which it is valid to use this card.
   * @see GameState
   */
  public EnumSet<GameState> getUsableStates() {return null;}


  
  //=========================================================================================
  /**
   * Some policy cards require a target region.
   * @param region Depending on the policy card, the given region will be
   *               required to be either a US region or a world region or
   *               perhaps some smaller subset of regions.
   */
  public void setTargetRegion(EnumRegion region)
  { targetRegion = region;
  }







  //=========================================================================================
  /**
   * Some policy cards require a target region.
   * @return region Depending on the policy card, the target region will be
   *  either a US region or a world region.
   */
  public EnumRegion getTargetRegion() {return targetRegion;}




  //=========================================================================================
  /**
   * Some policy cards require a target food (crop or livestock).
   * @param targetFood sets the targetFood. Ignored if this policy does not use a target food.
   */
  public void setTargetFood(EnumFood targetFood) {this.targetFood = targetFood;}




  //=========================================================================================
  /**
   * Some policy cards require a target food (crop or livestock).
   * @return targetFood.
   */

  public EnumFood getTargetFood()
  {
    return targetFood;
  }

  /**
   * Clears all enacting regions.
   */
  public void clearVotes()
  {
    approvedRegionBits = 0;
  }



  //=========================================================================================
  /**
   * The Server is responsible for verifying and setting each
   * enacting region of
   * all policy cards requiring a vote (votesRequired() &gt; 0).<br><br>
   *
   * Each policy sent to the simulator must be enacted by at least one
   * US region. <br><br>
   *
   * Automatic card are always and only enacted by the region who played the
   * policy card. <br><br>
   *
   * Some policy cards that require voting may be enacted by more than one region.
   * In some cases,
   * only the regions that voted for the policy enact it. In other cases
   * (generally for international card) either all US regions enact the policy or none do.
   * @param region a United States region that will be enacting the policy.
   */
  public void addEnactingRegion(EnumRegion region)
  {
    if (!region.isUS())
    {
      throw new IllegalArgumentException("addEnactingRegion(EnumRegion="+region+
        ") Only US regions may enact card.");
    }
    approvedRegionBits |= region.getBit();
  }



  //=========================================================================================
  /**
   * @param region a United States region.
   * @return true iff the given region voted to approve the policy.
   */
  public boolean didVoteYes(EnumRegion region)
  {
    if (!region.isUS()) return false;
    return (approvedRegionBits & region.getBit()) != 0;
  }




  //=========================================================================================
  /**
   * @return Number of US regions who voted yes for this policy.
   */
  public int getEnactingRegionCount()
  {
    int count = 0;
    for (EnumRegion region :EnumRegion.values())
    {
      if (!region.isUS()) break;  //assumes all us regions are before all world regions.
      if (didVoteYes(region)) count++;
    }
    return count;
  }




  //=========================================================================================
  /**
   * The default is null (no target region required).
   * @return An array of regions valid as the target region of this policyCard.
   * Returns null if this card does not require a region.
   */
  public EnumRegion[] getValidTargetRegions() {return null;}



  //=========================================================================================
  /**
   * The default is null (no target food required).
   * @return An array of food categories valid as the target food of this policyCard.
   * Returns null if this card does not require a region.
   */

  public EnumFood[] getValidTargetFoods() 
  {
    return null;
  }

  
  // =========================================================================================
  /**
   * The default is null (no target card required).
   * @return An array of cards that are valid as the target card of this policyCard.
   * Returns null if this card does not require a target card.
   */
  public ArrayList<EnumPolicy> getValidTargetCards() 
  {
    return null;
  }


  // =========================================================================================
  /**
   * The default is null (variable X not required).
   * @return Gives the options of available prices/percentages that correspond to card
   */
  public ArrayList<Integer> getOptionsOfVariable() {return null;}


  //=========================================================================================
  public String toString()
  {
    String msg = getTitle() + ":\n" + getGameText() + "\n";

    return msg;
  }

  //=========================================================================================
  private String validateTargetRegion()
  {
    EnumRegion[] regionList = getValidTargetRegions();
    if (regionList != null)
    { if (targetRegion == null) return type + ": requires a target Region.";

      boolean hasValidTarget=false;
      for (EnumRegion region : regionList)
      {
        if (region == targetRegion)
        {
          hasValidTarget = true;
          break;
        }
      }
      if (!hasValidTarget) return type + ": target Region ("+targetRegion+" is not valid).";
    }
    return null;
  }



  //=========================================================================================
  private String validateTargetFood()
  { EnumFood[] foodList = getValidTargetFoods();
    if (foodList != null)
    { if (targetFood == null) return type + ": requires a target Food.";

      boolean hasValidTarget = false;
      for (EnumFood food : foodList)
      {
        if (food == targetFood)
        {
          hasValidTarget = true;
          break;
        }
      }
      if (!hasValidTarget) return type + ": target food ("+targetFood+" is not valid).";
    }
    return null;
  }



  //=========================================================================================
  private String validateX()
  {
    ArrayList<Integer> validValueList = getOptionsOfVariable();
    if (validValueList == null) return null;

    for (Integer value : validValueList)
    {
      if (getX() == value) return null;
    }

    return "X=" + getX() + "is not a valid value.";
  }


  //=========================================================================================
  public String getPolicyName() { return getClass().getSimpleName(); }




  //=========================================================================================
  /**
   * This method provides general validation checking of the policy card.<br><br>
   *
   * Between the time when a GameCard is instantiated and drafted, it must have various fields
   * set. Which fields must be set and the ranges of those fields is dependant on
   * each particular subclass of GameCard. <br><br>
   *
   * GameCard subclasses that need validation beyond what can be inferred from it's
   * required fields should override this class, but open the override with super.validate().
   *
   * Note: A policy that requires votes, may not have sufficient votes to be <b>enacted</b>,
   * yet it may still be <b>valid</b>. <br><br>
   *
   * The Server uses this method to validate policy cards sent by the client.<br>
   * Note: The client should never send the server invalid cards. If
   * the simulator does find an error then either there is a bug in the client or
   * the client has been hacked.<br><br>
   *
   * After the Server validates a drafted policy card, if the policy requires voting,
   * the Server must notify the client that the card is part of the voting phase.
   * The Server must call didVoteYes(EnumRegion region) or each region that votes yes for
   * the drafted policy.<br><br>
   * The Server must determine if a vote requiring policy is enacted and
   * notify the clients.<br><br>
   *
   * Additionally, at the end of the voting phase, the Server must tell the simulator
   * which voting card did not get enacted (by calling the simulator's
   * discard(EnumRegion playerRegion, GameCard card) method.
   *
   * @return null if the policy is valid. Otherwise, returns an error message.
   */
  public String validate()
  {
    String msg = validateTargetRegion();
    if (msg != null) return msg;

    msg = validateTargetFood();
    if (msg != null) return msg;

    msg = validateX();
    if (msg != null) return msg;

    return null;
  }

  /*Explicitly defined hashCode and equals() to ensure they are static across
    different computers.*/

  @Override
  public int hashCode()
  {
    int result = type != null ? type.hashCode() : 0;
    result = 31 * result + (getOwner() != null ? getOwner().hashCode() : 0);
    result = 31 * result + approvedRegionBits;
    result = 31 * result + (getTargetFood() != null ? getTargetFood().hashCode() : 0);
    result = 31 * result + (getTargetRegion() != null ? getTargetRegion().hashCode() : 0);
    result = 31 * result + varX;
    return result;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof PolicyCard)) return false;

    PolicyCard that = (PolicyCard) o;

    if (approvedRegionBits != that.approvedRegionBits) return false;
    if (varX != that.varX) return false;
    if (type != that.type) return false;
    if (getOwner() != that.getOwner()) return false;
    if (getTargetFood() != that.getTargetFood()) return false;
    return getTargetRegion() == that.getTargetRegion();
  }

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument _json = JSONDocument.createObject();
    _json.setNumber("x", getX());
    _json.setNumber("votes-required", votesRequired());

    _json.setString("info-X", validateX());
    _json.setString("info-region", validateTargetRegion());
    _json.setString("info-food", validateTargetFood());
    _json.setString("owner", getOwner().name());
    _json.setString("game-text", getGameText());
    _json.setString("policy-name", getPolicyName());
    _json.setString("title", getTitle());
    _json.setString("card-type", getCardType().name());
    _json.setString("type", getType().name());

    return _json;
  }

  @Override
  public Type getType ()
  {
    return Type.POLICY_CARD;
  }

  @Override
  public void fromJSON (Object doc)
  {

  }
}

