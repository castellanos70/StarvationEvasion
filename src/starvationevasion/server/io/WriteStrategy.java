package starvationevasion.server.io;


import starvationevasion.server.model.Sendable;

import java.io.DataOutputStream;
import java.io.IOException;

public interface WriteStrategy
{
  <T extends Sendable> void write (T s) throws IOException;

  void close () throws IOException;

  DataOutputStream getStream ();

  void setStream (DataOutputStream outStream);
}
