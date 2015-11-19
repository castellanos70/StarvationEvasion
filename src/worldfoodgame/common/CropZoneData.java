package worldfoodgame.common;




public class CropZoneData
{
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
  
  public double annualMaximumTemperature; // in degrees Celsius.
  public double annualMinimumTemperature; // in degrees Celsius.
  public double averageAnnualDayTemperature; // in degrees Celsius.
  public double averageAnnualNightTemperature; // in degrees Celsius.
  public double annualPrecipitation; //in centimeters of rainfall.
}
