package starvationevasion.server.io.strategies;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.io.ReadStrategy;

import java.io.*;
import java.net.Socket;

public abstract class AbstractReadStrategy<T> implements ReadStrategy<T>
{
  private final Socket socket;
  private DataInputStream reader;

  public AbstractReadStrategy(Socket socket)
  {
    this.socket = socket;
    try
    {
      reader = new DataInputStream(socket.getInputStream());
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }

  }

  public AbstractReadStrategy(Socket socket, DataInputStream stream)
  {
    this.socket = socket;
    this.reader = stream;
  }

  @Override
  public DataInputStream getStream ()
  {
    return reader;
  }

  @Override
  public void setStream (DataInputStream inStream)
  {
    this.reader = inStream;
  }

  @Override
  public void close () throws IOException
  {
    getStream().close();
  }
}
