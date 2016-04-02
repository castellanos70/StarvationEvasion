package starvationevasion.server.io.strategies;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.model.Sendable;

import java.io.*;
import java.net.Socket;

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
  public void write (Sendable s) throws IOException
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(s);
    oos.close();

    byte[] bytes = baos.toByteArray();
    getStream().writeInt(bytes.length);
    getStream().write(bytes);
    getStream().flush();
  }

}
