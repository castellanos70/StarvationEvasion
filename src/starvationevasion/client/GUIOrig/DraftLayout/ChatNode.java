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
import javafx.scene.text.Text;
import starvationevasion.client.Client;
import starvationevasion.client.GUIOrig.GUI;
import starvationevasion.client.Logic.ChatManager;
import starvationevasion.common.EnumRegion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


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
  private Client client;
  private ChatManager chatManager;
  private Timer timer;
  private TimerTask timerTask;
  private ComboBox comboBox;

  public ChatNode(GUI gui)
  {
    ArrayList<EnumRegion> regions=new ArrayList<>(Arrays.asList(EnumRegion.values()));
    ObservableList<EnumRegion> regionList= FXCollections.observableArrayList(regions);
    comboBox=new ComboBox(regionList);
    timer=new Timer();
    timerTask=new TimerTask()
    {
      @Override
      public void run()
      {
        chatMessages.setText(chatManager.getChat());
      }
    };
    timer.schedule(timerTask,100,100);

    client=gui.getClient();
    chatManager=client.getChatManager();
    usersChat.getChildren().add(confirm);
    usersChat.getChildren().add(inputMessage);
    usersChat.getChildren().add(comboBox);
    chatFrame.setContent(chatMessages);
    this.setCenter(chatFrame);
    this.setBottom(usersChat);

    inputMessage.setOnKeyPressed(event1 ->
    {
      if(event1.getCode().equals(KeyCode.ENTER))
      {
        chatManager.sendChatToServer(inputMessage.getText(),EnumRegion.valueOf(comboBox.getValue().toString()));
        chatMessages.setText(chatMessages.getText() + "\n" + inputMessage.getText());
        inputMessage.clear();
      }
    });
    confirm.setOnAction(event ->
    {
      chatManager.sendChatToServer(inputMessage.getText(),EnumRegion.valueOf(comboBox.getValue().toString()));
      chatMessages.setText(chatMessages.getText() + "\n" + inputMessage.getText());
      inputMessage.clear();
    });

  }
}
