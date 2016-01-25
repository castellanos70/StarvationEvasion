package starvationevasion.server.io;


import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.ActionType;

public interface Serializer<T extends JSONDocument, V>
{
  T encode (ActionType action, V data);

  V decode (T data);
}
