package starvationevasion.common;

import com.oracle.javafx.jmx.json.JSONDocument;
import javafx.scene.image.Image;
import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;

import java.util.ResourceBundle;

/**
 * The game models the following 12 farm product categories.<br><br>
 * All farm products throughout the world used for food, animal
 * feed and/or biofuel are modeled as belonging to one of the above 12 categories.<br><br>
 * Farm products not modeled include Lumber, Cotton, and Wool.<br><br>
 *
 * The attempt is to group foods by two criteria:
 * <ol>
 *   <li>Catagory boundaries that corrospond to boundaries for which we can find data and </li>
 *   <li>Nuturional role in the human diat.</li>
 * </ol>
 */
public enum EnumFood implements Sendable
{
  CITRUS
  {
    public String toString() {return "Citrus Fruits";};
    public String toLongString() {return "Grapefruit, Lemons, Oranges, Pineapples, ...";};
    public boolean isCrop() {return true;}
  },

  FRUIT
  {
    public String toString() {return "Non-Citrus Fruits";};
    public String toLongString() {return "Apples, Apricots, Avocados, Cherries, Grapes, Olives, Strawberries, ...";};
    public boolean isCrop() {return true;}
  },

  NUT
  {
    public String toString() {return "Nuts";};
    public String toLongString() {return "Almonds, Pecans, Pistachios, Walnuts, ...";};
    public boolean isCrop() {return true;}
  },

  GRAIN
  {
    public String toString() {return "Grains";};
    public String toLongString() {return "Rice, Wheat, Corn, Soybean, ...";};
    public boolean isCrop() {return true;}
  },

  OIL
  {
    public String toString() {return "Seed Oil";};
    public String toLongString() {return "Safflower, Sunflower, Canola...";};
    public boolean isCrop() {return true;}
  },

  VEGGIES
  {
    public String toString() {return "";};
    public String toLongString() {return "";};
    public boolean isCrop() {return true;}
  },

  SPECIAL
  {
    public String toString() {return "Vegetables";};
    public String toLongString() {return "Beans, Potatoes, Carrots, Celery, Cucumbers, Pumpkins, Onions, Peppers, Tomatoes, Cantaloupe, Watermelon, ...";};
    public boolean isCrop() {return true;}
  },

  FEED
  {
    public String toString() {return "Feed";};
    public String toLongString() {return "Barley, Corn, Hay, Oats, Alfalfa, ...";};
    public boolean isCrop() {return true;}
  },

  FISH
  {
    public String toString() {return "Fish";};
    public String toLongString() {return "Fresh and salt water, wild caught and farm raised";};
    public boolean isCrop() {return false;}
  },

  MEAT
  {
    public String toString() {return "Red Meat";};
    public String toLongString() {return "Beef, Pork, Mutten, ...";};
    public boolean isCrop() {return false;}
  },

  POULTRY
  {
    public String toString() {return "Poultry";};
    public String toLongString() {return "Chicken, Turkey, ...";};
    public boolean isCrop() {return false;}
  },

  DAIRY
  {
    public String toString() {return "Dairy";};
    public String toLongString() {return "Milk, Cheese, Eggs and Butter";};
    public boolean isCrop() {return false;}
  };

  /**
   * Array of all crop foods used in the game.
   */
  public static final EnumFood[] CROP_FOODS =
  { CITRUS, FRUIT, NUT, GRAIN, OIL, VEGGIES, SPECIAL, FEED
  };

  /**
   * Array of all non-crop foods used in game.
   */
  public static final EnumFood[] NON_CROP_FOODS =
  { FISH, MEAT, POULTRY, DAIRY,
  };
  
  public static final EnumFood[] ALL_FOODS =
  { CITRUS, FRUIT, NUT, GRAIN, OIL, VEGGIES, SPECIAL, FEED,
	FISH, MEAT, POULTRY, DAIRY,
  };

  public static final int SIZE = values().length;

  /**
   * @return test name of food type.
   */
  public abstract String toString();

  /**
   *
   * @return text name of food type with example foods of type.
   */
  public abstract String toLongString();

  /**
   * @return file path of small icon.
   */
  public String getIconPathSmall() { return "assets/farmProductIcons"+name()+"_64x64.png"; }


  /**
   * @return file path of large icon.
   */
  public String getIconPathLarge()  { return "assets/farmProductIcons"+name()+"_256x256.png";}

  /**
   * Before this can be called, the images must be loaded.
   * @return small icon.
   */
  public Image getIconLarge() { return imageLarge[ordinal()];}

  /**
   * Before this can be called, the images must be loaded.
   * @return large icon.
   */
  public Image getIconSmall() { return imageSmall[ordinal()]; }




  private static Image[] imageSmall = new Image[EnumFood.SIZE];
  private static Image[] imageLarge= new Image[EnumFood.SIZE];

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument _json = JSONDocument.createObject();
    _json.setString("name", name());
    _json.setBoolean("is-crop", isCrop());

    return _json;
  }

  @Override
  public void fromJSON (Object doc)
  {

  }

  @Override
  public Type getType ()
  {
    return Type.FOOD;
  }

  /**
   * Use when need to distinguish between crop and non-crop foods.
   * For example, it does not make sense to apply fertilizer to meat, poultry or dairy.
   * @return true if the food is a crop.
   */
  public abstract boolean isCrop();

  /**
   * Must be called once when program in initilized.
   */
  public static void loadIcons()
  {
    assert(imageSmall[0] == null); //As this method is called only once.

    for (EnumFood type : values())
    {
      int i = type.ordinal();
      imageSmall[i] = new Image(type.getIconPathSmall());
      imageLarge[i] = new Image(type.getIconPathLarge());
    }
  }

}


