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

  private static Image graphLeftArrowBig;
  private static Image graphRightArrowBig;

  private static Image discardLeftArrowSmall;
  private static Image discardRightArrowSmall;
  
  private static Image chatToggle;
  private static Image coin;
  private static Image pop;
  private static Image HDI;
  private static Image crop;
  
  
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
    EnumFood.loadIcons();
    worldMap = new Image("WorldMap_MollweideProjection.png");
    graphLeftArrowBig = new Image("ActionButtons/leftArrowBig.png");
    graphRightArrowBig = new Image("ActionButtons/rightArrowBig.png");
    discardLeftArrowSmall = new Image("ActionButtons/leftArrowSmall.png");
    discardRightArrowSmall = new Image("ActionButtons/rightArrowSmall.png");

    chatToggle = new Image("ActionButtons/chatToggle.png");
    coin = new Image("ActionButtons/coin.png");
    
    undoButton = new Image("ActionButtons/undoResized.png");
    voteIcon = new ImageView("cardImages/vote.png");
    
    pop = new Image("ActionButtons/Pop.png");
    HDI = new Image("ActionButtons/HDI.png");
    crop = new Image("ActionButtons/crop.png");

    background = new Image("background.png");
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

  public Image getCropIcon()
  {
	  return crop;
  }
  
  public Image getPopIcon()
  {
	  return pop;
  }
  
  public Image getHDIIcon()
  {
	  return HDI;
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


}
