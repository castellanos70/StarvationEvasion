package starvationevasion.client.Logic;

import javafx.application.Application;
import javafx.stage.Stage;
import starvationevasion.client.Driver.RegionChooser;
import starvationevasion.client.GUIOrig.GUI;
import starvationevasion.client.Networking.ClientListener;
import starvationevasion.client.Networking.MessageContainer;
import starvationevasion.client.Networking.Messenger;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by arirappaport on 11/19/15.
 */
public class Client
{
  public Messenger messenger;
  public ClientListener listener;
  public GUI gui;
  public LocalDataContainer localDataContainer;
  public boolean isAI;
  private RegionChooser regionChooser;
  private String loginSalt;
  private String username;
  private Socket clientSocket;
  private boolean receivedLoginAttempt;
  private boolean loginSuccessful;
  private Map<EnumRegion, String> regionToUser;
  private EnumRegion assignedRegion;
  private Stage guiStage;
  private boolean readyToStart;
  private ArrayList<EnumRegion> availableRegions;
  private TreeMap<EnumRegion, String> takenRegions;
  //private LoginResponse.ResponseType gameConfigurationType;
  private boolean madeConnection;
  private boolean firstDraft = true;

  /**
   * Constructor for the client class
   * @param isAI true if client is ai, else false
   */
  public Client(boolean isAI)
  {
   // gui = new GUI(this, localDataContainer);
    listener = new ClientListener(this);
    availableRegions = new ArrayList<>();
    takenRegions = new TreeMap<>();
    localDataContainer = new LocalDataContainer(this);
    localDataContainer.init();
    this.isAI = isAI;
  }

