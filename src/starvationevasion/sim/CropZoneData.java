package starvationevasion.sim;


import starvationevasion.common.EnumFood;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;

public class CropZoneData
{
  private final static Map<EnumFood, CropZoneData> map = new HashMap<>();
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
  public int    fertilizerNPerKM2;
  public int    fertilizerP2O5PerKM2;
  public int    fertilizerK2OPerKM2;
  public int    daysGrownPerYear;

  public double annualMaximumTemperature; // in degrees Celsius.
  public double annualMinimumTemperature; // in degrees Celsius.
  public double averageAnnualDayTemperature; // in degrees Celsius.
  public double averageAnnualNightTemperature; // in degrees Celsius.
  public double annualPrecipitation; //in centimeters of rainfall.
  public double idealLowTemperature;
  public double idealHighTemperature;

  public final EnumFood food;
  public /*final*/ Integer maxTemp; // use Integer because need to be able to assign null
  public /*final*/ Integer minTemp;
  public /*final*/ Integer dayTemp;
  public /*final*/ Integer nightTemp;
  public /*final*/ Integer maxRain;
  public /*final*/ Integer minRain;

  public CropZoneData(EnumFood food)
  {
    this.food = food;
  }

  public static CropZoneData mapFood(EnumFood food)
  {
    CropZoneData data = map.get(food);
    //if (data == null)
    //{
    //  data = CropClimateData.getDefault(food);
    //  map.put(food, data);
    //}
    return data;
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

  public void setFertilizerNPerKM2(int fertilizerNPerKM2)
  {
    this.fertilizerNPerKM2 = fertilizerNPerKM2;
  }

  public void setFertilizerK2OPerKM2(int fertilizerK2OPerKM2)
  {
    this.fertilizerK2OPerKM2 = fertilizerK2OPerKM2;
  }

  public void setFertilizerP2O5PerKM2(int fertilizerP2O5PerKM2)
  {
    this.fertilizerP2O5PerKM2 = fertilizerP2O5PerKM2;
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
