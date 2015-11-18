package starvationevasion.sim;


import starvationevasion.common.*;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * This is the main API point of the Starvation Evasion Simulator.
 * This constructor should be called once at the start of each game by the Server.
 */
public class Simulator
{
  private final static Logger LOGGER = Logger.getLogger(Simulator.class.getName());
  private FileObject stateData;

  private final int startYear;
  private int year;

  /**
   * This constructor should be called once at the start of each game by the Server.
   * Initializes the model
   * Generates a random 80 card deck for each player (both
   * human and AI players)
   *
   * @param startYear year the game is starting. Generally this will be Constant.FIRST_YEAR.
   */
  public Simulator(int startYear)
  {
    LOGGER.setLevel(Level.ALL);

    if (startYear < Constant.FIRST_YEAR || startYear > Constant.LAST_YEAR)
    {
      String errMsg = "Simulator(startYear=" + startYear +
                      ") start year must be between [" +
                      Constant.FIRST_YEAR + ", " + Constant.LAST_YEAR + "].";
      LOGGER.severe(errMsg);
      throw new IllegalArgumentException(errMsg);
    }

    this.startYear = startYear;
    year = startYear;

    stateData = DataReader.retrieveStateData("data/sim/UnitedStatesData/UnitedStatesFarmAreaAndIncome.csv");
    instantiateRegions(stateData.getRawData());
    LOGGER.info("Starting Simulation at year " + startYear);
  }

  /**
   * The server must call this for each playerRegion before the first turn
   * and during each turn's draw phase. This method will return the proper number of
   * cards from the top of the given playerRegion's deck taking into account cards played
   * and discarded by that player.
   * @param playerRegion region of player who id given the drawn cards.
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
    LOGGER.info("Advancing Turn...");
    nextYear();
    nextYear();
    nextYear();
    LOGGER.info("Turn complete, year is now " + year);
    return year;
  }

  /**
   * @param region Any US or world region.
   * @param food Any food catagory.
   * @return Number of square km used for farming of the given food in the given region.
   */
  public int getLandUsed(EnumRegion region, EnumFood food)
  {
    int landUsed = 0;
    LOGGER.info("Land used for food " + food + " in region " + region + " = "
                + landUsed + " km^2");
    return landUsed;
  }

  /**
   *
   * @return the simulation year that has just finished.
   */
  private int nextYear()
  {
    year++;
    LOGGER.info("Advancing year to " + year);
    return year;
  }

  /**
   * This method is used to create State objects along with 
   * the Region data structure
   *
   * @param data
   */
  private void instantiateRegions(ArrayList<String> data)
  {
    if (data.size() == 0) return;

    ArrayList<State> states = new ArrayList<>();

    float[] avgConversionFactors = new float[Constant.TOTAL_AGRO_CATEGORIES];
    for (String state : data)
    {
      State currentState = new State(state);
      states.add(currentState);
      float[] currentStatePercentages = currentState.getPercentages();

      for (int i = 0; i < Constant.TOTAL_AGRO_CATEGORIES; i++)
      {
        avgConversionFactors[i] += currentStatePercentages[i];
      }
    }

    float sum = 0.f;
    for (int i = 0; i < Constant.TOTAL_AGRO_CATEGORIES; i++)
    {
      //divide ny num records
      avgConversionFactors[i] /= 50.f;
      sum += avgConversionFactors[i];
      //System.out.println("AVG CATEGORY "+avgConversionFactors[i]);
    }

    float averageConversionFactor = sum/Constant.TOTAL_AGRO_CATEGORIES;

    for (State state : states)
    {
      state.setAverageConversionFactor(averageConversionFactor);
    }

    //Still need to reorganize and create a mechanism to set
    //the region field of each state and which data structure
    //to use for the regions.
  }

  //Temporary main for testing & debugging Simulator and State Objs.
  public static void main(String[] args)
  {
    new Simulator(Constant.FIRST_YEAR);
  }
}
