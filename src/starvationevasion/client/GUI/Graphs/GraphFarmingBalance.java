package starvationevasion.client.GUI.Graphs;

import starvationevasion.common.EnumRegion;

/**
 * Graph which displays the farming balance for a region over time
 */
public class GraphFarmingBalance extends Graph
{
  EnumRegion region;

  /**
   * Default constructor which sets the title and y label
   * @param region
   */
  public GraphFarmingBalance(EnumRegion region)
  {
    this.region = region;
    this.title = region.toString() + " Government Farming Revenue";
    lineChart.setTitle(title);

    yAxis.setLabel("Millons of Dollars");
  }
}
