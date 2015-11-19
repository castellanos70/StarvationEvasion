package worldfoodgame.common;



// TODO: Auto-generated Javadoc
/**
 * The AbstractClimateData class should be extended by a class that
 * holds the climate data used in Milestone II.
 * In particular, this includes for all arable land areas on a 100 square 
 * kilometer resolution:
 * <ol>
 *   <li>Annual maximum temperature.</li>
 *   <li>Annual minimum temperature.</li>
 *   <li>Average annual daytime temperature.</li>
 *   <li>Average annual nighttime temperature.</li>
 *   <li>annual precipitation</li>
 * </ol>
 * 
 * When extending this class, each development team may implement the getters 
 * as table lookups, map lookups, calculations based on lower resolution space
 * and time data or whatever works.<br><br>
 * 
 * While the extending class must implement the abstract methods, a development
 * team may also implement other methods that return the same data but with 
 * arguments that can access the data in the team's particular structures 
 * more efficiently. 
 * 
 */
public abstract class AbstractClimateData
{
  
  /**
   * Gets the maximum temperature in the specified location during
   * the specified year.
   *
   * @param latitude [-90.0 to 90.0], South latitudes are < 0.
   * @param longitude [-180.0 to 180.0], West longitudes are < 0.
   * @param year between AbstractScenario.START_YEAR and AbstractScenario.END_YEAR
   * @return temperature in degrees Celsius.
   */
  public abstract float getTemperatureMax(float latitude, float longitude, int year);
  
  /**
   * Gets the minimum temperature in the specified location during
   * the specified year.
   *
   * @param latitude [-90.0 to 90.0], South latitudes are < 0.
   * @param longitude [-180.0 to 180.0], West longitudes are < 0.
   * @param year between AbstractScenario.START_YEAR and AbstractScenario.END_YEAR
   * @return temperature in degrees Celsius.
   */
  public abstract float getTemperatureMin(float latitude, float longitude, int year);
  
  /**
   * Gets the average annual daytime temperature in the specified location during
   * the specified year.
   *
   * @param latitude [-90.0 to 90.0], South latitudes are < 0.
   * @param longitude [-180.0 to 180.0], West longitudes are < 0.
   * @param year between AbstractScenario.START_YEAR and AbstractScenario.END_YEAR
   * @return temperature in degrees Celsius.
   */
  public abstract float getTemperatureDay(float latitude, float longitude, int year);
  
  /**
   * Gets the average annual nighttime temperature in the specified location during
   * the specified year.
   *
   * @param latitude [-90.0 to 90.0], South latitudes are < 0.
   * @param longitude [-180.0 to 180.0], West longitudes are < 0.
   * @param year between AbstractScenario.START_YEAR and AbstractScenario.END_YEAR
   * @return temperature in degrees Celsius.
   */
  public abstract float getTemperatureNight(float latitude, float longitude, int year);
  
  /**
   * Gets the total precipitation in the specified location during
   * the specified year.
   *
   * @param latitude [-90.0 to 90.0], South latitudes are < 0.
   * @param longitude [-180.0 to 180.0], West longitudes are < 0.
   * @param year between AbstractScenario.START_YEAR and AbstractScenario.END_YEAR
   * @return rainfall in centimeters.
   */
  public abstract float getRainfall(float latitude, float longitude, int year);
}
