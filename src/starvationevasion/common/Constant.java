package starvationevasion.common;


/**
 * This class contains all constant values used throughout the Starvation Evasion game.
 */
public class Constant
{

  /**
   * This is the first year for which simulation data has been gathered.
   */
  public static final int FIRST_YEAR = 1980;


  /**
   * This is the last year for which simulation data projections are approximated.
   */
  public static final int LAST_YEAR = 2050;


  /**
   * Each game turn advances the simulator by YEARS_PER_TURN years.
   */
  public static final int YEARS_PER_TURN = 3;

  /**
   * Constant value of total Crops + Livestock
   */
  public static final int TOTAL_AGRO_CATEGORIES = 12;

  /**
   * Constant indices of State data represented in CSV file
   */
  public static final int STATE_NAME_IDX            = 0;
  public static final int STATE_TOTAL_LAND_IDX      = 1;
  public static final int STATE_TOTAL_FARM_LAND_IDX = 2;
  public static final int STATE_CITRUS_IDX          = 3;
  public static final int STATE_NON_CITRUS_IDX      = 4;
  public static final int STATE_NUTS_IDX            = 5;
  public static final int STATE_GRAIN_IDX           = 6;
  public static final int STATE_OIL_CROP_IDX        = 7;
  public static final int STATE_VEGGIE_IDX          = 8;
  public static final int STATE_SPECIALTY_CROP_IDX  = 9;
  public static final int STATE_FEED_CROP_IDX       = 10;
  public static final int STATE_FISH_IDX            = 11;
  public static final int STATE_MEAT_IDX            = 12;
  public static final int STATE_POULTRY_EGG_IDX     = 13;
  public static final int STATE_DAIRY_IDX           = 14;
}
