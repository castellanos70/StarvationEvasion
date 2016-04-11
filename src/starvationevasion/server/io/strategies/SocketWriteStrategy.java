package starvationevasion.server.io.strategies;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.Constant;
import starvationevasion.server.model.Sendable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;

public class SocketWriteStrategy extends AbstractWriteStrategy
{

  public SocketWriteStrategy (Socket socket)
  {
    super(socket);
  }

  public SocketWriteStrategy (Socket socket, DataOutputStream stream)
  {
    super(socket, stream);
  }

  @Override
  public void write (Sendable s) throws IOException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException
  {
    String _jsonString = s.toJSON().toJSON();

    if (isEncrypted())
    {
      _jsonString = encrypt(_jsonString);
    }
    // Here I am going to assume if a client is using a standard socket they want JSON
    _jsonString += Constant.TERMINATION;
    getStream().write(_jsonString.getBytes());
    getStream().flush();
  }

}
