package starvationevasion.server.io;


import java.io.IOException;

public interface WriteStrategy
{
  void write (String s) throws IOException;
}
