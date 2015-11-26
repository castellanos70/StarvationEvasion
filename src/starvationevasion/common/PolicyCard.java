package starvationevasion.common;

import starvationevasion.common.policies.*;

import java.io.Serializable;

/**
 * PolicyCard is the structure used by the Client, Server and Simulator.
 * It holds all the data needed by the Simulator to implement the effects
 * of a a policy drafted and enacted by a player.<br><br>
 *
 * The game client's might want to extend this a class adding,
 * for example, an image, a status (in deck, in hand, proposed, voting,
 * enacted, rejected) and other data. <br><br>
 *
 * If the client does extend this class, DO NOT send the extended class
 * to the Server. This will cause the transmission of unused data causing
 * needed network lag. Rather, use the clone(PolicyCard source) method to
 * create a new PolicyCard that will copy the parts of your extended
 * policyCard that actually need to be sent.<br><br>
 *
 * Use the validate() method to verify all needed parameters of the policy
 * are defined in range.
 */
public abstract class PolicyCard  implements Serializable
{
  public static final int MIN_PERCENT = 1;
  public static final int MAX_PERCENT = 100;
  public static final int MIN_MILLION_DOLLARS = 1;
  public static final int MAX_MILLION_DOLLARS = 1000;

  public enum EnumVariable {X, Y, Z};
  public enum EnumVariableUnit {MILLION_DOLLAR, PERCENT, UNIT};

  protected EnumPolicy type;




  //=========================================================================================
  /**
   * Every policy card must have an owner. This is the player region from who's deck the
   * card was drawn. Note all automatic policy cards are enacted by the owner and only
   * the owner (enactingRegionBits is ignored).
   */
  protected EnumRegion owner;



  //=========================================================================================
  /**
   * This is a bitwise int with a 1 in the position corresponding to
   * each region that has approved the policy (EnumRegion.getBit()).<br>
   * Note: This field is ignored for all automatic policy cards.<br>
   * Note: The player who owns a policy always has an automatic approve vote.
   * and does not actually get to submit a vote.<br>
   */
  protected int approvedRegionBits = 0;




  //=========================================================================================
  /**
   * Some cards require a target Food.
   * This field is ignored if this policy does not require a target food.
   */
  protected EnumFood targetFood;



  //=========================================================================================
  /**
   * Some cards require a target region.
   * This field is ignored if this policy does not require a target region.
   */
  protected EnumRegion targetRegion;



  //=========================================================================================
  /**
   * Some policy cards require quantity X, Y and/or Z.
   * The units of these values are depend on the particular policy.
   * Use {@link #getValidTargetRegions() validTargetRegions()} method to
   * get the valid target regions of this policy card.<br><br.
   * This field is ignored if not require by this policy.
   */
  protected int varX, varY, varZ;




  //=========================================================================================
  /**
   *  This is a convenience method used to construct any of the many policy cards
   *  available in the game.
   *
   * @param owner US player region controlled by the player who drafts this policy.
   * @param type The policy card type to be constructed.
   */
  public static PolicyCard create(EnumRegion owner, EnumPolicy type)
  {
    if (!owner.isUS())
    {
      throw new IllegalArgumentException("addEnactingRegion(EnumRegion="+owner+
        ") Policy owner must be a US region.");
    }

    PolicyCard myCard = null;
    switch (type) {
      case Clean_River_Incentive:
        myCard = new CleanRiverIncentivePolicy();
        break;
      case Covert_Intelligence:
        myCard = new CovertIntelligencePolicy();
        break;
      case Educate_the_Women_Campaign:
        myCard = new EducateTheWomenCampaignPolicy();
        break;
      case Efficient_Irrigation_Incentive:
        myCard = new EfficientIrrigationIncentivePolicy();
        break;
      case Ethanol_Tax_Credit_Change:
        myCard = new EthanolTaxCreditChangePolicy();
        break;
      case Fertilizer_Subsidy:
        myCard = new FertilizerSubsidyPolicy();
        break;
      case Foreign_Aid_for_Farm_Infrastructure:
        myCard = new ForeignAidForFarmInfrastructurePolicy();
        break;
      case GMO_Seed_Insect_Resistance_Research:
        myCard = new GMOSeedInsectResistanceResearchPolicy();
        break;
      case International_Food_Relief_Program:
        myCard = new InternationalFoodReliefProgramPolicy();
        break;
      case Loan:
        myCard = new LoanPolicy();
        break;
      case MyPlate_Promotion_Campaign:
        myCard = new MyPlatePromotionCampaignPolicy();
        break;
    }

    if (myCard != null)
    {
      myCard.owner = owner;
      myCard.approvedRegionBits = owner.getBit(); //The owner auto votes yes.
    }

    return myCard;
  }



  //=========================================================================================
  /**
   * @return The title text for this policy.
   */
  public abstract  String getTitle();



