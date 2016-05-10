package starvationevasion.sim;

import javafx.scene.shape.Polygon;
import starvationevasion.common.Constant;
import starvationevasion.common.EnumCropZone;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.MapPoint;
import starvationevasion.common.MapProjectionMollweide;
import starvationevasion.common.RegionData;
import starvationevasion.common.SpecialEventData;
import starvationevasion.common.Util;
import starvationevasion.common.WorldData;
import starvationevasion.common.gamecards.GameCard;
import starvationevasion.sim.LandTile.Field;
import starvationevasion.sim.events.AbstractEvent;
import starvationevasion.sim.events.Drought;
import starvationevasion.sim.events.EventDriver;
import starvationevasion.sim.events.Hurricane;
import starvationevasion.sim.io.GeographyXMLparser;
import starvationevasion.sim.io.ProductionCSVLoader;
import starvationevasion.sim.io.SpecialEventCSVLoader;
import starvationevasion.util.JavaFX_DebugViewer;

import java.awt.geom.Area;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * The Simulator class is the main API for the Server to interact with the simulator.
 * This Model class is home to the calculations supporting that API.
 * <p>
 * Each currentYear the model advances, the model applies:
 * <ol>
 * <li>Most Policy Card Effects: Any changes in land use, fertilizer use, and world
 * trade penalty functions are calculated and applied.</li>
 * <li>Changes in land use: At the start of each simulated currentYear, it is assumed that
 * farmers in each region of the world each adjust how they use land based on currently
 * enacted policies, the last currentYear's crop yields and the last currentYear's crop prices so as
 * to maximize individual profit while staying within any enacted laws.</li>
 * <li>Population: In this model, each region's population is based only on data from
 * external population projections and a random number chosen at the start of the game.
 * Note that the occurrence of wide spread famine causes the game to end with all players
 * losing. Thus, it is not necessary to model the after effects of a famine. Random game
 * events such as hurricanes, typhoons, and political unrest are assumed to have
 * negligible effect on population.</li>
 * <li>Sea Level: This depends only on external model data, a random value chosen at the
 * start of the program and the currentYear. Sea level only has two effects on the model:
 * higher sea level reduces costal farm productivity and increases damage probabilities
 * of special storm events (hurricane, and typhoons).</li>
 * <li>Climate: In this model, climate consists of annual participation, average annual
 * day and night temperatures and the annual number of frost free days on a 10 km x 10 km
 * grid across all arable land areas of the Earth.
 * <li>Occurrence of Special Events: Each currentYear, there is a probability of each of many
 * random special events occurring. These include major storms, drought, floods,
 * unseasonable frost, a harsh winter, or out breaks of crop disease, blight, or insects.
 * Special events can also be positive the result in bumper crops in some areas. While
 * these events are random, their probabilities are largely affected by actions players
 * may or may not take. For example, policies encouraging improving irrigation can
 * mitigate the effects of drought, preemptive flood control spending can mitigate the
 * effects of floods and major storms and policies that encourage / discourage
 * monocropping can increase / decrease the probability of crop disease, blight, or
 * insects problems.</li>
 * <li>Farm Product Yield: The current currentYear's yield or each crop in each region is largely
 * a function of the current currentYear's land use, climate and special events as already
 * calculated.</li>
 * <li>Farm Product Need: This is based on each region's population, regional dietary
 * preferences, and required per capita caloric and nutritional needs.</li>
 * <li>Policy Directed Food Distribution: Some player enacted policies may cause the
 * distribution of some foods to take place before the calculation of farm product
 * prices and such distributions may affect farm product prices. For example, sending
 * grain to very low income populations reduces the supply of that grain while not
 * affecting the world demand. This is because anyone who's income is below being able
 * to afford a product, in economic terms, has no demand for that product.</li>
 * <li>Farm Product Demand and price: Product foodPrice on the world market and demand are
 * highly interdependent and therefore calculated together.
 * <li>Food Distribution: In the global market, food distribution is effected by many
 * economic, political, and transportation factors. The Food Trade Penalty Function
 * (see below) is an attempt to assign each country a single number that adjusts the
 * efficiency of food on the global market being traded into that country (even if it
 * originated in that country). The game simulation allocates food products to each
 * country by maximizing the number of people feed under the application of the country's
 * penalty function.</li>
 * <li>Human Development Index: Each country, based on the extent to which its nutritional
 * needs have been met, has its malnutrition, infant mortality, and life expectancy rates
 * adjusted. These are then used to calculate the country's HDI.</li>
 * <li>Player Region Income: Each player receives tax revenue calculated as a percentage
 * of the player's region's total net farm income with any relevant enacted policy applied.</li>
 * </ol>
 */


public class Model
{
  public static final int TOTAL_LAND_TILES = 167235;
  public static double EVENT_CHANCE = 0.02;
  private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  EnumRegion debugRegion = EnumRegion.USA_CALIFORNIA;
  private final static Logger LOGGER = Logger.getGlobal(); // getLogger(Model.class.getName())

  // Verbosity of debug information during startup
  //
  private final static Level debugLevel = Level.FINE;
  public static final int YEARS_OF_DATA = 1 + Constant.LAST_YEAR - Constant.FIRST_DATA_YEAR;

  private final static boolean DEBUG = true;

  private WorldData[] worldData = new WorldData[YEARS_OF_DATA];
  private CropData cropData;

  private int currentYear;

  /**
   * List of all territories. A copy of each pointer stored in this list is
   * placed in territoryList of the region to which that territory belongs.<br><br>
   *
   * This list is build from a data file when the model loads and is never changed.<br><br>
   *
   * This list must be in lexicographic order.
   */
  private ArrayList<Territory> territoryList;

  // The set of world regions includes all of the regions in the enum, plus an
  // extra United States region aggregating all of the US states for book keeping
  // purposes.
  //
  private Region[] regionList = new Region[EnumRegion.SIZE];

  /**
   * Right now, the players control the 7 US Regions.
   */
  private Region[] unitedStatesRegionList = new Region[EnumRegion.US_REGIONS.length];

  private SeaLevel seaLevel;

  private ArrayList<SpecialEventData> specialEventDatum;

  private List<AbstractEvent> specialEvents = new ArrayList<>();

  private PackedTileData packedTileData;

