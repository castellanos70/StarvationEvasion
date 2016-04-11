package starvationevasion.client.Setup;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import static java.lang.Math.random;


/**
 * Created by Dayloki on 12/6/2015.
 * tutorial from
 * https://docs.oracle.com/javafx/2/get_started/animation.htm
 */
public class StartAnimation extends Application
{
  @Override
  public void start(Stage primaryStage) throws Exception
  {
    Group root = new Group();
    Scene scene = new Scene(root, 800, 600, Color.BLACK);
    primaryStage.setScene(scene);

    primaryStage.show();
    Group circles = new Group();
    Label starvationEvasion=new Label("StarvationEvasion");
    starvationEvasion.setFont(Font.font("", FontWeight.BOLD,80));
    starvationEvasion.setWrapText(true);
    starvationEvasion.setAlignment(Pos.CENTER);
    starvationEvasion.setTextAlignment(TextAlignment.CENTER);
    for (int i = 0; i < 30; i++) {
      Circle circle = new Circle(150, Color.web("white", 0.05));
      circle.setStrokeType(StrokeType.OUTSIDE);
      circle.setStroke(Color.web("white", 0.16));
      circle.setStrokeWidth(4);
      circles.getChildren().add(circle);
    }
    root.getChildren().addAll(circles);

    circles.setEffect(new BoxBlur(10, 10, 3));
    Rectangle colors = new Rectangle(scene.getWidth(), scene.getHeight(),
            new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#f8bd55")),
                    new Stop(0.14, Color.web("#c0fe56")),
                    new Stop(0.28, Color.web("#5dfbc1")),
                    new Stop(0.43, Color.web("#64c2f8")),
                    new Stop(0.57, Color.web("#be4af7")),
                    new Stop(0.71, Color.web("#ed5fc2")),
                    new Stop(0.85, Color.web("#ef504c")),
                    new Stop(1, Color.web("#f2660f"))));
    colors.widthProperty().bind(scene.widthProperty());
    colors.heightProperty().bind(scene.heightProperty());
    root.getChildren().add(colors);
    Group blendModeGroup =
            new Group(new Group(new Rectangle(scene.getWidth(), scene.getHeight(),
                    Color.BLACK), circles), colors);
    colors.setBlendMode(BlendMode.OVERLAY);
    root.getChildren().addAll(blendModeGroup, starvationEvasion);

    Timeline timeline = new Timeline();
    for (Node circle: circles.getChildren()) {
      timeline.getKeyFrames().addAll(
              new KeyFrame(Duration.ZERO, // set start position at 0
                      new KeyValue(circle.translateXProperty(), random() * 800),
                      new KeyValue(circle.translateYProperty(), random() * 600)
              ),
              new KeyFrame(new Duration(4000), // set end position at 40s
                      new KeyValue(circle.translateXProperty(), random() * 800),
                      new KeyValue(circle.translateYProperty(), random() * 600)
              )
      );
    }
// play 40s of animation
    timeline.play();
    
    LandingPage landingPage=new LandingPage();

    timeline.setOnFinished(event -> {
      try
      {
        landingPage.start(primaryStage);
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    } );
  }

}
