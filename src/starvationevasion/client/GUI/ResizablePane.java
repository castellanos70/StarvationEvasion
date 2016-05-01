package starvationevasion.client.GUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;

/**
 * @author Ben
 * 
 * this class creates a basic template which all gui Nodes will extend
 */
public abstract class ResizablePane extends Pane
{
  private ResizeStrategy strategy;
  private Pane parent;
  
  /**default constructor
   * adds listeners to the widhtProperty and the heightProperty
   */
  public ResizablePane(Pane parent, ResizeStrategy strategy){
    this.strategy = strategy;
    this.parent = parent;
    
    this.widthProperty().addListener(new ChangeListener<Number>(){
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
      {
        resize();
      }
    });
    
    this.heightProperty().addListener(new ChangeListener<Number>(){
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
      {
        resize();
      }
    });
  }
  
  /**
   * The logic in this method will be executed whenever this node is resized
   */
  private void resize(){
    onResize();
    
    if (parent == null) return;
    if (strategy == null) return;
    
    double pWidth = parent.getWidth();
    double pHeight = parent.getHeight();
    
    this.setLayoutX(strategy.percentageX*pWidth);
    this.setLayoutY(strategy.percentageY*pHeight);
    this.setWidth(strategy.percentageWidth*pWidth);
    this.setHeight(strategy.percentageHeight*pHeight);
  }
  
  /**
   * Logic needed to be executed during resize should be place in this method
   */
  public abstract void onResize();
  public void setSize(double width, double height)
  {
	  this.setWidth(width);
	  this.setHeight(height);
  }
}
