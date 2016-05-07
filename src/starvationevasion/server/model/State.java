package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.common.Constant;

/**
 * Class that is used by the server set state of game.
 */
public enum State implements Sendable
{
  LOGIN,
  BEGINNING {
    @Override
    public long getDuration ()
    {
      return 3*1000;
    }
  },
  DRAWING {
    @Override
    public long getDuration ()
    {
      return Constant.DRAWING_TIME;
    }
  },
  DRAFTING {
    @Override
    public long getDuration ()
    {
      return Constant.DRAFTING_TIME;
    }
  },
  VOTING {
    @Override
    public long getDuration ()
    {
      return Constant.VOTING_TIME;
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

  /**
   * Get the duration of the current State
   * @return long number in milliseconds
   */
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
