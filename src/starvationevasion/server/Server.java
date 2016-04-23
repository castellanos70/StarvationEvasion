package starvationevasion.server;


/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.*;
import starvationevasion.server.io.*;
import starvationevasion.server.io.strategies.*;
import starvationevasion.server.model.*;
import starvationevasion.server.model.db.Transaction;
import starvationevasion.server.model.db.Users;
import starvationevasion.server.model.db.backends.Backend;
import starvationevasion.server.model.db.backends.Sqlite;
import starvationevasion.sim.Simulator;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.*;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


/**
 */
public class Server
{
  private ServerSocket serverSocket;
  // List of all the workers
  private final LinkedList<Worker> allConnections = new LinkedList<>();

  private LinkedList<Process> processes = new LinkedList<>();

  private ArrayList<User> playerCache = new ArrayList<>();

  private long startNanoSec = 0;
  private long endNanoSec = 0;
  private final Simulator simulator;

  // list of ALL the users
  private final List<User> userList = new ArrayList<>();//Collections.synchronizedList(new ArrayList<>());

  private State currentState = State.LOGIN;
  private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
  private Date date = new Date();

  // list of available regions
  private final List<EnumRegion> availableRegions = new ArrayList<>(EnumRegion.US_REGIONS.length);

  private final PolicyCard[][] _drafted = new PolicyCard[EnumRegion.US_REGIONS.length][2];

  // bool that listen for connections is looping over
  private boolean isWaiting = true;

  public static int TOTAL_PLAYERS = 2;

  // Create a backend, currently sqlite
  private final Backend db = new Sqlite(Constant.DB_LOCATION);

  // Class that save User's to a database
  private final Transaction<User> userTransaction;
  private volatile long counter = 0;
  private volatile boolean isPlaying = false;


