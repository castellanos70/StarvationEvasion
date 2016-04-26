package starvationevasion.server.io.formatters;


import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.io.strategies.SecureStream;
import starvationevasion.server.model.Sendable;


public class JSONFormat extends Format<Sendable, JSONDocument>
{

  public JSONFormat (SecureStream encryption)
  {
    super(encryption);
  }

  @Override
  public JSONDocument convert (Sendable sendable)
  {
    return sendable.toJSON();
  }

  @Override
  public byte[] convertToBytes (JSONDocument data)
  {
    String _data = data.toJSON();

    return _data.getBytes();
  }

  @Override
  public String toString ()
  {
    return "application/json";
  }
}
