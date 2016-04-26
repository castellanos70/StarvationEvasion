package starvationevasion.server.io.formatters;


import starvationevasion.server.io.strategies.SecureStream;
import starvationevasion.server.model.Sendable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;

public class POJOFormat extends Format<Sendable, byte[]>
{
  public POJOFormat (SecureStream encryption)
  {
    super(encryption);
  }

  @Override
  public byte[] convert (Sendable data)
  {
    return new byte[0];
  }

  @Override
  public byte[] convertToBytes (byte[] data)
  {
    return new byte[0];
  }

  @Override
  public byte[] format (Sendable data) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException, IOException
  {
    Serializable serializable = data;

    if (getEncryption().isEncrypted())
    {
      serializable = getEncryption().encrypt(serializable);
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(serializable);
    oos.close();

    byte[] bytes = baos.toByteArray();
    baos.close();
    return bytes;
  }

  @Override
  public String toString ()
  {
    return "java";
  }
}
