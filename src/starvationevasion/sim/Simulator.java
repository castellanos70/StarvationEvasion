package starvationevasion.sim;

import starvationevasion.common.*;

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
   * Before calling this method, the server must verify that:
   * <ol>
   * <li>The player can legally discard a card and </li>
   * <li>That the card the player is discarding is actually in the player's hand.</li>
   * </ol><br>
   *
   * There are three ways that a card may be discarded:
   * <ol>
   * <li>Each policy drafting phase, each player may discard one card.</li>
   * <li>Each time a player may draft a policy card, he or she may instead, discard up to
   *    3 policy cards to <b>immediately</b> draw that many new cards.</li>
   * <li>If a player drafts a policy that does not receive the required votes, then
   * the policy card is discarded.</li>
   * </ol><br>
   *
   * Note: Only the second method causes new cards to be immediately drawn. In the other
   * cases, cards are replaced during the draw phase.<br><br>
   *
   *
   * Throws an illegal Argument Exception if given region is not a player region or
   * if the player has no cards to discard.
   * @param playerRegion
   * @param discardList
   */
  public void discard(EnumRegion playerRegion, ArrayList<EnumPolicy> discardList)
  {
    if (!playerRegion.isUS())
    {
      throw new IllegalArgumentException("discard(EnumRegion="+playerRegion+
        ") Only US regions may discard cards.");
    }

  }

  /**
   * The server calls nextTurn(cards) when it is ready to advance the simulator
   * a turn (by Constant.YEARS_PER_TURN years).<br><br>
   *
   * The list of cards must contain an aggregate of all server verified legal
   * policy cards enacted this turn.<br><br> In particular, the server must verify that:
   * <ol>
   *   <li>Each card played was in the player's hand.</li>
   *   <li>No more than two policy cards are played by each player.</li>
   *   <li>No more than one policy requiring a vote was played by each player.</li>
   *   <li>For each time a player discarded for an immediate draw, that player
   *   gave up the ability to play one policy card.</li>
   *   <li>No policy card in the list was played after the end of the phase.</li>
   *   <li>Each player who drafted or voted for the policy has any required resources (a player
   *   with a revenue balance of 5 million dollars cannot spend 6 million).</li>
   *   <li>The policy's list of approved votes is correct.</li>
   *   <li>Each policy has legal targets, legal values and any required votes.
   *   (PolicyCard.validate() returns null).</li>
   * </ol><br>
   *
   * All except the last two should not be given to the server
   * by a well behaved client.<br><br>
   *
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
