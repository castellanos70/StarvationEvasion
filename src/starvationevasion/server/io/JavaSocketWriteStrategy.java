package starvationevasion.server.io;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class JavaSocketWriteStrategy implements WriteStrategy
{
  private final Socket socket;
  private ObjectOutputStream writer;

  public JavaSocketWriteStrategy (Socket socket)
  {
    this.socket = socket;
    try
    {
      writer = new ObjectOutputStream(socket.getOutputStream());
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void write (String s) throws IOException
  {
    writer.writeObject(s);
    writer.flush();
  }

  @Override
  public void close () throws IOException
  {

  }
}
