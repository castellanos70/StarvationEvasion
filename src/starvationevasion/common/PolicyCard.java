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
  private final EnumPolicy policy;

  /**
   * A policy card can be enacted by just one player region or by as many as all 7
   * player regions. This is a bitwise int with a 1 in the position corresponding to
   * each region that has enacted the policy (EnumRegion.getBit()).
   */
  private int enactingRegionBits = 0;
  private EnumFood targetFood;
  private EnumRegion targetRegion;
  private int varX, varY, varZ;


  /**
   *  Constructs a policy card of the given policy.
   *  Many properties of the policy may be set and changed, but the
   *  given EnumPolicy policy is immutable.
   *
   * @param policy
   */
  public PolicyCard(EnumPolicy policy)
  {
    this.policy = policy;
  }

  public String getTitle()    {return policy.title;}
  public String getGameText() {return policy.gameText;}

  /**
   * @return policy
   */
  public EnumPolicy getPolicy() {return policy;}



  /**
   * Some policy cards require quantity X. Depending on the Policy Card,
   * X might be in millions of dallars or an integer percentage.
   * @return the value of quantity X.
   */
  public int getX() {return varX;}

  /**
   * Some policy cards require quantity Y. Depending on the Policy Card,
   * Y might be in millions of dallars or an integer percentage.
   * @return the value of quantity Y.
   */
  public int getY() {return varY;}


  /**
   * Some policy cards require quantity Z. Depending on the Policy Card,
   * Z might be in millions of dallars or an integer percentage.
   * @return the value of quantity Z.
   */
  public int getZ() {return varZ;}


  /**
   * Some policy cards require quantity X. Depending on the Policy Card,
   * X might be in millions of dallars or an integer percentage.
   * @param x value to be set to quantity X.
   */
  public void setX(int x) {varX = x;}


  /**
   * Some policy cards require quantity Y. Depending on the Policy Card,
   * Y might be in millions of dallars or an integer percentage.
   * @param y value to be set to quantity Y.
   */
  public void setY(int y) {varY = y;}


  /**
   * Some policy cards require quantity Z. Depending on the Policy Card,
   * Z might be in millions of dallars or an integer percentage.
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
   * Different policy cards require different data to be valid.
   * This validate() method should be called before sending a card to the simulator since
   * the simulator will reject any cards that are not valid.
   * @return null if the policy is valid. Otherwise, returns an error message.
   */
  public String validate()
  {
    if (enactingRegionBits == 0)
    {
      return "All enacted policy cards must have at least one US enacting region.";
    }

    if (policy == EnumPolicy.GMO_Seed_Insect_Resistance_Research)
    {
      if ((targetFood == null) || (!targetFood.isCrop()))
      {
        return policy + ": must have a target food that is a crop.";
      }
      if (varX < 1 || varX > 1000)
      {
        return policy + ": X must be between 1 and 1000 million dollars.";
      }
    }




    if (policy == EnumPolicy.International_Food_Releif_Program)
    {
      if ((targetFood == null) || (!targetFood.isCrop()))
      {
        return policy + ": must have a target food that is a crop.";
      }
      if (varX < 1 || varX > 1000)
      {
        return policy + ": X must be between 1 and 1000 million dollars.";
      }
    }

    return "policy not recognized";
  }



  /**
   * Used only for testing this class.
   */
  public static void main(String[] args)
  {
    PolicyCard myCard = new PolicyCard(EnumPolicy.GMO_Seed_Insect_Resistance_Research);
    System.out.println(myCard.getTitle());
    System.out.println(myCard.getGameText());
  }
}
