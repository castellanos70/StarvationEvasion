package Logic.geography;

import javafx.scene.paint.Color;

/**
 * Interface containing constants for the interactive map.
 * These constants include the ID's of the different regions inside of the FXML
 */
public interface MapConstants
{
  String HEARTLAND = "heart";

  String CALIFORNIA = "cali";

  String MOUNTAIN = "mtn"; // and pacific northwest

  String NORTHERN_PLAINS = "nopl";

  String NORTHERN_CRESCENT = "cresc";

  String SOUTHEAST = "soea";

  String SOUTHERN_PLAINS = "sopl"; // and delta states

  double SELECT_RADIUS = 500;

  double UNSELECT_RADIUS = 10;


  Color CALIFORNIA_COLOR = new Color(0.8, 0.6, 1.0, 1.0);

  Color HEARTLAND_COLOR = new Color(0.964705882, 0.980392157, 0.529411765, 1.0);

  Color MOUNTAIN_COLOR = new Color(0.749019608, 0.501960784, 0.419607843, 1.0);

  Color NORTHERN_PLAINS_COLOR = new Color(0.654901961, 0.847058824, 0.909803922, 1.0);

  Color NORTHERN_CRESCENT_COLOR = new Color(0.529411765, 1.0, 0.545098039, 1.0);

  Color SOUTHEAST_COLOR = new Color(0.949019608, 0.623529412, 0.247058824, 1.0);

  Color SOUTHERN_PLAINS_COLOR = new Color(1.0, 0.541176471, 0.541176471, 1.0);

}