  public Model()
  {
    territoryList = Territory.territoryLoader();
    new GeographyXMLparser(this);

    assert (assertTerritoryGeography());

    instantiateRegions();

    ProductionCSVLoader.load(regionList);

    cropData = new CropData();


    Date dateStart = new Date();
    System.out.println("Model() Loading Climate Data: " +dateFormat.format(dateStart));

    ArrayList<LandTile> tileList = new ArrayList<>(TOTAL_LAND_TILES);
    LandTile.loadLocations(this, tileList);
    assert (tileList.size() == TOTAL_LAND_TILES);
    LandTile.loadClimate(tileList);
    //System.out.println("tileList.size()="+tileList.size());



    Date dateDone = new Date();
    double deltaSec = (dateDone.getTime() - dateStart.getTime())/1000.0;
    System.out.println("LandTile.load() Done: elapsed sec=" +deltaSec);
    
    assert (assertLandTiles());
    
    packedTileData = new PackedTileData(TOTAL_LAND_TILES);

    updateCropRatings(Constant.FIRST_GAME_YEAR-1);

    placeCrops();

    EventDriver driver = new EventDriver(this);

    setRegionalProduction();

    populateUSRegionList();

    for (int i = 0; i < YEARS_OF_DATA; i++)
    {
      worldData[i] = new WorldData();
      if (i < Constant.FIRST_GAME_YEAR - Constant.FIRST_DATA_YEAR)
      { populateWorldData(Constant.FIRST_DATA_YEAR + i); }
    }
  }

  public void init()
  {
    currentYear = Constant.FIRST_GAME_YEAR;
  }

  private boolean assertTerritoryGeography()
  {
    Territory NewMexico = null, China = null, UnitedKingdom = null, Ireland = null;
    for (Territory territory : territoryList)
    {
      if (territory.getName().equals("US-NewMexico"))
      {
        NewMexico = territory;
      }
      else if (territory.getName().equals("China"))
      {
        China = territory;
      }
      else if (territory.getName().equals("United Kingdom"))
      {
        UnitedKingdom = territory;
      }
      else if (territory.getName().equals("Ireland"))
      {
        Ireland = territory;
      }

    }
    assert (NewMexico != null);
    assert (China != null);
    assert (UnitedKingdom != null);
    assert (NewMexico.contains(35, -106)); //Albuquerque
    assert (!China.contains(35, -106)); //Albuquerque
    assert (China.contains(40, 116)); //Beijing
    assert (China.contains(31.2, 121.5)); //Shanghai
    assert (UnitedKingdom.contains(51.5, -0.13)); //London
    assert (UnitedKingdom.contains(54.5970, -5.93)); //Belfast, Northern Ireland
    assert (!UnitedKingdom.contains(53.349925, -6.270475)); //Dublin, Ireland
    assert (Ireland.contains(53.349925, -6.270475)); //Dublin, Ireland
    assert (!UnitedKingdom.contains(53.347309, -5.681383)); //Irish Sea
    assert (!Ireland.contains(53.347309, -5.681383)); //Irish Sea
    assert (!UnitedKingdom.contains(50.39, -1.7)); //English Channel


    return true;
  }




  private boolean assertLandTiles()
  {
    /*
    for (Territory territory : territoryList)
    {
      float area = territory.getLandTotal();
      System.out.println("LandTiles: " + territory.getName() + ": area=" +
        area + ", tile count=" + territory.getLandTiles().size() +
        ", land per tile = " + area / territory.getLandTiles().size());
    }
    */

    for (Region region : regionList)
    {
      int totalTiles = 0;
      ArrayList<Territory> myTerritories = region.getTerritoryList();
      for (Territory territory : myTerritories)
      {
        totalTiles += territory.getLandTiles().size();
      }

      float area = region.getLandTotal();
      System.out.println("LandTiles: " + region.getName() + ": area=" +
        area + ", tile count=" + totalTiles + ", land per tile = " + area / totalTiles);
    }
    return true;
  }

  public int getCurrentYear()
  {
    return currentYear;
  }

  public Region getRegion(EnumRegion r)
  {
    return regionList[r.ordinal()];
  }

  public Territory getTerritory(MapPoint mapPoint)
  {
    return getTerritory(mapPoint.latitude, mapPoint.longitude);
  }

  public Territory getTerritory(double latitude, double longitude)
  {
    //This is the code that should actually be used.
    for (Territory territory : territoryList)
    {
      if (territory.contains(latitude, longitude)) return territory;
    }
    return null;


    //This code is used for debug only.
    /*
    Territory found = null;
    for (Territory territory : territoryList)
    {
      if (territory.contains(latitude, longitude))
      {
        if (found != null)
        {
          System.out.println("ERROR: Point ["+latitude+", "+longitude+
            "] in two territories: " + found.getName() + " & " + territory.getName());
        }
        found = territory;
      }
    }
    return found;
  */
  }


  /**
   * Uses binary search to fine the given name in the territory list.
   * @param name of territory
   * @return reference to the unique territory with the given name or null if there does not
   * exist a territory with the given name in territoryList
   */
  public Territory getTerritory(String name)
  {

    //System.out.println("getTerritory("+name+")");
    int start = 0;
    int end = territoryList.size()-1;
    int i = end/2;

    while (end >= start)
    {
      Territory territory = territoryList.get(i);

      int result = name.compareTo(territory.getName());
      //System.out.println("    "+territory.getName() + ", result="+result + ": "+start+", "+i+", "+end);

      if (result < 0) end = i-1;
      else if (result > 0) start = i+1;
      else return territory;

      i = (end+start)/2;

      //System.out.println("           "+start+", "+i+", "+end);
    }
    return null;
    //for (Territory territory : territoryList)
    //{
    //  if (territory.getName().equals(name)) return territory;
    //}
    //return null;
  }

  public Region[] getRegionList()
  {
    return regionList;
  }


  public GeographicArea getGeographicArea(EnumRegion regionCode)
  {
    return regionList[regionCode.ordinal()].getGeographicArea();
  }


  public Area[] getRegionPerimetersSpherical()
  {
    Area[] areaList = new Area[EnumRegion.SIZE];

    for (EnumRegion regionID : EnumRegion.values())
    {
      int i = regionID.ordinal();
      areaList[i] = regionList[i].getGeographicArea().getPerimeter();
    }
    return areaList;
  }

