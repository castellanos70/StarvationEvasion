package starvationevasion.server.io.strategies;


import starvationevasion.common.Util;
import starvationevasion.server.model.Sendable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;

public class HTTPWriteStrategy extends AbstractWriteStrategy
{

  public HTTPWriteStrategy (Socket socket)
  {
    super(socket);
  }

  public HTTPWriteStrategy (Socket socket, DataOutputStream stream)
  {
    super(socket, stream);
  }

  @Override
  public void write (Sendable s) throws IOException,
                                        BadPaddingException,
                                        InvalidKeyException,
                                        IllegalBlockSizeException
  {
    String _jsonString = s.toJSON().toJSON();
    StringBuilder _sb = new StringBuilder();

    _sb.append("HTTP/1.0 501 Not Implemented\r\n");
    _sb.append("Date: ").append(Util.getServerTime()).append("\r\n");
    _sb.append("Content-Type: application/json\r\n");
    _sb.append("\r\n");

    _sb.append(_jsonString);
    getStream().write(_sb.toString().getBytes());
    getStream().flush();
  }
}
