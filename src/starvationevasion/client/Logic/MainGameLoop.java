package starvationevasion.client.Logic;

import com.sun.javafx.tk.TKPulseListener;
import com.sun.javafx.tk.Toolkit;
import javafx.application.Platform;
import starvationevasion.client.GUI.DraftLayout.ChatNode;
import starvationevasion.client.GUI.DraftLayout.DraftLayout;
import starvationevasion.client.GUI.DraftLayout.hand.Hand;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.VotingLayout.VotingLayout;
import starvationevasion.client.Networking.Client;
import starvationevasion.common.EnumPolicy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the main game loop that will update game state in correct order
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
    GameLoop gameLoop=new GameLoop();
//    //gameLoop.start();
   // run();
    Toolkit tk =Toolkit.getToolkit();
    Listener listener =new Listener();
    tk.addStageTkPulseListener(listener);
    tk.addPostSceneTkPulseListener(listener);

  }
  public class Listener implements TKPulseListener{

    @Override
    public void pulse()
    {
     System.out.println("Spam");
      Toolkit.getToolkit().requestNextPulse();
      chatNodeDraft.setChatMessages(chatManager.getChat());
      chatNodeVote.setChatMessages(chatManager.getChat());
      if(gui.needsHand()&&client.getHand()!=null&&!client.getHand().isEmpty())
      {
        
        //Toolkit.getToolkit().requestNextPulse();
        gui.setCardsInHand(client.getHand());
        cardsInHand=client.getHand();
        hand.setHand(client.getHand().toArray(new EnumPolicy[cardsInHand.size()]));
      }
      if(gui.getDraftLayout().getHand().getHand()!=null)
      {
        chatNodeDraft.setHand(gui.getDraftLayout().getHand().getHand());
        chatNodeVote.setHand(gui.getDraftLayout().getHand().getHand());
      }
      if(gui.isDraftingPhase()&&client.getState().equals(starvationevasion.server.model.State.VOTING))
      {
        //Toolkit.getToolkit().requestNextPulse();
        System.out.println("Here");
        gui.resetDraftingPhase();
        gui.switchScenes();
      }
      if(!gui.isDraftingPhase()&&(client.getState().equals(starvationevasion.server.model.State.DRAFTING)||client.getState().equals(starvationevasion.server.model.State.DRAWING)))
      {
        gui.resetVotingPhase();
        gui.switchScenes();
      }
      if(client.getVotingCards()!=null&&!gui.getVotingLayout().hasReceivedCards()) gui.getVotingLayout().updateCardSpaces(client.getVotingCards());

    }
  }


  private void run()
  {timer=new Timer();

    timerTask=new TimerTask()
    {
      boolean flag=false;
      @Override
      public void run()
      {
        Platform.runLater(()->{

          chatNodeDraft.setChatMessages(chatManager.getChat());
          chatNodeVote.setChatMessages(chatManager.getChat());
          if(gui.needsHand()&&client.getHand()!=null&&!client.getHand().isEmpty())
          {

            gui.setCardsInHand(client.getHand());
            cardsInHand=client.getHand();
            hand.setHand(client.getHand().toArray(new EnumPolicy[cardsInHand.size()]));
            System.out.println("please work " + Arrays.toString(hand.getHand())+Platform.isFxApplicationThread());
          }
          if(gui.getDraftLayout().getHand().getHand()!=null)
          {
            chatNodeDraft.setHand(gui.getDraftLayout().getHand().getHand());
            chatNodeVote.setHand(gui.getDraftLayout().getHand().getHand());
          }
          if(gui.isDraftingPhase()&&client.getState().equals(starvationevasion.server.model.State.VOTING))
          {
            gui.resetDraftingPhase();
            gui.switchScenes();
          }
          if(!gui.isDraftingPhase()&&(client.getState().equals(starvationevasion.server.model.State.DRAFTING)||client.getState().equals(starvationevasion.server.model.State.DRAWING)))
          {
            gui.resetVotingPhase();
            gui.switchScenes();
          }
          if(client.getVotingCards()!=null&&!gui.getVotingLayout().hasReceivedCards()) gui.getVotingLayout().updateCardSpaces(client.getVotingCards());

        });
      }
    };

    timer.schedule(timerTask,100,1000);
  }
  public void stop()
  {
    timer.cancel();
  }
  private class GameLoop extends Thread
  {
  public void run()
  {
    Platform.runLater(() -> {
      chatNodeDraft.setChatMessages(chatManager.getChat());
      chatNodeVote.setChatMessages(chatManager.getChat());
      if(gui.needsHand()&&client.getHand()!=null&&!client.getHand().isEmpty())
      {
        System.out.println("Will I ever get here?  "+client.getHand());
        gui.setCardsInHand(client.getHand());
        cardsInHand=client.getHand();
        hand.setHand(client.getHand().toArray(new EnumPolicy[cardsInHand.size()]));
        System.out.println("HERE MAYBE??");
      }
      if(gui.getDraftLayout().getHand().getHand()!=null)
      {
        chatNodeDraft.setHand(gui.getDraftLayout().getHand().getHand());
        chatNodeVote.setHand(gui.getDraftLayout().getHand().getHand());
      }
      if(gui.isDraftingPhase()&&client.getState().equals(starvationevasion.server.model.State.VOTING))
      {
        System.out.println("Here");
        gui.resetDraftingPhase();
        gui.switchScenes();
      }
      if(!gui.isDraftingPhase()&&(client.getState().equals(starvationevasion.server.model.State.DRAFTING)||client.getState().equals(starvationevasion.server.model.State.DRAWING)))
      {
        gui.resetVotingPhase();
        gui.switchScenes();
      }
      if(client.getVotingCards()!=null&&!gui.getVotingLayout().hasReceivedCards()) gui.getVotingLayout().updateCardSpaces(client.getVotingCards());
    });
  }
  }
}