  public List<AbstractEvent> getSpecialEvents()
  {
    return specialEvents;
  }

  public PackedTileData getPackedTileData()
  {
    return packedTileData;
  }

  /**
   * A Region is the base political unit exposed to the player.
   * A region a set of territoryList. Each territory is assigned to one region.
   */
  private void instantiateRegions()
  {
    for (int i = 0; i < EnumRegion.SIZE; i++)
    {
      regionList[i] = new Region(EnumRegion.values()[i]);
    }


    //Add each territory to its region
    for (Territory territory : territoryList)
    {
      int regionIdx = territory.getGameRegion().ordinal();
      regionList[regionIdx].addTerritory(territory);
    }

    for (int i = 0; i < EnumRegion.SIZE; i++)
    {
      for (int year = Constant.FIRST_DATA_YEAR; year < Constant.FIRST_GAME_YEAR; year++)
      {
        regionList[i].aggregateTerritoryData(year);
      }
    }

    //try{cropLoader = new CropCSVLoader();} catch (Throwable t){ System.out.println("CROP_LOADER "+t);}
    //cropZoneDatum = cropLoader.getCategoryData();


    //if (DEBUG) System.out.println("Model.instantiateRegions() estimate initial yield.");
    // Traverse all of the regions, estimating the initial yield.
    // Note that this includes the book-keeping regions.
    //
    //for (Region region : regionList)
    //{ // Roll up the population and undernourished for each region.
    //
    //  region.updatePopulation(Constant.FIRST_YEAR);

    // Update the initial yield.
    //
    // region.estimateInitialYield();
    //}

    //for (Region region : regionList) region.estimateInitialBudget(cropLoader.getCategoryData());
    //for (Region region : regionList)
    //{
    //  if (region.getRegionEnum() == null || !region.getRegionEnum().isUS())
    // {
    //    region.estimateInitialCropLandArea(cropLoader.getCategoryData());
    //  }
    //}

    // Now iterate over the enumeration to optimize planting for each game
    // region.
    //
    //for (EnumRegion region : EnumRegion.values())
    //{
    // TODO : The tile optimization function will only work if we have the
    // CropClimateData structure correctly populated for each of the crops.
    //
    // calculate OTHER_CROPS temp & rain requirements for each country
    //  for (Territory state : regionList[region.ordinal()].getTerritoryList())
    //  {

    //    CropOptimizer optimizer = new CropOptimizer(Constant.FIRST_YEAR, state);
    //    optimizer.optimizeCrops();
    //  }
    //}

    // Finally, aggregate the totals for all regions (including book keeping).
    //
    //if (debugLevel.intValue() < Level.INFO.intValue())
    //{ Simulator.dbg.println("*** Initialized territory data .............");
    //}

    //for (Region region : regionList)
    //{ region.aggregateTerritoryFields(Constant.FIRST_YEAR);
    //  if (debugLevel.intValue() < Level.INFO.intValue()) printRegion(region, Constant.FIRST_YEAR);
    //}
  }

  /**
   * Every method is currently uncommented as not everything is currently implemented.
   *
   * @return the simulation currentYear that has just finished.
   */
  protected int nextYear(ArrayList<GameCard> cards)
  {
    LOGGER.info("******* SIMULATION YEAR ******** " + currentYear);

    //applyPolicies(); // In progress.

    //updateLandUse(); // Not started.

    //updatePopulation(); // In progress.

    //updateClimate(); // Done.

    //generateSpecialEvents(); // In progress (Alfred).

    //applySpecialEvents(); // Done.

    //replantCrops();

    //updateFarmProductYield(); // Done.

    //updateFarmProductNeed(); // Done.

    //updateFarmProductMarket(); // Not started.

    //updateFoodDistribution(); // Not started.

    //updatePlayerRegionRevenue(); // Not started.

    //updateHumanDevelopmentIndex(); // Done.


    //if (debugLevel.intValue() < Level.INFO.intValue())
    //{ Simulator.dbg.println("******************************************* FINAL Stats for " + debugRegion + " in " +
    // currentYear);
    //  printRegion(regionList[debugRegion.ordinal()], currentYear);
    //}

    // updates the worlddata with all the values of this year. If none of the
    // above methods are called, everything is 0.
    populateWorldData(currentYear);
    currentYear++;
    return currentYear;
  }

  protected WorldData populateWorldData(int year)
  {
    //System.out.println("Model.populateWorldData("+year+")");
    //ArrayList<CropZoneData> categoryData = cropLoader.getCategoryData();

    int yearIdx = year - Constant.FIRST_DATA_YEAR;
    WorldData data = worldData[yearIdx];
    data.year = year;

    for (int i = 0; i < EnumFood.SIZE; i++)
    {
      for (EnumFood food : EnumFood.values())
      {
        data.foodPrice[food.ordinal()] = cropData.getPrice(year, food);
      }
    }


    //Region Data
    for (int i = 0; i < EnumRegion.SIZE; i++)
    {
      RegionData region = data.regionData[i];
      region.population = regionList[i].getPopulation(year);
      region.undernourished = regionList[i].getUndernourished(year);
      region.humanDevelopmentIndex = regionList[i].getHumanDevelopmentIndex();

      region.revenueBalance = regionList[i].getRevenue();
      region.landArea = regionList[i].getLandTotal();

      for (EnumFood food : EnumFood.values())
      {
        region.foodProduced[food.ordinal()] = regionList[i].getCropProduction(year, food);
        region.foodImported[food.ordinal()] = regionList[i].getCropImport(year, food);
        region.foodExported[food.ordinal()] = regionList[i].getCropExport(year, food);

        //Simulator keeps income in $1000s but client is given income in millions of dollars.
        //long thousandsOfDollars = regionList[i].getCropIncome(food);

        //If a very small amount, then make at least 1 million.
        //if ((thousandsOfDollars > 1) && (thousandsOfDollars<500)) thousandsOfDollars+= 500;

        //Round up
        //region.foodIncome[food.ordinal()]   += ( thousandsOfDollars + 600)/1000;
        //region.farmArea[food.ordinal()] = regionList[i].getCropLand(food);
      }
    }
    return data;
  }


