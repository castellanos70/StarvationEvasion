package starvationevasion.server.io;


import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.impl.JSONStreamReaderImpl;

import java.io.StringReader;

public interface JSON
{
  final class Parser
  {

    public static JSONDocument toJSON(Object data)
    {
      if (data instanceof String)
      {
      StringReader stringReader = new StringReader((String) data);
      JSONStreamReaderImpl s = new JSONStreamReaderImpl(stringReader);
      return s.build();

      }
      else if (data instanceof JSONDocument)
      {
        return (JSONDocument) data;
      }

      return null;
    }
  }

  JSONDocument toJSON ();

  void fromJSON (Object doc);
}
