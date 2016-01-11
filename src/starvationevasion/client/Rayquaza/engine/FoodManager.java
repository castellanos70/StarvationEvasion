/**
 * @author Mohammad R. Yousefi
 * Essentially a resource bundle for the EnumFood for the values used by the client.
 * @param SMALL_ICON The size of the image assigned to the image path.
 * @param LARGE_ICON The size of the image assigned to the image path.
 * @param FOOD_ID The default prefix id for javaFX nodes used in the program. Can use any string prefixed with the
 * prefix to get the appropriate EnumFood.
 */
package starvationevasion.client.Rayquaza.engine;

import starvationevasion.common.EnumFood;

import java.util.*;

public class FoodManager
{
  public final static int SMALL_ICON = 64;
  public final static int LARGE_ICON = 256;

  public final static String[] FOOD_ID = {"foodStockCI", "foodStockDA", "foodStockFE", "foodStockFI", "foodStockFR",
      "foodStockGR", "foodStockME", "foodStockNU", "foodStockOI", "foodStockPO", "foodStockSP", "foodStockVE"};

  private final static Map<EnumFood, String> foodImagePaths64;
  private final static Map<EnumFood, String> foodImagePaths256;
  private final static Map<String, EnumFood> foods;
  private final static Map<EnumFood, String> displayNames;
  private final static Map<EnumFood, String> foodId;

