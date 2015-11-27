package starvationevasion.common;

import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

/**
 * Created by scnaegl on 11/14/15.
 */
public class Region
{
  /**
   * Keeps track of the originally selected Region.
   */
  public final EnumRegion ENUM_REGION;

  private double wealth;
      //wealth of the player and there country (a way of measuring score)
  private double happiness;
      // happiness of the people in the region (a way of measuring score)

  /**
   * A arraylist of the farm stats to keep track of them throughout the turns.
   */
  private HashMap<EnumFood, Stack<Double>>  cropValues = new HashMap<>();


  /**
   * Creates a new Region with defaults based upon an EnumRegion. This will
   * only be called at the start of the game.
   *
   * @param enumRegion Deterines starting wealth and crops.
   */
  public Region(EnumRegion enumRegion)
  {
    this.ENUM_REGION = enumRegion;


    for(EnumFood food : EnumFood.values())
    {
      cropValues.put(food, new Stack<Double>());
    }
    // This is where a regions starting crops are determined.
    // This might be replaced with something from the simulator or possibly
    // server. But I figure a switch statement is
    // fine for now.
    // It defaults to 0s until we have actual starting values.
    switch (enumRegion)
    {
      case CALIFORNIA:

        break;

      case HEARTLAND:
      case NORTHERN_PLAINS:
      case SOUTHEAST:
      case NORTHERN_CRESCENT:
      case SOUTHERN_PLAINS:
      case MOUNTAIN:
      default:
        Random rand = new Random();
        for(EnumFood food : EnumFood.values())
        {
          addCropValue(food, rand.nextInt(100));
        }
    }
  }

  public void addCropValue(EnumFood food, double value)
  {
    cropValues.get(food).push(value);

  }

  public double getCropValue(EnumFood food)
  {
    return cropValues.get(food).peek();
  }

  public HashMap<EnumFood, Double> getLastCropData()
  {
    HashMap<EnumFood, Double> latestData = new HashMap<>();
    for(EnumFood food : EnumFood.values())
    {
      latestData.put(food, cropValues.get(food).peek());
    }
    return latestData;
  }

  public double getHappiness()
  {
    return happiness;
  }

  public double getWealth()
  {
    return wealth;
  }


  public HashMap<EnumFood, Stack<Double>> getCropValues()
  {
    return new HashMap<>(cropValues);
  }


}
