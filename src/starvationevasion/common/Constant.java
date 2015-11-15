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
   * At the start of the game and during each player's draw phase,
   * each player draws cards until he or she has HAND_SIZE cards.
   */
  public static final int HAND_SIZE = 7;
}
