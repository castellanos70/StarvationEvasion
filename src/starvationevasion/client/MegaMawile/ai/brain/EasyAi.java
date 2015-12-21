package starvationevasion.client.MegaMawile.ai.brain;


import starvationevasion.client.MegaMawile.controller.ComputerPlayerController;
import starvationevasion.client.MegaMawile.model.Ballot;
import starvationevasion.common.PolicyCard;
import starvationevasion.common.messages.VoteType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * An easy AI
 * <p>
 * This AI's behavior is relatively predictable - it makes decisions based on political/social "states"
 * which will change as a result of the impacts of past decisions. There is some degree of stochasticity
 * in its decision-making process (mostly as to whether it decides based on its political leaning or its
 * social leaning).
 *
 * @author Evan King
 */
public class EasyAi extends BasicBrain implements Ai
{
    short numDraftedCards = 0;
    short numDiscarded = 0;
    short numSupportCards = 0;

    boolean startOfPhase = true;

    PriorityQueue<PolicyCard> sortedHand;
    Ballot curBallot;

    /**
     * Creates an easy AI player which can make moves through the passed {@link ComputerPlayerController}.
     * The AI player's political/social leanings are randomly assigned, meaning that its behavior will be
     * pseudo-random for each game.
     *
     * @param controller  a ComputerPlayerController to use to make moves.
     */
    public EasyAi(ComputerPlayerController controller)
    {
        super(controller);

        AiState politics = (rand.nextBoolean()) ? AiState.LIBERAL : AiState.CONSERVATIVE;
        AiState social = (rand.nextBoolean()) ? AiState.COLLABORATING : AiState.ISOLATING;

        states.add(politics);
        states.add(social);

        System.out.println("EasyAI initializing. Politics: "+politics+", Social tendency: "+social);
    }

    /**
     * Updates the AI brain as appropriate for the drafting phase.
     * <p>
     * Note: it is important that this is only called during the drafting phase by the {@link ComputerPlayerController}
     * and not otherwise. The update methods rely heavily on several boolean flags which are shared between update methods.
     *
     * @param deltaSeconds deltaSeconds from the GameEngine.
     */
    @Override
    public void draftUpdate(float deltaSeconds)
    {
        if(startOfPhase)
        {
            System.out.println("EasyAI starting draft phase.");

            startDraft();
            stopWaiting();
            startOfPhase = false;
        }

        if(!deciding() && !waiting() && sortedHand.size() != 0)
        {
            startDeciding();

            PolicyCard card = choosePolicy(sortedHand);

            /*
             * Move options:
             *  - Draft 2 cards
             *  - Discard/draw 3
             *  - Discard 1
             */

            //True favors drafting, false favors discarding.
            if(rand.nextBoolean())
            {
                if(card.votesRequired() > 0 && numSupportCards < 1)
                {
                    System.out.println("Drafting support card: "+card.getPolicyName());

                    int x, y, z;

                    if(card.getRequiredVariables(PolicyCard.EnumVariable.X) != null)
                    {
                        switch(card.getRequiredVariables(PolicyCard.EnumVariable.X))
                        {
                            case MILLION_DOLLAR:
                                x = (states.contains(AiState.CONSERVATIVE)) ? card.MIN_MILLION_DOLLARS : card.MAX_MILLION_DOLLARS;
                                card.setX(x);
                                break;
                            case PERCENT:
                                x = (states.contains(AiState.CONSERVATIVE)) ? card.MIN_PERCENT : card.MAX_PERCENT;
                                card.setX(x);
                                break;
                        }
                    }

                    if(card.getRequiredVariables(PolicyCard.EnumVariable.Y) != null)
                    {
                        switch(card.getRequiredVariables(PolicyCard.EnumVariable.Y))
                        {
                            case MILLION_DOLLAR:
                                y = (states.contains(AiState.CONSERVATIVE)) ? card.MIN_MILLION_DOLLARS : card.MAX_MILLION_DOLLARS;
                                card.setY(y);
                                break;
                            case PERCENT:
                                y = (states.contains(AiState.CONSERVATIVE)) ? card.MIN_PERCENT : card.MAX_PERCENT;
                                card.setY(y);
                                break;
                        }
                    }


                    if(card.getRequiredVariables(PolicyCard.EnumVariable.Z) != null)
                    {
                        switch(card.getRequiredVariables(PolicyCard.EnumVariable.Z))
                        {
                            case MILLION_DOLLAR:
                                z = (states.contains(AiState.CONSERVATIVE)) ? card.MIN_MILLION_DOLLARS : card.MAX_MILLION_DOLLARS;
                                card.setZ(z);
                                break;
                            case PERCENT:
                                z = (states.contains(AiState.CONSERVATIVE)) ? card.MIN_PERCENT : card.MAX_PERCENT;
                                card.setZ(z);
                                break;
                        }
                    }

                    if(controller.draftPolicy(card)) addDraftSupport();
                }
                if(card.votesRequired() == 0)
                {
                    System.out.println("Drafting card: "+card.getPolicyName());
                    if(controller.draftPolicy(card)) addDraft();
                }
            }
            else
            {
                //Discard/Draw
                if(rand.nextBoolean() && numDiscarded == 0 && sortedHand.size() > 3)
                {
                    System.out.println("Discard/draw. Current numDiscarded: "+numDiscarded+", sortedHand size: "+sortedHand.size());
                    PolicyCard[] discards = {sortedHand.remove(), sortedHand.remove(), sortedHand.remove()};
                    //if(controller.discardDraw(discards))
                    numDiscarded = 3;
                }
                //Discard 1
                else if(numDiscarded < 1)
                {
                    System.out.println("Discarding 1");
                    controller.discard(sortedHand.remove());
                    numDiscarded++;
                }
            }

            if(numDraftedCards == 2)
            {
                System.out.println("2 Cards drafted, turn over.");
                startDeciding();
                startWaiting();
                startOfPhase = true;
            }

            stopDeciding();
        }
    }

