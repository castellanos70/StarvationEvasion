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
import java.util.concurrent.*;
import java.util.function.BooleanSupplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;


/**
 */
public class Server
{
  private ServerSocket serverSocket;
  // List of all the workers
  private LinkedList<Worker> allConnections = new LinkedList<>();

  private LinkedList<Process> processes = new LinkedList<>();

  private long startNanoSec = 0;
  private Simulator simulator;

  // list of ALL the users
  private final List<User> userList = Collections.synchronizedList(new ArrayList<>());

  private State currentState = State.LOGIN;
  private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
  private Date date = new Date();

  // list of available regions
  private List<EnumRegion> availableRegions = Collections.synchronizedList(new ArrayList<>());
  private ArrayList<PolicyCard> enactedPolicyCards = new ArrayList<>();
  private final LinkedBlockingQueue<PolicyCard> draftedPolicyCards = new LinkedBlockingQueue<>();


  private ScheduledFuture<?> phase;
  // Service that moves game along to next phase
  private ScheduledExecutorService advancer = Executors.newSingleThreadScheduledExecutor();

  // bool that listen for connections is looping over
  private boolean isWaiting = true;

  public static int TOTAL_PLAYERS = 2;

  public Server (int portNumber)
  {

    Collections.addAll(availableRegions, EnumRegion.US_REGIONS);

    createUser(new User("admin", "admin", null, new ArrayList<>()));
    createUser(new User("ANON", "", null, new ArrayList<>()));
    createUser(new User("Emma", "bot", null, new ArrayList<>()));
    createUser(new User("Olivia", "bot", null, new ArrayList<>()));
    createUser(new User("Noah", "bot", null, new ArrayList<>()));
    createUser(new User("Liam", "bot", null, new ArrayList<>()));
    createUser(new User("Sophia", "bot", null, new ArrayList<>()));


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
    }, 0, 1500);

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
      userList.add(u);
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
    for (Worker worker : allConnections)
    {
      worker.send(response);
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
    synchronized(currentState)
    {
      return currentState;
    }
  }

  public void restartGame ()
  {
    stopGame();
    broadcast(new Response(uptime(), "The game has been restarted."));
    simulator = new Simulator();

    for (User user : getPlayers())
    {
      user.getHand().clear();
    }
    enactedPolicyCards.clear();
    draftedPolicyCards.clear();
    currentState = State.LOGIN;
    broadcastStateChange();
  }

  public void stopGame ()
  {
    if (phase != null)
    {
      phase.cancel(true);
    }
    advancer.shutdownNow();
    advancer = Executors.newSingleThreadScheduledExecutor();
    currentState = State.END;
    broadcast(new Response(uptime(), "The game has been stopped."));
  }

  public List<User> getPlayers ()
  {
    return userList.stream()
                   .filter(user -> user.isPlaying())
                   .collect(Collectors.toList());
  }

  public int getPlayerCount ()
  {
    return (int) getPlayers().stream()
                             .count();
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
      // players.add(u);
      return true;
    }
    else
    {
      u.setRegion(availableRegions.get(Util.randInt(0, availableRegions.size() - 1)));
      u.setPlaying(true);
      return true;
    }
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
  private void begin ()
  {
    currentState = State.BEGINNING;
    broadcastStateChange();

    ArrayList<WorldData> worldDataList = simulator.getWorldData(Constant.FIRST_DATA_YEAR,
                                                                Constant.FIRST_GAME_YEAR - 1);

    Payload payload = new Payload();
    Response response = new Response(uptime(), payload);

    for (User user : getPlayers())
    {
      drawByUser(user);

      payload.putData(user);
      response.setType(Type.USER);
      user.getWorker().send(response);
    }

    payload.clear();
    payload.putData(worldDataList);
    response.setType(Type.WORLD_DATA_LIST);
    broadcast(response);

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
    ArrayList<PolicyCard> _list = new ArrayList<>();

    Iterator i = draftedPolicyCards.iterator();
    while(i.hasNext())
    {
      PolicyCard card = (PolicyCard)i.next();
      if (card.votesRequired() >= 1)
      {
        _list.add(card);
      }
    }


    cards.putData(_list);
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
    enactedPolicyCards = new ArrayList<>();

    PolicyCard p;
    while ((p=draftedPolicyCards.poll())!=null)
    {

      if (p.votesRequired() == 0 || p.getEnactingRegionCount() > p.votesRequired())
      {
        enactedPolicyCards.add(p);
      }
      simulator.discard(p.getOwner(), p.getCardType());
    }

    draftedPolicyCards.clear();



    for (User user : getPlayers())
    {
      Payload payload = new Payload();
      Response response = new Response(uptime(), payload);
      drawByUser(user);
      payload.putData(user);
      response.setType(Type.USER);
      user.getWorker().send(response);
    }


    ArrayList<WorldData> worldData = simulator.nextTurn(enactedPolicyCards);
    System.out.println("There is " + enactedPolicyCards.size() + " cards being enacted.");
    // System.out.println(String.valueOf(enactedPolicyCards));

    Payload payload = new Payload();
    Response response = new Response(uptime(), payload);
    payload.putData(worldData);
    response.setType(Type.WORLD_DATA_LIST);
    broadcast(response);

    if (simulator.getCurrentYear() >= Constant.LAST_YEAR)
    {
      currentState = State.END;
      broadcastStateChange();
      return;
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

    if (getPlayerCount() == TOTAL_PLAYERS && currentState == State.LOGIN)
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
   *
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
          ((PlainTextWriteStrategy) worker.getWriter())
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
    // System.out.println(currentState);
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


  public LinkedBlockingQueue<PolicyCard> getDraftedPolicyCards ()
  {
    return draftedPolicyCards;
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
    Process p = ServerUtil.StartAIProcess(new String[]{"java", "-classpath", "./dist:./dist/libs/*", "starvationevasion/ai/AI", "foodgame.cs.unm.edu", "5555"});
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
      p.destroy();
      try
      {
        p.waitFor();
      }
      catch(InterruptedException e)
      {
      }
      int val = p.exitValue();
      Payload data = new Payload();
      data.put("to-region", "ALL");

      Response response = new Response(uptime(), data);

      data.put("text", "AI was removed.");

      System.out.println("AI removed with exit value: " + String.valueOf(val));

      broadcast(response);

    }
  }




}
