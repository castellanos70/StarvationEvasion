package starvationevasion.common.policies;

import starvationevasion.common.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * The Fall2015PolicyProvider class provides access to the policy cards
 * developed during CS351 Fall 2015 semester project 3.
 *
 * Created by peter on 11/17/2015.
 */
public class Fall2015PolicyProvider implements PolicyProvider
{
  public enum EnumPolicy implements PolicyCard
  {
    GMO_Seed_Insect_Resistance_Research(
        GMOSeedInsectResistanceResearchPolicy.class,
        GMOSeedInsectResistanceResearchPolicy.TITLE,
        GMOSeedInsectResistanceResearchPolicy.TEXT,
        GMOSeedInsectResistanceResearchPolicy.VOTES_REQUIRED,
        GMOSeedInsectResistanceResearchPolicy.VOTE_WAIT_FOR_ALL
    ),
    International_Food_Relief_Program(
        InternationalFoodReliefProgramPolicy.class,
        InternationalFoodReliefProgramPolicy.TITLE,
        InternationalFoodReliefProgramPolicy.TEXT,
        InternationalFoodReliefProgramPolicy.VOTES_REQUIRED,
        InternationalFoodReliefProgramPolicy.VOTE_WAIT_FOR_ALL
    ),
    Efficient_Irrigation_Incentive(
        EfficientIrrigationIncentivePolicy.class,
        EfficientIrrigationIncentivePolicy.TITLE,
        EfficientIrrigationIncentivePolicy.TEXT,
        EfficientIrrigationIncentivePolicy.VOTES_REQUIRED,
        EfficientIrrigationIncentivePolicy.VOTE_WAIT_FOR_ALL
    ),
    Foreign_Aid_for_Farm_Infrastructure(
        ForeignAidForFarmInfrastructurePolicy.class,
        ForeignAidForFarmInfrastructurePolicy.TITLE,
        ForeignAidForFarmInfrastructurePolicy.TEXT,
        ForeignAidForFarmInfrastructurePolicy.VOTES_REQUIRED,
        ForeignAidForFarmInfrastructurePolicy.VOTE_WAIT_FOR_ALL
    ),
    Covert_Intelligence(
        CovertIntelligencePolicy.class,
        CovertIntelligencePolicy.TITLE,
        CovertIntelligencePolicy.TEXT,
        CovertIntelligencePolicy.VOTES_REQUIRED,
        CovertIntelligencePolicy.VOTE_WAIT_FOR_ALL
    ),
    Clean_River_Incentive(
        CleanRiverIncentivePolicy.class,
        CleanRiverIncentivePolicy.TITLE,
        CleanRiverIncentivePolicy.TEXT,
        CleanRiverIncentivePolicy.VOTES_REQUIRED,
        CleanRiverIncentivePolicy.VOTE_WAIT_FOR_ALL
    ),
    MyPlate_Promotion_Campaign(
        MyPlatePromotionCampaignPolicy.class,
        MyPlatePromotionCampaignPolicy.TITLE,
        MyPlatePromotionCampaignPolicy.TEXT,
        MyPlatePromotionCampaignPolicy.VOTES_REQUIRED,
        MyPlatePromotionCampaignPolicy.VOTE_WAIT_FOR_ALL
    ),
    Ethanol_Production_Tax_Credit_Change(
        EthanolProductionTaxCreditChangePolicy.class,
        EthanolProductionTaxCreditChangePolicy.TITLE,
        EthanolProductionTaxCreditChangePolicy.TEXT,
        EthanolProductionTaxCreditChangePolicy.VOTES_REQUIRED,
        EthanolProductionTaxCreditChangePolicy.VOTE_WAIT_FOR_ALL
    ),
    Fertilizer_Subsidy(
        FertilizerSubsidyPolicy.class,
        FertilizerSubsidyPolicy.TITLE,
        FertilizerSubsidyPolicy.TEXT,
        FertilizerSubsidyPolicy.VOTES_REQUIRED,
        FertilizerSubsidyPolicy.VOTE_WAIT_FOR_ALL
    ),
    Educate_the_Women_Campaign(
        EducateTheWomenCampaignPolicy.class,
        EducateTheWomenCampaignPolicy.TITLE,
        EducateTheWomenCampaignPolicy.TEXT,
        EducateTheWomenCampaignPolicy.VOTES_REQUIRED,
        EducateTheWomenCampaignPolicy.VOTE_WAIT_FOR_ALL
    );

