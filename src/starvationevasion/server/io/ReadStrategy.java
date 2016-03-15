package starvationevasion.server.io;


import java.io.IOException;

public interface ReadStrategy<Result>
{
  Result read () throws IOException, Exception;
  void close () throws IOException;
}
