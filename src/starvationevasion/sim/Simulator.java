package starvationevasion.sim;

import starvationevasion.common.*;
import starvationevasion.sim.datamodels.State;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the main API point of the Starvation Evasion Simulator.
 * This constructor should be called once at the start of each game by the Server.
 */
public class Simulator
{
  private final int startYear;
  private int year;

  //private final String stateDataPath = System.getenv("PWD")+"/data/sim/UnitedStatesData/UnitedStatesFarmAreaAndIncome.csv";
  private BufferedReader reader;

  /**
   * This constructor should be called once at the start of each game by the Server.
   * Initializes the model
   * Generates a random 80 card deck for each player (both
   * human and AI players)
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
    String stateDataPath = "/Users/miggens/Developer/StarvationEvasion/data/sim/UnitedStatesData/UnitedStatesFarmAreaAndIncome.csv";
    System.out.println("DATA PATH " + stateDataPath);

    //read in data
    //for each line pass to create a new state.
    Pattern stateDataPattern = Pattern.compile("^\\w+,\"");
    try
    {
      reader = new BufferedReader(new FileReader(stateDataPath));
      String line = null;
      while ((line = reader.readLine()) != null)
      {
        Matcher stateDataMatcher = stateDataPattern.matcher(line);
        if (stateDataMatcher.find())
        {
          System.out.println(line);
          State s = new State(line);
          System.exit(2);
        }

      }
    }
    catch (Throwable t)
    {
      System.err.println("File Reader Exception: "+ t);
    }

  }

  /**
   * The server must call this for each playerRegion before the first turn
   * and during each turn's draw phase. This method will return the proper number of
   * cards from the top of the given playerRegion's deck taking into account cards played
   * and discarded by that player.
   * @param playerRegion
   * @return list of cards.
   */
  public ArrayList<EnumPolicy> drawCards(EnumRegion playerRegion)
  {
    return null;
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

  //Temporary main for testing & debugging Simulator and State Objs.
  public static void main(String[] args)
  {
    new Simulator(Constant.FIRST_YEAR);
  }
}
