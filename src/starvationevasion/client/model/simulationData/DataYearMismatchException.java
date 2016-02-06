/**
 * @author Mohammad R. Yousefi
 * Indicates that a given WoldData does is not for the year expected by the model.
 * @param expectedValue The value expected by the model.
 * @param givenValue The value of the WorldData year.
 * @see starvationevasion.common.WorldData
 */
package starvationevasion.client.model.simulationData;

public class DataYearMismatchException extends RuntimeException
{
  public final int expectedValue;
  public final int givenValue;

  /**
   * Constructor for the class.
   *
   * @param expectedValue The value expected by the model.
   * @param givenValue    The value of the WorldData year.
   */
  public DataYearMismatchException(int expectedValue, int givenValue)
  {
    super(String.format("The expected year %d does not match given year %d", expectedValue, givenValue));
    this.expectedValue = expectedValue;
    this.givenValue = givenValue;
  }
}
