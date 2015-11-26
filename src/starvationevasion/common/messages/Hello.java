package starvationevasion.common.messages;

import starvationevasion.common.Util;

import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Shea Polansky
 * "Hello" message sent to the client by the server on connection
 */
public class Hello implements Serializable
{
  private static final String[] nonceAlphabet = //splits into 1 character strings by regex magic
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".split("(?!^)");
  public static final int NONCE_SIZE = 32;
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

  public static String generateRandomLoginNonce()
  {
    return Stream.generate(() ->
        nonceAlphabet[Util.rand.nextInt(nonceAlphabet.length)])
        .limit(NONCE_SIZE).collect(Collectors.joining());
  }
}
