package starvationevasion.client.GUI.Graphs;

import starvationevasion.common.EnumRegion;

/**
 * Graph which displays the HDI over time
 */
public class GraphHDI extends Graph
{
  EnumRegion region;

  /**
   * Default constructor which sets the title and y label
   * @param region
   */
  public GraphHDI(EnumRegion region)
  {
    this.region = region;
    this.title = "Human Development Index for " + region.toString();
    lineChart.setTitle(title);

    yAxis.setLabel("HDI");
  }

}
