package starvationevasion.model;

import starvationevasion.common.EnumFood;

public class CropClimateData
{
  public final String name;
  public final Integer maxTemp; // use Integer because need to be able to assign null
  public final Integer minTemp;
  public final Integer dayTemp;
  public final Integer nightTemp;
  public final Integer maxRain;
  public final Integer minRain;
  
  CropClimateData(String name, int maxTemp, int minTemp, int dayTemp, int nightTemp, int maxRain, int minRain)
  {
    this.name = name;
    this.maxTemp = maxTemp;
    this.minTemp = minTemp;
    this.dayTemp = dayTemp;
    this.nightTemp = nightTemp;
    this.maxRain = maxRain;
    this.minRain = minRain;
  }
  
  CropClimateData(String name)
  {
    this.name = name;
    this.maxTemp = null;
    this.minTemp = null;
    this.dayTemp = null;
    this.nightTemp = null;
    this.maxRain = null;
    this.minRain = null;
  }
  
  public String toString()
  {
    return name;
  }
  
  // TODO : Read the data from a file and map it.  This method is just a hook
  // to facilitate porting the Phase 2 code.
  //
  public static CropClimateData mapFood(EnumFood food)
  {
    return null;
  }
}
