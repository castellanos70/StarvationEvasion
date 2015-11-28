package spring2015code.model;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import starvationevasion.common.EnumFood;
import starvationevasion.geography.CropZoneData.EnumCropZone;
import spring2015code.common.AbstractScenario;
import spring2015code.model.geography.Territory;
import starvationevasion.geography.LandTile;

/**
 * CropOptimizer plants all crops for a given agriculturalUnit in a given year in an
 * optimal way.
 * We conceived of the problem as the Generalized Assignment Problem, with each
 * crop being a 0-1 Knapsack Problem. Our approach was inspired by:
 * http://www.cs.technion.ac.il/~lirank/pubs/2006-IPL-Generalized-Assignment-Problem.pdf
 * 
 * Since all the items (Land Tiles) are of identical size, we start with the largest knapsack
 * (crop with largest area allocated to it) and sort the tiles by their yield for that crop.
 * We proceed to the other crops in descending order of area allocated, resorting the tiles
 * by their yield for the crop and taking the number required from the top of the list.
 * @author jessica
 */
public class CropOptimizer
{
  private static final int NUM_CROPS = EnumFood.SIZE;
  private final int year;
  private final int START_YEAR = AbstractScenario.START_YEAR;
  private final Territory territory;
  private final List<CropBin> cropBins;
  private final List<TileYield> tileYields;
  private final double[] cropYields;
  
  /**
   * @param year      year of planting
   * @param territory   territory to plant
   */
  public CropOptimizer(int year, Territory territory)
  {
    this.year = year;
    this.territory = territory;
    cropBins = new ArrayList<CropBin>();
    tileYields = new ArrayList<TileYield>();
    cropYields = new double[EnumFood.SIZE];

    int zeros = 0;
    for (EnumFood crop:EnumFood.values())
    {
      int index = crop.ordinal();
      double yield = territory.getCropYield(START_YEAR, crop);
      if (Double.isFinite(yield)) cropYields[index] = yield;
      else cropYields[index] = 0.;

      if (cropYields[index] == 0.) zeros += 1;

      /*if (agriculturalUnit.getName().equals("Brazil"))
      {
        System.out.println("Brazil yield for "+crop+" is "+yield);
      }*/
    }

    // The only time we see all zero crop yields is when the crop data is
    // incomplete in the configuration files. Log it and set a uniform
    // distribution.
    //
    if (zeros == EnumFood.SIZE)
    {
      Logger.getGlobal().log(Level.SEVERE, "Territory {0} has nil crop yields.",  territory.getName());
    }
  }
  
  /**
   * Plant all the crops
   */
  public void optimizeCrops()
  { 
    plantingSetup();
    // plant crops
    for (CropBin bin:cropBins)
    {
      plantCrop(bin);
    }
    clearUnusedTiles();
  }

  /**
   Do calculations & sorting needed for planting.
   */
  private void plantingSetup()
  {
    // figure out how many tiles needed for each crop
    for (EnumFood crop:EnumFood.values())
    {
      double cropLand = territory.getCropLand(year, crop);
      CropBin bin = new CropBin(crop, (int) cropLand/100);
      cropBins.add(bin);
    }
    
    // calculate yield for each tile for each crop
    for (LandTile tile: territory.getLandTiles())
    {
      TileYield tYield = new TileYield(tile, territory);
      tileYields.add(tYield);
    }
    
    // sort crops by tiles needed, most to least
    Collections.sort(cropBins, Collections.reverseOrder());
  }
  
