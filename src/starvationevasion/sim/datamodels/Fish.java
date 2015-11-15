package starvationevasion.sim.datamodels;

/**
 * Created by Alfred on 11/14/15.
 * The Fish class describes a Fish as an Animal.
 */
public class Fish extends Animal
{

  /**
   * Class constructor creates a new Fish object with set values.
   *
   * @param waterConsumedPerYear
   * @param foodConsumedPerYear
   * @param forAThousand
   */
  public Fish(double waterConsumedPerYear, double foodConsumedPerYear, int forAThousand)
  {
    super(waterConsumedPerYear,foodConsumedPerYear,forAThousand);
  }
}
