package starvationevasion.server.io.strategies;


import starvationevasion.server.io.WriteStrategy;
import starvationevasion.server.model.Sendable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class AbstractWriteStrategy implements WriteStrategy
{

  private final Socket socket;
  private DataOutputStream writer;

  public AbstractWriteStrategy(Socket socket)
  {
    this.socket = socket;
    try
    {
      writer = new DataOutputStream(socket.getOutputStream());
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }

  }

  public AbstractWriteStrategy(Socket socket, DataOutputStream stream)
  {
    this.socket = socket;
    this.writer = stream;
  }

  @Override
  public DataOutputStream getStream ()
  {
    return writer;
  }

  @Override
  public void setStream (DataOutputStream dataOutputStream)
  {
    writer = dataOutputStream;
  }

  @Override
  public void close () throws IOException
  {
    getStream().close();
  }
}
