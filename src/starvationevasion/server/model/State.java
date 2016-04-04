package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import com.oracle.javafx.jmx.json.JSONDocument;

public enum State implements Sendable
{
  LOGIN,
  BEGINNING {
    @Override
    public long getDuration ()
    {
      return 10*1000;
    }
  },
  DRAWING {
    @Override
    public long getDuration ()
    {
      return 10*1000;
      // return 5*60*1000;
    }
  },
  DRAFTING {
    @Override
    public long getDuration ()
    {
      return 10*1000;
      // return 2*60*1000
    }
  },
  VOTING {
    @Override
    public long getDuration ()
    {
      return 10*1000;
      // return 2*60*1000;
    }
  },
  WIN,
  LOSE,
  END,
  TRANSITION;

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument _json = JSONDocument.createObject();
    _json.setString("name", name());
    _json.setString("type", getType().name());
    _json.setNumber("duration", getDuration());
    return _json;
  }

  public long getDuration()
  {
    return 0;
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
