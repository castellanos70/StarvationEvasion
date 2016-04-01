package starvationevasion.server.io.strategies;


import starvationevasion.server.io.strategies.AbstractWriteStrategy;
import starvationevasion.server.model.Encryptable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SecureSocketWriteStrategy extends AbstractWriteStrategy<String> implements Encryptable
{
  private final String key;

  public SecureSocketWriteStrategy (Socket socket)
  {
    super(socket);
    key = "kd";
  }

  public SecureSocketWriteStrategy (Socket socket, DataOutputStream stream)
  {
    super(socket, stream);
    key = "kd";
  }


  @Override
  public void write (String s) throws IOException
  {
    getStream().writeUTF(s);
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