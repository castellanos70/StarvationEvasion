package starvationevasion.server.io.formatters;


import starvationevasion.server.io.strategies.SecureStream;
import starvationevasion.server.model.Sendable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.InvalidKeyException;

public abstract class Format<I extends Sendable, O>
{
  private SecureStream encryption;

  public Format(SecureStream encryption)
  {
    this.encryption = encryption;
  }

  public abstract O convert (I data) throws IOException;

  public abstract byte[] convertToBytes (O data);

  public byte[] format (I data) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException, IOException
  {

    byte[] _data = convertToBytes(convert(data));
    byte[] nl = "\n".getBytes();
    String eData;

    if (encryption.isEncrypted())
    {
      _data = encryption.encrypt(_data);
      eData = DatatypeConverter.printBase64Binary(_data);
    }
    else
    {
      eData = new String(_data);
    }

    eData += "\n";


    return eData.getBytes();

  }

  public SecureStream getEncryption ()
  {
    return encryption;
  }

  public O convertWithInjection(I data, String i) throws IOException
  {
    return convert(data);
  }
}
