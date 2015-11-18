package starvationevasion.common.messages;

import starvationevasion.common.EnumRegion;

/**
 * Shea Polansky
 * A response to a login attempt.
 */
public class LoginResponse
{
  public enum ResponseType
  {
    /**
     * Unknown user/bad password/etc.
     */
    ACCESS_DENIED,
    /**
     * You have been assigned a specific region.
     */
    ASSIGNED_REGION,
    /**
     * You are rejoining an already-in-progress game
     */
    REJOIN,
    /**
     * You may choose a region. A message of this type will be immediately followed
     * by an AvailableRegions message.
     */
    CHOOSE_REGION
  }

  /**
   * The type of this response.
   */
  public final ResponseType responseType;

  /**
   * The region you have been assigned. This will only be set for
   * responses of type ResponseType.ASSIGNED_REGION or ResponseType.REJOIN.
   * Its value is unspecified otherwise.
   */
  public final EnumRegion assignedRegion;

  public LoginResponse(ResponseType responseType, EnumRegion assignedRegion)
  {
    this.responseType = responseType;
    this.assignedRegion = assignedRegion;
  }
}