    // The class implementing the policy card (eg FertilizerSubsidyPolicy.clas)
    //
    private final Class policyClass;

    // The policy card's title.
    //
    private final String title;

    // The policy card's game text.
    //
    private final String gameText;

    // The number of votes required.
    //
    private final int votesRequired;

    // True if the card requires the server to wait for all votes.
    //
    private final boolean voteWaitForAll;

    EnumPolicy(final Class clazz, final String title, final String gameText, final int votes, final boolean voteWait)
    {
      this.policyClass = clazz;
      this.title = title;
      this.gameText = gameText;
      this.votesRequired = votes;
      this.voteWaitForAll = voteWait;
    }

    /**
     * {@inheritDoc}
    */
    @Override
    public String title() { return title; }

    /**
     * {@inheritDoc}
    */
    @Override
    public String gameText() { return gameText; }

    /**
     * {@inheritDoc}
    */
    @Override
    public int votesRequired() { return votesRequired; }

    /**
     * {@inheritDoc}
    */
    @Override
    public boolean voteWaitForAll() { return voteWaitForAll; }

    /**
	 * Creates a policy card for this card.
	*/
    protected Policy createPolicy(EnumRegion region)
	{
      Policy policy = null;
      try {
          policy = (Policy) policyClass.getConstructor(EnumRegion.class).newInstance(region);
      } catch (Exception e) {
          e.printStackTrace();
      }

      return policy;
    }
  }

  public final static int POLICY_COUNT = EnumPolicy.values().length;

  private final Collection<PolicyCard> cards = new ArrayList<PolicyCard>(Arrays.asList(EnumPolicy.values()));

  /**
   * {@inheritDoc}
  */
  @Override
  public int getPolicyCount()
  {
    return POLICY_COUNT;
  }

  /**
   * {@inheritDoc}
  */
  @Override
  public boolean provides(String policyName)
  {
    try
    {
      EnumPolicy.valueOf(policyName);
      return true;
    } catch (IllegalArgumentException ex)
    {
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<PolicyCard> getCards()
  {
    return cards;
  }

  /**
   * {@inheritDoc}
  */
  @Override
  public boolean provides(PolicyCard card)
  {
    return (card instanceof EnumPolicy);
  }

  /**
   * {@inheritDoc}
  */
  @Override
  public boolean provides(Policy policy)
  {
    for (EnumPolicy p : EnumPolicy.values())
    {
      if (policy.getClass().equals(p.policyClass)) return true;
    }

    return false;
  }

  /**
   * {@inheritDoc}
  */
  @Override
  public Policy createPolicy(PolicyCard card, EnumRegion region)
  {
    if (card instanceof EnumPolicy == false) throw new IllegalArgumentException("Not a fall 2015 playing card.");

    Policy policy = null;
    try {
        policy = (Policy) ((EnumPolicy) card).policyClass.getConstructor(EnumRegion.class).newInstance(region);
    } catch (Exception e) {
        e.printStackTrace();
    }

    return policy;
 }

  /**
   * {@inheritDoc}
   */
  @Override
  public Policy createPolicy(String policyName, EnumRegion region)
  {
    EnumPolicy policy = EnumPolicy.valueOf(policyName);
    Policy card = createPolicy(policy, region);
    if (card == null) throw new IllegalStateException("Policy does not evaluate to a valid Policy");

    return card;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPolicyName(PolicyCard card)
  {
    if (card instanceof EnumPolicy == false) throw new IllegalArgumentException("Unsupported policy");

    for (EnumPolicy policy : EnumPolicy.values())
    {
      if (policy.policyClass.equals(card)) return policy.toString();
    }

    throw new IllegalArgumentException("Unsupported policy");
  }

  /**
   * {@inheritDoc}
  */
  @Override
  public PolicyCard getPolicyCard(String policyName)
  {
    return EnumPolicy.valueOf(policyName);
  }

    /**
   * @param args Not used.
   * Used only for testing this class.
   */
  public static void main(String[] args)
  {
    Fall2015PolicyProvider provider = new Fall2015PolicyProvider();
    Collection<PolicyCard> cards = provider.getCards();
    for (PolicyCard card : cards)
    {
      Policy policy = provider.createPolicy(card, EnumRegion.MOUNTAIN);
      System.out.println("Policy " + card.name() + " : " + policy.getTitle() + " / " + policy.getGameText());
    }
  }
}
