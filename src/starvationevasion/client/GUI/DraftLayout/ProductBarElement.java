package starvationevasion.client.GUI.DraftLayout;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import com.sun.prism.Image;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.images.ImageGetter;
import starvationevasion.common.EnumFood;
import starvationevasion.common.MapPoint;
import starvationevasion.common.MapProjectionMollweide;
import starvationevasion.sim.LandTile;

/**
 * ProductBarElement is the GUI element responsible for representing one of the
 * products on the product bar If a player clicks a ProductBarElement, the user
 * should, depending on the context, either get data about the product or select
 * the product for use as a variable on a card
 * 
 * 
 * Regarding the current state of the heat maps:
 * 
 * The main reason of the push is to add the methodology, but as you will
 * quickly notice that is just semi random I used to create the heat maps (this
 * will be changed to the crop tile ratings in packed data once it's sent over).
 * Also each crop heat map is the about the same because again the point of the
 * push is to add its general functionality. Also there is two different
 * approaches that can be taken for displaying the crop maps, I have tried both
 * and put the one that I thought was slightly better.
 * 
 * Option one (the one currently being used) Description: The points of the heat
 * map are rendered on the actual Javafx canvas itself (which overlays the map).
 * Currently this causes about a 1.5-1.75 second delay upon clicking a crop for
 * the heat map to show up. This time can probably be decreased once the GUI
 * events are handled through a loop through prioritizing the rendering.
 * 
 * Option two Description: Each Crop has a local volatile-> buffered image which
 * has that crops heatmap and those are generated with ever update in the crop
 * tile ratings (which currently is very infrequent). The benfit of doing this
 * is clicking on the crops to display the heatmap is seamless as it is just
 * toggling a image inside the image view. The downside is it takes around 25
 * seconds after optimization to create the images. This would mean lauching the
 * client would take 25 seconds longer and when ever the crop rating were
 * updated it would take 25 seconds again to update the images. This time I felt
 * was too long but the option of using it or something similar is still
 * possible.
 * 
 * 
 * 
 */
public class ProductBarElement extends StackPane
{
  final EnumFood type;
  final int ID;
  GUI gui;
  ProductBar pb;
  ImageView foodImg;
  boolean selected = false;
  DropShadow ds;
  Label label;
  Point p ;
  MapProjectionMollweide map;
  ArrayList<MapPoint> points;
  ImageView view;
  GraphicsContext heatMapGraphics;
  ImageGetter imageGetter;
  LandTile tile;
  private final int IMAGE_WIDTH;
  private final int IMAGE_HEIGHT; 
  Color red = new Color(1,0,0,.5);
  Color yellow = new Color(1,1,0,.5);
  Color green = new Color(0,1,0,.5);
  Random random = new Random();
  int rand = 0;
  public ProductBarElement(GUI gui, final EnumFood food, final int ID, double width, double height, ProductBar pb)
  {
    this.gui = gui;
    this.ID = ID;
    this.type = food;
    this.foodImg = new ImageView(gui.getImageGetter().getImageForFoodType64(type));
//    this.setMaxSize(width, height);
//    this.setPrefSize(width, height);  
    initializeLabel();
    
    this.red = new Color(1,0,0,.5);
    this.yellow = new Color(1,1,0,.5);
    this.green = new Color(0,1,0,.5);
    this.IMAGE_HEIGHT = (int)gui.getImageGetter().getWorldMap().getHeight();
    this.IMAGE_WIDTH = (int)gui.getImageGetter().getWorldMap().getWidth();
    this.pb = pb;
    this.ds = new DropShadow(20, Color.AQUA);
    this.tile = new LandTile(0,0);
    this.points = tile.loadLocations();
    this.map = new MapProjectionMollweide(2048, 1026); //In the future make the dimensions not hard coded in..
    this.p = new Point();

    //Please note all of the heat maps are the same and do not look completly
    //as the should, currently the data to create them are not being passed over yet. 
    //So some example data is generated to show the functionality. 
    
    this.setOnMouseClicked(new EventHandler<MouseEvent>()
    {     
      @Override
      public void handle(MouseEvent event)
      {
        
        if (gui.getSeletingProduct())
        {
          pb.pressElement(ID);
        }
        else
        {     
           heatMapGraphics = gui.getDraftLayout().getWorldMap().getGraphicsContext();
           
          // gui.getGraphManager()event.g
           
           ///gui.getPopupManager().toggleFoodPopup(ID);
             pb.pressElement(ID);  
            if(selected)
            {
            heatMapGraphics.clearRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
            for (MapPoint point : points) {
              if((point.latitude>65||point.latitude<-35))
                {
                rand = random.nextInt(101);
                if(rand<60)heatMapGraphics.setFill(red);
                if(rand>60&&rand<85)heatMapGraphics.setFill(yellow);
                if(rand>85)heatMapGraphics.setFill(green);
                
                }
              else if((point.latitude<65&&point.latitude>35)||point.latitude>-35&&point.latitude<-10)
              {
              rand = random.nextInt(101);
                if(rand<60)heatMapGraphics.setFill(yellow);
                if(rand>60&&rand<85)heatMapGraphics.setFill(red);
                if(rand>85)heatMapGraphics.setFill(green);
              }
              else
              {
                rand = random.nextInt(101);
                if(rand<60)heatMapGraphics.setFill(green);
                if(rand>60&&rand<85)heatMapGraphics.setFill(yellow);
                if(rand>85)heatMapGraphics.setFill(red);
              }
              map.setPoint(p, point.latitude, point.longitude);       
              heatMapGraphics.fillOval(p.x, p.y, 3, 3);
            }
          } else {
            heatMapGraphics.clearRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
          }
            }  
        }
      
    });

    this.setOnMouseEntered(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent event)
      {
        label.setVisible(true);
      }
    });

    this.setOnMouseExited(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent event)
      {
        label.setVisible(false);
      }
    });

    this.setAlignment(Pos.CENTER);
    this.getChildren().add(foodImg);
    this.getChildren().add(label);
  }

  /**
   * Press the element (add dropshadow)
   */
  public void press()
  {
    if (selected)
    {
      selected = false;
      foodImg.setEffect(null);
    }
    else
    {
      selected = true;
      foodImg.setEffect(ds);
    }
  }

  private void initializeLabel()
  {
    String s = type.toString();

    label = new Label(s);
    label.setWrapText(true);
    label.setTextFill(Color.BLACK);
    label.setVisible(false);
    label.getStylesheets().add("/starvationevasion/client/GUI/DraftLayout/style.css");
    label.getStyleClass().add("pbarelement");
  }

}