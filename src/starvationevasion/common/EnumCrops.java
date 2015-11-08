package starvationevasion.common;

/**
 * The game models the following 12 farm product categories.<br><br>
 * All farm products throughout the world used for food, animal
 * feed and/or biofuel are modeled as belonging to one of the above 12 categories.<br><br>
 * Farm products not modeled include Lumber, Cotton, and Wool.
 */
public enum EnumCrops
{
  CITRUS
  {
    public String toString() {return "Citrus Fruits";}
    public String toLongString() {return "Grapefruit, Lemons, Oranges, Tangerines, ...)";}
  },

  FRUIT
  {
    public String toString() {return "Non-Citrus Fruits";}
    public String toLongString() {return "Apples, Apricots, Avocados, Cherries, Grapes, Olives, Strawberries, ...";}
  },

  NUT
  {
    public String toString() {return "Nuts";}
    public String toLongString() {return "Almonds, Pecans, Pistachios, Walnuts, ...";}
  },

  GRAIN
  {
      public String toString() {return "Grains";}
      public String toLongString() {return "Rice, Wheat, ...";}
  },

  OIL
  {
    public String toString() {return "Oil Crops";}
    public String toLongString() {return "Safflower, Sunflower, ...";}
  },

  VEGGIES
  {
    public String toString() {return "Vegetables";}
    public String toLongString() {return "Beans, Potatoes, Carrots, Celery, Sweet Corn, Cucumbers, Pumpkins, Onions, Peppers, Tomatoes, Cantaloupe, Watermelon, ...";}
  },

  SPECIAL
  {
    public String toString() {return "Specialty Crops";}
    public String toLongString() {return "Sugar beets, Mint, Mushrooms, Honey, Miscellaneous crops";}
  },

  FEED
  {
    public String toString() {return "Feed Crops";}
    public String toLongString() {return "Barley, Corn, Hay, Oats, Alfalfa, ...";}
  },

  FISH
  {
    public String toString() {return "Fish";}
    public String toLongString() {return "Fresh and salt water, wild caught and farm raised";}
  },

  MEAT
  {
    public String toString() {return "Meat Animals";}
    public String toLongString() {return "Cattle, Hogs, Lamb...";}
  },

  POULTRY
  {
    public String toString() {return "Poultry and Eggs";}
    public String toLongString() {return "Chickens, Turkeys, eggs, ...";}
  },

  DAIRY
  {
    public String toString() {return "Dairy Products";}
    public String toLongString() {return "Milk and Cheese";}
  };

  public static final int SIZE = values().length;
  public abstract String toLongString();
}
