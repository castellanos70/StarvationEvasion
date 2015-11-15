package starvationevasion.sim.datamodels;

/**
 * Created by Alfred on 11/14/15.
 *
 * The Crop class is used to describe a general crop by quantifying <br>
 * its use of fertilizer, pesticides, and water in Kg, Kg, and Liters <br>
 * respectively. This class of crop is human consumable.
 *
 *
 */
public class Crop
{
  /**
   * Amount of fertilizer per acre in Kg used in one year.
   */
    private double fertilizerPerAcre;
  /**
   * Amount of pesticides per acre in Kg used in one year.
   */
  private double pesticidesPerAcre;

  /**
   * Amount of water per acre in Liters used in one year.
   */
  private double waterPerAcre;

  /**
   * Class constructor creates a new Crop object with <br>
   * set fields
   *
   * @param fertilizerPerAcre
   * @param pesticidesPerAcre
   * @param waterPerAcre
   */
  public Crop(double fertilizerPerAcre, double pesticidesPerAcre, double waterPerAcre)
  {
    this.fertilizerPerAcre = fertilizerPerAcre;
    this.pesticidesPerAcre = pesticidesPerAcre;
    this.waterPerAcre      = waterPerAcre;
  }

  /**
   * getter
   *
   * @return fertilizerPerAcre
   */
  public double getFertilizerPerAcre()
  {
    return fertilizerPerAcre;
  }

  /**
   * getter
   *
   * @return pesticidesPerAcre
   */
  public double getPesticidesPerAcre()
  {
    return pesticidesPerAcre;
  }

  /**
   * getter
   *
   * @return waterPerAcre
   */
  public double getWaterPerAcre()
  {
    return waterPerAcre;
  }
}
