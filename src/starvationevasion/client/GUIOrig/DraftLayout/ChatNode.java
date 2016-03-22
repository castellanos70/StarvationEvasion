package starvationevasion.client.GUIOrig.DraftLayout;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import starvationevasion.client.Client;
import starvationevasion.client.GUIOrig.GUI;
import starvationevasion.client.Logic.ChatManager;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Dayloki on 3/9/2016.
 */
public class ChatNode extends BorderPane
{
  private ScrollPane chatFrame=new ScrollPane();
  private TextField inputMessage=new TextField();
  private Text chatMessages=new Text("welcome to chat");
  private Button confirm=new Button("confirm");
  private HBox usersChat=new HBox();
  private VBox bottomContainer=new VBox();
  private Client client;
  private ChatManager chatManager;

  private ComboBox regionSelection;
  private ComboBox cardSelection;
  private EnumPolicy[] currentHand=new EnumPolicy[6];

  public ChatNode(GUI gui)
  {
    ArrayList<EnumRegion> regions=new ArrayList<>(Arrays.asList(EnumRegion.US_REGIONS));
    regions.add(null);
    ObservableList<EnumRegion> regionList= FXCollections.observableArrayList(regions);
    regionSelection =new ComboBox(regionList);

    if(gui.getDraftLayout()!=null)
    {
      currentHand=gui.getDraftLayout().getHand().getHand();
      ArrayList<EnumPolicy> hand = new ArrayList<>(Arrays.asList(currentHand));
      ObservableList<EnumPolicy> handList= FXCollections.observableArrayList(hand);
      cardSelection =new ComboBox(handList);
    }else cardSelection=new ComboBox();


    client=gui.getClient();
    chatManager=client.getChatManager();

    usersChat.getChildren().add(confirm);
    usersChat.getChildren().add(regionSelection);
    usersChat.getChildren().add(cardSelection);
    bottomContainer.getChildren().add(inputMessage);
    bottomContainer.getChildren().add(usersChat);
    chatFrame.setContent(chatMessages);
    this.setCenter(chatFrame);
    this.setBottom(bottomContainer);

    inputMessage.setOnKeyPressed(event1 ->
    {
      if(event1.getCode().equals(KeyCode.ENTER))
      {
        sendMessage();
      }
    });
    confirm.setOnAction(event ->
    {
      sendMessage();
    });

  }
  public void setHand(EnumPolicy[] newHand)
  {
    if(currentHand!=null&&!currentHand.equals(newHand))
    {
      System.out.println("hell0"+newHand.toString());
      ArrayList<EnumPolicy> hand = new ArrayList<>(Arrays.asList(newHand));
      ObservableList<EnumPolicy> handList = FXCollections.observableArrayList(hand);
      cardSelection.setItems(handList);
      currentHand = newHand;
    }
  }
  private void sendMessage()
  {
    if(inputMessage.getText()!="")
    {
      if(regionSelection.getValue()==null)
      {
        for(EnumRegion region:EnumRegion.US_REGIONS)
        {
          chatManager.sendChatToServer(inputMessage.getText(), region,EnumPolicy.valueOf(cardSelection.getValue().toString()));
          chatMessages.setText(chatMessages.getText() + "\n" + inputMessage.getText());
        }
      }else
      {
        chatManager.sendChatToServer(inputMessage.getText(), EnumRegion.valueOf(regionSelection.getValue().toString()),
                EnumPolicy.valueOf(cardSelection.getValue().toString()));
        chatMessages.setText(chatMessages.getText() + "\n" + inputMessage.getText());
      }
      inputMessage.clear();
      }
  }
  public void setChatMessages(String messages)
  {
    chatMessages.setText(messages);
  }
}
