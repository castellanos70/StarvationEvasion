package starvationevasion.client.Logic;

import starvationevasion.client.Client;
import starvationevasion.client.GUIOrig.DraftLayout.ChatNode;
import starvationevasion.client.GUIOrig.DraftLayout.DraftLayout;
import starvationevasion.client.GUIOrig.GUI;
import starvationevasion.client.GUIOrig.VotingLayout.VotingLayout;

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
  private VotingLayout votingLayout;
  private ChatNode chatNodeDraft;
  private ChatNode chatNodeVote;
  private Timer timer;
  private TimerTask timerTask;
  private ChatManager chatManager;
  public MainGameLoop(GUI gui)
  {
    this.gui=gui;
    client=gui.getClient();
    chatManager=client.getChatManager();
    draftLayout=gui.getDraftLayout();
    votingLayout=gui.getVotingLayout();
    chatNodeDraft=draftLayout.getChatNode();
    chatNodeVote=votingLayout.getChatNode();
    run();
  }
  private void run()
  {timer=new Timer();
    timerTask=new TimerTask()
    {
      @Override
      public void run()
      {
        chatNodeDraft.setChatMessages(chatManager.getChat());
        chatNodeDraft.setHand(gui.getDraftLayout().getHand().getHand());
        chatNodeVote.setChatMessages(chatManager.getChat());
        chatNodeVote.setHand(gui.getDraftLayout().getHand().getHand());
      }
    };
    timer.schedule(timerTask,100,100);
  }
  public void stop()
  {
    timer.cancel();
  }
}
