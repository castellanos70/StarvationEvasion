package starvationevasion.client.Logic;

import starvationevasion.client.Client;
import starvationevasion.client.GUIOrig.DraftLayout.ChatNode;
import starvationevasion.client.GUIOrig.DraftLayout.DraftLayout;
import starvationevasion.client.GUIOrig.GUI;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dayloki on 3/15/2016.
 *
 */
public class MainGameLoop
{
  private GUI gui;
  private Client client;
  private DraftLayout draftLayout;
  private ChatNode chatNode;
  private Timer timer;
  private TimerTask timerTask;
  private ChatManager chatManager;
  public MainGameLoop(GUI gui)
  {
    this.gui=gui;
    client=gui.getClient();
    chatManager=client.getChatManager();
    draftLayout=gui.getDraftLayout();
    chatNode=draftLayout.getChatNode();
    run();
  }
  private void run()
  {timer=new Timer();
    timerTask=new TimerTask()
    {
      @Override
      public void run()
      {
        chatNode.setChatMessages(chatManager.getChat());
      }
    };
    timer.schedule(timerTask,100,100);
  }
  public void stop()
  {
    timer.cancel();
  }
}
