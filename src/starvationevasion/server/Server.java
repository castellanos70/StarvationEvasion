package starvationevasion.server;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.messages.*;
import starvationevasion.common.Tuple;
import starvationevasion.sim.Simulator;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Entry point for Server
 */
public class Server
{
  private final Object stateSynchronizationObject = new Object();
  private volatile ServerState currentState = ServerState.LOGIN;
  private ServerSocket serverSocket;
  private final List<ServerWorker> connectedClients = new ArrayList<>();
  private ConcurrentLinkedQueue<Tuple<Serializable, ServerWorker>> messageQueue = new ConcurrentLinkedQueue<>();
  private PasswordFile passwordFile;
  private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
  private ScheduledFuture<?> gameStartFuture;
  private Simulator simulator;

  public Server(String loginFilePath)
  {
    try
    {
      passwordFile = PasswordFile.loadFromFile(loginFilePath);
      serverSocket = new ServerSocket(ServerConstants.DEFAULT_PORT);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      System.exit(1);
    }
  }

  //Usage: java starvationevasion.server.Server PasswordFilePath
  public static void main(String[] args)
  {
    new Server(args[0]).start();
  }

  private void start()
  {
    new Thread(this::processMessages).start();
    waitForConnections();
  }

  private void waitForConnections()
  {
    System.out.println("Waiting for clients to connect...");
    while (true)
    {
      //System.out.println("ServerMaster("+host+"): waiting for Connection on port: "+port);
      try
      {
        Socket client = serverSocket.accept();
        ServerWorker worker = new ServerWorker(this, client);
        worker.start();
        System.out.println("Client connected: " + client.getInetAddress());
        connectedClients.add(worker);
        String nonce = Hello.generateRandomLoginNonce();
        worker.setLoginNonce(nonce);
        worker.send(new Hello(nonce, ServerConstants.SERVER_VERSION));
      }
      catch (IOException e)
      {
        System.err.println("Server error: Failed to connect to client.");
        e.printStackTrace();
      }
    }
  }

  public ServerState getCurrentState()
  {
    synchronized (stateSynchronizationObject)
    {
      return currentState;
    }
  }

  private void setServerState(ServerState newState)
  {
    synchronized (stateSynchronizationObject)
    {
      currentState = newState;
    }
  }

  public void broadcast(Serializable message)
  {
    connectedClients.forEach(c -> c.send(message));
  }

  public void acceptMessage(Serializable message, ServerWorker client)
  {
    messageQueue.add(new Tuple<>(message, client));
  }

