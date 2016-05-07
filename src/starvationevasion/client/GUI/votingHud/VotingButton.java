package starvationevasion.client.GUI.votingHud;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import starvationevasion.client.GUI.ResizablePane;

public class VotingButton extends ResizablePane
{
	ImageView iv;

  public VotingButton(String str)
  {
      System.out.println("VotingButton");
    File file = new File("src/starvationevasion/client/GUI/votingHud/testImages/" + str + ".png");
    Image cardImage = new Image(file.toURI().toString());
    iv = new ImageView(cardImage);
    iv.setManaged(false);
    this.getChildren().add(iv);
  }
  
  @Override
  public void onResize() {
    double width = iv.getImage().getWidth();
    double height = iv.getImage().getHeight();

    double scaleX = this.getWidth() / width;
    double scaleY = this.getHeight() / height;

    iv.setScaleX(scaleX);
    iv.setScaleY(scaleY);
  }
}
