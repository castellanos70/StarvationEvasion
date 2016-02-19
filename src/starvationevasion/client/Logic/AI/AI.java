package starvationevasion.client.Logic.AI;

/**
 * This is the prototype for the main AI
 */
import starvationevasion.client.GUI.DraftLayout.hand.ClientPolicyCard;
import starvationevasion.client.GUI.DraftLayout.hand.Hand;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.Logic.Client;
import starvationevasion.client.Networking.Messenger;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Options For the AI in drafting phase
 *
 * Draft card
 *    Determine what values must be set and then set them
 * Discard single card
 * Discard up to 3 cards
 *
 * Look at hand
 * Look at world data
 * Look at discard pile
 * Look at other players discard pile
 * Look at draft status
 * Look at country data
 * Look at graphs
 * Look at Crop data
 *
 * EndTurn
 */
public class AI extends Application
{
  private Hand hand;
  private GUI gui;
  private Client client;
  private ArrayList<ClientPolicyCard> cardsInHand;
  private Messenger messenger;
  private Socket clientSocket;
  private Thread thread;

  public AI()
  {
    client = new Client(true);
    client.init();
    messenger = client.messenger;
    //hand=GUI.getDraftLayout().getHand();
  }

  public void start(Stage stage){}

  public void init()
  {
    //thread = new Thread(client.listener);
    //thread.setDaemon(true);
    //thread.start();
    client.listener.call();

  }
  private void draftCard(ClientPolicyCard clientPolicyCard)
  {
    hand.draftCard(clientPolicyCard);
  }

  public static void main(String[] args)
  {
    AI ai = new AI();
    ai.init();
    AI.launch();
  }
}
