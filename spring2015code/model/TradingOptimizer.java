package spring2015code.model;


import starvationevasion.common.EnumFood;
import spring2015code.model.geography.AgriculturalUnit;

import java.util.*;

/**
 @author david
 created: 2015-04-05

 description: 
 TradingOptimizer casts the import-export problem as something akin to the
 Generalized Assignment Problem, wherein either the importers or exporters
 may be conceptualized as the bins or knapsacks.
 The algorithm follows the ideas outlined at:
 http://www.cs.technion.ac.il/~lirank/pubs/2006-IPL-Generalized-Assignment-Problem.pdf

 The strategy is to to look at each bin as an individual knapsack problem 
 (in this case, the fractional knapsack) with a set of shared resources to place
 in that knapsack, and run them in an arbitrary order until the resources to
 place have been exhausted.
 This is a fairly naive solution, and due to the size of the data set, cannot
 be tightened efficiently (e.g. solving the knapsack problems in order of greatest
 outcome).  To mitigate the odds of a sub-optimal solution, the algorithm is
 threaded for each crop to be traded and run multiple times, the best result of
 which is the actual implemented set of trades.
 */
public class TradingOptimizer
{
  private final int year;
  private final Collection<AgriculturalUnit> countries;
  private static final boolean DEBUG = false;
  private final List<TradePair>[] allTrades = new ArrayList[]{
    new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList()
  };
  
  private List<SingleCropTrader> traders;
  
  /**
   Construct a new TradingOptimizer with the set of countries to trade between.

   @param countries
   Collection of Countries to trade between
   @param year
   year to calculate trades for
   */
  public TradingOptimizer(Collection<AgriculturalUnit> countries, int year)
  {
    this.year = year;
    this.countries = countries;
  }

  /**
   Starts the optimization algorithm.  This creates a new threaded
   SingleCropTrader
   for each crop to be analyzed independently
   */
  public void optimizeAndImplementTrades()
  {
    traders = new ArrayList<>();
    for(EnumFood crop : EnumFood.values())
    {
      traders.add(new SingleCropTrader(crop));
    }
    for(SingleCropTrader trader : traders) trader.start();
  }
  
  public boolean doneTrading()
  {
    for(SingleCropTrader t : traders) if (!t.isDone()) return false;
    return true;
  }
  
  public List<TradePair>[] getAllTrades(){ return allTrades; }

  /**
   TradePair class represents a potential pairing between an importer and
   exporter
   Very little is saved in each object, save the relatively expensive efficiency
   calculation and the AgriculturalUnit references.  They may be instantiated from an
   existing TradePair to mitigate computational complexity.
   */
  public static class TradePair implements Comparable<TradePair>
  {
    public final AgriculturalUnit importer;
    public final AgriculturalUnit exporter;
    public final double efficiency;
    private double amount = 0;


    private TradePair(AgriculturalUnit exporter, AgriculturalUnit importer)
    {
      this.exporter = exporter;
      this.importer = importer;
      this.efficiency = calcEfficiency(exporter, importer);
    }

    private TradePair(TradePair t)
    {
      importer = t.importer;
      exporter = t.exporter;
      efficiency = t.efficiency;
    }

    /* Calculates the efficiency between two countries based on the Great Circle
      Distance between them, as implemented in the AgriculturalUnit class, and half
      the radius of the Earth (theoretical maximum distance between the two
      countries) ~20,000km */
    private static double calcEfficiency(AgriculturalUnit c1, AgriculturalUnit c2)
    {
      return 1 - c1.getShippingDistance(c2.getCapitolLocation()) / 20_000d;
    }

    /* implement a trade between this pair, given a crop and year to trade in.
      The trade will only be implemented if both the importer still has a need
      for the crop and if the exporter can supply the crop.  The amount traded
      is bounded by both the need and the surplus of importer and exporter,
      respectively.  Returns true if a trade is made, false otherwise.
     */
    private boolean implementTrade(EnumFood crop, int year)
    {
      double need = -(importer.getNetCropAvailable(year, crop) - importer.getTotalCropNeed(year, crop));
      double supply = exporter.getNetCropAvailable(year, crop) - exporter.getTotalCropNeed(year, crop);

      if (need > 0 && supply > 0)
      {
        double curExport = exporter.getCropExport(year, crop);
        double curImport = importer.getCropImport(year, crop);

        double toGive = Math.min(need / efficiency, supply);
        double toReceive = toGive * efficiency;

        amount = toReceive;
        exporter.setCropExport(year, crop, toGive + curExport);
        importer.setCropImport(year, crop, toReceive + curImport);
        
        return true;
      }
      return false;
    }

