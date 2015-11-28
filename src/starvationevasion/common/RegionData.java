package starvationevasion.common;

/**
 * This structure contains all data of a particular region that the simulator shares with
 * each client via the Server.
 */

public class RegionData
{
  public final EnumRegion region;

  /**
   * This field is zero for non-player regions.
   * Total player revenue in millions of dollars for the current simulation year.
   * This is the past year's revenue balance plus new taxes earned during the
   * current turn of three years, minus expenses during the current turn of three years.
   */
  public int revenueBalance;

  /**
   * This region's production (in kg) of each foodType during the current simulation year.
   * Index by EnumFood.ordinal()
   */
  public int[] production = new int[EnumFood.SIZE];


  /**
   * This region's food imports (in kg) of each foodType during the current simulation year.
   * Index by EnumFood.ordinal()
   */
  public int[] foodImports = new int[EnumFood.SIZE];


  /**
   * This region's food consumption (in kg) of each foodType during the current simulation year.
   * Index by EnumFood.ordinal()
   */
  public int[] consumption = new int[EnumFood.SIZE];


  /**
   * This region's population during the current year
   */
  public int population;


  /**
   * This region's percent of undernourished people during the current year
   */
  public double undernourished;




  public RegionData(EnumRegion region)
  {
    this.region = region;
  }
}
