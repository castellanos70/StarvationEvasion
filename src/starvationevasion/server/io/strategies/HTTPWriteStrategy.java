package starvationevasion.server.io.strategies;


import starvationevasion.common.Util;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.Sendable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
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
  public void write (Response s) throws IOException,
                                        BadPaddingException,
                                        InvalidKeyException,
                                        IllegalBlockSizeException
  {

    // _sb.append("Cache-Control: public, no-cache=\"Authorization\"\r\n");
    // _sb.append("Content-Length:").append(_sb.toString().length()).append("\r\n");

    // set HTTP headers
    StringBuilder _sb = new StringBuilder();
    _sb.append(s.getType().getHeaderString()).append("\r\n");
    _sb.append("Date: ").append(Util.getServerTime()).append("\r\n");
    _sb.append("Content-Type: ").append(this.toString()).append("\r\n");



    _sb.append("\r\n");
    _sb.append(getFormatter().convertWithInjection(s, s.toJSON().toJSON()));

    getStream().write(_sb.toString().getBytes());
    getStream().flush();
    // fileIn.close();
  }
}
