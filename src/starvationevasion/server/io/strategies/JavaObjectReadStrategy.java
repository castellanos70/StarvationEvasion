package starvationevasion.server.io.strategies;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.model.Request;

import java.io.*;
import java.net.Socket;

public class JavaObjectReadStrategy extends AbstractReadStrategy<Request>
{

  public JavaObjectReadStrategy (Socket socket)
  {
    super(socket);
  }

  public JavaObjectReadStrategy (Socket socket, DataInputStream stream)
  {
    super(socket, stream);
  }

  @Override
  public Request read () throws IOException, ClassNotFoundException
  {
    // we are always expecting an integer that represents the size of the byte array
    int ch1 = getStream().read();
    int ch2 = getStream().read();
    int ch3 = getStream().read();
    int ch4 = getStream().read();

    // return null if something happened
    if ((ch1 | ch2 | ch3 | ch4) < 0)
    {
      return null;
    }

    // merge frames into a single int
    int size  = ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    // create buffer
    byte[] object = new byte[size];
    // read into buffer
    getStream().readFully(object);
    // convert into object
    ByteArrayInputStream in = new ByteArrayInputStream(object);
    ObjectInputStream is = new ObjectInputStream(in);
    // we are only expecting Requests
    return (Request) is.readObject();
  }
}
