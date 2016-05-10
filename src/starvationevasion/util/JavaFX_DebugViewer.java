package starvationevasion.util;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.List;

public class JavaFX_DebugViewer extends Application
{
  private String title = "JavaFX_DebugViewer";
  private String imagePath;
  private Image backgroundImage;
  private StackPane drawPane;

  @Override
  public void init()
  {
    List<String> args = getParameters().getRaw();
    if (args.size() > 0) imagePath = args.get(0);
    if (args.size() > 1) title = args.get(1);
  }
  @Override
  public void start(Stage stage)
  {
    stage.setTitle(title);

    // load the image
    backgroundImage = new Image(imagePath);

    ImageView imageView = new ImageView();
    imageView.setImage(backgroundImage);


    // construct the scene contents over a stacked background.
    drawPane = new StackPane();
    drawPane.getChildren().setAll(imageView);

    Scene scene = new Scene(drawPane);
    scene.setFill(Color.BLACK);
    HBox box = new HBox();
    box.getChildren().add(imageView);
    drawPane.getChildren().add(box);


    stage.setScene(scene);
    stage.setX(0); stage.setY(0);
    stage.sizeToScene();
    stage.show();
  }

  public int getImageWidth() {return (int)backgroundImage.getWidth();}

  public int getImageHeight() {return (int)backgroundImage.getHeight();}

  public void add(Shape shape, Color color)
  {
    drawPane.getChildren().add(shape);
  }

  public static void main(String[] args)
  {
    String[] imagePath = {"BlankBlue_MollweideProjection-1280x641.png"};
    JavaFX_DebugViewer.launch(imagePath);
  }
}
