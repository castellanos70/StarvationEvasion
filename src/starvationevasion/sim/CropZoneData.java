package starvationevasion.sim;


import starvationevasion.common.EnumFood;
import starvationevasion.io.CSVReader;

import java.util.logging.Logger;

public class CropZoneData
{
  private final static Logger LOGGER = Logger.getLogger(CropZoneData.class.getName());
  public enum EnumCropZone
  {
    IDEAL
    {
      public double productionRate() {return 1.0;}
    },
    
    ACCEPTABLE 
    {
      public double productionRate() {return 0.60;}
    },
    
    POOR  
    {
      public double productionRate() {return 0.25;}
    };
    
    public static final int SIZE = values().length;
    public abstract double productionRate();
  } 
  
  public static final double ACCEPTABLE_THRESHOLD = 0.30;
  
  private double annualMaximumTemperature; // in degrees Celsius.
  private double annualMinimumTemperature; // in degrees Celsius.
  private double averageAnnualDayTemperature; // in degrees Celsius.
  private double averageAnnualNightTemperature; // in degrees Celsius.
  private double annualPrecipitation; //in centimeters of rainfall.

}
