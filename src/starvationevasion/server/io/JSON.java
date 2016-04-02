package starvationevasion.server.io;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

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

  /**
   * Convert class into json
   * @return JSON representation of class
   */
  JSONDocument toJSON ();

  /**
   * Convert String into JSON
   * @param doc object to be converted
   */
  void fromJSON (Object doc);
}
