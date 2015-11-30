package starvationevasion.server;

/**
 * Shea Polansky
 * Various server constants
 */
public class ServerConstants
{
  public static final int DEFAULT_PORT = 27015;
  public static final String SERVER_VERSION = "M1";
  public static final long GAME_START_WAIT_TIME = 10*1000; //milliseconds
  public static final long DRAFTING_PHASE_TIME = 5*60*1000; //milliseconds
  public static final long VOTING_PHASE_TIME = 2*60*1000; //milliseconds
  public static final int ACTIONS_PER_DRAFT_PHASE = 2;
}
