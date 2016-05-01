package starvationevasion.communication.AITest.personality;

/**
 * This class implements a balanced AI for Starvation Evasion. <br>
 * This AI will strive to make cooperative choices that benefit others or itself
 * with equal probability.
 * 
 * @author Antonio Griego
 */
public class BalancedPersonality extends AbstractPersonality
{
  public BalancedPersonality()
  {
    super(PersonalityType.BALANCED);
  }
}
