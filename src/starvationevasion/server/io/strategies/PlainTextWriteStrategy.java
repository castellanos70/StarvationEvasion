package starvationevasion.server.io.strategies;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class PlainTextWriteStrategy extends AbstractWriteStrategy<String>
{
  private PrintWriter writer;

  public PlainTextWriteStrategy (Socket socket)
  {
    super(socket);
  }

  public PlainTextWriteStrategy (Socket socket, DataOutputStream stream)
  {
    super(socket, null);
    try
    {
      writer = new PrintWriter(socket.getOutputStream(), true);
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void write (String s) throws IOException
  {
    writer.println(s);
    writer.flush();
  }

  @Override
  public void close () throws IOException
  {

  }
}
