package starvationevasion.client.GUI.Popups;

import starvationevasion.client.GUI.GUI;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Popup Manager is the class which handles all the logic for the popups
 * when one is open it closes it when something else is requesting to be open
 */
public class PopupManager
{
  Stage primaryStage;
  GUI gui;

  Node currentlyOpen;

  ProductBarDataDisplay pbDataDisplay;
  GraphDisplay graphDisplay;
  DiscardDisplay discardDisplay;

  /**
   * Constructor
   * @param gui which owns the popup manager
   */
  public PopupManager(GUI gui)
  {
    this.gui = gui;
    this.primaryStage = gui.getPrimaryStage();
    this.currentlyOpen = null;
  }

  /**
   * when Discard Display is clicked do the appropriate thing (close, open, close and open something else)
   */
  public void toggleDiscardDisplay()
  {
    if (currentlyOpen == null)
    {
      currentlyOpen = discardDisplay;
      discardDisplay.open();
    }
    else if (currentlyOpen == discardDisplay)
    {
      currentlyOpen = null;
      discardDisplay.close();
    }
    else if (currentlyOpen instanceof GraphDisplay)
    {
      currentlyOpen = discardDisplay;
      graphDisplay.close();
      discardDisplay.open();
    }
    else if (currentlyOpen instanceof ProductBarDataDisplay)
    {
      pbDataDisplay.close();
      pbDataDisplay.setDataNull();

      discardDisplay.open();
      currentlyOpen = discardDisplay;

      gui.getDraftLayout().getProductBar().unselectSelected();
    }
  }

  public Node isOpen()
  {
    return currentlyOpen;
  }
  
  
  /**
   * When graph display is clicked, do the appropriate thing (close, open, close and open something else)
   */
  public void toggleGraphDisplay()
  {
    if(gui.isDraftingPhase())
    {
      if (currentlyOpen == null)
      {
        currentlyOpen = graphDisplay;
        graphDisplay.open();
      } else if (currentlyOpen == graphDisplay)
      {
        currentlyOpen = null;
        graphDisplay.close();
      } else if (currentlyOpen instanceof ProductBarDataDisplay)
      {
        pbDataDisplay.close();
        pbDataDisplay.setDataNull();
        graphDisplay.open();
        currentlyOpen = graphDisplay;
        gui.getDraftLayout().getProductBar().unselectSelected();
      } else if (currentlyOpen instanceof DiscardDisplay)
      {
        discardDisplay.close();
        graphDisplay.open();
        currentlyOpen = graphDisplay;
      }
    }else
    {

    }
  }

  /**
   * When foodpopup is clicked, do the appropriate thing (close, open, close and open something else)
   * @param id  the id.
   */
  public void toggleFoodPopup(int id)
  {
    if (currentlyOpen == null)
    {
      pbDataDisplay.setData(id);
      pbDataDisplay.open();
      currentlyOpen = pbDataDisplay;
    }
    else if (currentlyOpen instanceof ProductBarDataDisplay)
    {
      if (pbDataDisplay.currentlyShowing.getID() == id)
      {
        currentlyOpen = null;
        pbDataDisplay.close();
        pbDataDisplay.setDataNull();
      }
      else
      {
        pbDataDisplay.setData(id);
      }
    }
    else if (currentlyOpen instanceof GraphDisplay)
    {
      graphDisplay.close();
      pbDataDisplay.setData(id);
      pbDataDisplay.open();
      currentlyOpen = pbDataDisplay;
    }
    else if (currentlyOpen instanceof DiscardDisplay)
    {
      discardDisplay.close();
      pbDataDisplay.setData(id);
      pbDataDisplay.open();
      currentlyOpen = pbDataDisplay;
    }
  }

  public ProductBarDataDisplay getPBDataDisplay()
  {
	  return pbDataDisplay;
  }
  
  /**
   * sets the pbDataDisplay to the passed in pbDataDisplay
   * @param pbDataDisplay something
   */
  public void setPbDataDisplay(ProductBarDataDisplay pbDataDisplay)
  {
    this.pbDataDisplay = pbDataDisplay;
  }

  /**
   * sets the graphDisplay to the passed in pbDataDisplay
   * @param graphDisplay something
   */
  public void setGraphDisplay(GraphDisplay graphDisplay)
  {
    this.graphDisplay = graphDisplay;
  }

  /**
   * sets the discardDisplay to the passed in pbDataDisplay
   * @param discardDisplay something
   */
  public void setDiscardDisplay(DiscardDisplay discardDisplay)
  {
    this.discardDisplay = discardDisplay;
  }

}
