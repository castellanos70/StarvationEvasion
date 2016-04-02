package starvationevasion.server.io;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import com.oracle.javafx.jmx.json.JSONDocument;

public interface Serializer<T extends JSONDocument, V extends String>
{
  T encode (V data);

  V decode (T data);
}
