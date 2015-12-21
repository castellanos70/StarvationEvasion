package starvationevasion.client.MegaMawile.model;


import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;
import starvationevasion.common.messages.VoteType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Ballots are used to tally the client's votes on drafted policies during the voting phase. They are ideally created
 * at the start of each voting phase. When a Ballot is instantiated, a list of cards is set, analogous to questions on
 * a regular voting ballot.
 */
public class Ballot
{
  private HashMap<EnumRegion, HashMap<PolicyCard, VoteType>> votes;
  private HashMap<PolicyCard, VoteType> cardsToVote;
  private ArrayList<PolicyCard> votingHand;

  /**
   * Creates a Ballot for a player from a list of {@link PolicyCard}s.
   *
   * @param ballots a list of PolicyCards on which the player will vote.
   */
  public Ballot(ArrayList<PolicyCard> ballots)
  {
    votes = new HashMap<>();
    votingHand = ballots;

    HashMap<EnumRegion, HashMap<PolicyCard, VoteType>> setting = new HashMap<>();
    cardsToVote = new HashMap<>();
    ballots.forEach(k -> cardsToVote.put(k, VoteType.ABSTAIN));
    for (EnumRegion region : EnumRegion.US_REGIONS)
    {
      votes.put(region, cardsToVote);
    }
    votes = setting;
  }

  /*
   * Returns a list of regions mapped to their votes, as hashmap of hashmaps.
   *
   * @return a hashmap, where keys are {@link EnumRegion}s and values are hashmaps of {@link PolicyCard}s mapped to<br>
   *     the amount of votes they've received.

  public HashMap<EnumRegion, HashMap<PolicyCard, Integer>> getVotes()
  {
    return votes;
  }
  */

  /**
   * Returns a list of PolicyCards mapped to the number of votes for that card. The enacting region can be determined
   * by querying the PolicyCard.
   *
   * @return a hashmap, where keys are {@link PolicyCard}s mapped to integers indicating the number of votes recieved.
   */
  public HashMap<PolicyCard, VoteType> getBallotItems()
  {
    return cardsToVote;
  }
  
  /**
   * Returns the list of policy cards to be voted on using this Ballot.
   *
   * @return the list of {@link PolicyCard}s voted on by this Ballot, as an ArrayList.
   */
  public ArrayList<PolicyCard> getVotingHand()
  {
    return votingHand;
  }

  /**
   * Clears the Ballot so that it may be reused in a new voting round.
   */
  public void clear()
  {
    votes = new HashMap<>();
    cardsToVote = new HashMap<>();
    votingHand = new ArrayList<>();
  }
}