  public Server (int portNumber)
  {
    userTransaction = new Users(db);
    Collections.addAll(availableRegions, EnumRegion.US_REGIONS);

    // get all the users from the database
    for (User user : userTransaction.getAll())
    {
      userList.add(user);
    }


    // startNanoSec = System.nanoTime();
    simulator = new Simulator();

    try
    {
      serverSocket = new ServerSocket(portNumber);
      serverSocket.setSoTimeout(10);
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
    }, 0, 1000);

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
   * TODO: Refactor waiting to use a NON-Blocking Input/Output. Look at ServerSocketChannel
   *
   * @param port port to listen on.
   */
  private void waitForConnection (int port)
  {

    String host = "";
    Worker worker = null;
    Socket potentialClient = null;
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
      try
      {
        potentialClient = serverSocket.accept();
        System.out.println(dateFormat.format(date) + " Server: new Connection request received.");
        worker = new Worker(potentialClient, this);

        setStreamType(worker, potentialClient);
        worker.start();
        System.out.println(dateFormat.format(date) + " Server: Connected to " + potentialClient.getRemoteSocketAddress());
        worker.setName("worker" + uptimeString());

        allConnections.add(worker);
        userList.add(worker.getUser());

      }
      catch(IOException e)
      {
        // System.out.println(dateFormat.format(date) + " Server error: Failed to connect to client.");
        // e.printStackTrace();
      }
      catch(HandshakeException e)
      {
        if (worker != null)
        {
          worker.shutdown();
          System.out.println(dateFormat.format(date) + " Server: Failed to complete handshake. Closing connection");
        }
      }
      catch(Exception e)
      {
        // e.printStackTrace();
      }
      finally
      {
        cleanConnectionList();
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


  public synchronized Simulator getSimulator ()
  {
    return simulator;
  }

  public synchronized User getUserByUsername (String username)
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

  public List<User> getUserList ()
  {
    return userList;
  }

  public boolean createUser (User u)
  {
    boolean found = userList.stream()
                            .anyMatch(user -> user.getUsername().equals(u.getUsername()));
    if (!found)
    {
      User created = userTransaction.create(u);
      if (created == null)
      {
        return false;
      }
      userList.add(created);
      return true;
    }
    return false;
  }


  public int getLoggedInCount ()
  {
    return (int) getLoggedInUsers().stream()
                                   .count();
  }

  public List<User> getLoggedInUsers ()
  {
    return userList.stream()
                   .filter(user -> user.isLoggedIn())
                   .collect(Collectors.toList());
  }

  public int getUserCount ()
  {
    return userList.size();
  }


  public void broadcast (Response response)
  {
    int i = 0;
    try
    {
      for (; i < allConnections.size(); i++)
      {
        allConnections.get(i).send(response);
      }
    }
    catch(Exception e)
    {
      System.out.println("Error sending message " + e.getMessage());
      System.out.println(allConnections.get(i).getName() +
                                 " User: " + allConnections.get(i).getUser().getUsername());
    }
  }

  public void killServer ()
  {
    System.out.println(dateFormat.format(date) + " Killing server.");
    isWaiting = false;
    for (Worker connection : allConnections)
    {
      connection.send(new ResponseFactory().build(uptime(),
                                            currentState,
                                            Type.BROADCAST, "Server will shutdown in 3 sec"
      ));
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
    broadcast(new ResponseFactory().build(uptime(), currentState, Type.BROADCAST, "Game restarted."));
    for (User user : userList)
    {
      user.reset();
      user.setHand(new ArrayList<>());
      user.setPlaying(false);
    }
    playerCache = new ArrayList<>();
    simulator.init();

    for (int i = 0; i < _drafted.length; i++)
    {
      _drafted[i] = new PolicyCard[2];
    }
    isPlaying = true;
    currentState = State.LOGIN;
    broadcastStateChange();
  }

  public void stopGame ()
  {
    currentState = State.END;
    isPlaying = false;
    broadcast(new ResponseFactory().build(uptime(), currentState, Type.GAME_STATE, "Game has been stopped."));
  }

  public List<User> getPlayers ()
  {
    return playerCache;
  }

  public int getPlayerCount ()
  {
    return playerCache.size();
  }

  public synchronized boolean addPlayer (User u)
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
      // players.add(u);
    }
    else
    {
      u.setRegion(availableRegions.get(Util.randInt(0, availableRegions.size() - 1)));
      u.setPlaying(true);
    }

    playerCache = new ArrayList<>();
    for (User user : userList)
    {
      if (user.isPlaying())
      {
        playerCache.add(user);
      }
    }
    return true;
  }

  public List<EnumRegion> getAvailableRegions ()
  {
    return availableRegions;
  }

  /**
   * Beginning of the game!!!
   *
   * Users are delt cards and world data is sent out.
   */
  private Void begin ()
  {

    ArrayList<WorldData> worldDataList = simulator.getWorldData(Constant.FIRST_DATA_YEAR,
                                                                Constant.FIRST_GAME_YEAR - 1);

    currentState = State.BEGINNING;
    broadcastStateChange();
    isPlaying = true;

    for (User user : getPlayers())
    {
      drawByUser(user);
      user.reset();
      user.getWorker().send(new ResponseFactory().build(uptime(),
                                                  user,
                                                  Type.USER));
    }

    broadcast(new ResponseFactory().build(uptime(), new Payload(worldDataList), Type.WORLD_DATA_LIST));
    draft();
    return null;
  }

  /**
   * Sets the state to drafting and schedules a new task.
   * Drafting allows for users to discard and draw new cards
   */
  private Void draft ()
  {
    currentState = State.DRAFTING;
    broadcastStateChange();

    waitAndAdvance(Server.this::vote);

    return null;
  }

  /**
   * Sets the state to vote and schedules a new draw task
   * Allows users to send votes on cards
   */
  private Void vote ()
  {
    currentState = State.VOTING;

    ArrayList<PolicyCard> _list = new ArrayList<>();
    synchronized(_drafted)
    {
      for (PolicyCard[] policyCards : _drafted)
      {
        for (PolicyCard policyCard : policyCards)
        {
          if (policyCard != null)
          {
            _list.add(policyCard);
          }
        }
      }
    }
    broadcastStateChange();
    broadcast(new ResponseFactory().build(uptime(),
                                          new Payload(_list),
                                          Type.VOTE_BALLOT));

    waitAndAdvance(Server.this::draw);
    return null;
  }


  /**
   * Draw cards for all users
   */
  private Void draw ()
  {
    ArrayList<PolicyCard> enactedPolicyCards = new ArrayList<>();
    ArrayList<PolicyCard> _list = new ArrayList<>();


    for (PolicyCard[] policyCards : _drafted)
    {
      for (PolicyCard p : policyCards)
      {
        if (p == null) continue;
        if (p.votesRequired() == 0 || p.getEnactingRegionCount() > p.votesRequired())
        {
          enactedPolicyCards.add(p);
        }
        _list.add(p);
        simulator.discard(p.getOwner(), p.getCardType());
      }
    }
    VoteData voteData = new VoteData(_list, enactedPolicyCards, _drafted);
    broadcast(new ResponseFactory().build(uptime(), voteData, Type.VOTE_RESULTS));

    currentState = State.DRAWING;
    broadcastStateChange();


    for (User user : getPlayers())
    {
      drawByUser(user);
      user.reset();

      user.getWorker().send(new ResponseFactory().build(uptime(),
                                                  user,
                                                  Type.USER));
    }

    // make sure there is no other thread calling the sim before advancing
    synchronized(simulator)
    {
      ArrayList<WorldData> worldData = simulator.nextTurn(enactedPolicyCards);
      System.out.println("There is " + enactedPolicyCards.size() + " cards being enacted.");
      broadcast(new ResponseFactory().build(uptime(), new Payload(worldData), Type.WORLD_DATA_LIST));
    }
    // clear the votes
    for (int i = 0; i < EnumRegion.US_REGIONS.length; i++)
    {
      // the 2d array is organized by regions that drafted card
      if (_drafted[i] == null) continue;
      for (int j = 0; j < 2; j++)
      {
        if (_drafted[i][j] == null) continue;
        _drafted[i][j].clearVotes();
      }
      _drafted[i] = new PolicyCard[2];
    }

    if (simulator.getCurrentYear() >= Constant.LAST_YEAR)
    {
      currentState = State.END;
      broadcastStateChange();
      isPlaying = false;
      return null;
    }

    waitAndAdvance(Server.this::draft);

    return null;
  }

  /**
   * Method that is on a timer called every 500ms
   *
   * Mainly to start the game and clean the connection list
   */
  private void update ()
  {
    if (getPlayerCount() == TOTAL_PLAYERS && currentState == State.LOGIN)
    {
      isPlaying = true;
      currentState = State.BEGINNING;
      broadcast(new ResponseFactory().build(uptime(), currentState, Type.GAME_STATE, "Game will begin in 10s"));
      waitAndAdvance(this::begin);
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

  private static byte[] asymmetricHandshake (String desKey, String clientPublicKey)
  {
    PublicKey pubKey = null;
    byte[] cipherText = null;

    try
    {
      final KeyFactory keyFact = KeyFactory.getInstance("RSA");

      byte[] clientBytes = Base64.getDecoder().decode(clientPublicKey);
      X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(clientBytes);

      pubKey = keyFact.generatePublic(x509KeySpec);

      final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, pubKey);

      cipherText = cipher.doFinal(desKey.getBytes());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    return cipherText;
  }

  /**
   * Set up the worker with proper streams
   *
   * @param worker worker that is holding the socket connection
   * @param s socket that is opened
   *
   * @return boolean true if web-socket
   */
  private void setStreamType (Worker worker, Socket s) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InterruptedException
  {
    // Handling websocket
     StringBuilder reading = new StringBuilder();
    String line = "";
    String socketKey = "";
    byte[] encryptedKey = new byte[128];
    ReadStrategy<String> reader = worker.getReader();
    SecretKey secretKey = null;
    boolean encrypted = false;
    boolean success = false;
    int secs = 0;
    int length = 0;
    s.setSoTimeout(3000);
    ReadStrategy discoveredReader = worker.getReader();
    WriteStrategy discoveredWriter = worker.getWriter();

    HttpParse paresr = new HttpParse();

    while(secs < 3)
    {
      try
      {
        line = reader.read();
        reading.append(line);
        if (line.startsWith("Content-Length"))
        {
          String number = line.replaceAll("[^0-9]", "").trim();
          length = Integer.valueOf(number);
        }
        secs = 0;
      }
      catch(SocketTimeoutException e)
      {
        secs++;
        continue;
      }
      catch(Exception e)
      {
        e.printStackTrace();
        return;
      }

      if(line.equals("\r\n"))
      {
        success = true;
        break;
      }
    }

    if (length > 0)
    {
      byte[] messageBytes = new byte[length];
      reader.getStream().read(messageBytes);
      reading.append(new String(messageBytes));
    }

    paresr.parseRequest(reading.toString());

    String connectionType = paresr.getHeaderParam("Connection");
    String acceptType = paresr.getHeaderParam("Accept");


    if (connectionType.equals("Upgrade"))
    {
      System.out.println(dateFormat.format(date) + " Server: Connected to socket.");
      if (acceptType == null || acceptType.equals(DataType.JSON.toString()))
      {
        if (paresr.getHeaderParam("User-Agent") != null)
        {
          System.out.println("\tServer: Web client.");
          discoveredReader = new WebSocketReadStrategy(s, null);
          discoveredWriter = new WebSocketWriteStrategy(s, null);
        }
        else
        {
          System.out.println("\tServer: JSON client.");
          discoveredReader = new SocketReadStrategy(s, null);
          discoveredWriter = new SocketWriteStrategy(s, null);
        }
      }
      else if (acceptType.equals(DataType.POJO.toString()))
      {
        System.out.println("\tServer: Java client.");
        discoveredReader = new JavaObjectReadStrategy(s, null);
        discoveredWriter = new JavaObjectWriteStrategy(s, null);
      }
      else if (acceptType.equals(DataType.TEXT.toString()))
      {
        throw new NotImplementedException();
      }

      if (paresr.getHeaderParam("RSA-Socket-Key") != null)
      {
        System.out.println("\tServer: Encrypted Socket.");
        KeyGenerator keygenerator = KeyGenerator.getInstance(Constant.DATA_ALGORITHM);
        keygenerator.init(128);

        secretKey = keygenerator.generateKey();
        encryptedKey = Server.asymmetricHandshake(
                Base64.getEncoder().encodeToString(secretKey.getEncoded()),
                paresr.getHeaderParam("RSA-Socket-Key"));

        encrypted = true;
      }
      else if (paresr.getHeaderParam("Sec-WebSocket-Key") != null)
      {
        System.out.println("\tServer: Encrypted WS.");
        socketKey = Server.handshake(paresr.getHeaderParam("Sec-WebSocket-Key"));
        encrypted = true;
      }
    }
    else if (connectionType.equals("keep-alive"))
    {
      System.out.println(dateFormat.format(date) + " Server: HTTP request.");
      worker.setWriter(new HTTPWriteStrategy(s, null));
      worker.handleDirectly(paresr);
      return;
    }


    if (!success)
    {
      throw new HandshakeException();
    }
    // setting back to default blocking
    s.setSoTimeout(0);
    if (encrypted)
    {
      if (paresr.getHeaderParam("User-Agent") != null)
      {
        worker.getWriter().getStream().writeUTF("HTTP/1.1 101 Web Socket Protocol Handshake\n" +
                                                        "Upgrade: WebSocket\n" +
                                                        "Connection: Upgrade\n" +
                                                        "Sec-WebSocket-Accept: " + socketKey + "\r\n\r\n");
      }
      else
      {
        worker.getWriter().getStream().write(encryptedKey);
      }
      worker.getWriter().getStream().flush();
      worker.setReader(discoveredReader);
      worker.setWriter(discoveredWriter);
      worker.getWriter().setEncrypted(true, secretKey);
      worker.getReader().setEncrypted(true, secretKey);
    }
    else
    {
      broadcast(new ResponseFactory().build(uptime(), currentState, Type.CHAT, "Someone joined with unencrypted!"));
      worker.setReader(discoveredReader);
      worker.setWriter(discoveredWriter);
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
        User u = allConnections.remove(i).getUser();
        u.setLoggedIn(false);
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
    getPlayers().stream().forEach(user -> user.isDone = false);
    broadcast(new ResponseFactory().build(uptime(), currentState, Type.GAME_STATE));
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


  public void draftCard (PolicyCard card, User u)
  {
    synchronized (_drafted)
    {
      _drafted[card.getOwner().ordinal()][u.drafts] = card;
    }
  }

  public boolean addVote (PolicyCard card, EnumRegion user)
  {
    synchronized(_drafted)
    {
      for (int i = 0; i < _drafted[card.getOwner().ordinal()].length; i++)
      {
        if (_drafted[card.getOwner().ordinal()][i] == null) continue;
        if (_drafted[card.getOwner().ordinal()][i].getCardType().equals(card.getCardType()))
        {
          _drafted[card.getOwner().ordinal()][i].addEnactingRegion(user);
          return true;
        }
      }
    }
    return false;
  }


  public void drawByUser (User user)
  {
    EnumPolicy[] _hand = simulator.drawCards(user.getRegion());
    if (_hand == null)
    {
      return;
    }
    Collections.addAll(user.getHand(), _hand);

  }


  public void startAI()
  {
    Process p = ServerUtil.StartAIProcess(new String[]{"java",
                                                       "-XX:+OptimizeStringConcat",
                                                       "-XX:+UseCodeCacheFlushing",
                                                       "-client",
                                                       "-classpath",
                                                       "./dist:./dist/libs/*",
                                                       "starvationevasion/ai/AI",
                                                       "foodgame.cs.unm.edu", "5555"});
    if (p != null)
    {
      processes.add(p);
    }
  }

  public void killAI()
  {
    if (processes.size() > 0)
    {
      Process p = processes.poll();
      p.destroyForcibly();
      try
      {
        p.waitFor();
      }
      catch(InterruptedException e)
      {
        System.out.println("Interrupted and could not wait for exit code");
      }
      int val = p.exitValue();
      Payload data = new Payload();
      data.put("to-region", "ALL");
      data.put("card", "");
      data.put("text", "AI was removed.");
      data.put("from", "admin");

      System.out.println("AI removed with exit value: " + String.valueOf(val));

      broadcast(new ResponseFactory().build(uptime(), data, Type.CHAT));

    }
  }


  public void discard (User u, EnumPolicy card)
  {
    synchronized(simulator)
    {
      simulator.discard(u.getRegion(), card);
      u.getHand().remove(card);
    }
  }

  void waitAndAdvance(Callable phase)
  {
    startNanoSec = System.currentTimeMillis();
    endNanoSec = startNanoSec + currentState.getDuration();
    while(true)
    {
      if (!isPlaying)
      {
        return;
      }
      boolean allDone = getPlayers().stream().allMatch(user -> user.isDone);
      if (allDone || endNanoSec < System.currentTimeMillis())
      {
        try
        {
          phase.call();
        }
        catch(Exception e)
        {
          System.out.println("Could not advance. Stopping game.");
          System.out.println("Error: " + e.getMessage());
          stopGame();
          return;
        }
      }
    }
  }
}
