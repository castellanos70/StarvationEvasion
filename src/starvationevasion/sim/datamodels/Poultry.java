package starvationevasion.sim.datamodels;

/**
 * Created by Alfred on 11/14/15.
 *
 * The Poultry class describes a Poultry as an Animal.
 */
public class Poultry extends Animal
{
  /**
   * Class constructor creates a new Poultry object with set values.
   *
   * @param waterConsumedPerYear
   * @param foodConsumedPerYear
   * @param forAThousand
   */
  public Poultry(double waterConsumedPerYear, double foodConsumedPerYear, int forAThousand)
  {
    super(waterConsumedPerYear,foodConsumedPerYear,forAThousand);
  }
}
