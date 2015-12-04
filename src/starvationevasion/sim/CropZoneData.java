package starvationevasion.sim;


import starvationevasion.common.EnumFood;

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

  public int    pricePerMetricTon;
  public int    tonsPerKM2;
  public int    litersPerKG;
  public int    fertilizerPerKM2;
  public int    pesticide;
  public int    daysGrownPerYear;

  public double annualMaximumTemperature; // in degrees Celsius.
  public double annualMinimumTemperature; // in degrees Celsius.
  public double averageAnnualDayTemperature; // in degrees Celsius.
  public double averageAnnualNightTemperature; // in degrees Celsius.
  public double annualPrecipitation; //in centimeters of rainfall.
  public double idealLowTemperature;
  public double idealHighTemperature;

  public final EnumFood food;
  //public final Integer maxTemp; // use Integer because need to be able to assign null
  //public final Integer minTemp;
  public /*final*/ Integer dayTemp;
  public /*final*/ Integer nightTemp;
  public /*final*/ Integer maxRain;
  public /*final*/ Integer minRain;

  public CropZoneData(EnumFood food)
  {
    this.food = food;
  }

  public void setPricePerMetricTon(int pricePerMetricTon)
  {
    this.pricePerMetricTon = pricePerMetricTon;
  }

  public void setTonsPerKM2(int tonsPerKM2)
  {
    this.tonsPerKM2 = tonsPerKM2;
  }

  public void setLitersPerKG(int litersPerKG)
  {
    this.litersPerKG = litersPerKG;
  }

  public void setFertilizerPerKM2(int fertilizerPerKM2)
  {
    this.fertilizerPerKM2 = fertilizerPerKM2;
  }

  public void setPesticide(int pesticide)
  {
    this.pesticide = pesticide;
  }

  public void setDaysGrownPerYear(int days)
  {
    this.daysGrownPerYear = days;
  }

  public void setAnnualMinimumTemperature(double annualMinimumTemperature)
  {
    this.annualMinimumTemperature = annualMinimumTemperature;
  }

  public void setAnnualMaximumTemperature(double annualMaximumTemperature)
  {
    this.annualMaximumTemperature = annualMaximumTemperature;
  }

  public void setIdealLowTemperature(double idealLowTemperature)
  {
    this.idealLowTemperature = idealLowTemperature;
  }

  public void setIdealHighTemperature(double idealHighTemperature)
  {
    this.idealHighTemperature = idealHighTemperature;
  }

}
