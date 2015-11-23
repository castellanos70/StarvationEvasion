package starvationevasion.sim;


import starvationevasion.common.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * This is the main API point of the Starvation Evasion Simulator.
 * This constructor should be called once at the start of each game by the Server.
 */
public class Simulator
{
  private final static Logger LOGGER = Logger.getLogger(Simulator.class.getName());
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
    LOGGER.setLevel(Level.ALL);

    if ((startYear < Constant.FIRST_YEAR || startYear > Constant.LAST_YEAR) ||
      ((Constant.LAST_YEAR - startYear) % 3 != 0))
    {
      String errMsg = "Simulator(startYear=" + startYear +
                      ") start year must be less than " + Constant.LAST_YEAR +
        " and must be a nonnegative integer multiple of 3 years after " + Constant.FIRST_YEAR;
      LOGGER.severe(errMsg);
      throw new IllegalArgumentException(errMsg);
    }

    for (EnumRegion playerRegion : EnumRegion.US_REGIONS)
    {
      playerDeck[playerRegion.ordinal()] = new CardDeck(playerRegion);
    }

    model = new Model(startYear);

    LOGGER.info("Starting Simulation at year " + startYear);
  }

  /**
   * The server must call this for each playerRegion before the first turn
   * and during each turn's draw phase. This method will return the proper number of
   * cards from the top of the given playerRegion's deck taking into account cards played
   * and discarded by that player.
   * @param playerRegion region of player who id given the drawn cards.
   * @return collection of cards.
   */
  public PolicyCard[]  drawCards(EnumRegion playerRegion)
  {
    return playerDeck[playerRegion.ordinal()].drawCards();
  }

  public void discard(EnumRegion playerRegion, ArrayList<PolicyCard> cards)
  {
    if (!playerRegion.isUS())
    {
      throw new IllegalArgumentException("discard(="+playerRegion+", cards) must be " +
        "a player region.");
    }

    CardDeck deck = playerDeck[playerRegion.ordinal()];
    deck.discard(cards);
  }

  /**
   * The server should call nextTurn(cards) when it is ready to advance the simulator
   * a turn (Constant.YEARS_PER_TURN years)
   * @param cards List of PolicyCards played this turn.
   * @return the simulation year after nextTurn() has finished.
   */
  public int nextTurn(ArrayList<Policy> cards)
  {
    LOGGER.info("Advancing Turn...");
    model.nextYear(cards);
    model.nextYear(cards);
    model.nextYear(cards);
    LOGGER.info("Turn complete, year is now " + model.getCurrentYear());
    return model.getCurrentYear();
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
   * This entry point is for testing only. <br><br>
   *
   * This test shows how to instanciate the simulator and how to tell it
   * to deal each player a hand of cards.
   * @param args ignored.
   */
  public static void main(String[] args)
  {
    Simulator sim = new Simulator(Constant.FIRST_YEAR);
    String msg = "Starting Hands: \n";
    for (EnumRegion playerRegion : EnumRegion.US_REGIONS)
    {
      PolicyCard[]  hand = sim.drawCards(playerRegion);
      msg += playerRegion+": ";
      for (PolicyCard  card : hand)
      {
        msg += card +", ";
      }
      msg+='\n';
    }
    LOGGER.info(msg);
  }
}
