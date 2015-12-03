package starvationevasion.common.messages;

/**
 * Shea Polansky
 * Class name and description goes here
 */
public enum ActionResponseType
{
  /**
   * The action was correct/valid and was successfully committed.
   */
  OK,
  /**
   * The action was performed too many times (e.g. trying to use the free discard twice,
   * or playing too many cards.
   */
  TOO_MANY_ACTIONS,
  /**
   * The player attempted to play two vote-required cards in a round.
   */
  TOO_MANY_VOTING_CARDS,
  /**
   * The player tried to play a card they did not possess
   */
  NONEXISTENT_CARD,
  /**
   * An invalid card was attempted to be drafted. Additional information may in this case
   * be available in the ActionResponse's message field.
   */
  INVALID
}
