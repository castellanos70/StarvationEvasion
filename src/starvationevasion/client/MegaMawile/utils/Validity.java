package starvationevasion.client.MegaMawile.utils;

import starvationevasion.common.PolicyCard;

/**
 * The Validity object is used to validate the legality of moves per the game rules before they are
 * submitted.
 */
public class Validity
{
  private final int MAX_DRAFTS = 2;
  private final int MAX_REPLACEMENTS = 2;
  private int drafts = 0;
  private int replacements = 0;

  Validity()
  {
    drafts = 0;
    replacements = 0;
  }

  public int getDrafts()
  {
    return drafts;
  }

  public int getReplacements()
  {
    return replacements;
  }

  public boolean draftCard(PolicyCard policyCard)
  {
    if (drafts < MAX_DRAFTS)
    {
      if (!policyCard.validate().equals(""))
      {
        drafts++;
        return true;
      }
    }
    return false;
  }

  public boolean replaceCard()
  {
    if (replacements < MAX_REPLACEMENTS)
    {
      replacements++;
      return true;
    }
    return false;
  }

  public void resetCount()
  {
    drafts = 0;
    replacements = 0;
  }
}
