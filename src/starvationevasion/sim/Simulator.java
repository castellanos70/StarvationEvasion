package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.GeographicArea;
import starvationevasion.common.MapPoint;
import starvationevasion.common.RegionData;
import starvationevasion.common.WorldData;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.common.gamecards.GameCard;
import starvationevasion.sim.events.AbstractEvent;

import java.awt.geom.Area;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


/**
 * This is the main API point of the Starvation Evasion Simulator.<br>
 * This constructor should be called once at the start of each game by the Server.<br><br>
 *
 * The design of this simulator is for its non-static methods to ONLY BE CALLED FROM A
 * <em>SINGLE THREAD</em> (the main game loop thread) of the Server. The effect this has is that
 * the server must guarantee synchronization of all clients before advancing the game turn.
 */
public class Simulator
{
  public static PrintStream dbg = System.out;

  private final static Logger LOGGER = Logger.getGlobal(); // getLogger(Simulator.class.getName());
  private final static Level debugLevel = Level.FINEST;

  private CardDeck[] playerDeck = new CardDeck[EnumRegion.US_REGIONS.length];
  private Model model;

  /**
   * Allocate memory and read massive data files.
   */
  public Simulator()
  {
    assert (Constant.FIRST_GAME_YEAR > Constant.FIRST_DATA_YEAR);
    assert ((Constant.FIRST_GAME_YEAR - Constant.FIRST_DATA_YEAR) % Constant.YEARS_PER_TURN == 0);
    assert ((Constant.LAST_YEAR - (Constant.FIRST_GAME_YEAR+1)) % Constant.YEARS_PER_TURN == 0);

    LOGGER.info("Loading and initializing model");
    model = new Model();

    assert (assertSimulatorPreGameData());
    init();
  }

  /**
   * This is called by the constructor to initialize the model.
   * It may also be called to restart the model without needing to
   * reallocate memory and without reloading the large data files.
   */
  public void init()
  {
    LOGGER.info("Starting Simulator: year=" + Constant.FIRST_GAME_YEAR);
    model.init();
    for (EnumRegion playerRegion : EnumRegion.US_REGIONS)
    {
      playerDeck[playerRegion.ordinal()] = new CardDeck(playerRegion);
    }
  }


  private boolean assertSimulatorPreGameData()
  {
    for (int year = Constant.FIRST_DATA_YEAR; year < model.getCurrentYear(); year++)
    {
      WorldData world = model.getWorldData(year);
      assert ((world.year == year));
      if (year == Constant.FIRST_DATA_YEAR)
      {
        assert (world.seaLevel == 0); //As seaLevel is defined as the difference from the 1981 level, the 1981 should
        // always be 0.
      }
      for (RegionData region : world.regionData)
      {
        //    assert(region.ethanolProducerTaxCredit != 0);
        assert (region.population > 0);
        //System.out.println(region.region+"("+year+"): pop="+region.population +", underfeed="+region.undernourished);
        assert ((region.undernourished <= region.population) && (region.undernourished >= 0));
        //assert(region.humanDevelopmentIndex != 0);
        assert (region.landArea > 0);

        int totalFarmArea = 0;
        for (int i = 0; i < EnumFood.SIZE; i++)
        {
          totalFarmArea += region.farmArea[i];
          if (region.farmArea[i] > 0)
          {
            assert (region.foodProduced[i] > 0);
            assert (region.foodIncome[i] > 0);
          }
        }
        assert ((totalFarmArea < region.landArea) && (totalFarmArea >= 0));
      }
      //Check that WorldData is properly instantiated
      //for (int i = 0; i < EnumFood.SIZE; i++)
      //{
      //  assert (world.foodPrice[i] > 0);
      //}
    }
      /*
    EnumRegion player = EnumRegion.USA_CALIFORNIA;
    EnumPolicy[] hand = drawCards(player);
    assert(hand.length == Constant.MAX_HAND_SIZE);
    //Check that hand is empty at beginning of game, all cards have been drawn,
    assert(drawCards(player) == null);
    //Constant check of enum value, hand should be full so nothing happens
    for(int i = 0; i < Constant.MAX_HAND_SIZE; i++){}
      //validate cards, doesn't do anything if they are enums
    Boolean ex = false;
    discard(player, hand[0]);
    try
    {
      discard(player, hand[0]);
    }
    catch(IllegalArgumentException e)
    {
      ex = true;
    }
    assert(ex);
    //Assuring that cards are being removed from the hand
      */
    return true;
  }

