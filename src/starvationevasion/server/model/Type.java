package starvationevasion.server.model;

import com.oracle.javafx.jmx.json.JSONDocument;

public enum Type implements Sendable
{

  AUTH
  {
    @Override
    public String getHeaderString()
    {
      return "HTTP/1.1 401 Not Authorized\r\n" + "WWW-Authenticate: Basic realm=\"Starvation Evasion\"";
    }
  },

  ERROR
  {
    @Override
    public String getHeaderString()
    {
      return "HTTP/1.1 500 Internal Server Error";
    }
  },

  SUCCESS
  {
    @Override
    public String getHeaderString()
    {
      return "HTTP/1.1 200 OK";
    }
  },

  AUTH_ERROR
  {
    @Override
    public String getHeaderString()
    {
      return "HTTP/1.1 403 Forbidden";
    }
  },

  NOT_FOUND
  {
    @Override
    public String getHeaderString()
    {
      return "HTTP/1.1 404 Not Found";
    }
  },

  CHAT,

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

  AREA, TIME, GAME_STATE, AVAILABLE_REGIONS, USER_HAND, WORLD_DATA_LIST, DRAFTED, DRAFTED_INTO_VOTE, VOTE_BALLOT, USERS_READY_LIST, USERS_LOGGED_IN_LIST, VOTE_ERROR, VOTE_SUCCESS, AUTH_SUCCESS, CREATE_SUCCESS, CREATE_ERROR, USERS, VOTE_RESULTS;

  public String getHeaderString()
  {
    return SUCCESS.getHeaderString();
  }

  @Override
  public JSONDocument toJSON()
  {
    JSONDocument _json = JSONDocument.createObject();
    _json.setString("name", name());
    _json.setString("type", "TYPE");
    return _json;
  }

  @Override
  public void fromJSON(Object doc)
  {

  }

  @Override
  public Type getType()
  {
    return this;
  }
}
