package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import com.oracle.javafx.jmx.json.JSONDocument;
import com.sun.org.apache.bcel.internal.classfile.ConstantDouble;
import starvationevasion.common.Constant;

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
