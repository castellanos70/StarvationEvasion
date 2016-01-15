package starvationevasion.client.MegaMawile2.ai.brain;

/**
 * An enumeration of AI states which, depending on their combination, influence <br>
 * AI behavior. Each state should have an "inverse" which allows the Ai to alter its <br>
 * behavior if a previous state led to negative consequences.
 *
 * @author Evan King
 */
public enum AiState
{
    /**
     * AI will tend to favor "liberal" policies, and put more money towards them.
     */
    LIBERAL {
        public AiState inverse(){ return AiState.CONSERVATIVE; }
    },

    /**
     * AI will tend to favor "conservative" policies, and spend less on them.
     */
    CONSERVATIVE {
        public AiState inverse(){ return AiState.LIBERAL; }
    },

    /**
     * AI will collaborate less.
     */
    ISOLATING {
        public AiState inverse(){ return AiState.COLLABORATING; }
    },

    /**
     * AI will attempt to collaborate more frequently.
     */
    COLLABORATING {
        public AiState inverse(){ return AiState.ISOLATING; }
    },

    /**
     * AI has completed its decisions for this round.
     */
    WAITING,

    /**
     * AI is in the middle of a decision.
     */
    DECIDING
}
