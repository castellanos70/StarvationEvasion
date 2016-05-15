package starvationevasion.common;


import java.awt.*;
import java.util.logging.Level;

/**
 * This class contains all constant values used throughout the Starvation Evasion game.
 */
public class Constant
{
  public static final int SERVER_PORT = 5555;
  public static final String[] AI_NAMES =
          {"Emma", "Olivia", "Noah", "Sophia", "Liam", "Mason", "Isabella", "Jacob", "William", "Ethan"};

  public static final String[] ANON_NAME_ARRAY = {
          "Ashley", " Wasinger",
          "Shizue", " Dodd",
          "Monroe", " Strauch",
          "Arline", " Downard",
          "Lenora", " Ponds",
          "Wilber", " Moreles",
          "Mariah", " Fretz",
          "Shila", " Causby",
          "Brett", " Friedrichs",
          "Tamera", " Colman",
          "Margareta", " Haygood",
          "Joanie", " Dearmond",
          "Sherrill", " Reuter",
          "Ashlea", " Esch",
          "Adena", " Booker",
          "Travis", " Ruffo",
          "Lita", " Meltzer",
          "Jonie", " Landgraf",
          "Jeffie", " Wasserman",
          "Kellie", " Dieguez"};

  public static final String DATA_ALGORITHM = "AES";

  public static final String ASYM_ALGORITHM = "RSA";

  public static final String TERMINATION = "\n";

  public static final Level LOG_LEVEL = Level.INFO;



  /**
   * Month names in calendar order where:<br>
   * .ordinal() is the month number -1,<br>
   * .name() is the three letter abbreviation of the month, <br>
   * .full() returns the full name of the month, and <br>
   * .days() returns the number of days in the month as used by the simulator (in the
   * simulator, including leap years, February has 28 days).
   */
  public enum Month
  { JAN {public String full() {return "January";}   public int days() {return 31;}},
    FEB {public String full() {return "February";}  public int days() {return 28;}},
    MAR {public String full() {return "March";}     public int days() {return 31;}},
    APR {public String full() {return "April";}     public int days() {return 30;}},
    MAY {public String full() {return "May";}       public int days() {return 31;}},
    JUN {public String full() {return "June";}      public int days() {return 30;}},
    JLY {public String full() {return "July";}      public int days() {return 31;}},
    AUG {public String full() {return "August";}    public int days() {return 31;}},
    SEP {public String full() {return "September";} public int days() {return 30;}},
    OCT {public String full() {return "October";}   public int days() {return 31;}},
    NOV {public String full() {return "November";}  public int days() {return 30;}},
    DEC {public String full() {return "December";}  public int days() {return 31;}};
    public static int SIZE = values().length;
    public abstract String full();
    public abstract int days();
  }

  /**
   * Server DB
   */
  public static final String DB_LOCATION = "jdbc:sqlite:data/test_db.db";

  /**
   * Each game turn advances the simulator by YEARS_PER_TURN years.
   */
  public static final int YEARS_PER_TURN = 2;

  /**
   * This is the first year for which simulation data has been gathered.
   */
  public static final int FIRST_DATA_YEAR = 2000;


  /**
   * This is the year for of the first turn in the simulation.
   * Note: (LAST_YEAR - FIRST_GAME_YEAR) % YEARS_PER_TURN must equal zero.
   */
  public static final int FIRST_GAME_YEAR = 2010;

  /**
   * This is the last year for which simulation data projections are approximated.
   */
  public static final int LAST_YEAR = 2049;


  /**
   * This is the total number of game turns.
   */
  public static final int GAME_TOTAL_TURNS = (LAST_YEAR - FIRST_GAME_YEAR) % YEARS_PER_TURN;


  /**
   * Maximum number of cards that a player may have in his, her or its hand.
   */
  public static final int MAX_HAND_SIZE = 7;

  //EnumFood.SIZE is what should be used for TOTAL_AGRO_CATEGORIES
  //public static final int TOTAL_AGRO_CATEGORIES = 12;

  /**
   * The circumference of the earth in kilometers.
    */

  public static final double EARTH_CIRCUMFERENCE = 40075;

  public static final long DRAFTING_TIME = 2*60*1000;

  public static final long DRAWING_TIME = 5*1000;

  public static final long VOTING_TIME = 3*60*1000;
  
  /**
   * This value represents the starting amount of ActionPoints for US_REGIONS.
   * At the beginning of each turn, the region's number of ActionPoints should
   * be reset to this default value.
   */
  public static final int ACTION_POINTS = 3;



  public static final Color COLOR_KHAKI = new Color(195, 176, 145);
  public static final Color COLOR_BUFF = new Color(240, 220, 130);
  public static final Color COLOR_AMBER = new Color(255, 191, 0);
  public static final Color COLOR_MOSS = new Color(138, 154, 91);
  public static final Color COLOR_FOREST = new Color(34, 139, 34);


  public static final Color[] COLOR_MOISTURE_LIST =
  { COLOR_KHAKI, new Color(221, 198, 135), COLOR_BUFF, new Color(242, 213, 97),
    COLOR_AMBER, new Color(224,188,8), new Color(191,171,50), new Color(159,161,74),
    COLOR_MOSS, new Color(119,151,81), new Color(91,147,65), new Color(62,143,50),
    COLOR_FOREST, new Color(28,132,28), new Color(12,114,12), new Color(0,100,0),
    new Color(19, 90, 39), new Color(86, 53, 173), new Color(105, 43, 212)
  };
}
