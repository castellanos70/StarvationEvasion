package starvationevasion.sim;

import starvationevasion.common.*;
import starvationevasion.io.DataReader;
import starvationevasion.io.FileObject;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The Simulator class is the main API for the Server to interact with the simulator.
 * This Model class is home to the calculations supporting that API.
 *
 * Each year the model advances, the model applies:
 * <ol>
 * <li>Most Policy Card Effects: Any changes in land use, fertilizer use, and world
 * trade penalty functions are calculated and applied.</li>
 * <li>Changes in land use: At the start of each simulated year, it is assumed that
 * farmers in each region of the world each adjust how they use land based on currently
 * enacted policies, the last year’s crop yields and the last year’s crop prices so as
 * to maximize individual profit while staying within any enacted laws.</li>
 * <li>Population: In this model, each region’s population is based only on data from
 * external population projections and a random number chosen at the start of the game.
 * Note that the occurrence of wide spread famine causes the game to end with all players
 * losing. Thus, it is not necessary to model the after effects of a famine. Random game
 * events such as hurricanes, typhoons, and political unrest are assumed to have
 * negligible effect on population.</li>
 * <li>Sea Level: This depends only on external model data, a random value chosen at the
 * start of the program and the year. Sea level only has two effects on the model:
 * higher sea level reduces costal farm productivity and increases damage probabilities
 * of special storm events (hurricane, and typhoons).</li>
 * <li>Climate: In this model, climate consists of annual participation, average annual
 * day and night temperatures and the annual number of frost free days on a 10 km x 10 km
 * grid across all arable land areas of the Earth.
 * <li>Occurrence of Special Events: Each year, there is a probability of each of many
 * random special events occurring. These include major storms, drought, floods,
 * unseasonable frost, a harsh winter, or out breaks of crop disease, blight, or insects.
 * Special events can also be positive the result in bumper crops in some areas. While
 * these events are random, their probabilities are largely affected by actions players
 * may or may not take. For example, policies encouraging improving irrigation can
 * mitigate the effects of drought, preemptive flood control spending can mitigate the
 * effects of floods and major storms and policies that encourage / discourage
 * monocropping can increase / decrease the probability of crop disease, blight, or
 * insects problems.</li>
 * <li>Farm Product Yield: The current year’s yield or each crop in each region is largely
 * a function of the current year’s land use, climate and special events as already
 * calculated.</li>
 * <li>Farm Product Need: This is based on each region’s population, regional dietary
 * preferences, and required per capita caloric and nutritional needs.</li>
 * <li>Policy Directed Food Distribution: Some player enacted policies may cause the
 * distribution of some foods to take place before the calculation of farm product
 * prices and such distributions may affect farm product prices. For example, sending
 * grain to very low income populations reduces the supply of that grain while not
 * affecting the world demand. This is because anyone who’s income is below being able
 * to afford a product, in economic terms, has no demand for that product.</li>
 * <li>Farm Product Demand and Price: Product price on the world market and demand are
 * highly interdependent and therefore calculated together.
 * <li>Food Distribution: In the global market, food distribution is effected by many
 * economic, political, and transportation factors. The Food Trade Penalty Function
 * (see below) is an attempt to assign each country a single number that adjusts the
 * efficiency of food on the global market being traded into that country (even if it
 * originated in that country). The game simulation allocates food products to each
 * country by maximizing the number of people feed under the application of the country’s
 * penalty function.</li>
 * <li>Human Development Index: Each country, based on the extent to which its nutritional
 * needs have been met, has its malnutrition, infant mortality, and life expectancy rates
 * adjusted. These are then used to calculate the country’s HDI.</li>
 * <li>Player Region Income: Each player receives tax revenue calculated as a percentage
 * of the player’s region’s total net farm income with any relevant enacted policy applied.</li>
 </ol>
 */


public class Model
{
  private final static Logger LOGGER = Logger.getLogger(Simulator.class.getName());
  private FileObject stateData;

  private final int startYear;
  private int year;




  public Model(int startYear)
  {

    this.startYear = startYear;
    year = startYear;

    stateData = DataReader.retrieveStateData("data/sim/UnitedStatesData/UnitedStatesFarmAreaAndIncome.csv");
    instantiateRegions(stateData.getRawData());


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

    float[] avgConversionFactors = new float[EnumFood.SIZE];
    for (String state : data)
    {
      State currentState = new State(state);
      states.add(currentState);
      float[] currentStatePercentages = currentState.getPercentages();

      for (int i = 0; i < EnumFood.SIZE; i++)
      {
        avgConversionFactors[i] += currentStatePercentages[i];
      }
    }

    float sum = 0.f;
    for (int i = 0; i < EnumFood.SIZE; i++)
    {
      //divide ny num records
      avgConversionFactors[i] /= 50.f;
      sum += avgConversionFactors[i];
      //System.out.println("AVG CATEGORY "+avgConversionFactors[i]);
    }

    float averageConversionFactor = sum/EnumFood.SIZE;

    for (State state : states)
    {
      state.setAverageConversionFactor(averageConversionFactor);
    }

    //Still need to reorganize and create a mechanism to set
    //the region field of each state and which data structure
    //to use for the regions.
  }

  /**
   *
   * @return the simulation year that has just finished.
   */
  protected int nextYear(ArrayList<Policy> cards)
  {
    year++;
    LOGGER.info("Advancing year to " + year);

    applyPolicies();
    updateLandUse();
    updatePopulation();
    updateClimate();
    generateSpecialEvents();
    updateFarmProductYield();
    updateFarmProductNeed();
    updateFarmProductMarket();
    updateFoodDistribution();
    updatePlayerRegionRevenue();
    updateHumanDevelopmentIndex();
    return year;
  }

  protected int getCurrentYear() {return year;}


  private void applyPolicies(){}
  private void updateLandUse(){}
  private void updatePopulation(){}
  private void updateClimate(){}
  private void generateSpecialEvents(){}
  private void updateFarmProductYield(){}
  private void updateFarmProductNeed(){}
  private void updateFarmProductMarket(){}
  private void updateFoodDistribution(){}
  private void updatePlayerRegionRevenue(){}
  private void updateHumanDevelopmentIndex(){}

}
