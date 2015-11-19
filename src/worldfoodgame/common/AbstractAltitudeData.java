package worldfoodgame.common;


/**
 * The AbstractAltitudeData class should be extended by a class that
 * somehow represents a country's boarder.
 */
public abstract class AbstractAltitudeData
{
  /**
   * Gets the altitude (meters above 2014 sea level) at the specified location.
   * The resolution of the returned value must be at least 100 square 
   * kilometers.
   * 
   * When this method is used in years of the model after the starting year,
   * the caller of this method will need to subtract the rise in sea level from
   * 2014 to the desired year to find the altitude in the desired year.
   * 
   * The development team may implement this method to:
   * <ol>
   *   <li> return the same value for all locations within each 100 square kilometer area,</li>
   *   <li> use higher resolution data to return higher resolution results, or</li> 
   *   <li> use interpolation to return intermediate (smoother looking) results.</li> 
   *</ol>
   * @param latitude [-90.0 to 90.0], South latitudes are < 0.
   * @param longitude [-180.0 to 180.0], West longitudes are < 0.
   * @return specified location's meters above 2014 sea level.
   */
  public abstract float getAltitude(float latitude, float longitude);
  
}
