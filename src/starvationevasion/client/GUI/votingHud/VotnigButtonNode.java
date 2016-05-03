package starvationevasion.client.GUI.votingHud;

import starvationevasion.client.GUI.ResizablePane;

public class VotnigButtonNode extends ResizablePane {
	VotingButton up;
	VotingButton abs;
	VotingButton down;
	VotingButton[] buttons = new VotingButton[3];

	public VotnigButtonNode() {
		super(null, null);
		buttons[0] = new VotingButton("up");
		buttons[1] = new VotingButton("abs");
		buttons[2] = new VotingButton("down");

		double height = 10;

		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setManaged(false);

			buttons[i].setTranslateY(i * height);
			System.out.println(i * height);
			this.getChildren().add(buttons[i]);
		}

		onResize();
	}

	@Override
	public void onResize() {

		for (int i = 0; i < buttons.length; i++) {
			VotingButton vb = buttons[i];
			if (vb == null)
				return;
			vb.setSize(50, 50);
			buttons[i].setLayoutX(0);
			buttons[i].setLayoutY(50 * i);

		}
		// TODO Auto-generated method stub

	}

}
