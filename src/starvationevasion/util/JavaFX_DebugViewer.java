package starvationevasion.util;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class JavaFX_DebugViewer extends Application
{
  public volatile static JavaFX_DebugViewer me;
  private String title = "JavaFX_DebugViewer";
  private String imagePath;
  private Image backgroundImage;
  private Canvas canvas;
  private GraphicsContext gfx;
  private int x = 0;
  private StackPane stackPane;

  private ArrayList<Polygon> shapeQue = new ArrayList<>();

  @Override
  public void init()
  {
    me = this;
    List<String> args = getParameters().getRaw();
    if (args.size() > 0) imagePath = args.get(0);
    if (args.size() > 1) title = args.get(1);
  }
  @Override
  public void start(Stage stage)
  {
    stage.setTitle(title);
    stage.setX(0); stage.setY(0);

    // load the image
    backgroundImage = new Image(imagePath);

    ImageView imageView = new ImageView();
    imageView.setImage(backgroundImage);


    // construct the scene contents over a stacked background.
    stackPane = new StackPane();
    canvas = new Canvas(backgroundImage.getWidth(), backgroundImage.getHeight());
    //gfx = canvas.getGraphicsContext2D();
    stackPane.getChildren().setAll(imageView, canvas);

    Scene scene = new Scene(stackPane);
    scene.setFill(Color.BLACK);

    stage.setScene(scene);
    stage.sizeToScene();
    stage.show();

    Update timer = new Update();
    timer.start();
  }



  public int getImageWidth() {return (int)backgroundImage.getWidth();}

  public int getImageHeight() {return (int)backgroundImage.getHeight();}

  public void add(Polygon shape)
  {
    //System.out.println("JavaFX_DebugViewer.add("+shape+")");
    synchronized (shapeQue)
    {
      shapeQue.add(shape);
    }
  }


  //public void externalLaunch(String[] args)
  //{
  // JavaFX_DebugViewer.launch(args);
  //}


  class Update extends AnimationTimer
  {
    @Override
    public void handle(long now)
    {
      gfx = canvas.getGraphicsContext2D();
      synchronized (shapeQue)
      {
        //Line myLine = new Line(0, 0, x, 500);
        //gfx.beginPath();
        gfx.setStroke(Color.rgb(128,0,255));
        //gfx.moveTo(0, 0);
        //gfx.lineTo(x, 500);
        //gfx.closePath();
        gfx.setFill(Color.rgb(0,200,0));
        gfx.fillOval(0,400,200,200);
        x += 10;
        if (x > 1000) x = 5;

        for (Polygon shape : shapeQue)
        {
          ObservableList<Double> points = shape.getPoints();
          double[] x = new double[points.size()/2];
          double[] y = new double[points.size()/2];
          for (int i=0; i< points.size()/2; i+=2)
          {
            x[i] = points.get(i*2);
            y[i] = points.get(i*2+1);
            System.out.println(x[i]+", "+y[i]);
          }
          gfx.fillPolygon(x,y, x.length);
          gfx.strokePolygon(x,y, x.length);
        }

        shapeQue.clear();
      }
      //stackPane.requestLayout();
    }
  }

  public static void main(String[] args)
  {
    JavaFX_DebugViewer.launch(args);
  }
}
