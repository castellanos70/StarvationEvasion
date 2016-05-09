package starvationevasion.client.Networking;

import javafx.application.Platform;
import javafx.stage.Stage;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.Logic.ChatManager;
import starvationevasion.client.Logic.LocalDataContainer;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.WorldData;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.common.gamecards.GameCard;
import starvationevasion.communication.Communication;
import starvationevasion.server.model.*;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Communicates with server
 */
public class DeprecatedClient implements Client
{
  private Socket clientSocket;

  private DataInputStream reader;
  private DataOutputStream writer;

  // time of server start
  private long startNanoSec;
  private Scanner keyboard;
  // writes to user
  private ClientListener listener;

  private EnumRegion region;
  private volatile boolean isRunning = true;
  private ArrayList<User> allUsers=new ArrayList<>();
  private ChatManager chatManager;
  private ArrayList<EnumPolicy> hand;
  private User user;
  private String userName;
  private boolean loginSuccessful;
  private boolean recivedMessege=false;
  private State state;
  private ArrayList<GameCard> votingCards;

  private GUI gui;

  private LocalDataContainer localDataContainer;
  private ArrayList<EnumRegion> availableRegion;

  /**
   * Basic constructor that establishes connection with server
   * @param host  your host name
   * @param portNumber your port number
   */
  public DeprecatedClient (String host, int portNumber)
  {
    chatManager=new ChatManager(this);
    keyboard = new Scanner(System.in);

    openConnection(host, portNumber);
    listener = new ClientListener();
    System.out.println("JavaClient: Starting listener = : " + listener);
    listener.start();
    //requestAvaliableRegions();
    // listenToUserRequests();
    localDataContainer=new LocalDataContainer(this);
    localDataContainer.init();
  }

  public GUI getGui(){return gui;}

  /**
   * Sets the GUI for this client.
   *
   * @param gui valid gui reference
   */
  @Override
  public void setGUI(GUI gui)
  {

  }

  public ChatManager getChatManager(){return  chatManager;}

  /**
   * Gets the communication module.
   *
   * @return communication module
   */
  @Override
  public Communication getCommunicationModule()
  {
    return null;
  }

  public EnumRegion getRegion(){return region;}
  public State getState()
  {
    return state;
  }
  public ArrayList<EnumRegion> getAvailableRegion()
  {
    return availableRegion;
  }
  public ArrayList<GameCard> getVotingCards()
  {
    return votingCards;
  }

  /**
   * Send a login request to Server
   */
  public boolean loginToServer(String username, String password, EnumRegion region)
  {
    System.out.println("Client.loginToServer");
    this.userName=userName;
    sendRequest(new RequestFactory().login(startNanoSec, userName, password, region));
    return true;
  }

  public void restart()
  {
    Request f = new Request(startNanoSec, Endpoint.RESTART_GAME);
    // Create a payload (this is the class that stores Sendable information)
    sendRequest(f);
    readHand();
  }

  /**
   * Gets hand from server, could be used for recovery
   */
  public void readHand()
  {
    Request f = new Request(startNanoSec, Endpoint.HAND_READ);
    sendRequest(f);
  }

  /**
   * Tells Server that Client is ready
   */
  public boolean ready()
  {

    Request f = new Request(startNanoSec, Endpoint.READY);
    sendRequest(f);
    readHand();
    //createHand();
    return true;
  }

  /**
   * request world data
   */
  public void worldData()
  {
    Request f = new Request(startNanoSec, Endpoint.WORLD_DATA);
    // Create a payload (this is the class that stores Sendable information)
    Payload data = new Payload();
    data.putData("user");
    data.put("client-done", true);
    data.put("region-polygons", true);
    data.put("data-start",2000);
    data.put("data-end",2001);
    f.setData(data);
    sendRequest(f);
    requestAvaliableRegions();
  }

  /**
   * Tells server your done with an action
   */
  public boolean done()
  {
    sendRequest(new RequestFactory().build(startNanoSec, Endpoint.DONE));
    return true;
  }
  public void getUsers()
  {
    Request f = new Request(startNanoSec, Endpoint.USERS);
    sendRequest(f);
  }
  public ArrayList<EnumPolicy> getHand()
  {
    return hand;
  }
  public void getGameState()
  {
    Request f = new Request(startNanoSec, Endpoint.GAME_STATE);
    // Create a payload (this is the class that stores Sendable information)
    sendRequest(f);
  }
  public void drawCard()
  {
    Request f = new Request(startNanoSec, Endpoint.DRAW_CARD);
    // Create a payload (this is the class that stores Sendable information)
    sendRequest(f);
  }

