package starvationevasion.client.GUI.images;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumPolicy;

/**
 * Created by jmweisburd on 11/15/15.
 */
public class ImageGetter implements ImageConstants
{
  private static Image worldMap;
  private static Image regionWorldMap;
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

  private String leftArm;
  private String rightArm;
  private String head;
  private String leftLeg;
  private String rightLeg;
  private String wingedDragonOfRa;
  private String sliferTheSkyDragon;
  private String obeliskTheTormentor;
  private String kuriboh;
  private Image undoButton;
  private ImageView voteIcon;

  private Image background;

  /**
   * Default constructor for the imagegetter
   */
  public ImageGetter()
  {
    initialize64();
    initialize256();
    worldMap = new Image("WorldMap_MollweideProjection.png");
    regionWorldMap = new Image("WorldMap_MollweideProjection_With_Region_Boarders_Added.png");
    graphLeftArrowBig = new Image("ActionButtons/leftArrowBig.png");
    graphRightArrowBig = new Image("ActionButtons/rightArrowBig.png");
    discardLeftArrowSmall = new Image("ActionButtons/leftArrowSmall.png");
    discardRightArrowSmall = new Image("ActionButtons/rightArrowSmall.png");

    undoButton = new Image("ActionButtons/undoResized.png");
    voteIcon = new ImageView("cardImages/vote.png");

    leftArm = "cardImages/left2.jpg";
    rightArm = "cardImages/right2.jpg";
    head = "cardImages/head2.png";
    leftLeg = "cardImages/leftLeg.jpg";
    rightLeg = "cardImages/rightLeg.jpg";
    wingedDragonOfRa = "cardImages/wingedDragonOfRaw.jpg";
    sliferTheSkyDragon = "cardImages/slifer.jpg";
    obeliskTheTormentor = "cardImages/obeliskTheTormentor.jpg";
    kuriboh = "cardImages/kuriboh.jpg";

    background = new Image("background.png");
  }

  /**
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
   * 
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

  public Image getRegionWorldMap()
  {
	  return regionWorldMap;
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
   * 
   * @param policy
   *          policy card you want to get an image for
   * @return image of the policy card
   */
  public ImageView getImageForCard(EnumPolicy policy)
  {

    switch (policy)
    {
      case Clean_River_Incentive:
        return new ImageView(new Image(head, ImageConstants.INIT_CARD_WIDTH * 8,
            ImageConstants.INIT_CARD_HEIGHT * 8, false, false));
      case Fertilizer_Subsidy:
        return new ImageView(
            new Image(leftArm, ImageConstants.INIT_CARD_WIDTH * 8,
                ImageConstants.INIT_CARD_HEIGHT * 8, false, false));
      case Educate_the_Women_Campaign:
        return new ImageView(
            new Image(rightArm, ImageConstants.INIT_CARD_WIDTH * 8,
                ImageConstants.INIT_CARD_HEIGHT * 8, false, false));
      case Covert_Intelligence:
        return new ImageView(
            new Image(leftLeg, ImageConstants.INIT_CARD_WIDTH * 8,
                ImageConstants.INIT_CARD_HEIGHT * 8, false, false));
      case Efficient_Irrigation_Incentive:
        return new ImageView(
            new Image(rightLeg, ImageConstants.INIT_CARD_WIDTH * 8,
                ImageConstants.INIT_CARD_HEIGHT * 8, false, false));
      case Ethanol_Tax_Credit_Change:
        return new ImageView(
            new Image(wingedDragonOfRa, ImageConstants.INIT_CARD_WIDTH * 8,
                ImageConstants.INIT_CARD_HEIGHT * 8, false, false));
      case Foreign_Aid_for_Farm_Infrastructure:
        return new ImageView(
            new Image(obeliskTheTormentor, ImageConstants.INIT_CARD_WIDTH * 8,
                ImageConstants.INIT_CARD_HEIGHT * 8, false, false));
      case GMO_Seed_Insect_Resistance_Research:
        return new ImageView(
            new Image(sliferTheSkyDragon, ImageConstants.INIT_CARD_WIDTH * 8,
                ImageConstants.INIT_CARD_HEIGHT * 8, false, false));
      default:
        return new ImageView(
            new Image(kuriboh, ImageConstants.INIT_CARD_WIDTH * 8,
                ImageConstants.INIT_CARD_HEIGHT * 8, false, false));
    }

  }

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
