package starvationevasion.server.io.strategies;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.model.Encryptable;
import starvationevasion.server.model.Sendable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SecureSocketWriteStrategy extends AbstractWriteStrategy implements Encryptable
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
  public void write (Sendable s) throws IOException
  {
    getStream().writeUTF(s.toString());
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