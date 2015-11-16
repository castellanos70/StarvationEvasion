package starvationevasion.sim;


/**
 * Created by Joel on 11/5/2015.<br>
 * In the Starvation Evasion game, sea level refers to the mean sea level (MSL),
 * averaged over many different times AND in many different locations during the
 * current year.<br><br>
 * The game Starvation Evasion starts in the year ZERO_YEAR=1980.
 * Let the MSL in ZERO_YEAR be zero. Then, in all years after ZERO_YEAR, the
 * MSL is the positive distance above the ZERO_YEAR MSL or the
 * negative distance below the ZERO_YEAR MSL.<br><br>
 *
 * This class uses historical measurements of the MSL from ZERO_YEAR through
 * LAST_HISTORICAL_DATA_YEAR.<br>
 * These constant values are stored in an array. When the class is queried for
 * the MSL for a year during that historical period, a simple table lookup is returned.<br>
 * Data Source: https://www.ipcc.ch/pdf/assessment-report/ar5/wg1/WG1AR5_Chapter13_FINAL.pdf
 * <br><br>
 *
 * For future values of MSL, For future years, projected values reported
 * by the Intergovernmental Panel on Climate Change (IPCC) are given
 * in ranges with increased variance for years more distant in the
 * future. At the start of each game the sim selects a random
 * 2050 sea level (=h2050), with a Gaussian distribution about the
 * IPCC projected mean. During that game, for all years after 2014,
 * the sim must report the sea level as the value given by
 * the quadratic equation passing through (h1980=0, 0), (h2014,29)
 * and (h2050,65).
 */
public class SeaLevel
{
  /**
   * First year of historical data
   */
  public static final int ZERO_YEAR = 1980;

  /**
   * Final future year this class will be used to estimate the MSL relitive to ZERO_YRAR.
   */
  public static final int LAST_PREDICTED_YEAR = 2050;

  /**
   * TODO: set the values of this array to the MSL above ZERO_YEAR in cm for each year through the most recent.
   */
  private static final int[] annualMeanSeaLevel = {0};

  /**
   * Last year of historical data.
   */
  public int lastYearOfHistoricalData = annualMeanSeaLevel.length - 1 + ZERO_YEAR;

  /** fields a, b and c are used to store
   * the coefficients of the quadratic equation y = ax<sup>2</sup> + bx + c
   * which for x=year, gives y=Mean Annual Sea Level in cm above the ZERO_YEAR level.
   */
  private final double a, b, c;


  /** TODO: implement
   *
   */
  public SeaLevel()
  {
    a = 0;
    b = 0;
    c = 0;
  }


  /** TODO: implement
   *
   */
  public double getSeaLevel(int year)
  {
    int x = year - ZERO_YEAR;
    double seaLevel = a*x*x + b*x + c;
    return seaLevel;
  }
}