  public boolean createUser(String user,String pass,EnumRegion region)
  {
    // Create a request to login
    Request f = new Request(startNanoSec, Endpoint.USER_CREATE);
    // Create a payload (this is the class that stores Sendable information)
    Payload data = new Payload();

    //data.putData("user");

    data.put("username", user);
    data.put("password", pass);
    data.put("region", EnumRegion.USA_SOUTHEAST);
    f.setData(data);
    sendRequest(f);
    return true;
  }
  public boolean draftCard(GameCard card)
  {
    Request f = new Request(startNanoSec, Endpoint.DRAFT_CARD);
    Payload data = new Payload();
    data.putData(card);
    data.put("card", card);
    f.setData(data);
    // Create a payload (this is the class that stores Sendable information)
    sendRequest(f);
    return true;
  }
  public boolean voteUp(GameCard card)
  {
    Request f = new Request(startNanoSec, Endpoint.VOTE_UP);
    Payload data = new Payload();
    data.putData(card);
    data.put("card", card);
    f.setData(data);
    // Create a payload (this is the class that stores Sendable information)
    sendRequest(f);
    return true;
  }
  public boolean voteDown(GameCard card)
  {
    Request f = new Request(startNanoSec, Endpoint.VOTE_DOWN);
    Payload data = new Payload();
    data.putData(card);
    data.put("card", card);
    f.setData(data);
    // Create a payload (this is the class that stores Sendable information)
    sendRequest(f);
    return true;
  }
  public boolean discardCard(GameCard card)
  {
    Request f = new Request(startNanoSec, Endpoint.DELETE_CARD);
    Payload data = new Payload();


    data.putData(card.getCardType());
    f.setData(data);
    // Create a payload (this is the class that stores Sendable information)
    sendRequest(f);
    return true;
  }

  /**
   * Checks to see if the client is still running. This might return false if something
   * like the server disconnected.
   *
   * @return true if running and false if not
   */
  @Override
  public boolean isRunning()
  {
    return isRunning;
  }

