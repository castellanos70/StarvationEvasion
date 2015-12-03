package starvationevasion.common.messages;

import starvationevasion.common.PolicyCard;

import java.io.Serializable;

/**
 * Shea Polansky
 * A message sent from the client to the server indicating that it wishes
 * to draft a given PolicyCard.
 */
public class DraftCard implements Serializable
{
  public final PolicyCard policyCard;

  public DraftCard(PolicyCard policyCard)
  {
    this.policyCard = policyCard;
  }
}
