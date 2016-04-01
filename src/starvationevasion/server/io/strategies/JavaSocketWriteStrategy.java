package starvationevasion.server.io.strategies;


import starvationevasion.server.model.Sendable;

import java.io.*;
import java.net.Socket;

public class JavaSocketWriteStrategy extends AbstractWriteStrategy
{

  public JavaSocketWriteStrategy (Socket socket)
  {
    super(socket);
  }

  public JavaSocketWriteStrategy (Socket socket, DataOutputStream stream)
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
