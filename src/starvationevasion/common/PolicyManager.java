package starvationevasion.common;

import java.util.*;

/**
 * The PolicyManager class is a service provider that aggregates and provides all of the
 * available policy cards found in the VM's classpath.
 *
 * Created by peter on 11/17/2015.
 */
public class PolicyManager
{
  /* The singleton instance of the policy manager.
  */
  private static PolicyManager service;

  /* The service loader for policy providers.
  */
  private ServiceLoader<PolicyProvider> loader;

  /* The list of discovered policy providers.
  */
  private Collection<PolicyProvider> providers;

  /* The total number of policies avalable across all providers.
  */
  private final int policyCount;

  private final Map<Object, PolicyData> policyMap = new HashMap<>();
  private final Map<PolicyCard, PolicyData> cardMap = new HashMap<>();
  private final Collection<PolicyCard> cards = new ArrayList<>();

  /* The constructor for the PolicyManager is private because we only want exactly
  ** one instance created via getPolicyManager().
  */
  private PolicyManager() throws ServiceConfigurationError, DuplicatePolicyException
  {
    providers = new ArrayList<>();

    // Get the provider set from the loader.
    //
    loader = ServiceLoader.load(PolicyProvider.class);
    for (PolicyProvider aLoader : loader)
    {
      providers.add(aLoader);
    }

    // The service provider architecture was added a couple of weeks into
    // the project. If they don't have any configured, give them the Fall
    // 2015 policy set.
    //
    if (providers.isEmpty())
    { providers.add(new starvationevasion.common.policies.Fall2015PolicyProvider());
    }

    // Build the policy map.
    //
    int count = 0;
    for (PolicyProvider provider : providers)
    {
      Collection<PolicyCard> providerCards = provider.getCards();
      for (PolicyCard card : providerCards)
      {
        String policyName = card.name();
        if (policyMap.get(policyName) != null) throw new DuplicatePolicyException(policyName);
        
        cards.add(card);

        // It should simplify the server's job considerably if we map by both
        // policy name and number.
        //
        PolicyData data = new PolicyData(provider, card, count);
        cardMap.put(card, data);
        policyMap.put(policyName, data);
        policyMap.put((Integer) count, data);

        count += 1;
      }
    }

    policyCount = count;
  }

  /**
   * @throws ServiceConfigurationError, DuplicatePolicyException
   * @return The singleton instance of the policy manager.
   */
  public static synchronized PolicyManager getInstance() throws ServiceConfigurationError, DuplicatePolicyException
  {
    if (service == null) service = new PolicyManager();

    return service;
  }

  /**
   * @return The total number of policies in the game.
   */
  public int getPolicyCount()
  {
    return policyCount;
  }

  /**
   * @return The policies
   */
  public Collection<PolicyCard> getCards()
  {
    return cards;
  }

  /**
   * @param policyNumber A policy's master sequence number
   * @return The playing card for the policy.
   */
  public PolicyCard getCard(int policyNumber)
  {
    PolicyData data = policyMap.get(policyNumber);
    if (data == null) throw new IllegalArgumentException("Unknown policy " + policyNumber);
    return data.card;
  }

  /**
   * @param policyName A policy's master sequence number
   * @return The playing card for the policy.
   */
  public PolicyCard getCard(String policyName)
  {
    PolicyData data = policyMap.get(policyName);
    if (data == null) throw new IllegalArgumentException("Unknown policy " + policyName);
    return data.card;
  }

  /**
   * Creates a new policy card instance for the policy number and region.
   * @param card The policy card.
   * @param region The region playing the card.
   * @return A policy card object.
   */
  public Policy createPolicy(PolicyCard card, EnumRegion region)
  {
    PolicyData entry = cardMap.get(card);
    if (entry == null) throw new IllegalArgumentException("Invalid playing card.");

    return entry.provider.createPolicy(card, region);
  }

  /**
   * Creates a new policy card instance for the policy number and region.
   * @param policyNumber The policy number for which to instantiate a card.
   * @param region The region playing the card.
   * @return The total number of policies in the game.
   */
  public Policy createPolicy(int policyNumber, EnumRegion region)
  {
    PolicyData entry = policyMap.get(policyNumber);
    if (entry == null) throw new IllegalArgumentException("Unknown policy " + policyNumber);

    return createPolicy(entry.card, region);
  }

  /**
   * Creates a new policy card instance for the policy number and region.
   * @param policyName The policy number for which to instantiate a card.
   * @param region The region playing the card.
   * @return The total number of policies in the game.
   */
  public Policy createPolicy(String policyName, EnumRegion region)
  {
    PolicyData entry = policyMap.get(policyName);
    if (entry == null) throw new IllegalArgumentException("Unknown policy " + policyName);

    return createPolicy(entry.card, region);
  }

  /**
   * Maps a policy class back to a policy number.
   * @param card The class for which the policy number is required.
   * @return The policy number for the class.
   */
  public int getCardNumber(PolicyCard card)
  {
    PolicyData entry = cardMap.get(card);
    if (entry == null) throw new IllegalArgumentException("Unknown policy " + card.name());

    return entry.sequence;
  }

