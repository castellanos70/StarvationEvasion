package starvationevasion.server.io.strategies;


import starvationevasion.common.Constant;
import starvationevasion.server.io.formatters.Format;
import starvationevasion.server.io.formatters.JSONFormat;
import starvationevasion.server.model.Encryptable;
import starvationevasion.server.model.Sendable;

import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public abstract class SecureStream implements Encryptable
{
  private boolean isEncrypted = false;
  private SecretKey key;
  private Cipher aesCipher;

  public SecureStream setEncrypted (boolean encrypted) throws NoSuchAlgorithmException,
                                                              NoSuchPaddingException
  {
    return setEncrypted(encrypted, null);
  }

  @Override
  public SecureStream setEncrypted (boolean encrypted, SecretKey key) throws NoSuchPaddingException,
                                                                             NoSuchAlgorithmException
  {
    this.key = key;
    isEncrypted = encrypted;
    if (isEncrypted)
    {
      aesCipher = Cipher.getInstance(Constant.DATA_ALGORITHM);
    }
    return this;
  }

  public boolean isEncrypted ()
  {
    return isEncrypted;
  }

  @Override
  public byte[] encrypt (byte[] msg) throws InvalidKeyException,
                                            BadPaddingException,
                                            IllegalBlockSizeException
  {
    aesCipher.init(Cipher.ENCRYPT_MODE, key);
    return aesCipher.doFinal(msg);
  }

  @Override
  public byte[] decrypt (byte[] msg) throws InvalidKeyException,
                                            BadPaddingException,
                                            IllegalBlockSizeException
  {
    aesCipher.init(Cipher.DECRYPT_MODE, key);
    return aesCipher.doFinal(msg);
  }

  protected String decrypt(String data) throws BadPaddingException,
                                               InvalidKeyException,
                                               IllegalBlockSizeException
  {
    byte[] dataBytes = DatatypeConverter.parseBase64Binary(data.trim());

    byte[] al =  decrypt(dataBytes);
    String sl = new String(al);
    return sl;
  }

  protected String encrypt(String data) throws BadPaddingException,
                                               InvalidKeyException,
                                               IllegalBlockSizeException
  {
    byte[] _data = data.getBytes();
    String ss = DatatypeConverter.printBase64Binary(encrypt(_data));
    return ss;
  }

  public Serializable encrypt(Serializable data) throws BadPaddingException,
                                                           InvalidKeyException,
                                                           IllegalBlockSizeException,
                                                           IOException
  {
    aesCipher.init(Cipher.ENCRYPT_MODE, key);
    return new SealedObject(data, aesCipher);
  }

  protected Serializable decrypt(Serializable data) throws BadPaddingException,
                                                           InvalidKeyException,
                                                           IllegalBlockSizeException,
                                                           IOException,
                                                           ClassNotFoundException
  {
    aesCipher.init(Cipher.DECRYPT_MODE, key);
    return (Serializable) ((SealedObject) data).getObject(aesCipher);
  }
}
