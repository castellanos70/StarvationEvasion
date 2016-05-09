package starvationevasion.client.GUI.Graphs;

import starvationevasion.common.EnumRegion;

/**
 * Graph which shows the region population over time
 */
public class GraphPopulation extends Graph
{
  EnumRegion region;

  /**
   * Default constructor which sets the title and y label
   * @param region
   */
  GraphPopulation(EnumRegion region)
  {
    this.region = region;
    this.title = "Population for " + region.toString();
    lineChart.setTitle(title);

    yAxis.setLabel("Population (thousands of people)");
  }
}
