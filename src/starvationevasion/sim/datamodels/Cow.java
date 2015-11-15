package starvationevasion.sim.datamodels;

/**
 * Created by Alfred on 11/14/15.
 *
 * The Cow class describes a Cow as an Animal.
 */
public class Cow extends Animal
{
  /**
   * Class constructor creates a new Cow object with set values.
   *
   * @param waterConsumedPerYear
   * @param foodConsumedPerYear
   * @param forAThousand
   */
  public Cow(double waterConsumedPerYear, double foodConsumedPerYear, int forAThousand)
  {
    super(waterConsumedPerYear,foodConsumedPerYear,forAThousand);
  }

}
