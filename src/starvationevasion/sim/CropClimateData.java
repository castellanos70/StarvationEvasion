package starvationevasion.sim;

import starvationevasion.common.EnumFood;

import java.util.HashMap;
import java.util.Map;

public class CropClimateData
{
  private final static Map<EnumFood, CropClimateData> map = new HashMap<>();
  public final EnumFood food;
  public final Integer maxTemp; // use Integer because need to be able to assign null
  public final Integer minTemp;
  public final Integer dayTemp;
  public final Integer nightTemp;
  public final Integer maxRain;
  public final Integer minRain;
  
  CropClimateData(EnumFood food, int maxTemp, int minTemp, int dayTemp, int nightTemp, int maxRain, int minRain)
  {
    this.food = food;
    this.maxTemp = maxTemp;
    this.minTemp = minTemp;
    this.dayTemp = dayTemp;
    this.nightTemp = nightTemp;
    this.maxRain = maxRain;
    this.minRain = minRain;
  }
  
  public String toString()
  {
    return food.name();
  }
  
  // TODO : Read the data from a file and map it.  This method is just a hook
  // to facilitate porting the Phase 2 code before we have data.
  //
  public static CropClimateData mapFood(EnumFood food)
  {
    CropClimateData data = map.get(food);
    //if (data == null)
    //{
    //  data = CropClimateData.getDefault(food);
    //  map.put(food, data);
    //}

    return data;
  }
}
