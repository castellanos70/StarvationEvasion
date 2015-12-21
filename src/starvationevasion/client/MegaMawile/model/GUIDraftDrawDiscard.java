package starvationevasion.client.MegaMawile.model;

import javafx.scene.image.ImageView;
import starvationevasion.client.MegaMawile.view.PolicyCardImageManager;
import starvationevasion.common.PolicyCard;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used to keep track of cards to display in the gui of what cards that are drafted, discard/drawn, and discarded. Keeps
 * counters of cards, does not validate. Maps images to their respective place in the hash map to be pulled later.
 */
public class GUIDraftDrawDiscard
{
  public enum MoveType
  {
    DISCARD, DRAFT, DISCARDDRAW, NONE;
  }
  private HashMap<ImageView, PolicyCard> imagePolicyMap = new HashMap<>();
  private ArrayList<ImageView> actionTableList= new ArrayList<>(), handImageArray=new ArrayList<>();
  private MoveType currentMove = MoveType.NONE;
  private int actionTableCount = 0;
  private PolicyCardImageManager policyCardImageManager = new PolicyCardImageManager();

  public ArrayList<ImageView> getActionTableList()
  {
    return actionTableList;
  }

  public HashMap<ImageView, PolicyCard> getImagePolicyMap()
  {
    return imagePolicyMap;
  }

  public ArrayList<ImageView> getHandImageArray()
  {
    return handImageArray;
  }

  public void setActionTableList(ArrayList<ImageView> actionTableList)
  {
    this.actionTableList = actionTableList;
  }

  public void setImagePolicyMap(HashMap<ImageView, PolicyCard> imagePolicyMap)
  {
    this.imagePolicyMap = imagePolicyMap;
  }

  public void setHandImageArray(ArrayList<ImageView> handImageArray)
  {
    this.handImageArray = handImageArray;
  }

  public void reset()
  {
    for (ImageView imageView : actionTableList)
    {
      imageView.setImage(null);
      imagePolicyMap.put(imageView,null);
    }
    actionTableCount = 0;
    currentMove = MoveType.NONE;
  }

  public void setCurrentMove(MoveType currentMove)
  {
    this.currentMove = currentMove;
  }

  public MoveType getCurrentMove()
  {
    return currentMove;
  }

  /**
   * Takes an ImageView that can be used with policy card to setup the map and set up the image of the action image
   * @param policyCard
   * @return
   */
  public boolean addToActionTable(PolicyCard policyCard)
  {
    if(currentMove == MoveType.DISCARDDRAW)
    {
      if(actionTableCount<3)
      {
        ImageView imageView = actionTableList.get(actionTableCount);
        imagePolicyMap.put(imageView, policyCard);
        imageView.setImage(policyCardImageManager.getPolicyCardImage(policyCard.getCardType()));
        actionTableCount++;
        return true;
      }
    }
    if(currentMove == MoveType.DISCARD || currentMove == MoveType.DRAFT)
    {
      if(actionTableCount <1)
      {
        ImageView imageView = actionTableList.get(actionTableCount);
        imagePolicyMap.put(imageView, policyCard);
        imageView.setImage(policyCardImageManager.getPolicyCardImage(policyCard.getCardType()));
        actionTableCount++;
        return true;
      }
    }
    return false;
  }
}
