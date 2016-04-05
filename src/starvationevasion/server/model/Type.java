package starvationevasion.server.model;


import com.oracle.javafx.jmx.json.JSONDocument;

public enum Type implements Sendable
{
  CHAT,

  AUTH,

  GAME,

  USER_DATA,

  USER,

  FOOD,

  BROADCAST,

  POLICY,

  REGION,

  SPECIAL_EVENT,

  POLICY_CARD,

  REGION_DATA,

  WORLD_DATA,

  AREA,
  TIME,
  GAME_STATE,
  AVAILABLE_REGIONS, USER_HAND, WORLD_DATA_LIST, DRAFTED, DRAFTED_INTO_VOTE, VOTE_BALLOT;


  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument _json = JSONDocument.createObject();
    _json.setString("name", name());
    _json.setString("type", "TYPE");
    return _json;
  }

  @Override
  public void fromJSON (Object doc)
  {

  }

  @Override
  public Type getType ()
  {
    return this;
  }
}
