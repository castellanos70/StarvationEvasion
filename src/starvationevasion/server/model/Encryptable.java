package starvationevasion.server.model;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.util.Random;

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


  void encrypt (String msg, String key);

  String decrypt (String msg, String key);


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

}