    @Override
    public int compareTo(TradePair o)
    {
      return o.efficiency > efficiency? 1 : efficiency > o.efficiency ? -1 : 0;
    }

    public double getAmountTraded()
    {
      return amount;
    }
  }

  /**
   SingleCropTrader manages the trading optimization algorithm for a single
   crop.
   */
  private class SingleCropTrader extends Thread
  {
    private boolean isDone = false;
    private static final int TRIALS = 50;
    private final EnumFood crop;
    private final List<TradePair> pairs = new ArrayList<>();
    private final TradePairList master;


    /**
     Construct a new SingleCropTrader with a specified crop.
     @param crop      crop to trade
     */
    private SingleCropTrader(EnumFood crop)
    {
      this.crop = crop;
      List<AgriculturalUnit> importers = new ArrayList<>();
      List<AgriculturalUnit> exporters = new ArrayList<>();
      double net = 0;

      /* Divide the countries in the parent class into importers and exporters
      based on the crop surplus for each country */
      for(AgriculturalUnit c : countries)
      {
        double surplus = c.getSurplus(year, crop);
        net += surplus;
        if(surplus < 0) importers.add(c);
        else if(surplus > 0) exporters.add(c);
      }

      /* create the TradePairs between importer and exporter */
      for(AgriculturalUnit i : importers)
      {
        for(AgriculturalUnit e : exporters)
        {
          pairs.add(new TradePair(e, i));
        }
      }

      /* create the master list used to instantiate all the temporary lists
      during the multiple runs of the algorithm */
      master = new TradePairList(pairs, countries, crop, year);

      if (DEBUG) System.out.printf("Trader for %s setup:%n" +
          "net availability: %.3f tons, importers.size(): %d, exporters.size(): %d%n",
        crop.toString(), net, importers.size(), exporters.size());
    }

    /**
     Defines the running behavior of the SingleCropTrader.
     */
    @Override
    public void run()
    {
      tradeDeterministic();
//      tradeProbablistic();
      isDone = true;
    }
    
    public boolean isDone() { return isDone; }

    private void tradeDeterministic()
    {
      Collections.sort(master);
      allTrades[crop.ordinal()].addAll(master.implementTrades());
    }


    /* Multiple runs of the GAP algorithm are run, the best result of which is
    saved, along with the TradePair ordering that produced it.
    This ordering is them implemented in the actual country objects. */
    private void tradeProbablistic()
    {
      long start = System.currentTimeMillis();
      TradePairList tmp;
      TradePairList best = new TradePairList(master);

      double tmpResult;

      double bestResult = best.getTradeResult();

      double sum = 0;
      for (int i = 0; i < TRIALS; i++)
      {
        tmp = new TradePairList(master);

        /* switch the ordering scheme every loop */
        if (i % 2 == 0)
        {
          tmp.shuffleOnExporters();
        }
        else
        {
          tmp.shuffleOnImporters();
        }

        tmpResult = tmp.getTradeResult();
        sum += tmpResult;

        if(tmpResult > bestResult)
        {
          best = tmp;
          bestResult = tmpResult;
        }
      }

      allTrades[crop.ordinal()].addAll(best.implementTrades());

      long end = System.currentTimeMillis();
      if (DEBUG) System.out.printf("Trader for %s done in %dms%n" +
        "best result: %.3f, avg: %.3f%n", crop.toString(), end - start, bestResult, sum / TRIALS);
    }
  }

  private class TradePairList extends ArrayList<TradePair>
  {
    private final EnumFood crop;

    /* maintain the state of the need/surplus for each importer/exporter here.
      Values are adjusted with each theoretical trade in getTradeResult() */
    private Map<AgriculturalUnit, Double> exporterMap = new HashMap<>();
    private Map<AgriculturalUnit, Double> importerMap = new HashMap<>();

    private TradePairList(Collection<TradePair> pairs, Collection<AgriculturalUnit> all, EnumFood crop, int year)
    {
      super(pairs);
      this.crop = crop;
      createMaps(all);
    }

    private TradePairList(TradePairList list)
    {
      super(list.size());
      crop = list.crop;

      for(TradePair pair: list)
      {
        add(new TradePair(pair));
      }
      importerMap = new HashMap<>(list.importerMap);
      exporterMap = new HashMap<>(list.exporterMap);
    }

    private void createMaps(Collection<AgriculturalUnit> countries)
    {
      for (AgriculturalUnit c : countries)
      {
        double surplus = c.getSurplus(year, crop);
        if (surplus > 0)
        {
          exporterMap.put(c, surplus);
        }
        else if (surplus < 0)
        {
          importerMap.put(c, -surplus);
        }
      }
    }