  protected WorldData getWorldData(int year)
  {
    int yearIdx = year - Constant.FIRST_DATA_YEAR;
    return worldData[yearIdx];
  }

  public CropData getCropData()
  {
    return this.cropData;
  }

  /**
   * Searches the regionList for the US regions and adds them to
   * unitedStatesRegionList[].
   */
  private void populateUSRegionList()
  {
    int it = 0;
    ArrayList<EnumRegion> usRegions = new ArrayList<>();

    for(int i = 0; i < EnumRegion.US_REGIONS.length; i++)
    {
      usRegions.add(EnumRegion.US_REGIONS[i]);
    }

    for(int i = 0; i < regionList.length; i++)
    {
      if(usRegions.contains(regionList[i].getRegionEnum()))
      {
        unitedStatesRegionList[it] = regionList[i];
        it++;
      }
    }
  }

  /**
   * Linear interpolate population.
   */
  private void interpolatePopulation(Territory territory, int year0, int year1)
  {
    int y0 = territory.getPopulation(year0);
    int y1 = territory.getPopulation(year1);

    for (int i = year0 + 1; i < year1; i += 1)
    {
      double y = y0 + (y1 - y0) * (((double) i - year0) / (year1 - year0));
      territory.setPopulation(i, (int) y);
    }
  }

  /**
   * Looks at each policy enacted for this turn and applies the appropriate
   * ones.
   *
   * Cards not directly affecting the simulation model presumably should not be
   * handled here. Cards like CovertIntelligence, which lets you look at another
   * player's hand, do not directly affect the model. Right now they are handled
   * in Simulator.NextTurn(). Since policy cards are the first thing applied in
   * Model.nextYear(), it doesn't alter any calculations as the model completes
   * a year's simulation.
   *
   * Hard-coded values are constants of the specific policy card. The card
   * classes do not currently provide these values.
   *
   * Cases in switch statement with TODO: still need to be implemented.
   *
   * @param cards
   *          the list of all cards to be applied to the model
   */
  private void applyPolicies(ArrayList<GameCard> cards)
  {
    if (debugLevel.intValue() < Level.INFO.intValue())
    {
      Simulator.dbg.println("******************************************* Applying policies");
    }
    
    for (GameCard c : cards)
    {
      switch(c.getCardType())
      {
        case Policy_CleanRiverIncentive:
          //TODO:
          break;
        case Policy_DivertFunds:
          //remove all cards from owners hand -- done in Simulator.java
          //give 14 million dollars to owner
          getRegion(c.getOwner()).addToRevenue(14000000);
          break;
        case Policy_EducateTheWomenCampaign:
          //TODO:
          break;
        case Policy_EfficientIrrigationIncentive:
          //TODO:
          break;
        case Policy_EthanolTaxCreditChange:
          // if c.getX() == 25, user selected a 25% tax credit
           getRegion(c.getOwner()).setEthanolProducerTaxCredit(c.getX());
          break;
        case Policy_FarmInfrastructureSubSaharan:
          //TODO:
          break;
        case Policy_FertilizerAidCentralAsia:
          // if c.getX()==6 send 6 million dollars in fertilizer to Central Asia
          sendFertilizerAid(EnumRegion.CENTRAL_ASIA, c.getX());
          break;
        case Policy_FertilizerAidMiddleAmerica:
          sendFertilizerAid(EnumRegion.MIDDLE_AMERICA, c.getX());
          break;
        case Policy_FertilizerAidOceania:
          sendFertilizerAid(EnumRegion.OCEANIA, c.getX());
          break;
        case Policy_FertilizerAidSouthAsia:
          sendFertilizerAid(EnumRegion.SOUTH_ASIA, c.getX());
          break;
        case Policy_FertilizerAidSubSaharan:
          sendFertilizerAid(EnumRegion.SUB_SAHARAN, c.getX());
          break;
        case Policy_FertilizerSubsidy:
          //TODO:
          break;
        case Policy_FoodReliefCentralAsia:
          // Sends 5 thousand tons of selected food
          sendFoodRelief(EnumRegion.CENTRAL_ASIA, c.getOwner(), c.getTargetFood(), 5000);
          break;
        case Policy_FoodReliefMiddleAmerica:
          sendFoodRelief(EnumRegion.MIDDLE_AMERICA, c.getOwner(), c.getTargetFood(), 5000);
          break;
        case Policy_FoodReliefOceania:
          sendFoodRelief(EnumRegion.OCEANIA, c.getOwner(), c.getTargetFood(), 5000);
          break;
        case Policy_FoodReliefSouthAsia:
          sendFoodRelief(EnumRegion.SOUTH_ASIA, c.getOwner(), c.getTargetFood(), 5000);
          break;
        case Policy_FoodReliefSubSaharan:
          sendFoodRelief(EnumRegion.SUB_SAHARAN, c.getOwner(), c.getTargetFood(), 5000);
          break;
        case Policy_Fundraiser:
          getRegion(c.getOwner()).addToRevenue(1000000);
          break;
        case Policy_InternationalFoodRelief:
          //TODO:
          break;
        case Policy_Loan:
          //TODO:
          break;
        case Policy_MyPlatePromotionCampaign:
          getRegion(c.getOwner()).subtractFromRevenue(c.getX());
          //TODO: Apply effect of the advertising effects.
          break;
        case Policy_ResearchInsectResistanceGrain:
          //TODO:
          break;
        case Policy_SpecialInterests:
          //TODO:
          break;
        default:
          break;
      }
    }
  }

  /**
   * These cards are of the same type: Each US region sends fertilizer aid to a
   * specific region.
   *
   * Subtract amount from each US Region, and send 7 times that amount to the
   * specific region
   */
  private void sendFertilizerAid(EnumRegion region, int amount)
  {
    for(int i = 0; i < unitedStatesRegionList.length; i++)
    {
      unitedStatesRegionList[i].subtractFromRevenue(amount);
    }

    for(int i = 0; i < regionList.length; i++)
    {
      if(regionList[i].getRegionEnum().equals(region))
      {
        regionList[i].addFertilizerAid(amount*7);
      }
    }
  }

