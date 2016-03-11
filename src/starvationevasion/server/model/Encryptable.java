package starvationevasion.server.model;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.security.SecureRandom;
import java.util.Random;
import java.io.UnsupportedEncodingException;
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

  void encrypt (String msg, String key);

  <T> T decrypt (String msg, String key);


  static String xorEncrypt (String message, String key)
  {
    try
    {
      if (message == null || key == null)
      {
        return "";
      }

      char[] _keys = key.toCharArray();
      char[] _msg = message.toCharArray();

      int ml = _msg.length;
      int kl = _keys.length;
      char[] _newMsg = new char[ml];

      for (int i = 0; i < ml; i++)
      {
        _newMsg[i] = (char) (_msg[i] ^ _keys[i % kl]);
      }
      _msg = null;
      _keys = null;
      String temp = new String(_newMsg);
      return new BASE64Encoder().encodeBuffer(temp.getBytes());
    }
    catch(Exception e)
    {
      return "";
    }
  }


  static String xorDecrypt (String message, String key)
  {
    try
    {
      if (message == null || key == null)
      {
        return "";
      }
      char[] _keys = key.toCharArray();
      message = new String(new BASE64Decoder().decodeBuffer(message));
      char[] _msg = message.toCharArray();

      int ml = _msg.length;
      int kl = _keys.length;
      char[] _newMsg = new char[ml];

      for (int i = 0; i < ml; i++)
      {
        _newMsg[i] = (char) (_msg[i] ^ _keys[i % kl]);
      }
      _msg = null;
      _keys = null;
      return new String(_newMsg);
    }
    catch(Exception e)
    {
      return "";
    }
  }

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
  static String generateHashedPassword(String salt, String password)
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
    byte[] saltDigest = md.digest(saltBytes);
    byte[] saltDigestAndPasswordBytes = new byte[saltDigest.length + passwordBytes.length];
    System.arraycopy(saltDigest, 0, saltDigestAndPasswordBytes, 0, saltDigest.length);
    System.arraycopy(passwordBytes, 0, saltDigestAndPasswordBytes, saltDigest.length, passwordBytes.length);
    return bytesToHex(md.digest(saltDigestAndPasswordBytes));
  }

}
