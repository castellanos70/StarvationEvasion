package starvationevasion.client.model;


import starvationevasion.common.RegionData;
import starvationevasion.common.WorldData;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;

public class Statistics
{
  private WorldData worldData;


  public WorldData getWorldData()
  {
    return worldData;
  }

  public void setWorldData(WorldData worldData)
  {
    this.worldData = worldData;
  }


  public RegionData getRegionByPlayer(Player player)
  {
    throw new NotImplementedException();
//    return Arrays.asList(worldData.regionData).stream().filter(regionData -> {
//      return regionData.region == player.getRegion();
//    }).findFirst().get();
  }
}