  /**
   * At the start of the game, before dealing cards to players, the Server should call:
   *     getWorldData(Constant.FIRST_DATA_YEAR, Constant.FIRST_GAME_YEAR-1);
   *
   * @return data structure populated with all game state data needed by the client
   * except high resolution data that might be needed by the visualizer.
   */
  public ArrayList<WorldData> getWorldData(int yearFrom, int yearThrough)
  {
    if (yearFrom < Constant.FIRST_DATA_YEAR)
    {
      throw new IllegalArgumentException("Cannot Request WorldData before " + Constant.FIRST_DATA_YEAR);
    }
    if (yearThrough >= model.getCurrentYear())
    {
      throw new IllegalArgumentException("May only request WorldData from before the current year.");
    }
    if (yearFrom > yearThrough)
    {
      throw new IllegalArgumentException("yearFrom ("+yearFrom+
        ") cannot be greater than yearThrough ("+yearFrom+").");
    }

    ArrayList<WorldData> dataList = new ArrayList<>();
    for (int year = yearFrom; year <= yearThrough; year++)
    {
      dataList.add(model.getWorldData(year));
    }
    return dataList;
  }

  public  GeographicArea[] getRegionBoundaries()
  {
    GeographicArea[] boundaryList = new GeographicArea[EnumRegion.SIZE];

    for (EnumRegion region : EnumRegion.values())
    {
      boundaryList[region.ordinal()] = model.getGeographicArea(region);
    }
    return boundaryList;
  }



  /**
   * FROM ONE THREAD ONLY, the Server should call nextTurn(cards) when it is ready to advance the
   * simulator a turn (Constant.YEARS_PER_TURN years).<br><br>
   * Before calling nextTurn, the Server must:
   * <ol>
   * <li>Verify all policy cards drafted by the clients during the draft phase.</li>
   * <li>Verify that any cards discarded by a player could legally be discarded.</li>
   * <li>Wait for all players to either finish the turn or time out clients that go over
   * the turn phase time limit.</li>
   * <li>Call discard(), FROM A SINGLE THREAD, on each card discarded by a player.</li>
   * <li>End the voting phase and decide the results.</li>
   * <li>Call discard on each card that did not receive enough votes.</li>
   * <li>Call drawCards for each player and send them their new cards.</li>
   * </ol>
   *
   * @param cards Combined list of ALL PolicyCards enacted this turn by <em>ALL players</em>. <br>
   *              Note: Since this is a combined list, all clients must report their cards and votes
   *              or be timed out before the server may call this method.<br>
   *              Note: cards played but not
   *              enacted (did not get required votes) must NOT be in this list.
   *              Such cards must be discarded
   *              (call discard(EnumRegion playerRegion, PolicyCard card))
   *              <b>before</b> calling this method.
   * @return data structure populated with all game state data needed by the client
   * except high resolution data that might be needed by the visualizer.
   */
  public ArrayList<WorldData> nextTurn(ArrayList<GameCard> cards)
  {
    LOGGER.info("Advancing Turn ...");
    ArrayList<WorldData> worldData = getWorldData();
    
    //applyCardEffectsToHand(cards);
    
    model.nextYear(cards);
    model.nextYear(cards);

    LOGGER.info("Turn complete. Game is now Jan 1, " + getCurrentYear());
    return worldData;
  }

  public int getCurrentYear()
  {
    return model.getCurrentYear();
  }


