package starvationevasion.client.GUI.Graphs;

import starvationevasion.common.EnumFood;

/**
 * Graph which is displayed for each product in the product bar
 */
public class GraphProductBar extends Graph
{
  EnumFood food;

  /**
   * Constructor for a product bar graph
   * @param food type for the graph to display
   */
  public GraphProductBar(EnumFood food)
  {
    this.food = food;
    this.title = "World Market Price for " + food.toString();
    lineChart.setTitle(title);
    lineChart.getStylesheets().add(getClass().getResource("/starvationevasion/client/GUI/Graphs/graphCSS.css").toExternalForm());
    yAxis.setLabel("Price (millions of $)");

    lineChart.setPrefSize(725,330);
  }
}



















