package starvationevasion.server.io;


import starvationevasion.server.model.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class JavaSocketReadStrategy implements ReadStrategy<Object>
{
  private final Socket socket;
  private ObjectInputStream stream;

  public JavaSocketReadStrategy (Socket socket)
  {
    this.socket = socket;
    // stream =  null;
    try
    {
      stream =  new ObjectInputStream(socket.getInputStream());

    }
    catch(IOException e)
    {
      e.printStackTrace();
    }

  }

  @Override
  public Object read () throws Exception
  {
    return stream.readObject();
  }

  @Override
  public void close () throws IOException
  {

  }
}
