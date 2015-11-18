package starvationevasion.common.messages;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Shea Polansky
 * Message sent from client to server attempting to login.
 */
public class Login
{
  public final String username, hashedPassword;

  /**
   * Creates a new login with the given username and salted/hashed password
   * @param username the username to use
   * @param hashedPassword the S/H password to use
   */
  public Login(String username, String hashedPassword)
  {
    this.username = username;
    this.hashedPassword = hashedPassword;
  }

  /**
   * Creates a new login with the given username, and automatically
   * salts and hashes a given password with a given salt.
   * @param username the username to login with
   * @param salt the salt to use
   * @param password the password to use
   */
  public Login(String username, String salt, String password)
  {
    this(username, generateHashedPassword(salt, password));
  }

  private static final MessageDigest MESSAGE_DIGEST;

  public static final String HASH_ALGORITHM = "SHA-256";

  static
  {
    MessageDigest md = null;
    try
    {
      md = MessageDigest.getInstance(HASH_ALGORITHM);
    }
    catch(NoSuchAlgorithmException e)
    {
      System.out.println(HASH_ALGORITHM +
          " is not supported on this system! Login will be impossible. Exiting...");
      System.exit(1);
    }
    MESSAGE_DIGEST = md;
  }

  final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

  /**
   * Converts a byte array to a hexadecimal string.
   * Credit to maybeWeCouldStealAVan on
   * this StackOverflow answer: http://stackoverflow.com/a/9855338
   * @param bytes the byte array to convert
   * @return the hex string equivalent of bytes
   */
  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }

  /**
   * Generates a hashed password based on a given salt and password.
   * The algorithm for generation is hash = SHA256(SHA256(salt) + password), where
   * '+' is the concatenation operator, and the salt and password are
   * interpreted as UTF-8 strings of arbitrary length.
   * @param salt the salt to use
   * @param password the password to use
   * @return the resulting salted/hashed password.
   */
  public static String generateHashedPassword(String salt, String password)
  {
    byte[] passwordBytes, saltBytes;
    try
    {
      saltBytes = salt.getBytes("UTF-8");
      passwordBytes = password.getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
      throw new RuntimeException("Unable to convert strings to UTF-8");
    }
    byte[] saltDigest = MESSAGE_DIGEST.digest(saltBytes);
    byte[] saltDigestAndPasswordBytes = new byte[saltDigest.length + passwordBytes.length];
    System.arraycopy(saltDigest, 0, saltDigestAndPasswordBytes, 0, saltDigest.length);
    System.arraycopy(passwordBytes, 0, saltDigestAndPasswordBytes, saltDigest.length, passwordBytes.length);
    return bytesToHex(MESSAGE_DIGEST.digest(saltDigestAndPasswordBytes));
  }
}