    //Updates the AI brain as appropriate for the voting phase.
    @Override
    public void voteUpdate(float deltaSeconds)
    {
        //Stop waiting and begin the phase.
        if(startOfPhase)
        {
            System.out.println("EasyAI start of voting phase.");

            stopWaiting();
            startOfPhase = false;
        }

        if(!deciding() && !waiting())
        {
            startDeciding();
            startVote();
            placeVotes();
            startDeciding();
            startWaiting();
            startOfPhase = true;
        }
    }

    //Returns the top of the sorted hand.
    @Override
    public PolicyCard choosePolicy(PriorityQueue<PolicyCard> hand) { return hand.poll(); }

    @Override
    public PriorityQueue<PolicyCard> sortHand(Iterator<PolicyCard> hand)
    {
        PriorityQueue<PolicyCard> queue;
        //50/50 likelihood that the AI will choose based on political leanings or social tendencies.
        if(rand.nextBoolean())
        {
            //Create a queue based on political leaning
            queue = (states.contains(AiState.CONSERVATIVE)) ?
                new PriorityQueue<PolicyCard>(7, new PolicyComparator(AiState.CONSERVATIVE)) :
                new PriorityQueue<PolicyCard>(7, new PolicyComparator(AiState.LIBERAL));
        }
        else
        {
            //Create a queue based on social tendency
            queue = (states.contains(AiState.ISOLATING)) ?
                    new PriorityQueue<PolicyCard>(7, new PolicyComparator(AiState.ISOLATING)) :
                    new PriorityQueue<PolicyCard>(7, new PolicyComparator(AiState.COLLABORATING));
        }

        while(hand.hasNext())
            queue.add(hand.next());

        return queue;
    }

    @Override
    public void placeVotes()
    {
        HashMap<PolicyCard, VoteType> ballotItems = curBallot.getBallotItems();
        PolicyComparator comparator = (rand.nextBoolean()) ?
                new PolicyComparator(myPolitics()) : new PolicyComparator(mySocial());

        /*
         * Grab each EnumRegion and their PolicyCards to vote on. We will favor policies which match our leanings,
         * or policies that have been drafted by friendly regions.
         */
        ballotItems.forEach((k, v) -> {

            //Vote yes
            if(isFriendly(k.getOwner()) || comparator.isFavorable(k))
                controller.voteYes(k);

            //Vote no
            else
                controller.voteNo(k);
        });
    }

    /**
     * Adds to the tally of drafted cards. Used to enforce game rules on drafts allowed per round.
     */
    private void addDraft()
    {
        numDraftedCards++;
    }

    /**
     * Adds to the tally of drafted support cards (those requiring a vote). Used to enforce game rules
     * on drafts allowed per round.
     */
    private void addDraftSupport()
    {
        numDraftedCards++;
        numSupportCards++;
    }

    /**
     * Initializes fields needed for the drafting phase.
     */
    private void startDraft()
    {
        Iterator<PolicyCard> hand = controller.getHand();
        this.sortedHand = sortHand(hand);

        numDraftedCards = 0;
        numDiscarded = 0;
        numSupportCards = 0;
    }

    /**
     * Initializes fields needed for the voting phase.
     */
    private void startVote()
    {
        curBallot = controller.getBallot();
    }

}
