package starvationevasion.server.io.strategies;


import starvationevasion.server.io.strategies.AbstractReadStrategy;
import starvationevasion.server.model.Encryptable;

import java.io.*;
import java.net.Socket;

public class SecureSocketReadStrategy extends AbstractReadStrategy<String> implements Encryptable
{
  private String key;

  public SecureSocketReadStrategy (Socket socket)
  {
    super(socket);
  }

  public SecureSocketReadStrategy (Socket socket, DataInputStream stream)
  {
    super(socket, stream);
  }


  @Override
  public String read () throws IOException
  {
    return getStream().readUTF();
  }

  @Override
  public void encrypt (String msg, String key)
  {

  }

  @Override
  public <T> T decrypt (String msg, String key)
  {
    return null;
  }
}