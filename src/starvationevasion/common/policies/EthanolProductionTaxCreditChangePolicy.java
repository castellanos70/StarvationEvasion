package starvationevasion.common.policies;

import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.Policy;
import starvationevasion.common.PolicyCard;

import java.util.Collection;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, the model must inform
 * the player of X and require the player selects Y.<br><br>
 *
 * Votes Required: Automatic <br><br>
 *
 * Model Effects: <br><br>
 */
public class EthanolProductionTaxCreditChangePolicy extends Policy
{
  public static PolicyCard CARD = Fall2015PolicyProvider.EnumPolicy.Ethanol_Production_Tax_Credit_Change;

  public static final String TITLE =
      "Ethanol Production Tax Credit Change";

  public static final String TEXT =
      "Currently an ethanol producer located in my region is entitled " +
      "to a credit of $X per gallon of ethanol produced, including " +
      "cellulosic ethanol. This policy changes that to $Y per gallon.";

  /* The number of votes required for this policy.  A value of 0 means that
   * the policy is automatic.
  */
  public final static int VOTES_REQUIRED = 0;

  /* Combined with 0 required votes, this Indicates that this policy is automatic.
  */
  public final static boolean VOTE_WAIT_FOR_ALL = false;

  /* The crop types applicable to this policy.
  */
  public final static Collection<EnumFood> TARGET_FOOD = null;

  /* The target regions applicable to this policy. A v
  */
  public final static Collection<EnumRegion> TARGET_REGIONS = null;

  public EthanolProductionTaxCreditChangePolicy(EnumRegion region)
  {
    super(region);
  }

  /**
   * {@inheritDoc}
  */
  @Override
  public int votesRequired() {return VOTES_REQUIRED;}

  /**
   * {@inheritDoc}
  */
  @Override
  public boolean voteWaitForAll() {return false;}

  /**
   * {@inheritDoc}
  */
  @Override
  public String getTitle(){ return TITLE;}

  /**
   * {@inheritDoc}
  */
  @Override
  public String getGameText(){ return TEXT;}

  /**
   * {@inheritDoc}
   */
  @Override
  public PolicyCard getCardType() { return CARD; }

  public String validate()
  {
     // case EthanolProductionTaxCreditChangePolicy:
     String msg = validatePercentValue(varY);
     if (msg != null) return getPolicyName() + msg;

    return null;
  }

  /**
   * Used only for testing this class.
   * @param args Not used.
   */
  public static void main(String[] args)
  {

    Policy myCard = new EthanolProductionTaxCreditChangePolicy(EnumRegion.MOUNTAIN);
    System.out.println(myCard.getTitle());
    System.out.println(myCard.getGameText());
  }
}
