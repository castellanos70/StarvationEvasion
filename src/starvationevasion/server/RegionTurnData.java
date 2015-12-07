package starvationevasion.server;

import starvationevasion.common.PolicyCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Shea Polansky
 * Holds per-region data relevant to game state (such as the number of remaining actions)
 */
public class RegionTurnData
{
  public int actionsRemaining = ServerConstants.ACTIONS_PER_DRAFT_PHASE;
  public boolean hasUsedFreeDiscard = false;
  public boolean hasPlayedVoteCard = false;
}
