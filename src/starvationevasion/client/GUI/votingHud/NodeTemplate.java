package starvationevasion.client.GUI.votingHud;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;

/**
 * @author Ben
 * 
 * this class creates a basic template which all gui Nodes will extend
 */
public abstract class NodeTemplate extends Pane
{
  
  /**default constructor
   * adds listeners to the widhtProperty and the heightProperty
   */
  public NodeTemplate(){
    this.widthProperty().addListener(new ChangeListener<Number>(){
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
      {
        onResize();
      }
    });
    
    this.heightProperty().addListener(new ChangeListener<Number>(){
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
      {
        onResize();
      }
    });
  }
  
  /**
   * The logic in this method will be executed whenever this node is resized
   */
  public abstract void onResize();
  
  /**
   * 
   * sets the size of this Pane to the given width and height
   * 
   * @param width
   * @param height
   */
  public void setSize(double width, double height){
    this.setWidth(width);
    this.setHeight(height);
  }
}