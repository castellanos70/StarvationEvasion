package starvationevasion.common;

import java.util.Collection;

/**
 * The PolicyProvider interface is implemented by any class that provides policy cards
 * to the simulator.
 *
 * Created by peter on 11/17/2015.
 */
public interface PolicyProvider
{
  /**
   * @return The total number of policies in the game.
   */
  public int getPolicyCount();

  /**
   * @return The collection of cards provided by the implementing class.
   */
  public Collection<PolicyCard> getCards();

  /**
   * @param policyName The name of the policy.
   * @return True if this provider provides the named policy.
   */
  public boolean provides(String policyName);

  /**
   * @param card A playing card
   * @return True if this provider provides the policy class.
   */
  public boolean provides(PolicyCard card);

  /**
   * @param card The class of the policy.
   * @return True if this provider provides the policy class.
   */
  public boolean provides(Policy card);

  /**
   * Maps a policy name to a playing card.
   * @param policyName The name of the policy.
   * @return The playing card for the named policy.
   */
  public PolicyCard getPolicyCard(String policyName);

  /**
   * Creates a new policy card instance for the playing card and region.
   * @param card A playing card provided by this policy provider.
   * @param region The region playing the card.
   * @return A policy card for the specified policy..
   */
  public Policy createPolicy(PolicyCard card, EnumRegion region);

  /**
   * Creates a new policy card instance for the policy number and region.
   * @param policyName The name of the policy.
   * @param region The region playing the card.
   * @return A policy card for the specified policy..
   */
  public Policy createPolicy(String policyName, EnumRegion region);

  /**
   * Maps a playing card to a policy name.
   * @param card The class for which the policy number is required.
   * @return The policy number for the class.
   */
  public String getPolicyName(PolicyCard card);
}
