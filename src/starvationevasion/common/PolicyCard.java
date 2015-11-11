package starvationevasion.common;

public class PolicyCard
{
  private final EnumPolicy policy;

  /**
   * A policy card can be enacted by just one player region or by as many as all 7
   * player regions. This is a bitwise int with a 1 in the position corresponding to
   * each region that has enacted the policy (EnumRegion.getBit()).
   */
  private int enactingRegionBits = 0;
  private EnumRegion targetRegion;
  private int varX, varY, varZ;

  public PolicyCard(EnumPolicy policy)
  {
    this.policy = policy;
  }

  public String getTitle()    {return policy.title;}
  public String getGameText() {return policy.gameText;}
  public void setTargetRegion(EnumRegion region)
  { targetRegion = region;
  }
  public EnumRegion getTargetRegion() {return targetRegion;}

  public void clearEnactingRegions()
  {
    enactingRegionBits = 0;
  }

  public void addEnactingRegion(EnumRegion region)
  {
    enactingRegionBits |= region.getBit();
  }

  public boolean isEnactingRegion(EnumRegion region)
  { return (enactingRegionBits | region.getBit()) != 0;
  }

  public static void main(String[] args)
  {
    PolicyCard myCard = new PolicyCard(EnumPolicy.GMO_Seed_Insect_Resistance_Research);
    System.out.println(myCard.getTitle());
    System.out.println(myCard.getGameText());
  }
}
