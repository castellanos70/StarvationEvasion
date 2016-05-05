package starvationevasion.client.GUI.votingHud;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class VotnigButtonNode extends NodeTemplate
{
    VotingButton up;
    VotingButton abs;
    VotingButton down;
    VotingButton[] buttons = new VotingButton[3];

    public VotnigButtonNode()
    {
        System.out.println("VotingButtonNode");
        buttons[0] = new VotingButton("up");
        buttons[1] = new VotingButton("abs");
        buttons[2] = new VotingButton("down");

        double width = 10;
        double height = 10;

        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i].setManaged(false);
            buttons[i].setTranslateY(i * height);
            this.getChildren().add(buttons[i]);
        }

        onResize();
        buttons[0] = new VotingButton("up");
        buttons[1] = new VotingButton("abs");
        buttons[2] = new VotingButton("down");
    }

    @Override
    public void onResize()
    {
        double spacing = 50;

        for (int i = 0; i < buttons.length; i++)
        {
            VotingButton vb = buttons[i];
            if (vb == null) return;
            vb.setSize(spacing, spacing);
            buttons[i].setLayoutX(0);
            buttons[i].setLayoutY(spacing * i);

        }
        // TODO Auto-generated method stub

    }

}
