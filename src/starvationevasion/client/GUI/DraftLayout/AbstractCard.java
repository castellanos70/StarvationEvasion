package starvationevasion.client.GUI.DraftLayout;

import starvationevasion.client.GUI.ResizablePane;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

/**
 * Created by Dayloki on 7/2/2016.
 */
public abstract class AbstractCard extends ResizablePane{

//    public AbstractCard(EnumRegion owner, EnumPolicy policy){
//
//    }
    public boolean isDrafted;
    public boolean isDiscarded;
    public abstract PolicyCard getGameCard();
    public abstract EnumRegion getOwner();

    public abstract EnumPolicy getPolicy();
    

}
