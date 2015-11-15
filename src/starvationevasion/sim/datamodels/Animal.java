package starvationevasion.sim.datamodels;

/**
 * Created by Alfred on 11/14/15.
 *
 * The Animal class describes a general animal by the <br></>
 * amount of water and food it consumes, and a quantity of <br></>
 * how many of its class are worth $1000.00
 */
public class Animal
{
  /**
   * Amount of water in liters consumed in a year.
   */
  private double waterConsumedPerYear;

  /**
   * Amount of food in Kg consumed in a year.
   */
  private double foodConsumedPerYear;

  /**
   * Amount of class required to earn $1000.00
   */
  private int forAThousand;

  /**
   * Class constructor creates a general animal with set values
   *
   * @param waterConsumedPerYear
   * @param foodConsumedPerYear
   * @param forAThousand
   */
  public Animal(double waterConsumedPerYear, double foodConsumedPerYear, int forAThousand)
  {
    this.waterConsumedPerYear = waterConsumedPerYear;
    this.foodConsumedPerYear  = foodConsumedPerYear;
    this.forAThousand         = forAThousand;
  }

  /**
   * getter
   *
   * @return waterConsumedPerYear
   */
  public double getWaterConsumedPerYear()
  {
    return waterConsumedPerYear;
  }

  /**
   * getter
   *
   * @return foodConsumedPerYear;
   */
  public double getFoodConsumedPerYear()
  {
    return foodConsumedPerYear;
  }

  /**
   * getter
   *
   * @return forAThousand
   */
  public int getForAThousand()
  {
    return forAThousand;
  }
}
