package starvationevasion.common;

/**
 * The game models the following 12 farm product categories.<br><br>
 * All farm products throughout the world used for food, animal
 * feed and/or biofuel are modeled as belonging to one of the above 12 categories.<br><br>
 * Farm products not modeled include Lumber, Cotton, and Wool.
 */
public enum EnumFood
{
  CITRUS
  {
    public String toString() {return "Citrus Fruits";}
    public String toLongString() {return "Grapefruit, Lemons, Oranges, Tangerines, ...)";}
    public boolean isCrop() {return true;}
  },

  FRUIT
  {
    public String toString() {return "Non-Citrus Fruits";}
    public String toLongString() {return "Apples, Apricots, Avocados, Cherries, Grapes, Olives, Strawberries, ...";}
    public boolean isCrop() {return true;}
  },

  NUT
  {
    public String toString() {return "Nuts";}
    public String toLongString() {return "Almonds, Pecans, Pistachios, Walnuts, ...";}
    public boolean isCrop() {return true;}
  },

  GRAIN
  {
    public String toString() {return "Grains";}
    public String toLongString() {return "Rice, Wheat, ...";}
    public boolean isCrop() {return true;}
  },

  OIL
  {
    public String toString() {return "Oil Crops";}
    public String toLongString() {return "Safflower, Sunflower, ...";}
    public boolean isCrop() {return true;}
  },

  VEGGIES
  {
    public String toString() {return "Vegetables";}
    public String toLongString() {return "Beans, Potatoes, Carrots, Celery, Sweet Corn, Cucumbers, Pumpkins, Onions, Peppers, Tomatoes, Cantaloupe, Watermelon, ...";}
    public boolean isCrop() {return true;}
  },

  SPECIAL
  {
    public String toString() {return "Specialty Crops";}
    public String toLongString() {return "Sugar beets, Mint, Mushrooms, Honey, Miscellaneous crops";}
    public boolean isCrop() {return true;}
  },

  FEED
  {
    public String toString() {return "Feed Crops";}
    public String toLongString() {return "Barley, Corn, Hay, Oats, Alfalfa, ...";}
    public boolean isCrop() {return true;}
  },

  FISH
  {
    public String toString() {return "Fish";}
    public String toLongString() {return "Fresh and salt water, wild caught and farm raised";}
    public boolean isCrop() {return false;}
  },

  MEAT
  {
    public String toString() {return "Meat Animals";}
    public String toLongString() {return "Cattle, Hogs, Lamb...";}
    public boolean isCrop() {return false;}
  },

  POULTRY
  {
    public String toString() {return "Poultry and Eggs";}
    public String toLongString() {return "Chickens, Turkeys, eggs, ...";}
    public boolean isCrop() {return false;}
  },

  DAIRY
  {
    public String toString() {return "Dairy Products";}
    public String toLongString() {return "Milk and Cheese";}
    public boolean isCrop() {return false;}
  };

  public static final int SIZE = values().length;
  public abstract String toLongString();

  /**
   * Use when need to distinguish between crop and non-crop foods.
   * For example, it does not make sense to apply fertilizer to meat, poultry or dairy.
   * @return true if the food is a crop.
   */
  public abstract boolean isCrop();
}
