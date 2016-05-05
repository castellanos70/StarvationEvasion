package starvationevasion.common.gamecards;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 * Flavor Text: <i>{@value #FLAVOR_TEXT}</i><br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: The fact that such cards as this exist, implies that
 * the cards played must be kept inside of some Collection as a "Discard"
 * pile that can be iterated over.
*/
public class Policy_Redraft extends GameCard
{
  public static final String TITLE = "Redraft";
  
  public static final String TEXT = 
      "Return target policy card from your discard pile to your hand.";
  
  public static final String FLAVOR_TEXT = 
      "Yeah, that was a good idea, let's do it again!";
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getTitle() {return TITLE;}
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getGameText() {return TEXT;}
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getFlavorText() {return FLAVOR_TEXT;}
  
  /**
   * {@inheritDoc}
   */
  //@Override
  //TODO Might need to be changed how this accesses the discard.
  //public ArrayList<EnumPolicy> getValidTargetCards() 
  //{
  //  EnumRegion region = this.getOwner();
  //  int playerNumer = region.ordinal();
  //  return Simulator.getCardsInDiscard(region);
  //}
  
}
