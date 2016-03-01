package starvationevasion.server;


/**
 * @author Javier Chavez
 */

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.common.Constant;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import starvationevasion.server.io.WebSocketReadStrategy;
import starvationevasion.server.io.WebSocketWriteStrategy;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.State;
import starvationevasion.server.model.User;
import starvationevasion.sim.Simulator;
import starvationevasion.common.WorldData;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 */
public class Server
{
  private ServerSocket serverSocket;
  private LinkedList<Worker> allConnections = new LinkedList<>();
  private long startNanoSec = 0l;
  private final Timer timer = new Timer();
  private Simulator simulator;
  private HashMap<String, User> users = new HashMap<>();
  private ArrayList<User> userList = new ArrayList<>();
  private ArrayList<EnumRegion> availableRegions = new ArrayList<>();
  private State currentState = State.LOGIN;
  

  public Server (int portNumber)
  {

    Collections.addAll(availableRegions, EnumRegion.US_REGIONS);

    addUser(new User("admin", "admin", EnumRegion.CALIFORNIA, new ArrayList<>()));
    startNanoSec = System.nanoTime();
    simulator = new Simulator(Constant.FIRST_YEAR);


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
    timer.schedule(new TimerTask()
    {
      @Override
      public void run ()
      {
        cleanConnectionList();
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
  public void waitForConnection (int port)
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
    while(true)
    {
      System.out.println("Server(" + host + "): waiting for Connection on port: " + port);
      try
      {
        Socket client = serverSocket.accept();
        System.out.println("Server: *********** new Connection");
        Worker worker = new Worker(client, this);
        worker.setServerStartTime(startNanoSec);


        if(websocketConnect(worker))
        {
          worker.setReader(new WebSocketReadStrategy(client));
          worker.setWriter(new WebSocketWriteStrategy(client));
        }
        worker.start();
        worker.setName("worker" + uptimeString());

        allConnections.add(worker);

      }
      catch(IOException e)
      {
        System.err.println("Server error: Failed to connect to client.");
        e.printStackTrace();
      }
    }
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


  public String uptimeString ()
  {
    return String.format("%.3f", uptime());
  }

  public double uptime ()
  {
    long nanoSecDiff = System.nanoTime() - startNanoSec;
    double secDiff = nanoSecDiff / 1000000000.0;
    return secDiff;
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

  public User getUserByWorker (String worker)
  {
    return new User(new JSONDocument(JSONDocument.Type.OBJECT));//users.get(worker);
  }

  public Worker getWorkerByRegion(EnumRegion region)
  {
    for (Worker worker : allConnections)
    {
      if (worker.getUser().getRegion() == region)
      {
        return worker;
      }
    }
    return null;
  }

  public Iterable<User> getUserList ()
  {
    return userList;
  }

  public boolean addUser (User u)
  {
    if (getActiveCount() == 7 )
    {
      return false;
    }
    // users.put(client.getName(), u);
    EnumRegion _region = u.getRegion();

    if (_region != null)
    {
      int loc = availableRegions.lastIndexOf(_region);
      if (loc == -1)
      {
        return false;
      }
      availableRegions.remove(loc);
    }
    else
    {
      u.setRegion(availableRegions.remove(0));
    }

    userList.add(u);
    broadcast(new Response(uptime(), u.toJSON(), "user logged in"));

    return true;
  }

  
  public int getActiveCount()
  {
    int i = 0;
    for (User user : userList)
    {
      if (user.isActive())
      {
        i++;
      }
    }
    return i;
  }

  public Iterable<User> getActiveUserList ()
  {
    ArrayList<User> _active = new ArrayList<>();
    for (User user : userList)
    {
      if (user.isActive())
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
   * Tell the server that the player is ready
   */
  public void begin ()
  {  
    WorldData currentWorldData = simulator.init();
    for (Worker workers : allConnections)
    {
      // NOTE: can either send it as soon as we get it or have client request it.
      workers.send(currentWorldData);
    }
  }

  public void vote ()
  {
    
  }
  

  /**
   * Draw new cards for a specific user
   */
  public void draw (Worker worker)
  {
    EnumPolicy[] _hand = simulator.drawCards(worker.getUser().getRegion());
    worker.getUser().setHand(new ArrayList<>(Arrays.asList(_hand)));
  }

  /**
   * Draw cards for all users
   */
  public void draw ()
  {
    for (Worker workers : allConnections)
    {
      // This will not happen when the API's are secured
      // Bug check if user is logged in... user is logged in when worker is associated with user
      if (workers.getUser() == null)
      {
        return;
      }
      EnumPolicy[] _hand = simulator.drawCards(workers.getUser().getRegion());
      workers.getUser().setHand(new ArrayList<>(Arrays.asList(_hand)));
      // NOTE: can either send it as soon as we get it or have client request it.
      // System.out.println(JsonAnnotationProcessor.gets(workers.getUser()));
    }
    currentState = State.DRAWING;
  }

  /**
   * Handle a handshake with web client
   * @param x Key recieved from client
   * @return Hashed key that is to be given back to client for auth check.
   */
  private static String handshake (String x)
  {

    MessageDigest digest = null;
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
  
  private boolean websocketConnect(Worker worker) throws IOException
  {
    // Handling websocket
    StringBuilder reading = new StringBuilder();
    String line = "";
    String key = "";
    String socketKey = "";
    while(true)
    {
      line = worker.getReader().read();

      if (line == null || line.equals("client")|| line.equals("\r\n") || line.isEmpty())
      {
        if (socketKey.isEmpty())
        {
          return false;
        }
        else
        {
          // System.out.println(reading);
          worker.send("HTTP/1.1 101 Switching Protocols\r\n" +
                      "Upgrade: websocket\r\n" +
                      "Connection: Upgrade\r\n" +
                      "Sec-WebSocket-Accept: " + socketKey + "\r\n" + "\r\n");
        
          return true;
        }
        
      }

      reading.append(line);
      if (line.contains("Sec-WebSocket-Key:"))
      {
        key = line.replace("Sec-WebSocket-Key: ", "");
        socketKey = Server.handshake(key);
      }
    }
  }

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
          allConnections.get(i).getUser().setActive(false);
        }
        // the worker is not running. remove it.
        allConnections.remove(i);
        con++;
      }
    }
    // check if any removed. Show removed count
    if (con > 0)
    {
      System.out.println("Removed " + con + " connection workers.");
    }
  }

  public void broadcast (Response response)
  {
    for (Worker worker : allConnections)
    {
      worker.send(response);
    }
  }
}
