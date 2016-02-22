package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.Util;

import java.util.Random;

/**
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
   * TODO: set the values of this array to the MSL above ZERO_YEAR in cm for each year through the most recent.
   */
  private static final int[] annualMeanSeaLevel = {0};

  /**
   * Last year of historical data.
   */
  public int lastYearOfHistoricalData = annualMeanSeaLevel.length - 1 + Constant.FIRST_YEAR;

  /**
   * fields a, b and c are used to store
   * the coefficients of the quadratic equation y = ax<sup>2</sup> + bx + c
   * which for x=year, gives y=Mean Annual Sea Level in cm above the ZERO_YEAR level.
   */
  private final double a, b, c, y0, y1, y2;
  private final int year0, year1, year2, x1, x2;



  public SeaLevel()
  {
    year0 = Constant.FIRST_YEAR;
    year1 = 2014;
    year2 = Constant.LAST_YEAR;

    x1 = year1 - year0;
    x2 = year2 - year0;

    y0 = Util.rand.nextGaussian() * 1.5;
    y1 = 29 + Util.rand.nextGaussian() * 3.5;
    y2 = 65 + Util.rand.nextGaussian() * 5;

    c = y0;
    a = getA();
    b = getB();
  }


  /**
   * @param year any year between Constant.FIRST_YEAR and Constant.LAST_PREDICTED_YEAR.
   * @return mean sea level in cm above/below (+/-) the mean sea level at ZERO_YEAR.
   */
  public double getSeaLevel(int year)
  {
    //if(year < Constant.FIRST_YEAR || year > Constant.LAST_YEAR) return 0;

    int x = year - year0;
    return a * x * x + b * x + c;
  }

  /**
   * y0 = c
   * y1 - c = ax1^2 + bx1
   *
   * y2 * x1 - c * x1 = a * x2 * x2 * x1 + y1 - a * x1 * x1 - c
   *
   * y2 * x1 - c * x1 - y1 + c = (x2 * x2 * x1 - x1 * x1) * a
   *
   * a = (y2 * x1 - c * x1 - y1 + c) / (x2 * x2 * x1 - x1 * x1)
   *
   * @return
   */
  private double getA() { return (y2 * x1 - c * x1 - y1 + c) / (x2 * x2 * x1 - x1 * x1); }

  /**
   * b = (y1 - a * x1 * x1 - c) / x1
   *
   * @return
   */
  private double getB() { return (y1 - a * x1 * x1 - c) / x1; }

  public void print()
  {
    System.out.println(year0 + " = " + y0 + ": " + getSeaLevel(year0));
    System.out.println(year1 + " = " + y1 + ": " + getSeaLevel(year1));
    System.out.println(year2 + " = " + y2 + ": " + getSeaLevel(year2));
    System.out.println("a = " + a + ", b = " + b + ", c = " + c);
  }

  public static void main(String[] args)
  {

    SeaLevel seaLevel = new SeaLevel();
     for (int i=Constant.FIRST_YEAR; i<=Constant.LAST_YEAR; i=i+5)
     {
       System.out.println("Sea Level in " + i + " = " + seaLevel.getSeaLevel(i));
     }
  }
}