  //=========================================================================================
  /**
   * @return The game text for this policy.
   */
  public abstract String getGameText();



  //=========================================================================================
  /**
   * In the abstract Policy class, this method returns the default
   * behavior of a policy card (votesRequired() = 0).
   * If a class extending Policy is not automatic, then it must override this method.
   * @return 0 if the policy is automatic. Otherwise, returns the number of
   * votes required for the policy to be enacted.<br>
   * Note: This will always return a number that is
   *   no smaller than the number of regions returned by getRegionsEligibleToVote().
   */
  public int votesRequired() {return 0;}



  //=========================================================================================
  /**
   * In the abstract Policy class, this method returns the default
   * behavior of a policy card (voteWaitForAll() = false).
   * If a class extending Policy is not automatic and requires waiting,
   * then it must override this method.
   * @return true if voting should continue until all eligible players
   * have voted on this policy. Return false if voting should stop as soon as
   * the required number of votes have been reached.
   */
  public boolean voteWaitForAll() {return false;}



  //=========================================================================================
  /**
   * In the abstract Policy class, this method returns the default
   * behavior of a policy card (isEligibleToVote() = false if the policy is
   * automatic and true for any region if not automatic).
   * If a class extending Policy requires different behavior, then it must override this method.
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
   * @return the value of quantity Y.
   */
  public int getY() {return varY;}



  //=========================================================================================
  /**
   * @return the value of quantity Z.
   */
  public int getZ() {return varZ;}



  //=========================================================================================
  /**
   * @param x value to be set to quantity X.
   */
  public void setX(int x) {varX = x;}



  //=========================================================================================
  /**
   * @param y value to be set to quantity Y.
   */
  public void setY(int y) {varY = y;}



  //=========================================================================================
  /**
   * @param z value to be set to quantity Z.
   */
  public void setZ(int z) {varZ = z;}




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
  public EnumFood getTargetFood() {return targetFood;}



  //=========================================================================================
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
   * Automatic policies are always and only enacted by the region who played the
   * policy card. <br><br>
   *
   * Some policy cards that require voting may be enacted by more than one region.
   * In some cases,
   * only the regions that voted for the policy enact it. In other cases
   * (generally for international policies) either all US regions enact the policy or none do.
   * @param region a United States region that will be enacting the policy.
   */
  public void addEnactingRegion(EnumRegion region)
  {
    if (!region.isUS())
    {
      throw new IllegalArgumentException("addEnactingRegion(EnumRegion="+region+
        ") Only US regions may enact policies.");
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
    return (approvedRegionBits | region.getBit()) != 0;
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
  public EnumFood[] getValidTargetFoods() {return null;}




  //=========================================================================================
  /**
   * The default is null (the given variable is not required).
   * @param variable to be queried.
   * @return The units of the required variable or null if the given variable is not required.
   */
  public EnumVariableUnit getRequiredVariables(EnumVariable variable) {return null;}



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
  private String validateVariables()
  {
    for (EnumVariable variable : EnumVariable.values())
    {
      int n = varX;
      if (variable == EnumVariable.Y) n = varY;
      else n = varZ;

      EnumVariableUnit unit = getRequiredVariables(variable);
      if (unit == EnumVariableUnit.PERCENT)
      {
        if (n < MIN_PERCENT || n > MAX_PERCENT)
        {
          return variable+"[" + n + "]: must be between " + MIN_PERCENT + " and " + MAX_PERCENT + " percent.";
        }
      }
      else if (unit == EnumVariableUnit.MILLION_DOLLAR)
      {
        if (n < MIN_MILLION_DOLLARS || n > MAX_MILLION_DOLLARS)
        {
          return "[" + n + "]: must be between " + MIN_MILLION_DOLLARS + " and " +
            MAX_MILLION_DOLLARS + " million dollars.";
        }
      }
      else if (unit == EnumVariableUnit.UNIT)
      { if (n <= 0)
        {
          return "[" + n + "]: must be greater than zero.";
        }
      }
    }
    return null;
  }


  //=========================================================================================
  public String getPolicyName() { return getClass().getSimpleName(); }




  //=========================================================================================
  /**
   * This method provides general validation checking of the policy card.<br><br>
   *
   * Between the time when a PolicyCard is instantiated and drafted, it must have various fields
   * set. Which fields must be set and the ranges of those fields is dependant on
   * each particular subclass of PolicyCard. <br><br>
   *
   * PolicyCard subclasses that need validation beyond what can be inferred from it's
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
   * which voting policies did not get enacted (by calling the simulator's
   * discard(EnumRegion playerRegion, PolicyCard card) method.
   *
   * @return null if the policy is valid. Otherwise, returns an error message.
   */
  public String validate()
  {
    String msg = validateTargetRegion();
    if (msg != null) return msg;

    msg = validateTargetFood();
    if (msg != null) return msg;

    msg = validateVariables();
    if (msg != null) return msg;

    return null;
  }
}