  /**
   * Validates a policy card given the provided settings.
   *
   * Some policy cards require quantity X, Y and/or Z. The units of these
   * values depend on the particular policy. The respective field is ignored
   * if the named policy does not require that field. 
   *
   * The targetFood field is ignored if the named policy does not require
   * a target food.  Some cards require the target region to be a US player
   * region. Other cards require the target region to be a non-player world
   * region.  The targetRegion field is ignored if the named policy does not
   * require a target region.
   *
   * @param card A playing card.
   * @param region The implementing region.
   * @param targetFood The target food.
   * @param targetRegion The target region.
   * @param x The value to apply to X.
   * @param y The value to apply to Y.
   * @param z The value to apply to Z.
   *
   * @throws IllegalArgumentException policyName is not a valid Fall 2015 policy name.
   * @return A text string indicating the error, or null if the card and associated parameters
   * are valid.
   *
   */
  public String validate(
      PolicyCard card, EnumRegion region,
      int x, int y, int z,
      EnumFood targetFood, EnumRegion targetRegion
  ) {
    PolicyData data = cardMap.get(card);
    if (data == null) throw new IllegalArgumentException("Unrecognized card " + card.name());

    return validate(data, region, x, y, z, targetFood, targetRegion);
  }

  /**
   * Validates a policy card given the provided settings.
   *
   * Some policy cards require quantity X, Y and/or Z. The units of these
   * values depend on the particular policy. The respective field is ignored
   * if the named policy does not require that field. 
   *
   * The targetFood field is ignored if the named policy does not require
   * a target food.  Some cards require the target region to be a US player
   * region. Other cards require the target region to be a non-player world
   * region.  The targetRegion field is ignored if the named policy does not
   * require a target region.
   *
   * @param policyName The policy name.
   * @param region The implementing region.
   * @param targetFood The target food.
   * @param targetRegion The target region.
   * @param x The value to apply to X.
   * @param y The value to apply to Y.
   * @param z The value to apply to Z.
   *
   * @throws IllegalArgumentException policyName is not a valid Fall 2015 policy name.
   * @return A text string indicating the error, or null if the card and associated parameters
   * are valid.
   *
   */
  public String validate(
      String policyName, EnumRegion region,
      int x, int y, int z,
      EnumFood targetFood, EnumRegion targetRegion
  ) {
    PolicyData data = policyMap.get(policyName);
    if (data == null) throw new IllegalArgumentException("Unknown policy " + policyName);
    
    return validate(data, region, x, y, z, targetFood, targetRegion);
  }

  private String validate(
          PolicyData data, EnumRegion region,
          int x, int y, int z,
          EnumFood targetFood, EnumRegion targetRegion
  ) {
    Policy policy;
    try
    {
      policy = data.provider.createPolicy(data.card, region);
    }
    catch (IllegalArgumentException ex)
    {
      return ex.getMessage();
    }

    policy.setX(x);
    policy.setY(y);
    policy.setZ(z);
    policy.setTargetFood(targetFood);
    policy.setTargetRegion(targetRegion);

    return policy.validate();
  }

  static private class PolicyData
  {
    private final PolicyProvider provider;
    private final PolicyCard card;
    private final int sequence;

    PolicyData(final PolicyProvider provider, final PolicyCard card, final int sequence)
    {
      this.provider = provider;
      this.card = card;
      this.sequence = sequence;
    }
  }

  static public class DuplicatePolicyException extends Exception
  {
    protected DuplicatePolicyException(String policyName)
    {
      super(policyName);
    }
  }
  
  /**
   * @param args Not used.
   * Used only for testing this class.
   */
  public static void main(String[] args)
  {
    PolicyManager pm = null;
    try
    { pm = PolicyManager.getInstance();
    }
    catch (ServiceConfigurationError | DuplicatePolicyException ex)
    {
      ex.printStackTrace();
      System.exit(1);
    }

    // We could simply iterate over the collection of cards, but at some point
    // in the future we may simply want to reference cards by number to simplify
    // data passing.
    //
    int count = pm.getPolicyCount();
    for (int i = 0 ; i < count ; i += 1)
    {
      PolicyCard playing = pm.getCard(i);
      Policy policy = pm.createPolicy(playing, EnumRegion.MOUNTAIN);

      Policy test = pm.createPolicy(i, EnumRegion.MOUNTAIN);
      if (policy.equals(test) == false)
      {
        System.err.println("Policy " + i + " failed reverse lookup.");
      } 
      else
      { 
        System.out.println("Policy " + i + " : " + policy.getTitle() + " / " + policy.getGameText());
      }
    }

	String result = pm.validate(
            starvationevasion.common.policies.Fall2015PolicyProvider.EnumPolicy.GMO_Seed_Insect_Resistance_Research,
            EnumRegion.CALIFORNIA,
            0, 0, 0,
            EnumFood.CITRUS,
            EnumRegion.SOUTHEAST_ASIA
    );
	if (result != null) System.out.println("Validation result : " + result);
  }
}
