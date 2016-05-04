package starvationevasion.common.gamecards;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 * Flavor Text: <i>{@value #FLAVOR_TEXT}</i><br><br>
 *
 * Votes Required: Automatic<br><br>
 *
*/

public class Policy_DiverttheFunds extends GameCard
{
  
  public static final String TITLE = "Divert the Funds";
  
  public static final String TEXT = 
      "Discard your current hand and gain " + 
      "14 million dollars";
 
  public static final String FLAVOR_TEXT =
      "No one can come up with a good idea. Good thing we're paid anyways.";
  
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
  
}
