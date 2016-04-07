package starvationevasion.client.Logic;

import javafx.application.Platform;
import starvationevasion.client.Client;
import starvationevasion.client.GUIOrig.DraftLayout.ChatNode;
import starvationevasion.client.GUIOrig.DraftLayout.DraftLayout;
import starvationevasion.client.GUIOrig.DraftLayout.hand.Hand;
import starvationevasion.client.GUIOrig.GUI;
import starvationevasion.client.GUIOrig.VotingLayout.VotingLayout;
import starvationevasion.common.EnumPolicy;
import starvationevasion.server.model.State;

import java.util.ArrayList;
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
  private ArrayList<EnumPolicy> cardsInHand;
  private Hand hand;
  public MainGameLoop(GUI gui)
  {
    this.gui=gui;
    hand=gui.getDraftLayout().getHand();
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
        Platform.runLater(()->{


       // System.out.println(gui.getCardsInHand());
//        if(gui.getCardsInHand()==null||gui.getCardsInHand().isEmpty()&&client.getHand()!=null)
//        {
//          gui.setCardsInHand(client.getHand());
//        }
//        cardsInHand=client.getHand();
//          hand.setHand(cardsInHand.toArray(new EnumPolicy[cardsInHand.size()]));

           chatNodeDraft.setChatMessages(chatManager.getChat());

          chatNodeVote.setChatMessages(chatManager.getChat());
          if(cardsInHand==null||cardsInHand.isEmpty()&&client.getHand()!=null&&!client.getHand().isEmpty())
          {
            System.out.println("DSL:FKJ");
            cardsInHand=client.getHand();
            hand.setHand(client.getHand().toArray(new EnumPolicy[cardsInHand.size()]));
          }
          if(gui.getDraftLayout().getHand().getHand()!=null)
          {
            chatNodeDraft.setHand(gui.getDraftLayout().getHand().getHand());
            chatNodeVote.setHand(gui.getDraftLayout().getHand().getHand());
          }
          if(gui.isDraftingPhase()&&client.getState().equals(State.VOTING)) gui.switchScenes();
          if(!gui.isDraftingPhase()&&(client.getState().equals(State.DRAFTING)||client.getState().equals(State.DRAWING))) gui.switchScenes();
        });
      }
    };
    timer.schedule(timerTask,100,100);
  }
  public void stop()
  {
    timer.cancel();
  }
}