  /**
   * Called from LandingPage, getWorldData creates a socket
   * connection with the server and assigns its
   * messenger object that socket.
   */
  public void init()
  {
    try
    {
      if(!madeConnection)
      {
        //while(!openConnection("localhost", ServerConstants.DEFAULT_PORT));
      }
      messenger = new Messenger(clientSocket);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Begins the game
   * @param response Response to determine if the game should begin
   */
//  public void beginGame(BeginGame response)
//  {
//    synchronized (this)
//    {
//      regionToUser  = response.finalRegionChoices;
//      readyToStart = true;
//    }
//    Platform.runLater(() ->
//    {
//        Stage guiStage = new Stage();
//        gui.start(guiStage);
//        if(!isAI) regionChooser.primaryStage.close();
//    });
//  }

  /**
   * Sets/initializes the loginSalt with the Hello loginNonce recieved upon opening the client.
   * @param loginNonce loginNonce, or salt, to set locally
   */
  public void setLoginSalt(String loginNonce)
  {
    loginSalt = loginNonce;
  }

  /**
   * Getter for the login salt.
   * @return the login salt associated with this client's Hello nonce response
   */
  public String getLoginSalt()
  {
    return loginSalt;
  }

  /**
   * Sets the username of the client once a successful login has been acheived
   * @param username username to set in the client
   */
  public void setUsername(String username)
  {
    this.username = username;
  }

  /**
   * Provides getter functionality for the username bound to the client.
   * @return The client's username
   */
  public String getUsername()
  {
    return this.username;
  }

  /**
   * Getter for the game configuration type - either choose region or assigned region
   * @return the game configuration type
   */
//  public LoginResponse.ResponseType getGameConfigurationType()
//  {
//    return gameConfigurationType;
//  }

  /**
   * Retrieves the region choose associated with the client
   * @return the region chooser associated with the client
   */
  public Application getRegionChooser()
  {
    return regionChooser;
  }

  /**
   * This method is called from LandingPage upon a
   * successful login by the user. It creates the
   * regionChooser, and hands off user input to it.
   *
   */
  public void startSelection()
  {
    try
    {
      //regionChooser = new RegionChooser(this, localDataContainer);
      Stage regionStage = new Stage();
      regionChooser.start(regionStage);
    }
    catch (Exception e)
    {}
  }

  /**
   * Called from LandingPage, an attempt by the user to
   * log into the server. The password does not go over
   * the network in plaintext.
   * @param uname The entered username.
   * @param passwd The entered password.
   */
  public void sendLogin(String uname, String passwd)
  {
   setUsername(uname);
   messenger.send(new MessageContainer(uname, loginSalt, passwd));
  }

  /**
   * Called from RegionChooser to indicate that the user
   * is attempting to select a region.
   * @param desiredRegion The region the user is trying
   *                      to select.
   */
  public void sendRegionChoice(EnumRegion desiredRegion)
  {
    messenger.send(new MessageContainer(desiredRegion));
  }

  /**
   * Requests that the server draft the specified
   * card.
   * @param cardToBeDrafted card to discard.
   */
  public void draftCard(PolicyCard cardToBeDrafted)
  {
  //  messenger.send(new MessageContainer(new DraftCard(cardToBeDrafted)));
  }

  /**
   * Requests that the server discard the specified
   * card.
   * @param cardToDiscard The card to discard.
   */
  public void discard(EnumPolicy cardToDiscard)
  {
    //messenger.send(new MessageContainer(new Discard(cardToDiscard)));
  }

  /**
   * Sends a vote request to the server
   * @param region the region who's card you would like to vote for
   * @param type the type of vote to apply to the card
   */
//  public void vote(EnumRegion region, VoteType type)
//  {
//    messenger.send( new MessageContainer(region, type));
//  }

  /**
   * Upon receiving a LoginResponse object from the server
   * this method takes the appropriate action based on
   * the contents of the message. However the boolean
   * receivedLoginAttempt is always set to true so the
   * LandingPage doesn't hang.
   * @param response The response given by the server.
   */
//  public void parseLoginMessage(LoginResponse response)
//  {
//    synchronized (this)
//    {
//      receivedLoginAttempt = true;
//      if(!(response.responseType == LoginResponse.ResponseType.ACCESS_DENIED))
//      {
//        loginSuccessful = true;
//        if (response.responseType == LoginResponse.ResponseType.ASSIGNED_REGION)
//        {
//          gameConfigurationType = LoginResponse.ResponseType.ASSIGNED_REGION;
//          this.assignedRegion = response.assignedRegion;
//          Platform.runLater(() ->
//          {
//            gui.updateAssignedRegion(response.assignedRegion);
//          });
//          System.out.println(assignedRegion);
//        }
//        else if (response.responseType == LoginResponse.ResponseType.CHOOSE_REGION)
//        {
//          gameConfigurationType = LoginResponse.ResponseType.CHOOSE_REGION;
//        }
//      }
//    }
//  }

  /**
   * Called whenever a new phase starts. The Client uses this
   * method to restart the appropriate timer as well as changing
   * the GUIOrig's main Scene.
   * @param newPhaseInfo The info corresponding to the change of
   *                     phase.
   */
//  public void handlePhaseStart(PhaseStart newPhaseInfo)
//  {
//   // if(newPhaseInfo.currentGameState.equals(ServerState.DRAFTING))
//    {
//      if(isAI)
//      {
//        Platform.runLater(() ->
//        {
//          gui.getDraftLayout().getHand().playRandomCard();
//          gui.getDraftLayout().getHand().playRandomCard();
//        });
//      }
//      if(!firstDraft)
//      {
//        gui.switchScenes();
//      }
//      firstDraft = false;
//      DraftTimer.draftTimer.start();
//    }
//    //if(newPhaseInfo.currentGameState.equals(ServerState.VOTING))
//    {
//      VotingTimer.voteTimer.start();
//      gui.switchScenes();
//    }
//  }

  /**
   * Gets the region assigned to the client by the server
   * based on the password file.
   * @return The assigned region.
   */
  public EnumRegion getAssignedRegion()
  {
    synchronized (this)
    {
      return assignedRegion;
    }
  }

  /**
   * Called by the LandingPage to determine whether the
   * login attempt was successful. Needs to be synchronized
   * to prevent race conditions between ClientListener and
   * LandingPage.
   * @return Whether the login was successful.
   */
  public boolean isLoginSuccessful()
  {
    synchronized (this)
    {
      return loginSuccessful;
    }
  }

  /**
   * Called by the RegionChooser to determine what regions
   * have already been selected by other players in order to
   * grey them out.
   * @return An ArrayList of AvailableRegions.
   */
  public ArrayList<EnumRegion> getAvailableRegions()
  {
    synchronized (this)
    {
      return availableRegions;
    }
  }

  /**
   * Gets the Regions taken mapped to the name of the player
   * who has taken that region.
   * @return A map of regions to players who have taken them.
   */
  public TreeMap<EnumRegion, String> getTakenRegions()
  {
    synchronized (this)
    {
      return takenRegions;
    }
  }

  /**
   * Gets info from the server concerning the regions that have
   * yet to be selected as well as what regions have been selected
   * and by what user.
   * @param availableRegionInfo The info in the form of an Available
   *                            Region object.
   */
//  public void setAvailableRegionInfo(AvailableRegions availableRegionInfo)
//  {
//    synchronized (this)
//    {
//      availableRegions = new ArrayList<>(availableRegionInfo.availableRegions);
//      takenRegions = new TreeMap<>(availableRegionInfo.takenRegions);
//      if (regionChooser != null)
//      {
//        // if we have a region chooser, access the map and it's controller
//        ((RegionChooser) regionChooser).map.updateStyling(availableRegions, takenRegions, gameConfigurationType);
//      }
//    }
//  }

  /**
   * Sets the local instance bookkeeping receivedLoginAttempt
   * @param receivedLoginAttempt the new value of reveicedLoginAttemp
   */
  public void setReceivedLoginAttempt(boolean receivedLoginAttempt)
  {
    synchronized (this)
    {
      this.receivedLoginAttempt = receivedLoginAttempt;
    }
  }

  /**
   * Determines if the server received a login
   * @return true if server received login, else false
   */
  public boolean didServerReceiveLogin()
  {
    synchronized (this)
    {
      return receivedLoginAttempt;
    }
  }

  /**
   * Opens up a socket connection with the server
   * @param host The domain address of the host
   * @param portNumber the port number to open the socket with
   * @return true if connection is open, else false
   */
  public boolean openConnection(String host, int portNumber)
  {
    try
    {
      clientSocket = new Socket(host, portNumber);
    } catch (ConnectException e)
    {
      System.out.println("Waiting for server to initialize . . .");
      return false;
    } catch (UnknownHostException e)
    {
      System.err.println("Client Error: Unknown Host " + host);
      e.printStackTrace();
      return false;
    } catch (IOException e)
    {
      System.err.println("Client Error: Could not open connection to " + host
        + " on port " + portNumber);
      e.printStackTrace();
      return false;
    }
    return true;
  }

//  public void HandleVoteStatus(VoteStatus response)
//  {
//    Platform.runLater(() ->
//    {
//      //ArrayList card = rhesponse.currentCards;
//    });
//  }
}
