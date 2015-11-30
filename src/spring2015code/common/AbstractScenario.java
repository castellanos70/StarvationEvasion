package spring2015code.common;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.sim.geography.CropZoneData;
import starvationevasion.sim.geography.TileManager;

public abstract class AbstractScenario
{
  public static final int START_YEAR = Constant.FIRST_YEAR;
  public static final int END_YEAR = Constant.LAST_YEAR;
  public static final int YEARS_OF_SIM = (END_YEAR - START_YEAR) + 1;
  

  protected double randomizationPercentage = 1.0; //Domain: [0.0, 1.0]
  protected double baseSeaLevelRisePerYear;
  
  protected int currentYear = START_YEAR;
  
  
  /** The cumulative total sea level rise from START_YEAR through the year 
   * corresponding to each element of seaLevelByYear. */
  protected double[] seaLevelByYear = new double[YEARS_OF_SIM];
  
  protected TileManager[] idealCropZone = new TileManager[EnumFood.SIZE];
  
  protected AbstractAltitudeData altitudeData;
  protected AbstractClimateData climateData;
  
  
  public abstract CropZoneData.EnumCropZone classifyZone(EnumFood crop,
      double minTemp, double maxTemp, double dayTemp, double nightTemp, double rain);


  
  public int indexToYear(int i)
  {
    if (i<0 || i>=YEARS_OF_SIM) 
    { throw new IllegalArgumentException("spring2015code.common.indexToYear("+i+")");
    }
    return i+START_YEAR;
  }
  
  
  public int yearToIndex(int year)
  {
    if (year<START_YEAR || year>END_YEAR) 
    { throw new IllegalArgumentException("spring2015code.common.yearToIndex("+year+")");
    }
    return year-START_YEAR;
  }
}
