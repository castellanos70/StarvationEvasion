package starvationevasion.server;


/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.*;
import starvationevasion.server.io.*;
import starvationevasion.server.io.strategies.*;
import starvationevasion.server.model.*;
import starvationevasion.sim.Simulator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.DoubleBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;


/**
 */
public class Server
{
  private ServerSocket serverSocket;
  // List of all the workers
  private LinkedList<Worker> allConnections = new LinkedList<>();


  private long startNanoSec = 0;
  private Simulator simulator;

  // list of ALL the users
  private final ArrayList<User> userList = new ArrayList<>();

  // list of all the users playing the game (subset of userList)
  private final ArrayList<User> players = new ArrayList<>();

  private State currentState = State.LOGIN;
  private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
  private Date date = new Date();

  // list of available regions
  private ArrayList<EnumRegion> availableRegions = new ArrayList<>();
  private ArrayList<PolicyCard> enactedPolicyCards = new ArrayList<>(),
          draftedPolicyCards = new ArrayList<>();
  private HashMap<PolicyCard, Tuple<User, Boolean>> votes = new HashMap<>();


  private ScheduledFuture<?> phase;
  // Service that moves game along to next phase
  private ScheduledExecutorService advancer = Executors.newSingleThreadScheduledExecutor();

  // bool that listen for connections is looping over
  private boolean isWaiting = true;

  public Server (int portNumber)
  {

    Collections.addAll(availableRegions, EnumRegion.US_REGIONS);

    createUser(new User("admin", "admin", EnumRegion.USA_CALIFORNIA, new ArrayList<>()));
    createUser(new User("javier", "javier", EnumRegion.USA_MOUNTAIN, new ArrayList<>()));
    startNanoSec = System.nanoTime();
    simulator = new Simulator();


    try
    {
      serverSocket = new ServerSocket(portNumber);
    }
    catch(IOException e)
    {
      System.err.println("Server error: Opening socket failed.");
      e.printStackTrace();
      System.exit(-1);
    }

    // Mimic a chron-job that every half sec. it deletes stale workers.
    new Timer().schedule(new TimerTask()
    {
      @Override
      public void run ()
      {
        update();
      }
    }, 500, 500);

    waitForConnection(portNumber);

  }


  /**
   * Get the time of server spawn time to a given time.
   *
   * @param curr is the current time
   *
   * @return difference time in seconds
   */
  public double getTimeDiff (long curr)
  {
    long nanoSecDiff = curr - startNanoSec;
    return nanoSecDiff / 1000000000.0;
  }


