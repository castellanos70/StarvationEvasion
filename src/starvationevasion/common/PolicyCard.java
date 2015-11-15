package starvationevasion.common;


/**
 * PolicyCard is the structure that the Server uses to tell the Simulator which
 * policies have been enacted on the current game turn.<br><br>
 *
 * The game client's might want to extend this class adding,
 * for example, an image, a status (in deck, in hand, proposed, voting, enacted, rejected)
 * and other data. <br>
 * If the client does extend this class, be sure to downcast to PolicyCard before sending
 * across the network to the server.<br><br>
 *
 * Use the validate() method to verify all needed parameters of the policy are defined and
 * in range.
 */
public class PolicyCard
{
  public static final int MIN_PERCENT = 1;
  public static final int MAX_PERCENT = 100;
  public static final int MIN_MILLION_DOLLARS = 1;
  public static final int MAX_MILLION_DOLLARS = 1000;

  /**
   * Every policy card must have an owner. This is the player region from who's deck the
   * card was drawn. Note all automatic policy cards are enacted by the owner and only
   * the owner (enactingRegionBits is ignored).
   */
  private final EnumRegion owner;


  private final EnumPolicy policy;




  /**
   * A policy card can be enacted by just one player region or by as many as all 7
   * player regions. This is a bitwise int with a 1 in the position corresponding to
   * each region that has enacted the policy (EnumRegion.getBit()).<br>
   * Note: This field is ignored for all automatic policy cards.<br>
   * Note: The player who owns a policy is automatically added to the enacting list.
   */
  private int enactingRegionBits;


  /**
   * This field is ignored if this policy does not require a target food.
   */
  private EnumFood targetFood;



  /**
   * Some cards require the target region to be a US player region. Other cards require
   * the target region to be a non-player world region.
   * This field is ignored if this policy does not require a target region.
   */
  private EnumRegion targetRegion;


  /**
   * Some policy cards require quantity X, Y and/or Z.
   * The units of these values are depend on the particular policy.
   * The field is ignored if this policy does not require that field.
   */
  private int varX, varY, varZ;


  /**
   *  Constructs a policy card of the given policy.
   *  Many properties of the policy may be set and changed, but the
   *  given EnumPolicy policy is immutable.
   *
   * @param policy
   */
  public PolicyCard(EnumRegion owner, EnumPolicy policy)
  {
    if (!owner.isUS())
    {
      throw new IllegalArgumentException("addEnactingRegion(EnumRegion="+owner+
        ") Policy owner must be a US region.");
    }
    this.owner  = owner;
    this.policy = policy;
    enactingRegionBits = owner.getBit();
  }

  public String getTitle()    {return policy.title;}
  public String getGameText() {return policy.gameText;}

  /**
   * @return policy
   */
  public EnumPolicy getPolicy() {return policy;}



  /**
   * @return the value of quantity X.
   */
  public int getX() {return varX;}

  /**
   * @return the value of quantity Y.
   */
  public int getY() {return varY;}


  /**
   * @return the value of quantity Z.
   */
  public int getZ() {return varZ;}


  /**
   * @param x value to be set to quantity X.
   */
  public void setX(int x) {varX = x;}


  /**
   * @param y value to be set to quantity Y.
   */
  public void setY(int y) {varY = y;}


  /**
   * @param z value to be set to quantity Z.
   */
  public void setZ(int z) {varZ = z;}



  /**
   * Some policy cards require a target region.
   * @param region Depending on the policy card, the given region will be required to be
   *               either a US region or a world region.
   */
  public void setTargetRegion(EnumRegion region)
  { targetRegion = region;
  }


  /**
   * Some policy cards require a target region.
   * @return region Depending on the policy card, the target region will be
   *  either a US region or a world region.
   */
  public EnumRegion getTargetRegion() {return targetRegion;}



  /**
   * Some policy cards require a target food (crop or livestock).
   * @param targetFood
   */
  public void setTargetFood(EnumFood targetFood) {this.targetFood = targetFood;}



  /**
   * Some policy cards require a target food (crop or livestock).
   * @return targetFood
   */
  public EnumFood getTargetFood() {return targetFood;}

  public void clearEnactingRegions()
  {
    enactingRegionBits = 0;
  }


