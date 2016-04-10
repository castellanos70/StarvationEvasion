package starvationevasion.server.io.strategies;


import starvationevasion.server.io.NotImplementedException;
import starvationevasion.server.model.Encryptable;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;


abstract class SecureStream implements Encryptable
{
  private boolean isEncrypted = false;
  private SecretKey key;
  private Cipher des;

  public SecureStream setEncrypted (boolean encrypted, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException
  {
    this.key = key;
    isEncrypted = encrypted;
    if (isEncrypted)
    {
      des = Cipher.getInstance("DES");
    }
    return this;
  }

  public SecureStream setEncrypted (boolean encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException
  {
    return setEncrypted(encrypted, null);
  }

  public boolean isEncrypted ()
  {
    return isEncrypted;
  }

  @Override
  public void encrypt (String msg, String key)
  {
    throw new NotImplementedException();
  }

  @Override
  public <T> T decrypt (String msg, String key)
  {
    return null;
  }
}
