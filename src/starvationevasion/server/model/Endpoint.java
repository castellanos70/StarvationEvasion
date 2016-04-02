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
   * Required Payload: username, password, region
   */
  USER_CREATE("user_create"),

  /**
   * Get more information about a user
   * Handled by {@link starvationevasion.server.handlers.UserHandler}
   * Required Payload: username or region
   */
  USER_READ("user_read"),

  /**
   * Update current user "profile"
   * Handled by {@link starvationevasion.server.handlers.UserHandler}
   * Required Payload: username, password, region
   */
  USER_UPDATE("user_update"),

  /**
   * Get a list of ALL the users
   * Handled by {@link starvationevasion.server.handlers.UserHandler}
   * Required Payload: none
   */
  USERS("users"),

  /**
   * Get a list of all the logged in users
   * Handled by {@link starvationevasion.server.handlers.UserHandler}
   * Required Payload: none
   */
  USERS_LOGGED_IN("users_logged_in"),

  /**
   * Get a list of all the users ready to play the game
   * Handled by {@link starvationevasion.server.handlers.UserHandler}
   * Required Payload: none
   */
  USERS_READY("users_ready"),

  /**
   * Login to the server
   * Handled by {@link starvationevasion.server.handlers.LoginHandler}
   * Required Payload: username, password
   */
  LOGIN("login"),

  /**
   * Get current user hand
   * Handled by {@link starvationevasion.server.handlers.CardHandler}
   * Required Payload: none
   */
  HAND_READ("hand_read"),

  /**
   * Create current user hand
   * Handled by {@link starvationevasion.server.handlers.CardHandler}
   * Required Payload: none
   */
  HAND_CREATE("hand_create"),
  // HAND_UPDATE("hand_update"),

  /**
   * Vote up on a card
   * Handled by {@link starvationevasion.server.handlers.VoteHandler}
   * Required Payload: card
   */
  VOTE_UP("vote_up"),

  /**
   * Vote down on a card
   * Handled by {@link starvationevasion.server.handlers.VoteHandler}
   * Required Payload: card
   */
  VOTE_DOWN("vote_down"),

  /**
   * Draft a card
   * Handled by {@link starvationevasion.server.handlers.VoteHandler}
   * Required Payload: card
   */
  DRAFT_CARD("draft_card"),

  /**
   * Draw a card
   * Handled by {@link starvationevasion.server.handlers.VoteHandler}
   * Required Payload: none
   */
  DRAW_CARD("draw_card"),

  /**
   * Delete a card
   * Handled by {@link starvationevasion.server.handlers.VoteHandler}
   * Required Payload: card
   */
  DELETE_CARD("delete_card"),

  //draft can you draft, have you drafted you can only draft one voting card.

  /**
   * Send a chat, card or both
   * Handled by {@link starvationevasion.server.handlers.ChatHandler}
   * Required Payload: to, card, text
   */
  CHAT("chat"),
  // CARD("card"),

  /**
   * Get the game state
   * Handled by {@link starvationevasion.server.handlers.DataHandler}
   * Required Payload: none
   */
  GAME_STATE("game_state"),

  /**
   * Get the uptime of the server
   * Handled by {@link starvationevasion.server.handlers.DataHandler}
   * Required Payload: none
   */
  SERVER_UPTIME("server_uptime"),

  /**
   * Get a list of all the available regions
   * Handled by {@link starvationevasion.server.handlers.DataHandler}
   * Required Payload: none
   */
  AVAILABLE_REGIONS("available_regions"),


  // after ready is sent you cannot change regions

  /**
   * Tell the server that current user is ready to play game
   * Required Payload: none
   */
  READY("ready"),

  /**
   * Turn off the server
   * Handled by {@link starvationevasion.server.handlers.AdminTaskHandler}
   * Required Payload: none
   *
   * @apiNote Special permission required
   */
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