  /**
   * Policy card handler. Moves x amount of y food from sender to target.
   *
   * @param target The region receiving the food
   * @param sender The region sending the food
   * @param food The type of EnumFood.
   * @param amount The amount of tons of food being sent.
   */
  private void sendFoodRelief(EnumRegion target, EnumRegion sender, EnumFood food, int amount)
  {
    // TODO: determine where the food is coming from (from the sender). 5 thousand
    // tons of food needs to come from somewhere, we don't just make it magically.

    for(int i = 0; i < regionList.length; i++)
    {
      if(regionList[i].getRegionEnum().equals(target))
      {
        regionList[i].addFoodRelief(food,amount);
      }
    }
  }

  private void updateLandUse()
  {
    // TODO : Land use is based on policies.
    // Notes :
    // Start with how much each country is producing v/s how much land they are using.
    // This gives us a yield factor.  If a country with a high yield applies irrigation
    // won't benefit as much as countries with a low yield.  Make an 'S' curve (bezier)
    // with a fit quadratic equation.
    //
    if (debugLevel.intValue() < Level.INFO.intValue())
    {
      Simulator.dbg.println("******************************************* Updating land use");
    }
  }

  private void updateClimate()
  {
    // Done.
    //
    //if (debugLevel.intValue() < Level.INFO.intValue())
    //{ Simulator.dbg.println("******************************************* Updating climate");
    //}

    //if (debugLevel.intValue() < Level.INFO.intValue())
    //{ printCurrentClimate(regionList[debugRegion.ordinal()], currentYear);
    //}
  }

  private void generateSpecialEvents()
  {
//    // TODO: 12/6/2015 Alfred is working on this.
//    //
//    if (debugLevel.intValue() < Level.INFO.intValue())
//    {
//      Simulator.dbg.println("******************************************* Generating special events");
//    }
//
//    //check current currentYear.
//    int CURRENT_YEAR = 2015;
//    if (currentYear < CURRENT_YEAR)
//    {
//      //Then there should be a pre-existing event to draw upon. Then
//      //there ought to have been a process that loaded the events to draw from
//      for (SpecialEventData event : specialEventDatum)
//      {
//        if (event.year == currentYear)
//        {
//          //add current event to data structure of events for the currentYear
//        }
//      }
//    }
//    else
//    {
//      //If this is the case then examine the players behaviors. Is it probable
//      //that their region could experience an event based on the leaders actions
//      //through policy. So their current status is important:
//      //1. Are they in crisis already?
//      //2. What are their current policies?
//      //3. if in crisis will the current policies help or hurt?
//      //4. if not in crisis will the current policies improve the regions state?
//    }
//
//    // Temporary code just to make special events happen in the absence of Alfred's timeline.
//    //
//    int attempts = 5;
//    Random rand = new Random();
//    while (attempts > 0)
//    {
//      if (rand.nextFloat() < EVENT_CHANCE)
//      {
//        if (rand.nextBoolean())
//        {
//          // do a hurricane
//          Region us = regionList[EnumRegion.SIZE];
//          int idx = rand.nextInt(us.getTerritoryList().size() - 1) + 1;
//          for (Territory territory : us.getTerritoryList())
//          {
//            if (idx == 0)
//            {
//              specialEvents.add(new Hurricane(territory));
//              break;
//            }
//            idx--;
//          }
//        }
//        else
//        {
//          // do a drought
//          int idx = rand.nextInt(EnumRegion.US_REGIONS.length);
//          Region usRegion = regionList[EnumRegion.US_REGIONS[idx].ordinal()];
//          specialEvents.add(new Drought(usRegion));
//        }
//      }
//      attempts--;
//    }
  }

  private void applySpecialEvents()
  {
    if (specialEvents.isEmpty()) return;

    for (Iterator<AbstractEvent> iterator = specialEvents.iterator(); iterator.hasNext(); )
    {
      AbstractEvent event = iterator.next();
      event.applyEffects();

      // remove the event if its duration is 0.
      if (event.getDuration() < 1)
      {
        iterator.remove();
      }
    }
  }

  private void updateFarmProductYield()
  {
    if (debugLevel.intValue() < Level.INFO.intValue())
    {
      Simulator.dbg.println("******************************************* Updating farm product yield");
    }

    // Iterate over all of the regions, including the book keeping regions.  Each
    // region invokes a territory update and then computes an aggregate number
    // for the region.  Territories that are in both game and book-keeping regions
    // may compute their yield twice, but this has no side effects.
    //
    for (Region region : regionList)
    {
      region.updateYield(currentYear);
    }

    if (debugLevel.intValue() < Level.INFO.intValue())
    {
      printCropYield(regionList[debugRegion.ordinal()], currentYear);
    }
  }

  private void updateFarmProductNeed()
  {
    if (debugLevel.intValue() < Level.INFO.intValue())
    {
      Simulator.dbg.println("******************************************* Updating farm product need");
    }

    // Iterate over only the game regions.
    //
    for (int i = 0; i < EnumRegion.SIZE; i++)
    {
      regionList[i].updateCropNeed(currentYear);
    }

    if (debugLevel.intValue() < Level.INFO.intValue())
    {
      printCropNeed(regionList[debugRegion.ordinal()], currentYear);
    }
  }

  private void updateFarmProductMarket()
  {
    // TODO : Not implemented.
  }

  private void updateFoodDistribution()
  {
    // TODO: Not implemented.  Tie in the new trading optimizer, and subtract revenue.
    // If a territory can't buy enough product then we need to
    // update the undernourishment factor.
    //
  }

  private void updatePlayerRegionRevenue()
  {
    // TODO : Not implemented.  The US will be trading food as a region.  The results
    // of these trades need to be propegated to the US regions.
    //
  }

  private void updateHumanDevelopmentIndex()
  {
    // TODO: HDI is updated in the roll-up of the territoryList into regions, based on the
    // undernourished factor.
    //
  }

