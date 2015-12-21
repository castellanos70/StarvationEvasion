package starvationevasion.client.MegaMawile.ai.brain;


import starvationevasion.client.MegaMawile.controller.ComputerPlayerController;
import starvationevasion.common.EnumRegion;

import java.util.*;

/**
 * Shared AI methods/fields to be extended by an AI subclass. Most methods that
 * alter AI state are in here, along with fields.
 *
 * @author Evan King
 */
public abstract class BasicBrain
{
    //A list of EnumRegions considered friendly to this AI. Friendly regions are favored during voting
    private ArrayList<EnumRegion> friendlies = new ArrayList();

    //A list of AiStates, indicating this AI's political/social leaning, as well as decision state.
    protected ArrayList<AiState> states = new ArrayList();

    //All AIs implement varying levels of stochastic decision making.
    protected Random rand = new Random();

    protected ComputerPlayerController controller;

    /**
     * Creates a BasicBrain for AI state manipulation
     *
     * @param controller the controller to modify the player model/make moves
     */
    public BasicBrain(ComputerPlayerController controller)
    {
        this.controller = controller;
    }

    /**
     * Make the inheriting AI start waiting - this is called at the end of its decision-making for a round.
     */
    public void startWaiting()
    {
        states.add(AiState.WAITING);
    }

    /**
     * Returns whether or not the AI is currently waiting.
     *
     * @return a <code>boolean</code>, <code>true</code> if the AI is currently waiting for the next round to start.
     */
    public boolean waiting()
    {
        return states.contains(AiState.WAITING);
    }

    /**
     * Notifies the AI that it should stop waiting. Called at the beginning of each round.
     */
    public void stopWaiting()
    {
        states.remove(AiState.WAITING);
    }

    /**
     * Tells the AI to start deciding.
     */
    public void startDeciding()
    {
        states.add(AiState.DECIDING);
    }

    /**
     * Returns whether or not the AI is currently deciding. If the AI is deciding, it will not make changes to any fields
     * required for decisions during updates.
     *
     * @return a <code>boolean</code>, <code>true</code> if the AI is deciding.
     */
    public boolean deciding()
    {
        return states.contains(AiState.DECIDING);
    }

    /**
     * Notifies the AI to stop deciding. This is called once all the necessary moves have been made for a round, but
     * before the round is completely wrapped up and the AI is waiting for the next one to begin.
     */
    public void stopDeciding()
    {
        states.remove(AiState.DECIDING);
    }

    /**
     * Adds the passed {@link EnumRegion} to this AI's list of friendly regions. Friendly regions are favored during
     * voting rounds.
     *
     * @param friendly the EnumRegion to add.
     */
    public void addFriendly(EnumRegion friendly)
    {
        if(!friendlies.contains(friendly)) friendlies.add(friendly);
    }

    /**
     * Removes the passed {@link EnumRegion} from this AI's list of friendlies.
     *
     * @param notFriendly the EnumRegion to remove, probably because said EnumRegion is being a jerk to our AI.
     */
    public void removeFriendly(EnumRegion notFriendly)
    {
        if(friendlies.contains(notFriendly)) friendlies.remove(notFriendly);
    }

    /**
     * Returns whether or not the passed {@link EnumRegion} is considered friendly by this AI.
     *
     * @param maybeFriendly the EnumRegion to size up.
     * @return <code>true</code> if the passed region is friendly, false otherwise.
     */
    public boolean isFriendly(EnumRegion maybeFriendly)
    {
        return friendlies.contains(maybeFriendly);
    }

    /**
     * Returns this AI's political state.
     *
     * @return an AiState corresponding to this AI's politics.
     */
    public AiState myPolitics()
    {
        if(states.contains(AiState.LIBERAL)) return AiState.LIBERAL;
        else return AiState.CONSERVATIVE;
    }

    /**
     * Returns this AI's social leaning.
     *
     * @return an AiState corresponding to this AI's social leaning.
     */
    public AiState mySocial()
    {
        if(states.contains(AiState.COLLABORATING)) return AiState.COLLABORATING;
        else return AiState.ISOLATING;
    }

    /**
     * Inverts the political leaning of the AI.
     */
    public void invertPolitics()
    {
        if(states.contains(AiState.CONSERVATIVE))
        {
            states.remove(AiState.CONSERVATIVE);
            states.add(AiState.LIBERAL);
        }
        else
        {
            states.remove(AiState.LIBERAL);
            states.add(AiState.CONSERVATIVE);
        }
    }

    /**
     * Inverts the social leaning of the AI.
     */
    public void invertSocial()
    {
        if(states.contains(AiState.COLLABORATING))
        {
            states.remove(AiState.COLLABORATING);
            states.add(AiState.ISOLATING);
        }
        else
        {
            states.remove(AiState.ISOLATING);
            states.add(AiState.COLLABORATING);
        }
    }
}
