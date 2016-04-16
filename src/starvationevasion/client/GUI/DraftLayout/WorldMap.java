package starvationevasion.client.GUI.DraftLayout;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import starvationevasion.client.GUI.GUI;
import starvationevasion.common.GeographicArea;
import starvationevasion.sim.Simulator;

import java.util.ArrayList;

//import java.awt.*;

/**
 * Created by Dayloki on 3/29/2016.
 */
public class WorldMap extends ScrollPane
{
  private GUI gui;
  private double zoomLevel=1;
  private StackPane zoomGroup = new StackPane();
  private Canvas canvas;
  Image imageMap;
  public WorldMap(GUI gui)
  {
   // setHbarPolicy(ScrollBarPolicy.NEVER);
   // setVbarPolicy(ScrollBarPolicy.NEVER);

    this.gui=gui;
    imageMap=gui.getImageGetter().getWorldMap();
    ImageView worldMap=new ImageView(imageMap);

    VBox holder=new VBox();
    holder.getChildren().add(worldMap);
//    worldMap.setFitWidth(gui.getBoxWidth() * 10);
//    worldMap.setLayoutX(gui.getBoxWidth() * 10);
    //worldMap.setFitHeight(gui.getBoxHeight()*6);
    worldMap.setPreserveRatio(true);
    worldMap.setSmooth(true);
    worldMap.setCache(true);
//    worldMap.fitHeightProperty().bind(gui.getPrimaryStage().heightProperty());
//    worldMap.fitWidthProperty().bind(gui.getPrimaryStage().widthProperty());

    setPannable(true);
    zoomGroup = new StackPane();
    zoomGroup.getChildren().add(worldMap);
    setContent(zoomGroup);
    canvas=new Canvas(imageMap.getWidth(),imageMap.getHeight());
    zoomGroup.getChildren().add(canvas);
    //drawOnMap();
    //drawOutline();
    addEventFilter(ScrollEvent.SCROLL, event -> {

      //      DoubleProperty zoomProperty = new SimpleDoubleProperty(200);
//      worldMap.setFitHeight(zoomProperty.get());
//      worldMap.setFitWidth(zoomProperty.get());

      Group contentGroup = new Group();

      contentGroup.getChildren().add(zoomGroup);


      setContent(contentGroup);
      if(event.getDeltaY()>0&&zoomLevel<1.1)zoomLevel += .1;
      else if(event.getDeltaY()<0 &&zoomLevel>.5) zoomLevel -= .1;
      Scale scaleTransform = new Scale(zoomLevel, zoomLevel, 0,0);
      zoomGroup.setScaleX(zoomLevel);
      zoomGroup.setScaleY(zoomLevel);
      // Scale scaleTransform = new Scale(2, 2, -100, -10000000);
      // scaleTransform.transform(event.getX(),event.getY());
       scaleTransform.setPivotX(-event.getX());
      scaleTransform.setPivotY(-event.getY());

      double oldHvalue=getHvalue();
      double oldVvalue=getVvalue();
      setHvalue(event.getX() /getWidth() + oldHvalue * zoomLevel);
      setVvalue(event.getY()/ getHeight()+oldVvalue*zoomLevel);

      //setHvalue(.75);
     // setVvalue(.75);

      //zoomGroup.getTransforms().removeAll();
      //zoomGroup.getTransforms().add(scaleTransform);
    });
  }


}