  /**
   * Plant a given crop
   * @param bin   CropBin with crop to plant and tiles to plant
   */
  private void plantCrop(CropBin bin)
  { 
    EnumFood crop = bin.crop;
    int tilesToPlant = bin.tilesNeeded;
    double production = 0;
    Comparator reverseComparator = Collections.reverseOrder(new TileYieldComparator(crop)); 
    Collections.sort(tileYields, reverseComparator);                     // sort tiles by descending yield 
    while (tilesToPlant > 0 && tileYields.isEmpty() == false)            // for top n tiles, where n = tilesNeeded for crop
    {
      TileYield tYield = tileYields.get(0);
      double yield = tYield.yields[crop.ordinal()];   // get the tile's yield for crop
      production += yield * 100;                        // add tile's yield to total produced
      tYield.tile.setCurrCrop(crop);                  // set the tile's crop to this crop
      tileYields.remove(tYield);                      // remove tile's tYield object because tile now NA
      tilesToPlant--;
    }
    // after getting all the tiles we need, set total production for year
    if (year != AbstractScenario.START_YEAR)
    {
      territory.setCropProduction(year, crop, production);
    }
  }
  
  /**
   * If we are not using a tile this year, we need to make sure its currCrop field is set to null.
   */
  private void clearUnusedTiles()
  {
    for (TileYield tYield : tileYields)
    {
      LandTile tile = tYield.tile;
      if (tile.getCurrentCrop() != null) tile.setCurrCrop(null);
    }
  }

  /**
   * Class containing a LandTile and the amount that tile will yield of 
   * each crop for the current year (uses tile's currCrop for land use
   * penalty calculation).
   * @author jessica
   */
  private class TileYield
  {
    private LandTile tile;
    private double[] yields;
    
    private TileYield(LandTile tile, Territory agriculturalUnit)
    {
      this.tile = tile;
      yields = new double[NUM_CROPS];
      for (EnumFood crop:EnumFood.values())
      {
        EnumCropZone zone;
        double ctryYield = cropYields[crop.ordinal()];

        // PAB : The Phase 2 code had a special type for 'other' that is no longer supported.
        //
        // if (crop.equals(EnumFood.OTHER_CROPS)) zone = tile.rateTileForOtherCrops(agriculturalUnit.getOtherCropsData());
        // else zone = tile.rateTileForCrop(crop);
        zone = tile.rateTileForCrop(crop);

        double percentYield = 1;
        // for years after START, calculate percentage of yield depending on zone & prior crop
        if (year != START_YEAR)
        {
          percentYield = tile.getTileYieldPercent(crop, zone);
        }
        // for year 0, calculate percentage of yield based on zone only
        else
        {
          switch (zone)
          {
            case IDEAL:
              percentYield = 1;
              break;

            case ACCEPTABLE:
              percentYield = 0.6;
              break;

            case POOR:
              percentYield = 0.25;
              break;

            default:
          }
        }
        yields[crop.ordinal()] = percentYield * ctryYield;
      }
    }
    
  }
  
  /**
   * Comparator for TileYield class; compares based on their yield for the
   * crop given in the comparator's initializer.
   * @author jessica
   */
  private class TileYieldComparator implements Comparator<TileYield>
  {
    EnumFood crop;
    
    private TileYieldComparator(EnumFood crop)
    {
      this.crop = crop;
    }

    @Override
    public int compare(TileYield tile1, TileYield tile2)
    {
      double yield1 = tile1.yields[crop.ordinal()];
      double yield2 = tile2.yields[crop.ordinal()];
      double diff = yield1 - yield2;
      if (diff > 0) return 1;
      else if (diff < 0) return -1;
      else return 0;
    }
  }
  
  /**
   * Class that allows us to sort crops by number of tiles needed.
   * Note: this class has a natural ordering that is inconsistent with equals.
   * @author jessica
   */
  private class CropBin implements Comparable<CropBin>
  {
    private EnumFood crop;
    private int tilesNeeded;
    
    CropBin(EnumFood crop, int tilesNeeded)
    {
      this.crop = crop;
      this.tilesNeeded = tilesNeeded;
    }

    @Override
    public int compareTo(CropBin bin)
    {
      int diff = this.tilesNeeded - bin.tilesNeeded;
      return diff;
    }
  }
}
