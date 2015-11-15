package starvationevasion.sim.datamodels;

/**
 * Created by Alfred on 11/14/15.
 *
 * The FeedCrop class is used to describe a general crop by quantifying <br>
 * its use of fertilizer, pesticides, and water in Kg, Kg, and Liters <br>
 * respectively. This class of crop is animal consumable.
 *
 *
 */
public class FeedCrop extends Crop
{
  /**
   * Class constructor creates a FeedCrop object with set values.
   *
   * @param fertilizerPerAcre
   * @param pesticidesPerAcre
   * @param waterPerAcre
   */
  public FeedCrop(double fertilizerPerAcre, double pesticidesPerAcre, double waterPerAcre)
  {
    super(fertilizerPerAcre, pesticidesPerAcre,waterPerAcre);
  }
}