  public void sendChatMessage(String message,EnumRegion toRegion)
  {
    // Create a request to login
    Request f = new Request(startNanoSec, Endpoint.CHAT);
    // Create a payload (this is the class that stores Sendable information)
    Payload data = new Payload();
    data.putData("chat");
    data.put("to-region",toRegion.name());
    data.put("card", EnumPolicy.Policy_CleanRiverIncentive);
    data.put("text",message);
    f.setData(data);
    sendRequest(f);
  }
  public void requestAvaliableRegions()
  {
    Request f = new Request(startNanoSec, Endpoint.AVAILABLE_REGIONS);
    sendRequest(f);
  }
  public void ai()
  {
    Request f = new Request(startNanoSec, Endpoint.AI);
    sendRequest(f);
  }
  public void sendRequest(Request request)
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(request);
      oos.close();
      byte[] bytes = baos.toByteArray();
      writer.writeInt(bytes.length);
      writer.write(bytes);
      writer.flush();
      baos.close();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }

  public void openGUI()
  {
    gui=new GUI(this,localDataContainer);
    Stage guiStage=new Stage();
    gui.start(guiStage);
    gui.start(guiStage);
  }

  private void guiStateManagement(State state)
  {
    switch (state)
    {
      case LOGIN:
        return;
      case BEGINNING:
        return;
      case DRAWING:
        if(!gui.isDraftingPhase())
        {
          gui.resetVotingPhase();
          gui.switchScenes();
        }
        break;
      case DRAFTING:

        break;
      case VOTING:
        if(gui.isDraftingPhase())
        {
          gui.resetDraftingPhase();
          gui.switchScenes();
        }
        break;
      case WIN:
        break;
      case LOSE:
        break;
      case END:
        break;
      case TRANSITION:
        break;
    }
  }
  private boolean openConnection (String host, int portNumber)
  {

    try
    {
      clientSocket = new Socket(host, portNumber);
    }
    catch(UnknownHostException e)
    {

      isRunning = false;
      return false;
    }
    catch(IOException e)
    {
      System.err.println("Client Error: Could not open connection to " + host
              + " on port " + portNumber);
      e.printStackTrace();
      isRunning = false;
      return false;
    }

    try
    {
      writer = new DataOutputStream(clientSocket.getOutputStream());
      writer.write("JavaClient\n".getBytes());
      writer.flush();
    }
    catch(IOException e)
    {
      e.printStackTrace();
      return false;
    }
    try
    {
      reader = new DataInputStream(clientSocket.getInputStream());
    }
    catch(IOException e)
    {
      e.printStackTrace();
      return false;
    }
    isRunning = true;
    return true;
  }
  private void updateUser(User user)
  {
    this.user = user;
    region = user.getRegion();

    hand=user.getHand();
    if(gui!=null&&gui.needsHand()&&hand!=null&&!getHand().isEmpty())
    {
      gui.setAssignedRegion(region);
      gui.setCardsInHand(getHand());
      Platform.runLater(() -> gui.getDraftLayout().getHand().setPolicies(getHand().toArray(new EnumPolicy[hand.size()])));
    }
  }
  public void shutdown()
  {
    isRunning=false;
    try
    {
      writer.close();
      reader.close();
      clientSocket.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }

  }
  private void listenToUserRequests ()
  {
    while(isRunning)
    {

      String cmd = keyboard.nextLine();


      if (cmd == null || cmd.length() < 1)
      {
        continue;
      }

      if (cmd.charAt(0) == 'q')
      {
        isRunning = false;
      }
      if (cmd.equals("login"))
      {
        // Create a request to login
        Request f = new Request(startNanoSec, Endpoint.LOGIN);
        // Create a payload (this is the class that stores Sendable information)
        Payload data = new Payload();

        data.putData("user");

        data.put("username", "admin");
        data.put("password", "admin");

        f.setData(data);

        try
        {

          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ObjectOutputStream oos = new ObjectOutputStream(baos);
          oos.writeObject(f);
          oos.close();


          byte[] bytes = baos.toByteArray();

          writer.writeInt(bytes.length);
          writer.write(bytes);
          writer.flush();
        }
        catch(IOException e)
        {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * When called, this should perform all necessary updates to keep the client up to date with
   * the current state of the game. This function should only ever be called from one thread.
   *
   * @param deltaSeconds change in seconds since the last time this function was called
   */
  @Override
  public void update(double deltaSeconds)
  {

  }


  /**
   * ClientListener
   *
   * Handles reading stream from socket. The data is then outputted
   * to the console for user.
   */
  private class ClientListener extends Thread
  {

    public void run ()
    {
      while(isRunning)
      {
        read();
      }
    }

    private void read ()
    {
      try
      {
        Response response = readObject();
        //System.out.println(response.getType());
        //System.out.println(response.getPayload());
//        if(!response.getType().equals(Type.WORLD_DATA_LIST))System.out.println(response.getPayload());
        if (response.getPayload().get("data") instanceof User)
        {
          if(response.getType().equals(Type.AUTH_SUCCESS))
          {
            loginSuccessful=true;
            recivedMessege=true;

          }else if(response.getType().equals(Type.AUTH_ERROR))
          {
            loginSuccessful=false;
            recivedMessege=true;
          }
          System.out.println("Response.data = User object.");
          updateUser((User)response.getPayload().get("data"));
        }
        else if (response.getPayload().get("data") instanceof WorldData)
        {
          System.out.println("Response.data = WorldData object.");
        }
        else if(response.getType().equals(Type.VOTE_BALLOT))
        {
          System.out.println("Vote Ballot received  " + response.getPayload().getData().getClass());
          ArrayList arrayList=(ArrayList)response.getPayload().getData();
          System.out.println(arrayList);
          votingCards=(ArrayList) response.getPayload().getData();
          Platform.runLater(() -> gui.getVotingLayout().updateCardSpaces(votingCards));
        }
        else if(response.getPayload().get("data")instanceof ArrayList)
        {
          ArrayList data = (ArrayList) response.getPayload().get("data");
          if(!data.isEmpty())
          {
            if (data.get(0) instanceof EnumPolicy)
            {
              System.out.println("Response.data = Array list of EnumPolicies objects.");

              hand = ((ArrayList) response.getPayload().get("data"));
            } else if (data.get(0) instanceof EnumRegion)
            {
              System.out.println("Response.data = Array list of EnumRegion objects.");
              availableRegion = data;
            } else if (data.get(0) instanceof WorldData)
            {
              ArrayList<WorldData> worldData=data;
              for(WorldData wd: worldData)
              {
                localDataContainer.updateGameState(wd);
              }
              System.out.println("Response.data = Array list of WorldData objects.");
            }
          }
        }
        else if(response.getPayload().get("data")instanceof String)
        {
          chatManager.sendChatToClient((String)response.getPayload().get("text"));
          gui.getDraftLayout().getChatNode().setChatMessages(chatManager.getChat());
          gui.getVotingLayout().getChatNode().setChatMessages(chatManager.getChat());
        }
        else if(response.getPayload().get("data")instanceof starvationevasion.server.model.State)
        {

          state=(starvationevasion.server.model.State) response.getPayload().get("data");
          System.out.println(state+" Response.data = State");
          if(state.equals(starvationevasion.server.model.State.DRAWING)) readHand();

          Platform.runLater(() -> guiStateManagement(state));
        }

      }
      catch(EOFException e)
      {
        e.printStackTrace();
        isRunning = false;
        System.out.println("Lost server, press enter to shutdown.");
        return;
      }
      catch(Exception e)
      {

        e.printStackTrace();
        isRunning = false;
        System.out.println("Lost server, press enter to shutdown.");
        return;
      }
    }
  }

  private Response readObject() throws Exception
  {
    int ch1 = reader.read();
    int ch2 = reader.read();
    int ch3 = reader.read();
    int ch4 = reader.read();

    if ((ch1 | ch2 | ch3 | ch4) < 0)
    {
      throw new EOFException();
    }
    int size  = ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));

    byte[] object = new byte[size];
    reader.readFully(object);

    ByteArrayInputStream in = new ByteArrayInputStream(object);
    ObjectInputStream is = new ObjectInputStream(in);
    is.close();
    in.close();


    return (Response) is.readObject();
  }

  private String timeDiff ()
  {
    long nanoSecDiff = System.nanoTime() - startNanoSec;
    double secDiff = (double) nanoSecDiff / 1000000000.0;
    return String.format("%.3f", secDiff);
  }
}