    /* given the ordering of this TradePairList, get the theoretical result
      of all the trading that would be done with a greedy solution to the
      fractional knapsack algorithm */
    private double getTradeResult()
    {
      for(TradePair pair : this)
      {
        double need;
        double supply;

        /* if either importer or exporter are not present, they have been removed
          as a result of exhausting their need/supply */
        if(importerMap.containsKey(pair.importer) && exporterMap.containsKey(pair.exporter))
        {
          need = importerMap.get(pair.importer);
          supply = exporterMap.get(pair.exporter);
        }
        else continue;

        /* calculate the amounts to export and import */
        double toGive = Math.min(need/pair.efficiency, supply);
        double toReceive = toGive * pair.efficiency;

        /* calculate the new values for the importer and exporter maps */
        double newNeed = need - toReceive;
        double newSupply = supply - toGive;

        /* if either value is 0 or less, remove the appropriate country from
          the map.  It can no longer be used for trading.
          Otherwise, update the values in the maps to the results of the trade.
         */
        if(newNeed > 0) importerMap.put(pair.importer, newNeed);
        else importerMap.remove(pair.importer);

        if(newSupply > 0) exporterMap.put(pair.exporter, newSupply);
        else exporterMap.remove(pair.exporter);
      }

      double result = 0;

      /* Sum all the surpluses and deficits of the exporters to obtain the
         result of this trading sequence.  Any countries not in the maps have
         achieved a "balance" of the given crop, and do not contribute to the
         sum.
       */
      for(AgriculturalUnit ex : exporterMap.keySet())
      {
        /* values in export map are surpluses */
        result += exporterMap.get(ex);
      }
      for(AgriculturalUnit im : importerMap.keySet())
      {
        /* values in import map are deficits (as postive numbers) */
        result -= importerMap.get(im);
      }
      return result;
    }

    /* implement the trades in this TradePairList at the country level.  This
      mutates the underlying data used to calculate undernourishment, happiness,
      etc.  */
    private List<TradePair> implementTrades()
    {
      List<TradePair> list = new ArrayList<>();
      for(TradePair pair: this)
      {
        if(pair.implementTrade(crop, year)) list.add(pair);
      }
      return list;
    }

    /* sort this list based first on importer Eculidean distance from a randomly
     selected point and then on the efficiency (descending) between the importer
     and exporter in each TradePair */
    private void shuffleOnImporters()
    {
      final MapPoint pt = randomMapPoint();
      Collections.sort(this, new Comparator<TradePair>()
      {
        @Override
        public int compare(TradePair o1, TradePair o2)
        {
          /* if the importer are the same object, sort based on efficiency */
          if(o1.importer == o2.importer)
          {
            double ef1 = o1.efficiency;
            double ef2 = o2.efficiency;
            return ef1 > ef2? 1 : ef1 < ef2 ? -1 : 0;
          }
          /* otherwise sort based on Euclidean distance between the importers
            and the randomly selected point */
          else
          {
            double d1 = pt.distanceSq(o1.importer.getCapitolLocation());
            double d2 = pt.distanceSq(o2.importer.getCapitolLocation());
            return d1 > d2 ? 1 : d1 < d2 ? -1 : 0;
          }
        }
      });
    }

    /* sort this list based first on exporter Eculidean distance from a randomly
     selected point and then on the efficiency (descending) between the importer
     and exporter in each TradePair */
    private void shuffleOnExporters()
    {
      final MapPoint pt = randomMapPoint();
      Collections.sort(this, new Comparator<TradePair>()
      {
        @Override
        public int compare(TradePair o1, TradePair o2)
        {
          /* if the exporters are the same object, sort based on efficiency */
          if(o1.exporter == o2.exporter)
          {
            double ef1 = o1.efficiency;
            double ef2 = o2.efficiency;
            return ef1 > ef2? 1 : ef1 < ef2 ? -1 : 0;
          }
          /* otherwise sort based on Euclidean distance between the importers
            and the randomly selected point */
          else
          {
            double d1 = pt.distanceSq(o1.exporter.getCapitolLocation());
            double d2 = pt.distanceSq(o2.exporter.getCapitolLocation());
            return d1 > d2 ? 1 : d1 < d2 ? -1 : 0;
          }
        }
      });
    }

    /* make a random MapPoint somewhere in the range of acceptable Lon and Lat
      values */
    private MapPoint randomMapPoint()
    {
      double lat = (Math.random() - 0.5) * 360;
      double lon = (Math.random() - 0.5) * 180;
      return new MapPoint(lon, lat);
    }
  }


}
