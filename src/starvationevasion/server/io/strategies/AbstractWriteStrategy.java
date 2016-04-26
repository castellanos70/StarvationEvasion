package starvationevasion.server.io.strategies;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.io.WriteStrategy;
import starvationevasion.server.io.formatters.Format;
import starvationevasion.server.io.formatters.HTMLFormat;
import starvationevasion.server.io.formatters.JSONFormat;
import starvationevasion.server.io.formatters.POJOFormat;
import starvationevasion.server.model.DataType;
import starvationevasion.server.model.Sendable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class AbstractWriteStrategy extends SecureStream implements WriteStrategy
{

  private final Socket socket;
  private DataOutputStream writer;
  private Format<Sendable, ?> formatter = new JSONFormat(this);


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


  public Format<Sendable, ?> getFormatter ()
  {
    return formatter;
  }

  @Override
  public void setFormatter (DataType formatter)
  {
    if (formatter.equals(DataType.JSON))
    {
      this.formatter = new JSONFormat(this);
    }
    else if (formatter.equals(DataType.POJO))
    {
      this.formatter = new POJOFormat(this);
    }
    else if (formatter.equals(DataType.HTML))
    {
      this.formatter = new HTMLFormat(this);
    }
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
