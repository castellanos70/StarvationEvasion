package starvationevasion.sim;


import starvationevasion.common.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.*;

/**
 * This is the main API point of the Starvation Evasion Simulator.
 * This constructor should be called once at the start of each game by the Server.
 */
public class Simulator
{
  public static PrintStream dbg = System.out;

  private final static Logger LOGGER = Logger.getGlobal(); // getLogger(Simulator.class.getName());
  private final static Level debugLevel = Level.FINEST;

  private CardDeck[] playerDeck = new CardDeck[EnumRegion.US_REGIONS.length];
  private Model model;

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
    // Model instantiation parses all of the XML and CSV.
    //
    LOGGER.info("Loading and initializing model");
    model = new Model(startYear);
    model.instantiateRegions();

    LOGGER.info("Starting Simulator: year="+startYear);

    if ((startYear < Constant.FIRST_YEAR || startYear > Constant.LAST_YEAR) ||
      ((Constant.LAST_YEAR - startYear) % 3 != 0))
    {
      String errMsg = "Simulator(startYear=" + startYear +
                      ") start year must be less than " + Constant.LAST_YEAR +
        " and must be a non-negative integer multiple of 3 years after " + Constant.FIRST_YEAR;
      LOGGER.severe(errMsg);
      throw new IllegalArgumentException(errMsg);
    }

    for (EnumRegion playerRegion : EnumRegion.US_REGIONS)
    {
      playerDeck[playerRegion.ordinal()] = new CardDeck(playerRegion);
    }
  }



  /**
   * The Server should call init() at the start of the game before dealing cards to
   * players.
   *
   * @return data structure populated with all game state data needed by the client
   * except high resolution data that might be needed by the visualizer.
   */
  public WorldData init()
  {
    WorldData startWorldData = new WorldData();
    model.appendWorldData(startWorldData);
    return startWorldData;
  }

  /**
   * The Server should call nextTurn(cards) when it is ready to advance the simulator
   * a turn (Constant.YEARS_PER_TURN years).<br><br>
   * Before calling nextTurn, the Server must:
   * <ol>
   * <li>Verify all policy cards drafted by the clients during the draft phase.</li>
   * <li>Verify that any cards discarded by a player could be discarded.</li>
   * <li>Call discard on each card discarded by a player.</li>
   * <li>End the voting phase and decide the results.</li>
   * <li>Call discard on each card that did not receive enough votes.</li>
   * <li>Call drawCards for each player and send them their new cards.</li>
   * </ol>
   * @param cards List of PolicyCards enacted this turn. Note: cards played but not
   *              enacted (did not get required votes) must NOT be in this list.
   *              Such cards must be discarded
   *              (call discard(EnumRegion playerRegion, PolicyCard card))
   *              <b>before</b> calling this method.
   *
   * @return data structure populated with all game state data needed by the client
   * except high resolution data that might be needed by the visualizer.
   */
  public WorldData nextTurn(ArrayList<PolicyCard> cards)
  {
    LOGGER.info("Advancing Turn to ...");
    WorldData threeYearData = new WorldData();

    model.nextYear(cards, threeYearData);
    model.nextYear(cards, threeYearData);
    model.nextYear(cards, threeYearData);
    LOGGER.info("Turn complete, year is now " + threeYearData.year);
    return threeYearData;
  }




  /**
   * The server must call this for each playerRegion before the first turn
   * and during each turn's draw phase. This method will return the proper number of
   * cards from the top of the given playerRegion's deck taking into account cards played
   * and discarded by that player.
   * @param playerRegion region of player who id given the drawn cards.
   * @return collection of cards.
   */
  public EnumPolicy[]  drawCards(EnumRegion playerRegion)
  {
    return playerDeck[playerRegion.ordinal()].drawCards();
  }


  /**
   * The Server must call this for each card that is discarded <b>before</b> calling
   * nextTurn(). There are three different ways a card may be discarded:
   * <ol>
   *   <li>During the draft phase, a player may use an action to discard up to
   *   3 policy cards and <b>immediately</b> draw that many new cards. Using an action
   *   means the player can draft one less policy that turn. What is meant by
   *   immediately is that a player who does this and who still has a remaining
   *   action, may draft one of the newly drawn cards during that same draft phase.</li>
   *   <li>As part of each draft phase, each player may discard a single policy card. Cards
   *   discarded this way are not replaced until the draw phase (after the voting phase).</li>
   *   <li>A policy that is drafted, bt does not receive the required votes
   *   is discarded.</li>
   * </ol>
   *
   * @param playerRegion player who owns the discarded card.
   * @param card to be discarded.
   */
  public void discard(EnumRegion playerRegion, EnumPolicy card)
  {
    if (!playerRegion.isUS())
    {
      throw new IllegalArgumentException("discard(="+playerRegion+", cards) must be " +
        "a player region.");
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

    Formatter formatter = new Formatter() {
      @Override
      public String format(LogRecord arg0) {
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
    for(Handler handler : handlers)
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
   * This entry point is for testing only. <br><br>
   *
   * This test shows how to instantiate the simulator and how to tell it
   * to deal each player a hand of cards.
   * @param args ignored.
   */
  public static void main(String[] args)
  {
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
      System.err.println("Verbose debug logging to '" + tmpfile + "'");
    }
    catch (FileNotFoundException e)
    {
      System.err.println("Can't open log file '" + tmpfile + "'");
    }

    // Configure the logger for summary data (this is what the Server class will see).  We can
    // also send this to a file.
    // Handler fh = new FileHandler("test.txt");
    // fh.setFormatter(formatter);
    // logger.addHandler(fh);
    //
    LOGGER.setLevel(Level.INFO);

    Simulator sim = new Simulator(Constant.FIRST_YEAR);

    String msg = "Starting Hands: \n";

    for (EnumRegion playerRegion : EnumRegion.US_REGIONS)
    {
      EnumPolicy[]  hand = sim.drawCards(playerRegion);
      msg += playerRegion+": ";
      for (EnumPolicy  card : hand)
      {
        msg += card +", ";
      }
      msg+='\n';
    }

    WorldData worldData = sim.init();
    LOGGER.info(worldData.toString());

    // Now step through the simulation years for debugging
    //
    for (int i = Constant.FIRST_YEAR + 1; i < Constant.LAST_YEAR ; i += 3)
    {
      sim.nextTurn(null); // Test w/o playing any cards.
    }

    try
    {
      fos.flush();
      fos.close();;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
