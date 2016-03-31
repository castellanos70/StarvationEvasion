package starvationevasion.server.io;


import java.io.DataOutputStream;
import java.io.IOException;

public interface WriteStrategy<T>
{
  // <T extends Sendable> void write (T s) throws IOException;
  void write (T s) throws IOException;
  void close () throws IOException;

  DataOutputStream getStream ();

  void setStream (DataOutputStream outStream);
}
