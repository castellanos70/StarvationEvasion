package starvationevasion.client.MegaMawile2.view;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import starvationevasion.client.MegaMawile2.controller.VotePopupController;

import java.io.IOException;

/**
 * Stage for the PolicyCard voting popup.
 */
public class VotePopup extends Stage
{
    public VotePopup(VotePopupController voteController)
    {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("popups/VotePopup.fxml"));
        loader.setController(voteController);

        initStyle(StageStyle.UNDECORATED);

        TitledPane pane = null;
        try
        {
            pane = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        setScene(new Scene(pane));
        setAlwaysOnTop(true);
    }
}
