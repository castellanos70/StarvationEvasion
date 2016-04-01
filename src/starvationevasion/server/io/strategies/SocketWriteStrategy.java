package starvationevasion.server.io.strategies;



import starvationevasion.server.model.Sendable;

import java.io.*;
import java.net.Socket;

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
  public void write (Sendable s) throws IOException
  {
    // Here I am going to assume if a client is using a standard socket they want JSON
    byte[] rawData = s.toJSON().toString().getBytes();

    getStream().write(rawData);
    getStream().flush();
  }

}