  /**
   * The server must call this for each playerRegion before the first turn
   * and during each turn's draw phase. This method will return the proper number of
   * cards from the top of the given playerRegion's deck taking into account cards played
   * and discarded by that player.
   *
   * @param playerRegion region of player who id given the drawn cards.
   * @return collection of cards.
   */
  public EnumPolicy[] drawCards(EnumRegion playerRegion)
  {
    return playerDeck[playerRegion.ordinal()].drawCards();
  }


  public EnumPolicy[] getCardsInHand(EnumRegion playerRegion)
  {
    return playerDeck[playerRegion.ordinal()].getCardsInHand();
  }
  
  public ArrayList<EnumPolicy> getCardsInDiscard(EnumRegion playerRegion)
  {
    return playerDeck[playerRegion.ordinal()].getDiscardPile();
  }


  /**
   * The Server must call this for each card that is discarded <b>before</b> calling
   * nextTurn(). There are three different ways a card may be discarded:
   * <ol>
   * <li>During the draft phase, a player may use an action to discard up to
   * 3 policy cards and <b>immediately</b> draw that many new cards. Using an action
   * means the player can draft one less policy that turn. What is meant by
   * immediately is that a player who does this and who still has a remaining
   * action, may draft one of the newly drawn cards during that same draft phase.</li>
   * <li>As part of each draft phase, each player may discard a single policy card. Cards
   * discarded this way are not replaced until the draw phase (after the voting phase).</li>
   * <li>A policy that is drafted, bt does not receive the required votes
   * is discarded.</li>
   * </ol>
   *
   * @param playerRegion player who owns the discarded card.
   * @param card         to be discarded.
   */
  public void discard(EnumRegion playerRegion, EnumPolicy card)
  {
    if (!playerRegion.isUS())
    {
      throw new IllegalArgumentException("discard(=" + playerRegion + ", cards) " +
                                                 "must be a player region.");
    }

    CardDeck deck = playerDeck[playerRegion.ordinal()];
    deck.discard(card);
  }

  /**
   * Configure the system logger to write to both a log file and the console.
   *
   * @param fileName Path to output log file.
   * @throws IOException
   */
  public static void setLogFile(String fileName) throws IOException
  {
    Handler fh = new FileHandler(fileName);

    Formatter formatter = new Formatter()
    {
      @Override
      public String format(LogRecord arg0)
      {
        StringBuilder b = new StringBuilder();
        b.append(new Date());
        b.append(" ");
        b.append(arg0.getSourceClassName());
        b.append(" ");
        b.append(arg0.getSourceMethodName());
        b.append(" ");
        b.append(arg0.getLevel());
        b.append(" ");
        b.append(formatMessage(arg0));
        b.append(System.getProperty("line.separator"));
        return b.toString();
      }
    };

    LOGGER.setUseParentHandlers(false);

    // Remove the default console handler from the root logger.
    //
    Logger globalLogger = Logger.getGlobal();
    Handler[] handlers = globalLogger.getHandlers();
    for (Handler handler : handlers)
    {
      globalLogger.removeHandler(handler);
    }

    // Now add our own handlers.
    //
    fh.setFormatter(formatter);
    LOGGER.addHandler(fh);

    // We do not strictly need a console handler since we're logging to a file, so
    // this could be removed.
    //
    Handler ch = new ConsoleHandler();
    ch.setFormatter(formatter);
    LOGGER.addHandler(ch);

    LogManager lm = LogManager.getLogManager();
    lm.addLogger(LOGGER);
  }

  /**
   * Get a list of special events in the world
   *
   * @return a list of events effecting the current world state
   */
  public List<AbstractEvent> getWorldEvents()
  {
    return model.getSpecialEvents();
  }

  /**
   * @param latitude  Latitude ranges from -90 to 90. North latitude is positive.
   * @param longitude Longitude ranges from -180 to 180. East longitude is positive.
   * @return The region containing the given latitude and longitude or null if the given location
   * is not within a game region.
   */
  public EnumRegion getRegion(float latitude, float longitude)
  {
    MapPoint mapPoint = new MapPoint(latitude, longitude);
    for (EnumRegion regionEnum : EnumRegion.values())
    {
      Region region = model.getRegion(regionEnum);
      if (region.contains(mapPoint)) return regionEnum;
    }
    return null;
  }

