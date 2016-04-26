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
  public void write (Sendable s) throws IOException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException
  {
    String _jsonString = s.toJSON().toJSON();
    StringBuilder _sb = new StringBuilder();

    // Here I am going to assume if a client is using a standard socket they want JSON
    _sb.append("HTTP/1.0 200 OK\r\n");
    _sb.append("Date: ").append(Util.getServerTime()).append("\r\n");
    _sb.append("Content-Type: application/json\r\n");
    _sb.append("\r\n");

    _sb.append(_jsonString);
    getStream().write(_sb.toString().getBytes());
    getStream().flush();
  }
}
