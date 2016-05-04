package starvationevasion.client.GUI.votingHud;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import starvationevasion.client.GUI.ResizablePane;

public class VotingButton extends ResizablePane

{
	ImageView iv;

	public VotingButton(String str) {
		super(null, null);
		iv = new ImageView(new Image(
				getClass().getResource("/starvationevasion/GuiTestCode/resources/" + str + ".png").toString()));
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
