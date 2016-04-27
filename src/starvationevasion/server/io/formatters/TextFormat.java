package starvationevasion.server.io.formatters;


import starvationevasion.server.io.strategies.SecureStream;
import starvationevasion.server.model.Sendable;

public class TextFormat extends Format<Sendable, String>
{
  public TextFormat (SecureStream encryption)
  {
    super(encryption);
  }

  @Override
  public String convert (Sendable data)
  {
    return data.toJSON().toJSON() + "\r\n";
  }

  @Override
  public byte[] convertToBytes (String data)
  {
    return data.getBytes();
  }
}
