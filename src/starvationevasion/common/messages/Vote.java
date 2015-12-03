package starvationevasion.common.messages;

import starvationevasion.common.EnumRegion;

import java.io.Serializable;

/**
 * Shea Polansky
 * A message from client to server informing the server that it wishes
 * to vote in a particular manner on a given card.
 */
public class Vote implements Serializable
{
  public final EnumRegion cardOwner;
  public final VoteType voteType;


  public Vote(EnumRegion cardOwner, VoteType voteType)
  {
    this.cardOwner = cardOwner;
    this.voteType = voteType;
  }
}
