package starvationevasion.client.MegaMawile2.ai.brain;


import starvationevasion.client.MegaMawile2.ai.brain.*;
import starvationevasion.common.PolicyCard;

import java.util.Comparator;

/**
 * Compares the social/political leaning of the input policy (in a very naive manner).
 *
 * @author Evan King
 */
public class PolicyComparator implements Comparator
{
    starvationevasion.client.MegaMawile2.ai.brain.AiState leaning;

    /**
     * Creates a comparator which favors a particular leaning.
     * @param leaning  which leaning to favor
     */
    public PolicyComparator(starvationevasion.client.MegaMawile2.ai.brain.AiState leaning)
    {
        this.leaning = leaning;
    }

    @Override
    public int compare(Object o1, Object o2)
    {
        PolicyCard card1 = (PolicyCard)o1;
        String str1 = card1.getGameText();

        switch(leaning)
        {
            case CONSERVATIVE:
                if(isConservative(str1)) return 1;
                if(isLiberal(str1)) return -1;
                break;
            case LIBERAL:
                if(isConservative(str1)) return -1;
                if(isLiberal(str1)) return 1;
                break;
            case ISOLATING:
                if(isIsolating(str1)) return 1;
                if(isCollaborating(str1)) return -1;
                break;
            case COLLABORATING:
                if(isIsolating(str1)) return -1;
                if(isCollaborating(str1)) return 1;
                break;
        }
        return 0;
    }

    /**
     * Determines whether or not the passed PolicyCard is favorable based on the political/social leaning of this
     * PolicyComparator. Used by decision methods that do not make use of a PriorityQueue.
     *
     * @param card the card to assess the favorability of.
     * @return <code>true</code> if the card is favorable, <code>false</code> otherwise.
     */
    public boolean isFavorable(PolicyCard card)
    {
        switch(leaning)
        {
            case LIBERAL:
                if(isLiberal(card.getGameText())) return true;
                break;
            case CONSERVATIVE:
                if(isConservative(card.getGameText())) return true;
                break;
            case ISOLATING:
                if(isIsolating(card.getGameText())) return true;
                break;
            case COLLABORATING:
                if(isCollaborating(card.getGameText())) return true;
                break;
        }
        return false;
    }

    private boolean isConservative(String policyText)
    {
        if(policyText.contains("tax break") || policyText.contains("tax credit") || policyText.contains("tax deductible")
                    || policyText.contains("subsidy")) return true;
        return false;
    }

    private boolean isLiberal(String policyText)
    {
        if(policyText.contains("educate") || policyText.contains("loan") || policyText.contains("aid")
                || policyText.contains("lend") || policyText.contains("hunger")) return true;
        return false;
    }

    private boolean isIsolating(String policyText)
    {
        if(policyText.contains("my region") || policyText.contains("your region")) return true;
        return false;
    }

    private boolean isCollaborating(String policyText)
    {
        if(policyText.contains("target region") || policyText.contains("target world region")) return true;
        return false;
    }
}
