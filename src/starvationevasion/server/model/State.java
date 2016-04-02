package starvationevasion.server.model;


import com.oracle.javafx.jmx.json.JSONDocument;

public enum State implements Sendable
{
  LOGIN, BEGINNING, DRAWING, DRAFTING, VOTING, WIN, LOSE, END;

  @Override
  public JSONDocument toJSON ()
  {
    return null;
  }

  @Override
  public void fromJSON (Object doc)
  {

  }

  @Override
  public String getType ()
  {
    return null;
  }

  @Override
  public void setType (String type)
  {

  }
}
