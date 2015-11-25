package starvationevasion.server;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.messages.*;
import starvationevasion.common.Tuple;

import javax.swing.plaf.synth.Region;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Entry point for Server
 */
public class Server
{
  private static final int DEFAULT_PORT = 27015;
  public static final String SERVER_VERSION = "M1";
  private final Object stateSynchronizationObject = new Object();
  private volatile ServerState currentState = ServerState.LOGIN;
  private ServerSocket serverSocket;
  private final List<ServerWorker> connectedClients = new ArrayList<>();
  private ConcurrentLinkedQueue<Tuple<Serializable, ServerWorker>> messageQueue = new ConcurrentLinkedQueue<>();
  private PasswordFile passwordFile;

  public Server(String loginFilePath)
  {
    try
    {
      passwordFile = PasswordFile.loadFromFile(loginFilePath);
      serverSocket = new ServerSocket(DEFAULT_PORT);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public static void main(String[] args)
  {
    new Server(args[0]).start();
  }

  private void start()
  {
    new Thread(this::processMessages);
    waitForConnections();
  }

  private void waitForConnections()
  {
    String host = "";
    try
    {
      host = InetAddress.getLocalHost().getHostName();
    }
    catch (UnknownHostException e)
    {
      e.printStackTrace();
    }
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
        worker.send(new Hello(nonce, SERVER_VERSION));
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
    if (client.getRegion() != null || passwordFile.regionMap.get(client.getUserName()) != null)
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
    client.send(getAvailableRegions());
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
        s -> s.equals(passwordFile.regionMap.get(message.username))))
    {
      client.send(new LoginResponse(LoginResponse.ResponseType.DUPLICATE, null));
      return;
    }

    if (Login.generateHashedPassword(client.getLoginNonce(),
        passwordFile.credentialMap.get(message.username)).equals(message.hashedPassword))
    {
      client.setRegion(passwordFile.regionMap.get(message.username));
      client.send(new LoginResponse(client.getRegion() != null ?
          LoginResponse.ResponseType.ASSIGNED_REGION : LoginResponse.ResponseType.CHOOSE_REGION, client.getRegion()));
      if (client.getRegion() == null) //regions are user-chosen
      {
        client.send(getAvailableRegions());
      }
    }
  }

  private AvailableRegions getAvailableRegions()
  {
    Map<EnumRegion, String> takenRegions = connectedClients.stream().filter(c -> c.getRegion() != null)
        .collect(Collectors.toMap(ServerWorker::getRegion, ServerWorker::getUserName));
    Set<EnumRegion> availableRegions =
        Arrays.stream(EnumRegion.US_REGIONS).filter(r -> !takenRegions.keySet().contains(r)).collect(Collectors.toSet());
    return new AvailableRegions(takenRegions, availableRegions);
  }

  public void disconnectClient(ServerWorker client, String disconnectMessage)
  {
    connectedClients.remove(client);
    if (disconnectMessage != null)
    {
      client.send(new Goodbye(disconnectMessage));
    }
  }
}
