package starvationevasion.common;

import java.util.ResourceBundle;

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
    // public String toString() { return res.getString(name() + ".shortName"); } // "Citrus Fruits"
    // public String toLongString() { return res.getString(name() + ".longName");}  // "Grapefruit, Lemons, Oranges, Tangerines, ...)"
    public boolean isCrop() {return true;}
  },

  FRUIT
  {
    public boolean isCrop() {return true;}
  },

  NUT
  {
    public boolean isCrop() {return true;}
  },

  GRAIN
  {
    public boolean isCrop() {return true;}
  },

  OIL
  {
    public boolean isCrop() {return true;}
  },

  VEGGIES
  {
    public boolean isCrop() {return true;}
  },

  SPECIAL
  {
    public boolean isCrop() {return true;}
  },

  FEED
  {
    public boolean isCrop() {return true;}
  },

  FISH
  {
    public boolean isCrop() {return false;}
  },

  MEAT
  {
    public boolean isCrop() {return false;}
  },

  POULTRY
  {
    public boolean isCrop() {return false;}
  },

  DAIRY
  {
    public boolean isCrop() {return false;}
  };

  /** Resources for the default locale */
  private final ResourceBundle res = ResourceBundle.getBundle("starvationevasion.common.locales.strings");
  private final String shortName = res.getString("EnumFood." + name() + ".shortName"); // E.g. "Citrus Fruits"
  private final String longName = res.getString("EnumFood." + name() + ".longName"); // E.g. "Grapefruit, Lemons, Oranges, Tangerines, ...)"

  public static final int SIZE = values().length;
  public String toString() { return shortName; }
  public String toLongString() { return longName; }

  /**
   * Use when need to distinguish between crop and non-crop foods.
   * For example, it does not make sense to apply fertilizer to meat, poultry or dairy.
   * @return true if the food is a crop.
   */
  public abstract boolean isCrop();
}
