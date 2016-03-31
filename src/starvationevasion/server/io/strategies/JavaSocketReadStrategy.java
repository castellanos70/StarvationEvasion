package starvationevasion.server.io.strategies;


import java.io.*;
import java.net.Socket;

public class JavaSocketReadStrategy extends AbstractReadStrategy<Object>
{

  public JavaSocketReadStrategy (Socket socket)
  {
    super(socket);
  }

  public JavaSocketReadStrategy (Socket socket, DataInputStream stream)
  {
    super(socket, stream);
  }

  @Override
  public Object read () throws Exception
  {
    int ch1 = getStream().read();
    if (ch1 == -1)
    {
      return null;
    }

    int ch2 = getStream().read();
    int ch3 = getStream().read();
    int ch4 = getStream().read();
    if ((ch1 | ch2 | ch3 | ch4) < 0)
    {
      throw new EOFException();
    }
    int size  = ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));

    byte[] object = new byte[size];

    getStream().readFully(object);

    ByteArrayInputStream in = new ByteArrayInputStream(object);
    ObjectInputStream is = new ObjectInputStream(in);
    return is.readObject();
  }

  @Override
  public void close () throws IOException
  {

  }

}
