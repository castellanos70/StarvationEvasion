package starvationevasion.server.io.strategies;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.model.Sendable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;

public class JavaObjectWriteStrategy extends AbstractWriteStrategy
{

  public JavaObjectWriteStrategy (Socket socket)
  {
    super(socket);
  }

  public JavaObjectWriteStrategy (Socket socket, DataOutputStream stream)
  {
    super(socket, stream);
  }

  @Override
  public void write (Sendable s) throws IOException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException
  {
    Serializable serializable = s;

    if (isEncrypted())
    {
      serializable = encrypt(s);
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(serializable);
    oos.close();

    byte[] bytes = baos.toByteArray();
    getStream().writeInt(bytes.length);
    getStream().write(bytes);
    getStream().flush();
  }

}
