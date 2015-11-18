package starvationevasion.common.messages;

/**
 * Shea Polansky
 * "Hello" message sent to the client by the server on connection
 */
public class Hello
{
  /**
   * Nonce used for a login hash salt.
   * Will be a 32 character string matching the regex "^[A-Za-z1-9]{32}$"
   */
  public final String loginNonce;
  /**
   * The current server version, probably "M1" (Milestone 1)
   */
  public final String serverVersion;

  public Hello(String loginNonce, String serverVersion)
  {
    this.loginNonce = loginNonce;
    this.serverVersion = serverVersion;
  }
}
