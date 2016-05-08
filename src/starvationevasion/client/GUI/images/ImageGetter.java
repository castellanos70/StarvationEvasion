package starvationevasion.client.GUI.images;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import starvationevasion.common.EnumFood;
import starvationevasion.common.gamecards.EnumPolicy;

/**
 * Created by jmweisburd on 11/15/15.
 */
public class ImageGetter implements ImageConstants
{
  private static Image worldMap;
  private static Image citrus64;
  private static Image diary64;
  private static Image feed64;
  private static Image fish64;
  private static Image fruit64;
  private static Image grain64;
  private static Image meat64;
  private static Image nut64;
  private static Image oil64;
  private static Image poultry64;
  private static Image special64;
  private static Image veggies64;

  private static Image citrus256;
  private static Image diary256;
  private static Image feed256;
  private static Image fish256;
  private static Image fruit256;
  private static Image grain256;
  private static Image meat256;
  private static Image nut256;
  private static Image oil256;
  private static Image poultry256;
  private static Image special256;
  private static Image veggies256;

  private static Image graphLeftArrowBig;
  private static Image graphRightArrowBig;

  private static Image discardLeftArrowSmall;
  private static Image discardRightArrowSmall;
  
  private static Image chatToggle;
  private static Image coin;

  private Image undoButton;
  private ImageView voteIcon;

  private Image background;
  
  private static final String ERROR_CARD_ART_NOT_AVAILABLE_IMAGE_NAME = "Error_CardArtNotAvailable.png";
  
  private static final int INIT_CARD_HEIGHT_SCALE_FACTOR = 8;
  private static final int INIT_CARD_WIDTH_SCALE_FACTOR = 8;

  /**
   * Default constructor for the imagegetter
   */
  public ImageGetter()
  {
    initialize64();
    initialize256();
    worldMap = new Image("WorldMap_MollweideProjection.png");
    graphLeftArrowBig = new Image("ActionButtons/leftArrowBig.png");
    graphRightArrowBig = new Image("ActionButtons/rightArrowBig.png");
    discardLeftArrowSmall = new Image("ActionButtons/leftArrowSmall.png");
    discardRightArrowSmall = new Image("ActionButtons/rightArrowSmall.png");

    chatToggle = new Image("ActionButtons/chatToggle.png");
    coin = new Image("ActionButtons/coin.png");
    
    undoButton = new Image("ActionButtons/undoResized.png");
    voteIcon = new ImageView("cardImages/vote.png");



    background = new Image("background.png");
  }

  /**
   * TODO: replace this long switch with an array index --Joel
   * Gets the food image of the EnumFood passed in 256x256
   * 
   * @param type
   *          food type
   * @return image of food type
   */
  public Image getImageForFoodType256(EnumFood type)
  {
    switch (type)
    {
      case CITRUS:
        return citrus256;
      case DAIRY:
        return diary256;
      case FEED:
        return feed256;
      case FISH:
        return fish256;
      case FRUIT:
        return fruit256;
      case GRAIN:
        return grain256;
      case MEAT:
        return meat256;
      case NUT:
        return nut256;
      case OIL:
        return oil256;
      case POULTRY:
        return poultry256;
      case SPECIAL:
        return special256;
      case VEGGIES:
        return veggies256;
      default:
        return citrus256;
    }
  }

  /**
   * Gets the food image of the EnumFood passed in 64x64
   *  TODO: replace this long switch with an array index --Joel
   * @param type
   *          food type
   * @return image of food type
   */
  public Image getImageForFoodType64(EnumFood type)
  {
    switch (type)
    {
      case CITRUS:
        return citrus64;
      case DAIRY:
        return diary64;
      case FEED:
        return feed64;
      case FISH:
        return fish64;
      case FRUIT:
        return fruit64;
      case GRAIN:
        return grain64;
      case MEAT:
        return meat64;
      case NUT:
        return nut64;
      case OIL:
        return oil64;
      case POULTRY:
        return poultry64;
      case SPECIAL:
        return special64;
      case VEGGIES:
        return veggies64;
      default:
        return citrus64;
    }
  }

  public Image getChatToggleImage()
  {
	  return chatToggle;
  }
  
  public Image getCoin()
  {
	  return coin;
  }
  
  
  public Image getWorldMap()
  {
    return worldMap;
  }

  public Image getBackground()
  {
    return background;
  }

  public Image getGraphLeftArrowBig()
  {
    return graphLeftArrowBig;
  }

  public Image getGraphRightArrowBig()
  {
    return graphRightArrowBig;
  }