  /**
   * iterates through each region and places down crops, semi-randomly,
   *  based on recorded production amounts.
   */
  private void placeCrops()
  {

    System.out.println("Model.placeCrops() Starting");
    long start = System.nanoTime();
    List<LandTile> regionTileList = new ArrayList<>();
    List<Territory> territoryList;
    //iterate through each region and fill a list of territories for each region
    for(int i = 0; i < regionList.length; i++)
    {

      territoryList = regionList[i].getTerritoryList();
      //populate regionTileList with all tiles of all territories in the region
      for(Territory territory : territoryList)
      {
        regionTileList.addAll(territory.getLandTiles());
      }
      //go through the list of crops, one by one, and assign them to tiles, with
      //weighted probabilities, based on the crop ratings for each tile
      for(int j = 0; j < EnumFood.CROP_FOODS.length; j++)
      {
        //the amount of tiles per region for each crop is the total land area for each crop divided by the area
        //of a land tile.
        int numTilesForCrop = (int) regionList[i].getCropArea(Constant.FIRST_DATA_YEAR, EnumFood.CROP_FOODS[j])
                / (regionList[i].getLandTotal() / regionList[i].getNumTiles());
        //given the amount of tiles used for each crop, randomly place the appropriate number of the given crop.
        for(int k = 0; k < numTilesForCrop && regionTileList.size() > 0; k++)
        {

          int randomTileIndex = Util.rand.nextInt(regionTileList.size());
          LandTile tile = regionTileList.get(randomTileIndex);
          EnumCropZone cropRating = tile.getCropRatings()[j];
          if(cropRating.ordinal() == 3) //crop is IDEAL for this location. Place it.
          {
            tile.setCrop(EnumFood.CROP_FOODS[j]);
            regionTileList.remove(randomTileIndex);
          }
          else if(cropRating.ordinal() == 2) //crop is GOOD for this location. 80% chance to place it.
          {
            if(Util.rand.nextDouble() < 0.8)
            {
              tile.setCrop(EnumFood.CROP_FOODS[j]);
              regionTileList.remove(randomTileIndex);
            }
          }
          else if(cropRating.ordinal() == 1)
          {
            if(Util.rand.nextDouble() < .4) //crop is ACCEPTABLE for this location. 40% chance to place it.
            {
              tile.setCrop(EnumFood.CROP_FOODS[j]);
              regionTileList.remove(randomTileIndex);
            }
          }
          else
          {
            if(Util.rand.nextDouble() < 0.2) //crop is POOR for this location. 20% chance to place it.
            {
              tile.setCrop(EnumFood.CROP_FOODS[j]);
              regionTileList.remove(randomTileIndex);
            }
          }
        }
      }
      //do the same for non-crops
      for(int j = 0; j < EnumFood.NON_CROP_FOODS.length; j++)
      {
        //right now, this is just placing crops using FIRST_DATA_YEAR. Year updates will need to be implemented.
        int numTilesForCrop = (int) regionList[i].getCropArea(Constant.FIRST_DATA_YEAR, EnumFood.NON_CROP_FOODS[j])
                / (regionList[i].getLandTotal() / regionList[i].getNumTiles());

        for(int k = 0; k < numTilesForCrop && regionTileList.size() > 0; k++)
        {

          int randomTileIndex = Util.rand.nextInt(regionTileList.size());
          LandTile tile = regionTileList.get(randomTileIndex);
          EnumCropZone cropRating = tile.getCropRatings()[j];
          if(cropRating.ordinal() == 3) //non-crop is IDEAL for this location. Place it.
          {
            tile.setCrop(EnumFood.NON_CROP_FOODS[j]);
            regionTileList.remove(randomTileIndex);
          }

        }
      }
      regionTileList.clear();
    }

    //uncomment the following code for testing
    // List<LandTile> landTiles = new ArrayList<>();
    // int num = 1;
    // for (int i = 0; i < regionList.length; i++)
    // { //For each Region
    //   for (int j = 0; j < regionList[i].getTerritoryList().size(); j++)
    //   { //For each Territory
    //     landTiles = regionList[i].getTerritoryList().get(j).getLandTiles();

    //     for (LandTile tile : landTiles)
    //     {
    //       if(tile.getCrop() != null) System.out.println(num + " " + tile.getCrop().name());
    //       num++;
    //     }
    //   }
    // }
    long end = System.nanoTime();
    System.out.println("Model.placeCrops() Done: Time: " + ((end - start) / 1000000000.0));
  }
  
  /**
   * Updates all the cropRatings in all landTile.
   * 
   * Will generally only be called during initialization for now. The only reason
   * to call this method multiple times would be if cropData has changed or if 
   * there was a change in climate data for all/a lot of the landtiles.
   * @param dataYear 
   */
  private void updateCropRatings(int dataYear)
  {
    System.out.println("LandTile.updateCropRatings() Starting");
    long start = System.nanoTime();
    int index = 0;
    ArrayList<LandTile> landTiles;
    EnumCropZone[] ratings = new EnumCropZone[EnumFood.SIZE];
    
    for (int i = 0; i < regionList.length; i++)
    { //For each Region
      for (int j = 0; j < regionList[i].getTerritoryList().size(); j++)
      { //For each Territory
        landTiles = regionList[i].getTerritoryList().get(j).getLandTiles();
        
        for (LandTile tile : landTiles)
        {
          // For each crop, find the EnumCropZone value
          for (int k = 0; k < EnumFood.CROP_FOODS.length; k++)
          {
            ratings[k] = tile.rateTileForCrop(EnumFood.CROP_FOODS[k], regionList[i], dataYear, cropData, 1);
          }

          //for now, all 4 non crop foods get an ideal rating
          for (int m = 0; m < 4; m++)
          {
            ratings[m + EnumFood.CROP_FOODS.length] = EnumCropZone.IDEAL;
          }
          tile.updateRating(ratings);
          packedTileData.packData(tile , index);
          index++;
        }
      }
    }
    System.out.println("LandTile.updateCropRatings() Done: Time: " + ((System.nanoTime() - start)
        / 1000000000.0));
  }
  

  private void loadExistingSpecialEvents()
  {
    SpecialEventCSVLoader loader = null;
    try {loader = new SpecialEventCSVLoader();} catch (Throwable t) {}
    specialEventDatum = loader.getEventData();
  }