  /**
   * Wait for a connection.
   *
   * @param port port to listen on.
   */
  private void waitForConnection (int port)
  {

    String host = "";
    try
    {
      host = InetAddress.getLocalHost().getHostName();
    }
    catch(UnknownHostException e)
    {
      e.printStackTrace();
    }
    while(isWaiting)
    {
      System.out.println("Server(" + host + "): waiting for Connection on port: " + port);
      try
      {
        Socket client = serverSocket.accept();
        System.out.println(dateFormat.format(date) + " Server: new Connection request recieved.");
        System.out.println(dateFormat.format(date) + " Server " + client.getRemoteSocketAddress());
        Worker worker = new Worker(client, this);
        worker.setServerStartTime(startNanoSec);


        if (secureConnection(worker, client))
        {
          worker.setReader(new WebSocketReadStrategy(client, null));
          worker.setWriter(new WebSocketWriteStrategy(client, null));
        }
        worker.start();
        System.out.println(dateFormat.format(date) + " Server: Connected to ");
        worker.setName("worker" + uptimeString());

        allConnections.add(worker);

      }
      catch(IOException e)
      {
        System.out.println(dateFormat.format(date) + " Server error: Failed to connect to client.");
        e.printStackTrace();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
  }


  public String uptimeString ()
  {
    return String.format("%.3f", uptime());
  }

  public double uptime ()
  {
    long nanoSecDiff = System.nanoTime() - startNanoSec;
    return nanoSecDiff / 1000000000.0;
  }


  public Simulator getSimulator ()
  {
    return simulator;
  }

  public User getUserByUsername (String username)
  {
    for (User user : userList)
    {
      if (user.getUsername().equals(username))
      {
        return user;
      }
    }

    return null;
  }

  //  public User getUserByWorker (String worker)
  //  {
  //    return new User(new JSONDocument(JSONDocument.Type.OBJECT));//users.get(worker);
  //  }

  public Iterable<Worker> getWorkerByRegion (EnumRegion region)
  {
    ArrayList<Worker> _list = new ArrayList<>();
    for (Worker worker : allConnections)
    {
      if (worker.getUser().getRegion() == region)
      {
        _list.add(worker);
      }
    }
    return _list;
  }

  public Iterable<User> getUserList ()
  {
    return userList;
  }

  public boolean createUser (User u)
  {
    userList.add(u);
//    Payload data = new Payload();
//    data.putData(u);
//    broadcast(new Response(uptime(), data));

    return true;
  }




  public int getLoggedInCount ()
  {
    int i = 0;
    for (User user : userList)
    {
      if (user.isLoggedIn())
      {
        i++;
      }
    }
    return i;
  }

  public Iterable<User> getLoggedInUsers ()
  {
    ArrayList<User> _active = new ArrayList<>();
    for (User user : userList)
    {
      if (user.isLoggedIn())
      {
        _active.add(user);
      }
    }
    return _active;
  }

  public int getUserCount ()
  {
    return userList.size();
  }


  /**
   * Draw new cards for a specific user
   */
  public void drawByWorker (Worker worker)
  {
    EnumPolicy[] _hand = simulator.drawCards(worker.getUser().getRegion());
    worker.getUser().setHand(new ArrayList<>(Arrays.asList(_hand)));
  }





  public void broadcast (Response response)
  {
    for (User u : getLoggedInUsers())
    {
      u.getWorker().send(response);
    }
  }

  public void killServer ()
  {
    System.out.println(dateFormat.format(date) + " Killing server.");
    isWaiting = false;
    for (Worker connection : allConnections)
    {
      connection.send(new Response(uptime(), "Server will shutdown in 3 seconds"));
    }

    try
    {
      Thread.sleep(3100);
      for (Worker connection : allConnections)
      {
        connection.shutdown();
      }

    }
    catch(InterruptedException ex)
    {
      Thread.currentThread().interrupt();
    }

    System.exit(1);
  }

  public State getGameState ()
  {
    return currentState;
  }

  public void restartGame ()
  {
    stopGame();
    broadcast(new Response(uptime(), "The game has been restarted."));
    simulator = new Simulator();
    // TODO clear all hands and cards

    // There is a loop constantly checking if state is login...
    currentState = State.LOGIN;

  }

  public void stopGame ()
  {
    phase.cancel(true);
    advancer.shutdownNow();
    advancer = Executors.newSingleThreadScheduledExecutor();
    currentState = State.END;
    broadcast(new Response(uptime(), "The game has been stopped."));
  }

  public Iterable<User> getPlayers()
  {
    synchronized(players)
    {
      return players;
    }
  }

  public int getPlayerCount ()
  {
    synchronized(players)
    {
      return players.size();
    }
  }

  public boolean addPlayer (User u)
  {
    EnumRegion _region = u.getRegion();

    if (_region != null)
    {
      int loc = availableRegions.lastIndexOf(_region);
      if (loc == -1)
      {
        return false;
      }
      availableRegions.remove(loc);
      u.setPlaying(true);
      players.add(u);
      return true;
    }
    else
    {
      u.setRegion(availableRegions.remove(0));
      u.setPlaying(true);
      players.add(u);
      return true;
    }
  }

  public ArrayList<EnumRegion> getAvailableRegions ()
  {
    return availableRegions;
  }

  /**
   * Beginning of the game!!!
   *
   * Users are delt cards and world data is sent out.
   */
  private void begin ()
  {
    currentState = State.BEGINNING;
    broadcastStateChange();

    ArrayList<WorldData> worldDataList = simulator.getWorldData(Constant.FIRST_DATA_YEAR,
                                                                Constant.FIRST_GAME_YEAR - 1);

    Payload data = new Payload();

    for (User user : getPlayers())
    {
      EnumPolicy[] _hand = simulator.drawCards(user.getRegion());
      ArrayList<EnumPolicy> handList = new ArrayList<>();
      Collections.addAll(handList, _hand);
      user.setHand(handList);

      data.putData(user);
      data.clear();
      Response r = new Response(uptime(), data);
      r.setType(Type.USER_HAND);
      user.getWorker().send(r);
    }

    data.putData(worldDataList);
    Response r = new Response(uptime(), data);
    r.setType(Type.WORLD_DATA_LIST);
    broadcast(r);

    draft();
  }

  /**
   * Sets the state to drafting and schedules a new task.
   * Drafting allows for users to discard and draw new cards
   */
  private void draft ()
  {
    currentState = State.DRAFTING;
    broadcastStateChange();

    enactedPolicyCards.clear();


    phase = advancer.schedule(this::vote, currentState.getDuration(), TimeUnit.MILLISECONDS);

  }

  /**
   * Sets the state to vote and schedules a new draw task
   * Allows users to send votes on cards
   */
  private void vote ()
  {
    currentState = State.VOTING;
    broadcastStateChange();

    Payload cards = new Payload();
    ArrayList<PolicyCard> list = new ArrayList<>();
    for (PolicyCard card : draftedPolicyCards)
    {
      if (card.votesRequired() > 0)
      {
        list.add(card);
      }
    }

    cards.putData(list);
    Response r = new Response(uptime(), cards);
    r.setType(Type.VOTE_BALLOT);

    broadcast(r);

    phase = advancer.schedule(this::draw, currentState.getDuration(), TimeUnit.MILLISECONDS);

  }


  /**
   * Draw cards for all users
   */
  private void draw ()
  {
    currentState = State.DRAWING;
    broadcastStateChange();

    for (PolicyCard p : draftedPolicyCards)
    {
      System.out.println(p);
      if (p.votesRequired() > p.getEnactingRegionCount())
      {
        simulator.discard(p.getOwner(), p.getCardType());
      }
      else
      {
        enactedPolicyCards.add(p);

        simulator.discard(p.getOwner(), p.getCardType());
      }
    }

    for (User user : getPlayers())
    {
      Payload data = new Payload();
      EnumPolicy[] _hand = simulator.drawCards(user.getRegion());
      ArrayList<EnumPolicy> handList = new ArrayList<>();
      Collections.addAll(handList, _hand);
      user.setHand(handList);

      data.putData(user);
      data.clear();
      Response r = new Response(uptime(), data);
      r.setType(Type.USER_HAND);
      user.getWorker().send(r);
    }

    System.out.println("here");
    WorldData data = simulator.nextTurn(enactedPolicyCards);
    Payload payload = new Payload();
    payload.putData(data);
    Response r = new Response(payload);
    r.setType(Type.WORLD_DATA);
    broadcast(r);

    if (data.year >= Constant.LAST_YEAR)
    {
      currentState = State.END;
    }


    phase = advancer.schedule(this::draft, currentState.getDuration(), TimeUnit.MILLISECONDS);
  }

  /**
   * Method that is on a timer called every 500ms
   *
   * Mainly to start the game and clean the connection list
   */
  private void update ()
  {
    cleanConnectionList();

    if (getPlayerCount() == 1 && currentState == State.LOGIN)
    {
      currentState = State.BEGINNING;
      Payload data = new Payload();
      data.putData(currentState);
      data.putMessage("Game will begin in 10s");
      Response r = new Response(uptime(), data);
      r.setType(Type.GAME_STATE);
      broadcast(r);

      phase = advancer.schedule(this::begin, currentState.getDuration(), TimeUnit.MILLISECONDS);
    }
  }


  /**
   * Handle a handshake with web client
   *
   * @param x Key received from client
   *
   * @return Hashed key that is to be given back to client for auth check.
   */
  private static String handshake (String x)
  {

    MessageDigest digest;
    byte[] one = x.getBytes();
    byte[] two = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11".getBytes();
    byte[] combined = new byte[one.length + two.length];

    for (int i = 0; i < combined.length; ++i)
    {
      combined[i] = i < one.length ? one[i] : two[i - one.length];
    }

    try
    {
      digest = MessageDigest.getInstance("SHA-1");
    }
    catch(NoSuchAlgorithmException e)
    {
      e.printStackTrace();
      return "";
    }

    digest.reset();
    digest.update(combined);

    return new String(Base64.getEncoder().encode(digest.digest()));

  }

  /**
   * Set up the worker with proper streams
   *
   * @param worker worker that is holding the socket connection
   * @param s socket that is opened
   * @return
   */
  private boolean secureConnection (Worker worker, Socket s)
  {
    // Handling websocket
    // StringBuilder reading = new StringBuilder();
    String line = "";
    String key = "";
    String socketKey = "";
    ReadStrategy<String> reader = worker.getReader();

    while(true)
    {
      try
      {
        line = reader.read();
      }
      catch(Exception e)
      {
        e.printStackTrace();
        return false;
      }

      // check if the end of line or if data was found.
      if (line.trim().equals("client") || line.equals("\r\n") || line.trim().equals("JavaClient"))
      {
        if (line.contains("JavaClient"))
        {
          worker.setReader(new JavaObjectReadStrategy(s, null));
          worker.setWriter(new JavaObjectWriteStrategy(s, null));
          return false;
        }

        if (socketKey.isEmpty())
        {
          return false;
        }
        else
        {
          // use the plain text writer to send following data
          worker.setWriter(new PlainTextWriteStrategy(s, null));
          ((PlainTextWriteStrategy)worker.getWriter())
                  .getWriter().println("HTTP/1.1 101 Switching Protocols\n" +
                                               "Upgrade: websocket\n" +
                                               "Connection: Upgrade\n" +
                                               "Sec-WebSocket-Accept: " + socketKey + "\r\n");

          return true;
        }

      }

      // reading.append(line);
      if (line.contains("Sec-WebSocket-Key:"))
      {
        // removing whitespace (includes nl, cr)
        key = line.replace("Sec-WebSocket-Key: ", "").trim();
        socketKey = Server.handshake(key);
      }
      if (line.contains("Sec-Socket-Key: "))
      {
        key = line.replace("Sec-Socket-Key: ", "").trim();
        socketKey = Encryptable.generateKey();
      }
    }
  }

  /**
   * Cleans the connections list that have gone stale
   */
  private void cleanConnectionList ()
  {
    int con = 0;
    for (int i = 0; i < allConnections.size(); i++)
    {
      if (!allConnections.get(i).isRunning())
      {
        if (allConnections.get(i).getUser() != null)
        {
          // no longer active
          allConnections.get(i).getUser().setLoggedIn(false);
        }
        allConnections.get(i).shutdown();
        // the worker is not running. remove it.
        allConnections.remove(i);
        con++;
      }
    }
    // check if any removed. Show removed count
    if (con > 0)
    {
      System.out.println(dateFormat.format(date) + " Removed " + con + " connection workers.");
    }
  }

  private void broadcastStateChange ()
  {
    System.out.println(currentState);
    Payload _data = new Payload();
    _data.putData(currentState);
    Response r = new Response(uptime(), _data);
    r.setType(Type.GAME_STATE);
    broadcast(r);
  }

  public static void main (String args[])
  {
    //Valid port numbers are Port numbers are 1024 through 65535.
    //  ports under 1024 are reserved for system services http, ftp, etc.
    int port = 5555; //default
    if (args.length > 0)
    {
      try
      {
        port = Integer.parseInt(args[0]);
        if (port < 1)
        {
          throw new Exception();
        }
      }
      catch(Exception e)
      {
        System.out.println("Usage: Server portNumber");
        System.exit(0);
      }
    }

    new Server(port);
  }

  public void addDraftedCard (PolicyCard policyCard)
  {
    draftedPolicyCards.add(policyCard);
  }

  public HashMap<PolicyCard, Tuple<User, Boolean>> getVoteMap ()
  {
    return votes;
  }

  public ArrayList<PolicyCard> getDraftedPolicyCards ()
  {
    return draftedPolicyCards;
  }


}