  private ArrayList<WorldData> getWorldData ()
  {
    if (model.getCurrentYear() == Constant.FIRST_GAME_YEAR)
    {
      return getWorldData(Constant.FIRST_DATA_YEAR, Constant.FIRST_GAME_YEAR-1);
    }

    return getWorldData(model.getCurrentYear()-2, model.getCurrentYear()-1);
  }
  
  /**
   * Iterates through all the cards intended to be applied to the simulator and
   * then applies only the ones that effect a player's hand.
   * 
   * @param cards
   *          The list of all cards intended to be applied to the simulation.
   */
  private void applyCardEffectsToHand(ArrayList<GameCard> cards)
  {
    for (GameCard c : cards)
    {
      switch(c.getCardType())
      {
        case Policy_DivertFunds:
          //remove all cards from owners hand
          discardPlayerHand(c.getOwner());
          //give 14 million dollars to owner - applied in Model.java
          break;
        default:
          break;
      }
    }
  }
  
  private void discardPlayerHand(EnumRegion playerRegion)
  {
    CardDeck deck = playerDeck[playerRegion.ordinal()];

    for(int i = 0; i < deck.getCardsInHand().length; i++)
    {
      deck.discard(deck.getCardsInHand()[i]);
    }
  }

  public Area getPerimeter(EnumRegion regionID)
  {
    Region region = model.getRegion(regionID);
    return region.getGeographicArea().getPerimeter();
  }

  /**
   * This entry point is for testing only. <br><br>
   * <p>
   * This test shows how to instantiate the simulator and how to tell it
   * to deal each player a hand of cards.
   *
   * @param args ignored.
   */
  public static void main(String[] args)
  {
    System.out.println("==========================================================================");
    System.out.println("      Running Test entry point: starvationevasion.sim.Simulator()");
    System.out.println("==========================================================================");


    // Configure a debug output stream for dumping verbose simulatoin data.
    //
    String tmpdir = System.getProperty("java.io.tmpdir");
    String separator = System.getProperty("file.separator");
    String tmpfile = tmpdir + "StarvationEvation.log";
    FileOutputStream fos = null;
    Logger.getGlobal();
    try
    {
      fos = new FileOutputStream(new File(tmpfile));
      Simulator.dbg = new PrintStream(fos);
      System.err.println(debugLevel.getName() + " debug logging to '" + tmpfile + "'");
      System.err.println("To increase (decrease) the verbosity of this output, change the debugLevel member of the " +
        "Simulation class.");
    } catch (FileNotFoundException e)
    {
      System.err.println("Can't open log file '" + tmpfile + "'");
    }

    // Configure the logger for summary data (this is what the Server class will see).  We can
    // also send this to a file.
    // AbstractHandler fh = new FileHandler("test.txt");
    // fh.setFormatter(formatter);
    // logger.addHandler(fh);
    //
    LOGGER.setLevel(Level.INFO);


    Simulator sim = new Simulator(); //Allocates memory and reads LARGE data files.
    //To restart without reloading everything call sim.init();


    String startingHandMsg = "Starting Hands: \n";

    for (EnumRegion playerRegion : EnumRegion.US_REGIONS)
    {
      EnumPolicy[] hand = sim.drawCards(playerRegion);
      startingHandMsg += playerRegion + ": ";
      for (EnumPolicy card : hand)
      {
        startingHandMsg += card + ", ";
      }
      startingHandMsg += '\n';
    }

    ArrayList<WorldData> worldDataList = sim.getWorldData(Constant.FIRST_DATA_YEAR, Constant.FIRST_GAME_YEAR-1);
    for (WorldData data : worldDataList)
    {
      LOGGER.info("==================================================\n"+data.toString()+"\n");
    }
    LOGGER.info(startingHandMsg);

    ArrayList<GameCard> policiesEnactedThisTurnByAllPlayers = new ArrayList<>();
    sim.nextTurn(policiesEnactedThisTurnByAllPlayers);


    try
    {
      fos.flush();
      fos.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
