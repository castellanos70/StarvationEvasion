package starvationevasion.client.GUI.Popups;

import starvationevasion.client.GUI.GUI;
import javafx.scene.layout.Region;

import java.util.ArrayList;

public class ProductBarDataDisplay extends Region
{
  GUI gui;

  ArrayList<ProductBarData> pbPopups;
  ProductBarData currentlyShowing;

  public ProductBarDataDisplay(GUI gui)
  {
    this.gui = gui;
    initializePbPopups();
    this.getStylesheets().add("/starvationevasion/client/GUI/DraftLayout/style.css");
    this.getStyleClass().add("pbarpopup");
    this.setVisible(false);
  }


  public void setData(int id)
  {
    ProductBarData toShow = pbPopups.get(id);
    if (currentlyShowing != null)
    {
      this.getChildren().remove(currentlyShowing);
      currentlyShowing = null;
    }
    currentlyShowing = toShow;
    this.getChildren().add(toShow);
  }

  public void setDataNull()
  {
    if (currentlyShowing!=null)
    {
      this.getChildren().remove(currentlyShowing);
    }
    currentlyShowing = null;
  }

  public void open()
  {
    this.setVisible(true);
  }

  public void close()
  {
    this.setVisible(false);
  }

  public ArrayList<ProductBarData> getProductData()
  {
	  return pbPopups;
  }
  
  private void initializePbPopups()
  {
    pbPopups = new ArrayList<>();
    for (int i = 0; i < 12; ++i)
    {
      pbPopups.add(new ProductBarData(i, gui));
    }
  }
}