  public void printCropNeed(Region region, int year)
  {
    // Print just the cell at the capital.
    //
    Simulator.dbg.println("Region " + region.getName() + " crop need per capita : ");
    //for (EnumFood food : EnumFood.values()) Simulator.dbg.print(" " + region.getCropNeedPerCapita(food));
    Simulator.dbg.println();

    // Print each territory.
    //
    for (Territory territory : region.getTerritoryList())
    {
      Simulator.dbg.print("\t" + territory.getName() + ": ");
      //for (EnumFood food : EnumFood.values()) Simulator.dbg.print(" " + territory.getCropNeedPerCapita(food));
      Simulator.dbg.println();
    }

    Simulator.dbg.println("Region " + region.getName() + " total crop need  : ");
    for (EnumFood food : EnumFood.values()) Simulator.dbg.print(" " + region.getTotalCropNeed(year, food));
    Simulator.dbg.println();
  }

  public void printCropYield(Region region, int year)
  {
    // Print just the cell at the capital.
    //
    Simulator.dbg.println("Region " + region.getName() + " crop yield : ");
    //for (EnumFood food : EnumFood.values()) Simulator.dbg.print(" " + region.getCropYield(food));
    Simulator.dbg.println();

    // Print each territory.
    //
    for (Territory territory : region.getTerritoryList())
    {
      Simulator.dbg.print("\t" + territory.getName() + ": ");
      //for (EnumFood food : EnumFood.values()) Simulator.dbg.print(" " + territory.getCropYield(food));
      Simulator.dbg.println();
    }
  }

  public void printCurrentPopulation(Region region, int year)
  {
    Simulator.dbg.println("Region " + region.getName() + " population " + region.getPopulation(year));
    Simulator.dbg.print("\tTerritories : ");
    for (Territory territory : region.getTerritoryList())
    {
      Simulator.dbg.print("\t" + territory.getPopulation(year));
    }
    Simulator.dbg.println();
  }


  public void printRegions(boolean verbose)
  {
    for (Region region : regionList)
    {
      System.out.println("Region : " + region.getName());
      for (Territory unit : region.getTerritoryList())
      {
        System.out.println("\t" + unit.toString());
        if (verbose == false) continue;

        for (LandTile tile : unit.getLandTiles())
        {
          System.out.println("\t\t" + tile.toString());
        }
      }
    }
  }


  public void printRegion(Region region, int year)
  {
    Simulator.dbg.println("Region : " + region.getName());
    Simulator.dbg.print("\tTerritories : ");
    for (Territory territory : region.getTerritoryList())
    {
      Simulator.dbg.print("\t" + territory.getName());
    }
    Simulator.dbg.println();

    printData(region, year, "");

    for (Territory territory : region.getTerritoryList())
    {
      if (debugLevel.intValue() <= Level.FINER.intValue()) printData(territory, year, "\t");
      if (debugLevel.intValue() <= Level.FINEST.intValue())
      {
        /*
        for (LandTile tile : territory.getLandTiles())
        { if (tile.getCurrentCrop() != null) Simulator.dbg.println("\t\t" + tile.toString());
        }
        */
      }
    }
  }

  public void printData(Territory unit, int year, String prefix)
  {
    Simulator.dbg.println(prefix + "Data for " + unit.getName() + " in currentYear " + year);
    Simulator.dbg.print(prefix + prefix + "\t");
    if (unit instanceof Region) Simulator.dbg.print("sum ");

    Simulator.dbg.print(" population : " + unit.getPopulation(year));
    Simulator.dbg.print(", undernourished : " + unit.getUndernourished(year));
    Simulator.dbg.print(", landTotal : " + unit.getLandTotal());
    Simulator.dbg.println();

    Simulator.dbg.print(prefix + "\t            ");
    for (EnumFood food : EnumFood.values()) Simulator.dbg.print("\t" + food);
    Simulator.dbg.println();

    Simulator.dbg.print(prefix + "\tcropYield : ");
    //for (EnumFood food : EnumFood.values()) Simulator.dbg.print("\t" + unit.getCropYield(food));
    Simulator.dbg.println();

    Simulator.dbg.print(prefix + "\tcropNeedPerCapita : ");
    //for (EnumFood food : EnumFood.values()) Simulator.dbg.print("\t" + unit.getCropNeedPerCapita(food));
    Simulator.dbg.println();

    Simulator.dbg.print(prefix + "\tcropProduction : ");
    //for (EnumFood food : EnumFood.values()) Simulator.dbg.print("\t" + unit.getCropProduction(food));
    Simulator.dbg.println();

    Simulator.dbg.print(prefix + "\tcropIncome : ");
    //for (EnumFood food : EnumFood.values()) Simulator.dbg.print("\t" + unit.getCropIncome(food));
    Simulator.dbg.println();

    Simulator.dbg.print(prefix + "\tlandCrop : ");
    //for (EnumFood food : EnumFood.values()) Simulator.dbg.print("\t" + unit.getCropLand(food)); // Yes, they named
    // it backwards.
    Simulator.dbg.println();

    if (unit instanceof Territory)
    {
      Simulator.dbg.print(prefix + "\t            ");
      for (EnumFarmMethod method : EnumFarmMethod.values()) Simulator.dbg.print("\t" + method);
      Simulator.dbg.println();
      Simulator.dbg.print(prefix + "\tcultivationMethod : ");
      //for (EnumFarmMethod method : EnumFarmMethod.values()) Simulator.dbg.print("\t" + unit.getMethod(method));
      Simulator.dbg.println();
    }
  }

  private void setRegionalProduction()
  {
    for(Region region: regionList)
    {
      for(Territory territory: region.getTerritoryList())
      {
        for(LandTile tile: territory.getLandTiles())
        {
          int production = calculateTileProduction(region, tile);
          tile.setCurrentProduction(production);
          int cost = calculateTileCost(region, tile.getCrop());
          tile.setCurrentCost(cost);
        }
      }
      region.setTotalProduction();
    }
  }

  private int calculateTileProduction(Region region, LandTile tile)
  {
    return calculateTileProduction(region, tile, tile.getCrop());
  }



  private int calculateTileProduction(Region region, LandTile tile, EnumFood crop)
  {

    if(crop == null) return 0;

    //total production of tile in USD:
    // (crop rating) * (metric tons yield  / sq km) * (area of land tile in sq km) * (USD food price  / metric ton)

    double production_per_km = region.getCropProduction(2009, crop) / region.getCropArea(2009, crop);
    double tileSize = region.getLandTotal() / region.getNumTiles() ;
    int index = crop.ordinal();
    int revenue = (int) (tile.getCropRatings()[index].productionRate() * cropData.getPrice(2009,
        EnumFood.values()[index]) * tileSize * production_per_km);
    return revenue;
  }