  public void processMessages()
  {
    while (!getCurrentState().equals(ServerState.END))
    {
      Tuple<Serializable, ServerWorker> messageClientPair = messageQueue.poll();
      if (messageClientPair == null)
      {
        try
        {
          Thread.sleep(10);
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
        continue;
      }
      ServerWorker client = messageClientPair.b;
      Serializable message = messageClientPair.a;

      if (message instanceof Login)
      {
        handleLogin(client, (Login) message);
        continue;
      }
      if (message instanceof RegionChoice)
      {
        handleRegionChoice(client, (RegionChoice) message);
        continue;
      }
    }
  }

  private void handleRegionChoice(ServerWorker client, RegionChoice message)
  {
    if ((getCurrentState() != ServerState.LOGIN && getCurrentState() != ServerState.BEGINNING) ||
        passwordFile.regionMap.get(client.getUserName()) != null)
    {
      client.send(Response.INAPPROPRIATE);
      return;
    }
    client.send(Response.OK);
    final EnumRegion regionChoice = message.region;
    if (!connectedClients.stream().map(ServerWorker::getRegion).anyMatch(Predicate.isEqual(regionChoice)))
    {
      client.setRegion(regionChoice);
    }
    final AvailableRegions availableRegions = getAvailableRegions();
    broadcast(availableRegions);
    if (getCurrentState() == ServerState.LOGIN && availableRegions.availableRegions.size() == 0)
    {
      beginToStartGame();
    }
    else if (getCurrentState() == ServerState.BEGINNING && availableRegions.availableRegions.size() != 0)
    {
      broadcastCancelGameBeginning();
    }
  }

  private void broadcastCancelGameBeginning()
  {
    if (getCurrentState() == ServerState.DRAFTING) return; //already started, too late, oh well
    if (getCurrentState() != ServerState.BEGINNING) throw new IllegalStateException();
    if (!gameStartFuture.cancel(false)) return; //already started, too late
    setServerState(ServerState.LOGIN);
    broadcast(new ReadyToBegin(false, 0, 0));
  }

  private void startGame()
  {
    setServerState(ServerState.DRAFTING);
    broadcast(new BeginGame(getTakenRegions()));
    simulator = new Simulator(Constant.FIRST_YEAR);
  }

  private void handleLogin(ServerWorker client, Login message)
  {
    if (client.getRegion() != null)
    {
      client.send(Response.INAPPROPRIATE);
      return;
    }

    client.send(Response.OK);
    if (!passwordFile.credentialMap.keySet().contains(message.username))
    {
      client.send(new LoginResponse(LoginResponse.ResponseType.ACCESS_DENIED, null));
      return;
    }
    if (connectedClients.stream().map(ServerWorker::getRegion).anyMatch(
        s -> s != null && s.equals(passwordFile.regionMap.get(message.username))))
    {
      client.send(new LoginResponse(LoginResponse.ResponseType.DUPLICATE, null));
      return;
    }

    if (Login.generateHashedPassword(client.getLoginNonce(),
        passwordFile.credentialMap.get(message.username)).equals(message.hashedPassword))
    {
      client.setRegion(passwordFile.regionMap.get(message.username));
      client.setUserName(message.username);
      client.send(new LoginResponse(client.getRegion() != null ?
          LoginResponse.ResponseType.ASSIGNED_REGION : LoginResponse.ResponseType.CHOOSE_REGION, client.getRegion()));
      final AvailableRegions availableRegions = getAvailableRegions();
      broadcast(availableRegions);
      if (getCurrentState() == ServerState.LOGIN && availableRegions.availableRegions.size() == 0)
      {
        beginToStartGame();
      }
    }
    else
    {
      client.send(new LoginResponse(LoginResponse.ResponseType.ACCESS_DENIED, null));
    }
  }

  private void beginToStartGame()
  {
    if (getCurrentState() != ServerState.LOGIN) throw new IllegalStateException();
    final Instant now = Instant.now();
    broadcast(new ReadyToBegin(true,
        now.getEpochSecond(), now.plusMillis(ServerConstants.GAME_START_WAIT_TIME).getEpochSecond()));
    setServerState(ServerState.BEGINNING);
    gameStartFuture = scheduledExecutorService.schedule(
        this::startGame, ServerConstants.GAME_START_WAIT_TIME, TimeUnit.MILLISECONDS);
  }

  private AvailableRegions getAvailableRegions()
  {
    Map<EnumRegion, String> takenRegions = getTakenRegions();
    Set<EnumRegion> availableRegions = Arrays.stream(EnumRegion.US_REGIONS)
        .filter(r -> !takenRegions.keySet().contains(r)).collect(Collectors.toSet());
    return new AvailableRegions(takenRegions, availableRegions);
  }

  private Map<EnumRegion, String> getTakenRegions()
  {
    return connectedClients.stream().filter(c -> c.getRegion() != null)
          .collect(Collectors.toMap(ServerWorker::getRegion, ServerWorker::getUserName));
  }

  public void disconnectClient(ServerWorker client, String disconnectMessage)
  {
    connectedClients.remove(client);
    client.closeSocket();
    if (disconnectMessage != null)
    {
      client.send(new Goodbye(disconnectMessage));
    }
    if (getCurrentState() == ServerState.LOGIN || getCurrentState() == ServerState.BEGINNING)
    {
      broadcast(getAvailableRegions());
    }
    if (getCurrentState() == ServerState.BEGINNING) broadcastCancelGameBeginning();
  }
}
