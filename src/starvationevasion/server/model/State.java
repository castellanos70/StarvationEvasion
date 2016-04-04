package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import com.oracle.javafx.jmx.json.JSONDocument;

public enum State implements Sendable
{
  LOGIN, BEGINNING, DRAWING, DRAFTING, VOTING, WIN, LOSE, END;

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
  public Type getType ()
  {
    return Type.GAME;
  }

}
