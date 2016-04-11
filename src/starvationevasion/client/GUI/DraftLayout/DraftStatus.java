package starvationevasion.client.GUI.DraftLayout;

import starvationevasion.client.Logic.geography.MapUtil;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import starvationevasion.common.EnumRegion;

/**
 * DraftStatus is the GUI element responsible for showing the player the current state of the DraftPhase
 * It has 7 circles, each of which correspond by their color to a region on the US map
 * When a region drafts a card, this number should be updated by the corresponding colored scirle
 * It is not currently working
 *
 */
public class DraftStatus extends GridPane
{

  public DraftStatus()
  {

    this.getStylesheets().add("/starvationevasion/client/GUI/DraftLayout/style.css");
    this.getStyleClass().add("draftstatus");
    int[] draftedCard=new int[7];
    for (int i = 1; i <=7 ; i++)
    {
      Circle regionCircle=new Circle(20);
      Label playedCards=new Label(String.valueOf(draftedCard[i-1]));
      playedCards.setFont(Font.font("helvetica", FontWeight.BOLD,18));
      switch (i)
      {
        case 1:
          regionCircle.setFill(MapUtil.getRegionColor(EnumRegion.US_REGIONS[i-1]));
          break;
        case 2:
          regionCircle.setFill(MapUtil.getRegionColor(EnumRegion.US_REGIONS[i-1]));
          break;
        case 3:
          regionCircle.setFill(MapUtil.getRegionColor(EnumRegion.US_REGIONS[i-1]));
          break;
        case 4:
          regionCircle.setFill(MapUtil.getRegionColor(EnumRegion.US_REGIONS[i - 1]));
          break;
        case 5:
          regionCircle.setFill(MapUtil.getRegionColor(EnumRegion.US_REGIONS[i-1]));
          break;
        case 6:
          regionCircle.setFill(MapUtil.getRegionColor(EnumRegion.US_REGIONS[i - 1]));
          break;
        case 7:
          regionCircle.setFill(MapUtil.getRegionColor(EnumRegion.US_REGIONS[i - 1]));
          break;
      }
      this.add(playedCards,1,i);
      this.add(regionCircle, 0, i);
    }
  }


}