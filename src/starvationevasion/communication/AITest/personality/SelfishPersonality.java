package starvationevasion.communication.AITest.personality;

/**
 * This class implements a selfish AI for Starvation Evasion. <br>
 * This AI will strive to make cooperative choices that benefit itself before
 * making choices that benefit others.
 * 
 * @author Antonio Griego
 */
public class SelfishPersonality extends AbstractPersonality
{
  public SelfishPersonality()
  {
    super(PersonalityType.SELFISH);
  }
}