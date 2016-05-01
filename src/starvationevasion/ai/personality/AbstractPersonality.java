package starvationevasion.ai.personality;

/**
 * This class implements an abstract personality class that will be the basis
 * for any Starvation Evasion AI personality.
 * @author Antonio Griego
 */
public abstract class AbstractPersonality
{
  public static enum PersonalityType { ALTRUISTIC, BALANCED, SELFISH, UNDEFINED };

  private PersonalityType personality;
  
  public AbstractPersonality(PersonalityType personality)
  {
    this.personality = personality;
  }  
}