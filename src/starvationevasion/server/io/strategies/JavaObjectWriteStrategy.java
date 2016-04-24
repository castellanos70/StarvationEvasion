package starvationevasion.server.io.strategies;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.model.Response;
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
  public void write (Response s) throws IOException,
                                        BadPaddingException,
                                        InvalidKeyException,
                                        IllegalBlockSizeException
  {

    byte[] bytes = getFormatter().format(s);

    getStream().writeInt(bytes.length);
    getStream().write(bytes);
    getStream().flush();
  }

}
