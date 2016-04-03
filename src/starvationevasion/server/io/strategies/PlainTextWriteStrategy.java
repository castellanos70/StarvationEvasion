package starvationevasion.server.io.strategies;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.model.Sendable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class PlainTextWriteStrategy extends AbstractWriteStrategy
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
  public void write (Sendable s) throws IOException
  {
    //
    writer.println(s.toString());
    writer.flush();
  }

  @Override
  public void close () throws IOException
  {
    writer.close();
  }
}
