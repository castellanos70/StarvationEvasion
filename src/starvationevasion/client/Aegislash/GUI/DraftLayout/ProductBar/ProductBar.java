package starvationevasion.client.Aegislash.GUI.DraftLayout.ProductBar;

import starvationevasion.client.Aegislash.GUI.GUI;
import javafx.stage.Stage;
import starvationevasion.common.EnumFood;

import java.util.ArrayList;

/**
 * ProductBar is the starvationevasion.client.Aegislash.GUI element responsible for displaying the Food Product Icons on the bottom of the starvationevasion.client.Aegislash.GUI
 * When a user selects a product icon, it should be highlighted.
 * A user should only be able to select one product at a time, or none at all.
 */
public class ProductBar
{
  Stage primaryStage;
  GUI gui;

  ArrayList<EnumFood> productList;
  ArrayList<ProductBarElement> elements;

  int selectedIndex = -1;

  /**
   * Constructor for the Product Bar
   * Takes the starvationevasion.client.Aegislash.GUI which owns it
   * @param gui
   */
  public ProductBar(GUI gui)
  {
    this.gui = gui;
    int productListSize;
    productList = gui.getProductList();
    primaryStage = gui.getPrimaryStage();

    double elementWidth = gui.getBoxWidth();
    double elementHeight = gui.getBoxHeight();

    productListSize = productList.size();

    elements = new ArrayList<>();
    for (int i = 0; i < productListSize; ++i)
    {
      EnumFood food = productList.get(i);
      elements.add(new ProductBarElement(gui, food, i, elementWidth, elementHeight, this));
    }
  }

  /**
   * Get the array of elements
   * @return array of elements
   */
  public ArrayList<ProductBarElement> getElements()
  {
    return elements;
  }

  /**
   * Unselect the selected element
   */
  public void unselectSelected()
  {
    if (selectedIndex != -1)
    {
      elements.get(selectedIndex).press();
      selectedIndex = -1;
    }
  }

  /**
   * Presses an element (changes its drop shaddow)
   * @param elementID element to press
   */
  public void pressElement(int elementID)
  {
    if (selectedIndex == -1)
    {
      selectedIndex = elementID;
      elements.get(elementID).press();
    }
    else if (elementID == selectedIndex)
    {
      selectedIndex = -1;
      elements.get(elementID).press();
    }
    else
    {
      elements.get(selectedIndex).press();
      selectedIndex = elementID;
      elements.get(selectedIndex).press();
    }
  }

  /**
   *
   * @return the selectedElement
   */
  public EnumFood getSelectedElement()
  {
    if (selectedIndex == -1)
    {
      return null;
    }
    else
    {
      return productList.get(selectedIndex);
    }
  }


}
