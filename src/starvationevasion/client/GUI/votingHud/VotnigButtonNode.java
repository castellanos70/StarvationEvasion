package starvationevasion.client.GUI.votingHud;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import starvationevasion.client.GUI.ResizablePane;

public class VotnigButtonNode extends ResizablePane
{
    File u = new File("src/starvationevasion/client/GUI/votingHud/testImages/up.png");
    ImageView uiv = new ImageView(new  Image(u.toURI().toString()));
    Button up = new Button("", uiv);
    File a = new File("src/starvationevasion/client/GUI/votingHud/testImages/abs.png");
    ImageView aiv = new ImageView(new  Image(a.toURI().toString()));
    Button abs = new Button("", aiv);
    File d = new File("src/starvationevasion/client/GUI/votingHud/testImages/down.png");
    ImageView div = new ImageView(new  Image(d.toURI().toString()));
    Button down = new Button("", div);
    Button[] buttons = new Button[3];
    VotingCard card;

    public VotnigButtonNode()
    {
        up.setOnAction(new EventHandler<ActionEvent>()
        {
            
            @Override
            public void handle(ActionEvent event)
            {
                System.out.println("Card gets +1 vote");
                
            }
        });
        abs.setOnAction(new EventHandler<ActionEvent>()
        {
            
            @Override
            public void handle(ActionEvent event)
            {
                System.out.println("Player abstains");
                
            }
        });
        down.setOnAction(new EventHandler<ActionEvent>()
        {
            
            @Override
            public void handle(ActionEvent event)
            {
                System.out.println("Card gets -1 vote");
                
            }
        });
        
        buttons[0] = up;
        buttons[1] = abs;
        buttons[2] = down;

        double width = 10;
        double height = 10;

        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i].setTranslateY(i * height);
            buttons[i].setStyle("-fx-background-color: transparent;");
            
            this.getChildren().add(buttons[i]);
        }

        onResize();
    }

    @Override
    public void onResize()
    {
        double spacing = 50;

        for (int i = 0; i < buttons.length; i++)
        {
            Button vb = buttons[i];
            if (vb == null) return;
            // vb.setSize(spacing, spacing);
            buttons[i].setLayoutX(-60);
            buttons[i].setTranslateY(10);
            buttons[i].setLayoutY(spacing * i);

        }
        // TODO Auto-generated method stub

    }

    public void setSize(double width, double height)
    {
        this.setWidth(width);
        this.setHeight(height);
    }
    public void setCard(VotingCard card)
    {
        this.card = card;
        System.out.println(card);
    }

}
