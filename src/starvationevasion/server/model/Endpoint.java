package starvationevasion.server.model;


public enum Endpoint
{
  // UserHandler
  USER_CREATE("user_create"),
  USER_READ("user_read"),
  USER_UPDATE("user_update"),
  USERS("users"),
  USERS_LOGGED_IN("users_logged_in"),
  USERS_READY("users_ready"),

  // LoginHandler
  LOGIN("login"),

  // CardHandler
  HAND_READ("hand_read"),
  HAND_UPDATE("hand_update"),

  // VoteHandler
  VOTE_UP("vote_up"),
  VOTE_DOWN("vote_down"),

  // CardHandler
  DRAFT_CARD("draft_card"),
  DRAW_CARD("draw_card"),
  DELETE_CARD("delete_card"),

  //draft can you draft, have you drafted you can onyl draft one voting card.

  // ChatHandler
  CHAT("chat"),
  CARD("card"),
  
  GAME_STATE("game_state"),

  // after ready is sent you cannot change regions
  READY("ready"),
  KILL("kill");

  private String url;

  Endpoint(String url)
  {
    this.url = url;
  }

  public String getUrl ()
  {
    return url;
  }
}
