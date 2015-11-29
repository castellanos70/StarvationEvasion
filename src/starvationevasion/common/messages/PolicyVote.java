package starvationevasion.common.messages;

import starvationevasion.common.PolicyCard;

/**
 * Created by scnaegl on 11/23/15.
 */
public class PolicyVote {
  public PolicyCard policyCard;
  public VoteType voteType;

  public PolicyVote(PolicyCard policyCard, VoteType voteType) {
    this.policyCard = policyCard;
    this.voteType = voteType;
  }

  public enum VoteType {
    FOR, AGAINST, ABSTAIN;
  }
}
