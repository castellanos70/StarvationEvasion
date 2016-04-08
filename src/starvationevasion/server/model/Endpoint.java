package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 * All the endpoints to the api
 */


public enum Endpoint
{
  /**
   * Create a User
   * Handled by {@link starvationevasion.server.handlers.UserHandler}
   * Required Payload: username:string, password:string, region:EnumRegion
   */
  USER_CREATE("user_create"),

  /**
   * Get more information about a user
   * Handled by {@link starvationevasion.server.handlers.UserHandler}
   * Required Payload: username:string or region:EnumRegion
   */
  USER_READ("user_read"),

  /**
   * Update current user "profile"
   * Handled by {@link starvationevasion.server.handlers.UserHandler}
   * Required Payload: username:string, password:string, region:EnumRegion
   */
  USER_UPDATE("user_update"),

  /**
   * Get a list of ALL the users
   * Handled by {@link starvationevasion.server.handlers.UserHandler}
   * No Payload
   */
  USERS("users"),

  /**
   * Get a list of all the logged in users
   * Handled by {@link starvationevasion.server.handlers.UserHandler}
   * No Payload
   */
  USERS_LOGGED_IN("users_logged_in"),

  /**
   * Get a list of all the users ready to play the game
   * Handled by {@link starvationevasion.server.handlers.UserHandler}
   * No Payload
   */
  USERS_READY("users_ready"),

  /**
   * Login to the server
   * Handled by {@link starvationevasion.server.handlers.LoginHandler}
   * Required Payload: username:string, password:string
   */
  LOGIN("login"),

  /**
   * Get current user hand
   * Handled by {@link starvationevasion.server.handlers.CardHandler}
   * No Payload
   */
  HAND_READ("hand_read"),

  /**
   * Create current user hand
   * Handled by {@link starvationevasion.server.handlers.CardHandler}
   * No Payload
   */
  HAND_CREATE("hand_create"),
  // HAND_UPDATE("hand_update"),

  /**
   * Vote up on a card
   * Handled by {@link starvationevasion.server.handlers.VoteHandler}
   * Required Payload: card:PolicyCard
   */
  VOTE_UP("vote_up"),

  /**
   * Vote down on a card
   * Handled by {@link starvationevasion.server.handlers.VoteHandler}
   * Required Payload: card:PolicyCard
   */
  VOTE_DOWN("vote_down"),

  /**
   * Draft a card
   * Handled by {@link starvationevasion.server.handlers.VoteHandler}
   * Required Payload: card:PolicyCard
   */
  DRAFT_CARD("draft_card"),

  /**
   * Draw a card
   * Handled by {@link starvationevasion.server.handlers.VoteHandler}
   * No Payload
   */
  DRAW_CARD("draw_card"),

  /**
   * Delete a card
   * Handled by {@link starvationevasion.server.handlers.VoteHandler}
   * Required Payload: card:PolicyCard
   */
  DELETE_CARD("delete_card"),

  //draft can you draft, have you drafted you can only draft one voting card.

  /**
   * Send a chat, card or both
   * Handled by {@link starvationevasion.server.handlers.ChatHandler}
   * Required Payload: to-region:EnumRegion or to-username:string, card:EnumCard, text:string
   */
  CHAT("chat"),
  // CARD("card"),

  /**
   * Get the game state
   * Handled by {@link starvationevasion.server.handlers.DataHandler}
   * No Payload
   */
  GAME_STATE("game_state"),

  /**
   * Get the uptime of the server
   * Handled by {@link starvationevasion.server.handlers.DataHandler}
   * No Payload
   */
  SERVER_UPTIME("server_uptime"),

  /**
   * Get a list of all the available regions
   * Handled by {@link starvationevasion.server.handlers.DataHandler}
   * No Payload
   */
  AVAILABLE_REGIONS("available_regions"),


  // after ready is sent you cannot change regions

  /**
   * Tell the server that current user is ready to play game
   * No Payload
   */
  READY("ready"),

  /**
   * Turn off the server
   * Handled by {@link starvationevasion.server.handlers.AdminTaskHandler}<br><br>
   * No Payload<br>
   *
   * <em> Special permission required</em>
   */
  KILL("kill"),

  /**
   * Stop the current game
   * Handled by {@link starvationevasion.server.handlers.AdminTaskHandler}<br>
   * No Payload<br><br>
   *
   * <em> Special permission required</em>
   */
  STOP_GAME("stop_game"),

  /**
   * Restart the current game
   * Handled by {@link starvationevasion.server.handlers.AdminTaskHandler}<br>
   * No Payload<br><br>
   *
   * <em>Special permission required</em>
   */
  RESTART_GAME("restart_game"),

  /**
   * Get world data
   * Handled by {@link starvationevasion.server.handlers.UserHandler}
   * Required Payload: client-done:boolean, region-polygons:boolean, data-start:int, data-end:int
   */
  WORLD_DATA("world_data"),

  AI("ai"),

  KILL_AI("kill_ai"),

  TOTAL_PLAYERS("total_players");

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