  private int calculateTileCost(Region region, EnumFood crop)
  {
    if(crop == null) return 0;

    //total cost of tile in USD:
    // (metric tons yield  / sq km) * (area of land tile in sq km) * (pesticide cost + water cost + seed cost)

    double production_per_km = region.getCropProduction(2009, crop) / region.getCropArea(2009, crop);
    double tileSize = region.getLandTotal() / region.getNumTiles() ;
    int cost = (int)( tileSize *  production_per_km *
        ( cropData.getData(CropData.Field.PESTICIDE_COST,crop) +
            cropData.getData(CropData.Field.WATER_COST, crop) +
            cropData.getData(CropData.Field.SEED_COST, crop)) );

    return cost;
  }



  /**
   * This method is to be called at the end of a year.
   * For each landTile, farmer decides whether or not to replant same crop
   */
  private void replantCrops()
  {
    for(Region region: regionList)
    {
      region.resetProduction();
      for (Territory territory : territoryList)
      {
        for (LandTile tile : territory.getLandTiles())
        {
          for(EnumFood crop: EnumFood.ALL_FOODS)
          {
            int newCost =  calculateTileCost(region, crop);
            int production = (int)(.8 * calculateTileProduction(region, tile,crop));
            //proposed production is cut to 80% to account for cost of replanting
            int net = production - newCost;
            if(net > (tile.getCurrentProduction() - tile.getCurrentCost()))
            {
              tile.setCrop(crop);
              tile.setCurrentProduction(production);
              tile.setCurrentCost(newCost);
              break;
            }
          }
        }
      }
      region.setTotalProduction();
    }
  }




/*

  public void drawAllTiles(Picture pic, Region region, Color color)
  {
    MapProjectionMollweide map = new MapProjectionMollweide(pic.getImageWidth(), pic.getImageHeight());


    Point pixel = new Point();

    Graphics2D gfx = pic.getOffScreenGraphics();

    gfx.setColor(color);

    ArrayList<Territory> myTerritoryList = region.getTerritoryList();
    for (Territory territory : myTerritoryList)
    {
      ArrayList<LandTile> tileList = territory.getLandTiles();

      for (LandTile tile : tileList)
      {
        map.setPoint(pixel, tile.getLatitude(), tile.getLongitude());

        gfx.fillOval(pixel.x-1, pixel.y-1, 3, 3);
      }
    }

  }


  public void drawRain(Picture pic, int year, Constant.Month month)
  {
    System.out.println("drawRain(year="+year+", " + month +")");
    MapProjectionMollweide map = new MapProjectionMollweide(pic.getImageWidth(), pic.getImageHeight());

    Point pixel = new Point();

    Graphics2D gfx = pic.getOffScreenGraphics();

    for (Territory territory : territoryList)
    {
      ArrayList<LandTile> tileList = territory.getLandTiles();

      for (LandTile tile : tileList)
      {
        map.setPoint(pixel, tile.getLatitude(), tile.getLongitude());

        double rain = tile.getField(LandTile.Field.RAIN, year, month);

        int colorIdx = (int) ((rain / 20.0) * Constant.COLOR_MOISTURE_LIST.length);
        if (colorIdx < 0) colorIdx = 0;
        if (colorIdx >= Constant.COLOR_MOISTURE_LIST.length) colorIdx = Constant.COLOR_MOISTURE_LIST.length - 1;
        gfx.setColor(Constant.COLOR_MOISTURE_LIST[colorIdx]);

        gfx.fillOval(pixel.x - 1, pixel.y - 1, 3, 3);
      }
    }
    pic.repaint();

  }
*/

  /**
   * Testing entry point. This creates an instance of the model which, among other things,
   * loads the world territories. This test program then creates a JFrame displaying a world
   * map and a few example territories drawn on that map.
   *
   * @param args are ignored
   */
  public static void main(String[] args)
  {
    System.out.println("==========================================================================");
    System.out.println("      Running Test entry point: starvationevasion.sim.Model()");
    System.out.println("==========================================================================");



    String[] imagePath = {"BlankBlue_MollweideProjection-1280x641.png"};

    class GUIThread extends Thread
    {
      GUIThread()
      {
      }
      public void run()
      {
        JavaFX_DebugViewer.main(imagePath);
      }
    }

    GUIThread gui = new GUIThread();
    gui.start();

    Model model = new Model();

    JavaFX_DebugViewer pic = JavaFX_DebugViewer.me;
    MapProjectionMollweide map = new MapProjectionMollweide(pic.getImageWidth(), pic.getImageHeight());

    System.out.println("Model.main image size= "+pic.getImageWidth() + ", "+ pic.getImageHeight());
    map.setCentralMeridian(0);
    map.setRegionPerimetersSpherical(model.getRegionPerimetersSpherical());

    System.out.println("model.getRegionPerimetersSpherical().length = " + model.getRegionPerimetersSpherical().length);

    Polygon drawArea = map.getPerimeterDrawable(EnumRegion.SUB_SAHARAN);
    drawArea.setStroke(Util.brighten(EnumRegion.SUB_SAHARAN.getColor(), 0.5));
    pic.add(drawArea);
    //for (EnumRegion regionID : EnumRegion.values())
    //{
    //  Polygon drawArea = map.getPerimeterDrawable(regionID);
    //  pic.add(drawArea, Util.brighten(regionID.getColor(), 0.5));
    //}


/*
    for (int n = -180; n < 180; n+=1)
    {
      gfx.drawImage(background,0,0,null);
      map.setCentralMeridian(n);
      for (EnumRegion regionID : EnumRegion.values())
      {
        Area drawArea = map.getPerimeterDrawable(regionID);
        gfx.setColor(Util.brighten(regionID.getColor(), 0.5));

        gfx.draw(drawArea);
      }
      pic.repaint();

      try
      {
        Thread.sleep(10);
      } catch (InterruptedException e) { }

      //for (Constant.Month month : Constant.Month.values())
      //{
      //  model.drawRain(pic, 2000+n, month);
      //}
    }
    */
  }
}
