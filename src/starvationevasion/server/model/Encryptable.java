package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public interface Encryptable
{
  /**
   * I would like to use a asymmetric encryption
   * client generates a pair of public/private keys and sends the public key to the server
   * more info here https://docs.oracle.com/javase/tutorial/security/apisign/step2.html
   * 
   * server uses the public key to encrypt some secret key and sends it back to the client (secrete is generated in this class as well as encry. decrpt)
   *
   * client uses his private key to decrypt the secret
   *
   * client uses algo with the secret key to encrypt the message
   * server uses the secret key to decrypt the message
   */
  
  String[] nonceAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".split("(?!^)");
  int NONCE_SIZE = 32;
  final Random r = new SecureRandom();

  byte[] encrypt (byte[] msg) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException;

  byte[] decrypt (byte[] msg) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException;

  boolean isEncrypted ();

  Encryptable setEncrypted (boolean encrypted, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException;

  static String generateKey ()
  {
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < NONCE_SIZE; i++)
    {
      sb.append(nonceAlphabet[random.nextInt(nonceAlphabet.length)]);
    }
    return sb.toString();
  }

  /**
   * The following was taken from Shea Polansky with VERY few modifications. 
   * Thank you for taking the time doing this!
   */
  String HASH_ALGORITHM = "SHA-256";
  char[] hexArray = "0123456789ABCDEF".toCharArray();

  /**
   * Converts a byte array to a hexadecimal string.
   * Credit to maybeWeCouldStealAVan on
   * this StackOverflow answer: http://stackoverflow.com/a/9855338
   * @param bytes the byte array to convert
   * @return the hex string equivalent of bytes
   */
  static String bytesToHex(byte[] bytes) {
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
  static byte[] generateHashedPassword(byte[] salt, byte[] password)
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

    byte[] passwordBytes, saltBytes;
    saltBytes = salt;
    passwordBytes = password;

    byte[] saltDigest = md.digest(saltBytes);
    byte[] saltDigestAndPasswordBytes = new byte[saltDigest.length + passwordBytes.length];
    System.arraycopy(saltDigest, 0, saltDigestAndPasswordBytes, 0, saltDigest.length);
    System.arraycopy(passwordBytes, 0, saltDigestAndPasswordBytes, saltDigest.length, passwordBytes.length);
    return md.digest(saltDigestAndPasswordBytes);
  }

}
