package starvationevasion.client.GUI.Graphs;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Class which gets added to graphs to allow to display datapoint on mouse over
 */
public class HoveredNode extends StackPane
{
  final Label label;

  /**
   * Default construct
   * @param value to display
   */
  public HoveredNode(int value) {
    setPrefSize(15, 15);

    this.label = createDataThresholdLabel(value);

    setOnMouseEntered(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent mouseEvent) {
        getChildren().setAll(label);
        toFront();
      }
    });
    setOnMouseExited(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent mouseEvent) {
        getChildren().clear();
      }
    });
  }

  private Label createDataThresholdLabel(int value) {
    final Label label = new Label(value + "");

    label.setStyle("-fx-font-size: 10; -fx-font-weight: bold;");

    label.setTextFill(Color.BLACK);

    label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
    return label;
  }
}

