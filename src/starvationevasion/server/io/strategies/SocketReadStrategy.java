package starvationevasion.server.io.strategies;


import starvationevasion.server.io.strategies.AbstractReadStrategy;

import java.io.*;
import java.net.Socket;

public class SocketReadStrategy extends AbstractReadStrategy<String>
{


  public SocketReadStrategy (Socket socket)
  {
    super(socket);
  }

  public SocketReadStrategy (Socket socket, DataInputStream stream)
  {
    super(socket, stream);
  }

  @Override
  public String read () throws IOException
  {
    int i  = getStream().read();
    StringBuilder _sb = new StringBuilder();
    while(true)
    {
      if (i == -1 || i == 10)
      {
        break;
      }
      _sb.append((char) i);
      i = getStream().read();
    }
    return _sb.toString();
  }

  @Override
  public void close () throws IOException
  {
    // socket.shutdownInput();
  }



}