  static
  {
    foods = new HashMap<>();
    foods.put(FOOD_ID[0], EnumFood.CITRUS);
    foods.put(FOOD_ID[1], EnumFood.DAIRY);
    foods.put(FOOD_ID[2], EnumFood.FEED);
    foods.put(FOOD_ID[3], EnumFood.FISH);
    foods.put(FOOD_ID[4], EnumFood.FRUIT);
    foods.put(FOOD_ID[5], EnumFood.GRAIN);
    foods.put(FOOD_ID[6], EnumFood.MEAT);
    foods.put(FOOD_ID[7], EnumFood.NUT);
    foods.put(FOOD_ID[8], EnumFood.OIL);
    foods.put(FOOD_ID[9], EnumFood.POULTRY);
    foods.put(FOOD_ID[10], EnumFood.SPECIAL);
    foods.put(FOOD_ID[11], EnumFood.VEGGIES);

    displayNames = new HashMap<>();
    displayNames.put(EnumFood.CITRUS, "Citrus");
    displayNames.put(EnumFood.DAIRY, "Dairy");
    displayNames.put(EnumFood.FEED, "Feed");
    displayNames.put(EnumFood.FISH, "Fish");
    displayNames.put(EnumFood.FRUIT, "Fruit");
    displayNames.put(EnumFood.GRAIN, "Grain");
    displayNames.put(EnumFood.MEAT, "Meat");
    displayNames.put(EnumFood.NUT, "Nuts");
    displayNames.put(EnumFood.OIL, "Oil");
    displayNames.put(EnumFood.POULTRY, "Poultry");
    displayNames.put(EnumFood.SPECIAL, "Special");
    displayNames.put(EnumFood.VEGGIES, "Veggies");

    foodImagePaths64 = new HashMap<>();
    foodImagePaths64.put(EnumFood.CITRUS, "farmProductIcons/FarmProduct_CITRUS_64x64.png");
    foodImagePaths64.put(EnumFood.DAIRY, "farmProductIcons/FarmProduct_DAIRY_64x64.png");
    foodImagePaths64.put(EnumFood.FEED, "farmProductIcons/FarmProduct_FEED_64x64.png");
    foodImagePaths64.put(EnumFood.FISH, "farmProductIcons/FarmProduct_FISH_64x64.png");
    foodImagePaths64.put(EnumFood.FRUIT, "farmProductIcons/FarmProduct_Fruit_64x64.png");
    foodImagePaths64.put(EnumFood.GRAIN, "farmProductIcons/FarmProduct_GRAIN_64x64.png");
    foodImagePaths64.put(EnumFood.MEAT, "farmProductIcons/FarmProduct_MEAT_64x64.png");
    foodImagePaths64.put(EnumFood.NUT, "farmProductIcons/FarmProduct_NUT_64x64.png");
    foodImagePaths64.put(EnumFood.OIL, "farmProductIcons/FarmProduct_OIL_64x64.png");
    foodImagePaths64.put(EnumFood.POULTRY, "farmProductIcons/FarmProduct_POULTRY_64x64.png");
    foodImagePaths64.put(EnumFood.SPECIAL, "farmProductIcons/FarmProduct_SPECIAL_64x64.png");
    foodImagePaths64.put(EnumFood.VEGGIES, "farmProductIcons/FarmProduct_VEGGIES_64x64.png");

    foodImagePaths256 = new HashMap<>();
    foodImagePaths256.put(EnumFood.CITRUS, "farmProductIcons/FarmProduct_CITRUS_256x256.png");
    foodImagePaths256.put(EnumFood.DAIRY, "farmProductIcons/FarmProduct_DAIRY_256x256.png");
    foodImagePaths256.put(EnumFood.FEED, "farmProductIcons/FarmProduct_FEED_256x256.png");
    foodImagePaths256.put(EnumFood.FISH, "farmProductIcons/FarmProduct_FISH_256x256.png");
    foodImagePaths256.put(EnumFood.FRUIT, "farmProductIcons/FarmProduct_Fruit_256x256.png");
    foodImagePaths256.put(EnumFood.GRAIN, "farmProductIcons/FarmProduct_GRAIN_256x256.png");
    foodImagePaths256.put(EnumFood.MEAT, "farmProductIcons/FarmProduct_MEAT_256x256.png");
    foodImagePaths256.put(EnumFood.NUT, "farmProductIcons/FarmProduct_NUT_256x256.png");
    foodImagePaths256.put(EnumFood.OIL, "farmProductIcons/FarmProduct_OIL_256x256.png");
    foodImagePaths256.put(EnumFood.POULTRY, "farmProductIcons/FarmProduct_POULTRY_256x256.png");
    foodImagePaths256.put(EnumFood.SPECIAL, "farmProductIcons/FarmProduct_SPECIAL_256x256.png");
    foodImagePaths256.put(EnumFood.VEGGIES, "farmProductIcons/FarmProduct_VEGGIES_256x256.png");

    foodId = new HashMap<>();
    foodId.put(EnumFood.CITRUS, "#foodStockCI");
    foodId.put(EnumFood.DAIRY, "#foodStockDA");
    foodId.put(EnumFood.FEED, "#foodStockFE");
    foodId.put(EnumFood.FISH, "#foodStockFI");
    foodId.put(EnumFood.FRUIT, "#foodStockFR");
    foodId.put(EnumFood.GRAIN, "#foodStockGR");
    foodId.put(EnumFood.MEAT, "#foodStockME");
    foodId.put(EnumFood.NUT, "#foodStockNU");
    foodId.put(EnumFood.OIL, "#foodStockOI");
    foodId.put(EnumFood.POULTRY, "#foodStockPO");
    foodId.put(EnumFood.SPECIAL, "#foodStockSP");
    foodId.put(EnumFood.VEGGIES, "#foodStockVE");
  }

  /**
   * Returns an EnumFood with by strings with prefixId
   *
   * @param foodId A string starting with values in FOOD_ID
   */
  public static EnumFood getEnumFoodById(String foodId)
  {
    for (String key : FOOD_ID)
    {
      if (foodId.startsWith(key)) return foods.get(key);
    }
    return null;
  }

  /**
   * Retrieves the path to the graphics image for the given crop and size.
   *
   * @param food     The Crop.
   * @param iconSize The sizes defined by SMALL_ICON and LARGE_ICON
   */
  public static String getImagePath(EnumFood food, int iconSize)
  {
    if (food == null) return null;
    else
    {
      if (iconSize == SMALL_ICON) return foodImagePaths64.get(food);
      if (iconSize == LARGE_ICON) return foodImagePaths256.get(food);
      return null;
    }
  }

  public static String getDisplayName(EnumFood food)
  {
    if (displayNames.containsKey(food)) return displayNames.get(food);
    else return "None";
  }

  public static String getLookUpPrefix(EnumFood food)
  {
    if (foodId.containsKey(food)) return foodId.get(food);
    else return null;
  }

  public static String getImagePath(String foodId, int iconSize)
  {
    return getImagePath(getEnumFoodById(foodId), iconSize);
  }
}
