package starvationevasion.client.GUI.VotingLayout;

import javafx.scene.layout.StackPane;
import starvationevasion.client.GUI.DraftLayout.CardView;
import starvationevasion.common.PolicyCard;

/**
 * Created by Nathan on 8/6/2016.
 * A node that will show the currently selected card
 */
public class CardViewNode extends StackPane {
    public  CardViewNode(){

    }
    public void addCard(PolicyCard card){
        this.getChildren().clear();
        this.getChildren().add(new CardView(card.getCardType()));
    }


}
