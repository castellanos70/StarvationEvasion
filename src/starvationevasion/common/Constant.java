package starvationevasion.common;


import java.util.Random;

/**
 * This class contains all constant values used throughout the Starvation Evasion game.
 */
public class Constant
{

  /**
   * Each game turn advances the simulator by YEARS_PER_TURN years.
   */
  public static final int YEARS_PER_TURN = 2;

  /**
   * This is the first year for which simulation data has been gathered.
   */
  public static final int FIRST_DATA_YEAR = 2000;


  /**
   * This is the year for of the first turn in the simulation.
   * Note: (LAST_YEAR - FIRST_GAME_YEAR) % YEARS_PER_TURN must equal zero.
   */
  public static final int FIRST_GAME_YEAR = 2016;

  /**
   * This is the last year for which simulation data projections are approximated.
   */
  public static final int LAST_YEAR = 2050;


  /**
   * This is the total number of game turns.
   */
  public static final int GAME_TOTAL_TURNS = (LAST_YEAR - FIRST_GAME_YEAR) % YEARS_PER_TURN;


  /**
   * Maximum number of cards that a player may have in his, her or its hand.
   */
  public static final int MAX_HAND_SIZE = 7;

  //EnumFood.SIZE is what should be used for TOTAL_AGRO_CATEGORIES
  //public static final int TOTAL_AGRO_CATEGORIES = 12;

  /**
   * The circumference of the earth in kilometers.
    */

  public static final double EARTH_CIRCUMFERENCE = 40075;

}
