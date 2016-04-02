package starvationevasion.common;


import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.model.Sendable;

/**
 * Defines types of possible special events.
 */
public enum EnumSpecialEvent implements Sendable
{
  HURRICANE, DROUGHT;

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument _json = JSONDocument.createObject();
    _json.setString("name", name());
    return _json;
  }

  @Override
  public void fromJSON (Object doc)
  {

  }

  @Override
  public void setType (String type)
  {

  }

  @Override
  public String getType ()
  {
    return "EnumSpecialEvent";
  }

}
