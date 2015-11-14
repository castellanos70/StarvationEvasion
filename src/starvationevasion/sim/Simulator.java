package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.PolicyCard;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.EnumFood;
import java.util.ArrayList;

/**
 * This is the main API point of the Starvation Evasion Simulator.
 * This constructor should be called once at the start of each game by the Server.
 */
public class Simulator
{
  private final int startYear;
  private int year;




  /**
   * This constructor should be called once at the start of each game by the Server.
   */
  public Simulator(int startYear)
  {
    if (startYear < Constant.FIRST_YEAR || startYear > Constant.LAST_YEAR)
    {
      throw new IllegalArgumentException("Simulator(startYear="+startYear+
        ") start year must be between [" +
        Constant.FIRST_YEAR + ", " + Constant.LAST_YEAR+"].");
    }

    this.startYear = startYear;
    year = startYear;
  }



  /**
   * The server should call nextTurn(cards) when it is ready to advance the simulator
   * a turn (Constant.YEARS_PER_TURN years)
   * @param cards List of PolicyCards played this turn.
   * @return the simulation year after nextTurn() has finished.
   */
  public int nextTurn(ArrayList<PolicyCard> cards)
  {
    nextYear();
    nextYear();
    nextYear();

    return year;
  }



  private int nextYear()
  {
    year++;
    return year;
  }


  /**
   * @param region Any US or world region.
   * @param food Any food catagory.
   * @return Number of square km used for farming of the given food in the given region.
   */
  public int getLandUsed(EnumRegion region, EnumFood food)
  {
    return 0;
  }
}
