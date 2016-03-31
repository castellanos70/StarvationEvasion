package starvationevasion.server.io.strategies;

import starvationevasion.server.io.strategies.AbstractReadStrategy;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;


public class WebSocketReadStrategy extends AbstractReadStrategy<String>
{


  public WebSocketReadStrategy (Socket socket)
  {
    super(socket);
  }

  public WebSocketReadStrategy (Socket socket, DataInputStream stream)
  {
    super(socket, stream);
  }

  @Override
  public String read () throws IOException
  {
    int len = 0;
    byte[] b = new byte[140];

    len = getStream().read(b);

    if (len == -1)
    {
      return null;
    }


    byte rLength = 0;
    int rMaskIndex = 2;
    int rDataStart = 0;
    //b[0] is always text in my case so no need to check;
    byte data = b[1];
    byte op = (byte) 127;
    rLength = (byte) (data & op);

    if (rLength == (byte) 126)
    {
      rMaskIndex = 4;
    }
    if (rLength == (byte) 127)
    {
      rMaskIndex = 10;
    }

    byte[] masks = new byte[4];

    int j = 0;
    int i = 0;
    for (i = rMaskIndex; i < (rMaskIndex + 4); i++)
    {
      masks[j] = b[i];
      j++;
    }

    rDataStart = rMaskIndex + 4;

    int messLen = len - rDataStart;

    byte[] message = new byte[messLen];

    for (i = rDataStart, j = 0; i < len; i++, j++)
    {
      message[j] = (byte) (b[i] ^ masks[j % 4]);
    }

    return new String(message);
  }

  @Override
  public void close () throws IOException
  {
    // socket.shutdownInput();
  }

}