  /**
   * Each policy sent to the simulator must be enacted by at least one
   * US region. <br>
   * Automatic policies are always and only enacted by the region who played the
   * policy card. <br>
   * Policy cards that require voting may be enacted by more than one region. In some cases,
   * only the regions that voted for the policy are enacting regions. In other cases
   * (generally for international policies) all US regions enact the policy.
   * @param region a United States region that will be enacting the policy.
   */
  public void addEnactingRegion(EnumRegion region)
  {
    if (!region.isUS())
    {
      throw new IllegalArgumentException("addEnactingRegion(EnumRegion="+region+
        ") Only US regions may enact policies.");
    }
    enactingRegionBits |= region.getBit();
  }

  /**
   * @param region a United States region.
   * @returns true iff the given region is one of the enacting regions.
   */
  public boolean isEnactingRegion(EnumRegion region)
  { return (enactingRegionBits | region.getBit()) != 0;
  }


  /**
   * @returns Number of US regions enacting this policy.
   */
  public int getEnactingRegionCount()
  {
    int count = 0;
    for (EnumRegion region :EnumRegion.values())
    {
      if (!region.isUS()) break;  //assumes all us regions are before all world regions.
      if (isEnactingRegion(region)) count++;
    }
    return count;
  }

  /**
   * @param n a policy percentage amount.
   * @return null iff the amount is within range. Otherwise returns an error message.
   */
  public String validatePercentValue(int n)
  {
    if (n < MIN_PERCENT || n > MAX_PERCENT)
    {
      return "[" + n + "]: must be between " + MIN_PERCENT + " and " + MAX_PERCENT + " percent.";
    }
    return null;
  }



  /**
   * @param n a policy amount in millions of dollars.
   * @return null iff the amount is within range. Otherwise returns an error message.
   */
  public String validateDollarValue(int n)
  {
    if (n < MIN_MILLION_DOLLARS || n > MAX_MILLION_DOLLARS)
    {
      return "[" + n + "]: must be between " + MIN_MILLION_DOLLARS + " and " +
        MAX_MILLION_DOLLARS + " million dollars.";
    }
    return null;
  }



  /**
   * Different policy cards require different data to be valid.<br>
   * The simulator uses this method to validate enacted policy cards.<br>
   * Note: The client should never send the server invalid cards. If
   * the simulator does find an error then either there is a bug in the client or
   * the client has been hacked.
   * @return null if the policy is valid. Otherwise, returns an error message.
   */
  public String validate()
  {
    String msg;

    switch(policy)
    {
      case GMO_Seed_Insect_Resistance_Research:
        if ((targetFood == null) || (!targetFood.isCrop()))
        {
          return policy + ": must have a target food that is a crop (not livestock).";
        }
        msg = validateDollarValue(varX);
        if (msg != null) return policy + msg;
        break;




      case International_Food_Releif_Program:
        if ((targetFood == null) || (!targetFood.isCrop()))
        {
          return policy + ": must have a target food that is a crop.";
        }
        msg = validateDollarValue(varX);
        if (msg != null) return policy + msg;
        break;




      case Efficient_Irrigation_Incentive:
        if (getEnactingRegionCount() != 1)
        {
          return policy + ": This is an automatic policy. Only region may enact.";
        }
        msg = validatePercentValue(varX);
        if (msg != null) return policy + msg;
        break;


      case Foreign_Aid_for_Farm_Infrastructure:
        if (enactingRegionBits != EnumRegion.allUSRegionBits())
        {
          return policy + ": Must be enacted by all US regions.";
        }
        msg = validateDollarValue(varX);
        if (msg != null) return policy + msg;

        if (targetRegion == null || targetRegion.isUS())
        {
          return policy + "["+targetRegion +"]: Must have target world region";
        }

        break;

      case Covert_Intelligence:
        if (enactingRegionBits != EnumRegion.allUSRegionBits())
        {
          return policy + ": Must be enacted by all US regions.";
        }
        msg = validateDollarValue(varX);
        if (msg != null) return policy + msg;
        break;

     default:
        return "policy not recognized";
   }
   return "UNKNOWN ERROR";
  }


  /**
   * Used only for testing this class.
   */
  public static void main(String[] args)
  {
    PolicyCard myCard = new PolicyCard(EnumRegion.MOUNTAIN,
      EnumPolicy.GMO_Seed_Insect_Resistance_Research);
    System.out.println(myCard.getTitle());
    System.out.println(myCard.getGameText());
  }
}
