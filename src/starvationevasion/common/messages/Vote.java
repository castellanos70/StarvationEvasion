package starvationevasion.common.messages;

import starvationevasion.common.PolicyCard;

/**
 * Shea Polansky
 * A message from client to server informing the server that it wishes
 * to vote in a particular manner on a given card.
 */
public class Vote
{
  public final PolicyCard card;
  public final VoteType voteType;

  public Vote(PolicyCard card, VoteType voteType)
  {
    this.card = card;
    this.voteType = voteType;
  }
}
