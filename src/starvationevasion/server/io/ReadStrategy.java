package starvationevasion.server.io;


import java.io.IOException;

public interface ReadStrategy
{
  String read () throws IOException;
}
