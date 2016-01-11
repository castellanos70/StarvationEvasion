/**
 * @author Mohammad R. Yousefi
 * Contains the data for the world.
 */
package starvationevasion.client.Rayquaza.model.simulationData;

import starvationevasion.common.*;

import java.util.*;

public class World
{
  final Map<EnumRegion, Region> regionData = new HashMap<>();
  final Map<EnumFood, Crop> cropData = new HashMap<>();
  private int year = Constant.FIRST_YEAR;
  private double humanDevelopmentIndex = Double.MIN_VALUE;
  private ArrayList<SpecialEventData> eventList;


  public World(boolean dummy)
  {
    System.out.println("Creating Dummy World.");
    for (EnumRegion region : EnumRegion.values())
    {
      regionData.put(region, new Region(region, true));
    }
    for (EnumFood food : EnumFood.values())
    {
      cropData.put(food, new Crop(food, true));
    }
  }

  /**
   * Default constructor for the class.
   */
  public World()
  {
    for (EnumRegion region : EnumRegion.values()) regionData.put(region, new Region(region));
    for (EnumFood food : EnumFood.values()) cropData.put(food, new Crop(food));
  }

  /**
   * Updates the world data for all regions and crops.
   *
   * @param worldData provided data.
   */
  public void update(WorldData worldData)
  {
    year = worldData.year;
    eventList = new ArrayList<>(worldData.eventList);
    regionData.forEach((region, regionData) -> regionData.update(worldData));
    humanDevelopmentIndex = calculateWorldHDI();
    cropData.forEach((crop, cropData) -> cropData.update(worldData));
  }

  /* Helper method for update to calculate the world HDI */
  private double calculateWorldHDI()
  {
    double hdi = 0;
    List<Map.Entry<EnumRegion, Region>> data = new LinkedList<>(regionData.entrySet());
    for (int i = 0; i < data.size(); i++)
    {
      hdi += data.get(i).getValue().getHumanDevelopmentIndex();
    }
    return hdi / data.size();
  }

  /**
   * Returns the region data for the requested region.
   *
   * @param crop The requested crop.
   * @return Requested crop data.
   */
  public Crop getCropData(EnumFood crop)
  {
    return cropData.get(crop);
  }

  /**
   * Returns the region data for the requested region.
   *
   * @param region The requested region.
   * @return Requested region data.
   */
  public Region getRegionData(EnumRegion region)
  {
    return regionData.get(region);
  }

  /**
   * Returns the current human development index.
   *
   * @return Calculated average of the current human development index or Double.MIN_VALUE if no data is available.
   */
  public double getHumanDevelopmentIndex()
  {
    return humanDevelopmentIndex;
  }

  /**
   * Returns the current year of the world.
   *
   * @return The current year of the world.
   */
  public int getYear()
  {
    return year;
  }
}