  public Image getDiscardLeftArrowSmall()
  {
    return discardLeftArrowSmall;
  }

  public Image getDiscardRightArrowSmall()
  {
    return discardRightArrowSmall;
  }

  /**
   * Get the undo button image
   * 
   * @return undo button image
   */
  public Image getUndoButton()
  {
    return undoButton;
  }

  /**
   * Get the vote icon image
   * 
   * @return vote icon image
   */
  public ImageView getVoteIcon()
  {
    return voteIcon;
  }

  /**
   * Gets the image for the cards
   * TODO: do not read and create a new image each time you want to use it.
   *    Load and create card images at start (or on first use) Then keep. --Joel
   * @param policy
   *          policy card you want to get an image for
   * @return image of the policy card
   */
  public static ImageView getImageForCard(EnumPolicy policy)
  {
    String desiredPath = policy.getImagePath(); 
    desiredPath = desiredPath.replace("\\", System.getProperty("file.separator")); // Double-escaped to be "\\" in regex
    desiredPath = desiredPath.replace("/", System.getProperty("file.separator")); // Handle alternate case, escaped to be "/" in regex
    try
    {
      return new ImageView(new Image(desiredPath, ImageConstants.INIT_CARD_WIDTH * INIT_CARD_WIDTH_SCALE_FACTOR,
          ImageConstants.INIT_CARD_HEIGHT * INIT_CARD_HEIGHT_SCALE_FACTOR, false, false));
    } catch (Exception e)
    {
      // Don't hard-code the policy card image folder, instead replace the card name with the error card name
      String pathToErrorCard = desiredPath.substring(0, desiredPath.lastIndexOf(System.getProperty("file.separator")));
      pathToErrorCard += System.getProperty("file.separator") + ERROR_CARD_ART_NOT_AVAILABLE_IMAGE_NAME;
      
      return new ImageView(new Image(pathToErrorCard, ImageConstants.INIT_CARD_WIDTH * INIT_CARD_WIDTH_SCALE_FACTOR,
        ImageConstants.INIT_CARD_HEIGHT * INIT_CARD_HEIGHT_SCALE_FACTOR, false, false));
    }

  }

  // TODO: replace an equation that loads all images into an array. --Joel
  private void initialize64()
  {
    citrus64 = new Image("farmProductIcons/FarmProduct_CITRUS_64x64.png");
    diary64 = new Image("farmProductIcons/FarmProduct_DAIRY_64x64.png");
    feed64 = new Image("farmProductIcons/FarmProduct_FEED_64x64.png");
    fish64 = new Image("farmProductIcons/FarmProduct_FISH_64x64.png");
    fruit64 = new Image("farmProductIcons/FarmProduct_FRUIT_64x64.png");
    grain64 = new Image("farmProductIcons/FarmProduct_GRAIN_64x64.png");
    meat64 = new Image("farmProductIcons/FarmProduct_MEAT_64x64.png");
    nut64 = new Image("farmProductIcons/FarmProduct_NUT_64x64.png");
    oil64 = new Image("farmProductIcons/FarmProduct_OIL_64x64.png");
    poultry64 = new Image("farmProductIcons/FarmProduct_POULTRY_64x64.png");
    special64 = new Image("farmProductIcons/FarmProduct_SPECIAL_64x64.png");
    veggies64 = new Image("farmProductIcons/FarmProduct_VEGGIES_64x64.png");
  }


  private void initialize256()
  {
    citrus256 = new Image("farmProductIcons/FarmProduct_CITRUS_256x256.png");
    diary256 = new Image("farmProductIcons/FarmProduct_DAIRY_256x256.png");
    feed256 = new Image("farmProductIcons/FarmProduct_FEED_256x256.png");
    fish256 = new Image("farmProductIcons/FarmProduct_FISH_256x256.png");
    fruit256 = new Image("farmProductIcons/FarmProduct_FRUIT_256x256.png");
    grain256 = new Image("farmProductIcons/FarmProduct_GRAIN_256x256.png");
    meat256 = new Image("farmProductIcons/FarmProduct_MEAT_256x256.png");
    nut256 = new Image("farmProductIcons/FarmProduct_NUT_256x256.png");
    oil256 = new Image("farmProductIcons/FarmProduct_OIL_256x256.png");
    poultry256 = new Image("farmProductIcons/FarmProduct_POULTRY_256x256.png");
    special256 = new Image("farmProductIcons/FarmProduct_SPECIAL_256x256.png");
    veggies256 = new Image("farmProductIcons/FarmProduct_VEGGIES_256x256.png");
  }

}
