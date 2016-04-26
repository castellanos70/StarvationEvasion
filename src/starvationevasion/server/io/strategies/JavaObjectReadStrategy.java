package starvationevasion.server.io.strategies;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.model.Request;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;

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
  public Request read () throws IOException,
                                ClassNotFoundException,
                                BadPaddingException,
                                InvalidKeyException,
                                IllegalBlockSizeException
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
    byte[] encObject = new byte[size];
    // read into buffer
    getStream().readFully(encObject);
    // convert into object
    ByteArrayInputStream in = new ByteArrayInputStream(encObject);
    ObjectInputStream is = new ObjectInputStream(in);
    is.close();

    Serializable s = (Serializable) is.readObject();
    in.close();

    Request request = null;
    if (isEncrypted())
    {
      request = (Request) decrypt(s);
    }
    else
    {
      request = (Request) s;
    }
    // we are only expecting Requests
    return request;
  }
}
