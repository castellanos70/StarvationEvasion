package starvationevasion.client.GUI.DraftLayout;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.Logic.ChatManager;
import starvationevasion.client.Networking.Client;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.EnumPolicy;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * This is the node that displays and sends chat messages
 */
public class ChatNode extends BorderPane
{
  private ScrollPane chatFrame=new ScrollPane();
  private TextField inputMessage=new TextField();
  private Text chatMessages=new Text("welcome to chat");
  private Button confirm=new Button("confirm");
  private ToggleButton toggle = new ToggleButton();
  private HBox usersChat=new HBox();
  private VBox bottomContainer=new VBox();
  private Client client;
  private ChatManager chatManager;
  private ComboBox<EnumRegion> regionSelection;
  private ComboBox cardSelection;
  private EnumPolicy[] currentHand = new EnumPolicy[6];
  private int chatToggle = 0;
  private boolean votingMode = false;

  /**
   * Basic constructor
   * 
   * @param gui
   *          the main gui
   */
  public ChatNode(GUI gui)
  {
    ArrayList<EnumRegion> regions = new ArrayList<>(Arrays.asList(EnumRegion.US_REGIONS));
    regions.add(null);
    ObservableList<EnumRegion> regionList = FXCollections.observableArrayList(regions);
    regionSelection = new ComboBox<>(regionList);
    cardSelection = new ComboBox();
    client = gui.getClient();
    chatManager = client.getChatManager();
    usersChat.getChildren().add(toggle);
    usersChat.getChildren().add(confirm);
    usersChat.getChildren().add(regionSelection);
    usersChat.getChildren().add(cardSelection);
    bottomContainer.getChildren().add(inputMessage);
    bottomContainer.getChildren().add(usersChat);
    chatFrame.setContent(chatMessages);

    toggle.setGraphic(new ImageView(gui.getImageGetter().getChatToggleImage()));

    toggle.setOnMouseClicked(event1 ->
    {
      if (!votingMode)
      {
        if (toggle.isSelected())
        {
          this.getChildren().remove(chatFrame);

          gui.getDraftLayout().getChildren().remove(this);
          gui.getDraftLayout().add(this, 27, 17, 6, 1);
        }
        else
        {
          this.setCenter(chatFrame);
          gui.getDraftLayout().getChildren().remove(this);
          gui.getDraftLayout().add(this, 27, 7, 6, 11);
        }

      }
    });
    // chatFrame.setStyle("-fx-background-color:rgb(243,243,243)");
    // chatFrame.applyCss();
    this.setCenter(chatFrame);
    this.setBottom(bottomContainer);
    chatFrame.getStylesheets().add("/starvationevasion/client/GUI/DraftLayout/style.css");
    chatFrame.setStyle("-fx-background-color:rgb(255, 255, 255, 0.15)");
    this.setStyle("-fx-background-color:rgb(255, 255, 255, 0.15)");
    chatMessages.setStyle("-fx-background-color: blue;");
    inputMessage.setOnKeyPressed(event1 ->
    {
      if (event1.getCode().equals(KeyCode.ENTER))
      {
        sendMessage();
      }
    });
    confirm.setOnAction(event -> sendMessage());
  }

  /**
   * Sets hand of combo box chooser
   * 
   * @param newHand
   *          the new hand
   */
  public void setHand(EnumPolicy[] newHand)
  {
    if (currentHand != null && !currentHand.equals(newHand))
    {
      ArrayList<EnumPolicy> hand = new ArrayList<>(Arrays.asList(newHand));
      ObservableList<EnumPolicy> handList = FXCollections.observableArrayList(hand);
      cardSelection.setItems(handList);
      currentHand = newHand;
    }
  }


  /**
   * sets the entire chat log, used by chat manager
   * 
   * @param messages
   */
  public void setChatMessages(String messages)
  {
    chatMessages.setText(messages);
  }

  private void sendMessage()
  {
    if (!inputMessage.getText().equals(""))
    {
      // Sends too all regions if null
      if (regionSelection.getValue() == null)
      {
        for (EnumRegion region : EnumRegion.US_REGIONS)
        {
          chatManager.sendChatToServer(inputMessage.getText(), region,
              EnumPolicy.valueOf(cardSelection.getValue().toString()));
          chatMessages.setText(chatMessages.getText() + "\n" + inputMessage.getText());
        }
      }
      else
      {
        chatManager.sendChatToServer(inputMessage.getText(), regionSelection.getValue(), null);
        // chatManager.sendChatToServer(inputMessage.getText(),
        // regionSelection.getValue(),
        // EnumPolicy.valueOf(cardSelection.getValue().toString()));
        chatMessages.setText(chatMessages.getText() + "\n" + inputMessage.getText());
      }
      inputMessage.clear();
    }
  }

  public void setVotingMode(boolean voting)
  {
    votingMode = voting;
  }
}
