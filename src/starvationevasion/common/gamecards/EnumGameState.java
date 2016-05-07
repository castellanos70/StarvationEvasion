package starvationevasion.common.gamecards;

/**
 * @deprecated USE server.model.State INSTEAD
 * 
 * Represents the various "states" that the game can be in. For example, one state is the voting
 * phase where regions vote on submitted policies.
 * @author Michael Martin
 */

public enum EnumGameState
{
  PLANNING_STATE, //when regions are deciding which cards to use for the turn, before voting state
  VOTING_STATE    //when regions are voting on policies submitted by other regions
}
