package starvationevasion.common.messages;

import starvationevasion.common.EnumPolicy;

import java.io.Serializable;

/**
 * Shea Polansky
 * A response message sent from server to client that contains
 * the results of a given action.
 * Includes a specific response (member of the ActionResponseType enum), the
 * player's current hand (taking into account the results of the action if
 * that action was valid), and an optional ResponseMessage to provide extra information.
 * The ResponseMessage will principally be used to convey why a given played card
 * was invalid (using PolicyCard.validate()'s return value), but in general no guarantees are made
 * regarding the value of this field.
 */
public class ActionResponse implements Serializable
{
  public final ActionResponseType responseType;
  public final EnumPolicy[] playerHand;
  public final String responseMessage;

  public ActionResponse(ActionResponseType responseType, EnumPolicy[] playerHand, String responseMessage)
  {
    this.responseType = responseType;
    this.playerHand = playerHand;
    this.responseMessage = responseMessage;
  }

  public ActionResponse(ActionResponseType responseType, EnumPolicy[] playerHand)
  {
    this(responseType, playerHand, null);
  }
}
