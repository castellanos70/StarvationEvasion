package starvationevasion.server;

import starvationevasion.common.*;
import starvationevasion.common.messages.*;
import starvationevasion.sim.Simulator;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
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
  private Map<EnumRegion, List<EnumPolicy>> playerHands = new HashMap<>();
  private Map<EnumRegion, RegionTurnData> regionTurnData = new HashMap<>();
  private Map<EnumRegion, PolicyCard> regionVoteRequiredCards = new HashMap<>();
  private Map<PolicyCard, List<EnumRegion>> regionsWhoVotedOnCards = new HashMap<>();
  private ArrayList<PolicyCard> enactedPolicyCards = new ArrayList<>(), draftedPolicyCards = new ArrayList<>();

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
      if (message instanceof DraftCard)
      {
        handleDraftMessage(client, (DraftCard) message);
        continue;
      }
      if (message instanceof Discard)
      {
        handleDiscard(client, (Discard) message);
        continue;
      }
      if (message instanceof Vote)
      {
        handleVote(client, (Vote) message);
        continue;
      }
      if (message instanceof ClientChatMessage)
      {
        handleChatMessage(client, (ClientChatMessage) message);
        continue;
      }
      client.send(Response.INAPPROPRIATE);
    }
  }

  private void handleVote(ServerWorker client, Vote message)
  {
    final EnumRegion region = client.getRegion();
    if (getCurrentState() != ServerState.VOTING || region == null)
    {
      client.send(Response.INAPPROPRIATE);
      return;
    }
    client.send(Response.OK);
    List<EnumPolicy> hand = playerHands.get(region);
    EnumPolicy[] handArray = hand.toArray(new EnumPolicy[hand.size()]);
    if (!regionVoteRequiredCards.containsKey(message.cardOwner))
    {
      client.send(new ActionResponse(ActionResponseType.NONEXISTENT_CARD, handArray));
      return;
    }
    final PolicyCard card = regionVoteRequiredCards.get(message.cardOwner);
    if (regionsWhoVotedOnCards.get(card).contains(region))
    {
      client.send(new ActionResponse(ActionResponseType.TOO_MANY_ACTIONS, handArray));
      return;
    }
    if (!card.isEligibleToVote(region))
    {
      client.send(new ActionResponse(ActionResponseType.INVALID, handArray));
      return;
    }

    card.addEnactingRegion(region);
    regionsWhoVotedOnCards.get(card).add(region);
    broadcastVoteStatus();
  }

  private void broadcastVoteStatus()
  {
    Collection<PolicyCard> voteRequiredCards = regionVoteRequiredCards.values();
    broadcast(new VoteStatus(voteRequiredCards.toArray(new PolicyCard[voteRequiredCards.size()])));
  }

  private void handleDiscard(ServerWorker client, Discard message)
  {
    final EnumRegion region = client.getRegion();
    if (getCurrentState() != ServerState.DRAFTING || region == null)
    {
      client.send(Response.INAPPROPRIATE);
      return;
    }
    client.send(Response.OK);
    List<EnumPolicy> hand = playerHands.get(region);
    EnumPolicy[] handArray = hand.toArray(new EnumPolicy[hand.size()]);
    final RegionTurnData regionTurnData = this.regionTurnData.get(region);
    if ((message.isFreeDiscard && regionTurnData.hasUsedFreeDiscard) || regionTurnData.actionsRemaining < 1)
    {
      client.send(new ActionResponse(ActionResponseType.TOO_MANY_ACTIONS, handArray));
      return;
    }
    if (!Arrays.stream(message.discards).allMatch(hand::contains))
    {
      client.send(new ActionResponse(ActionResponseType.NONEXISTENT_CARD, handArray));
      return;
    }
    if (message.isFreeDiscard)
    {
      hand.remove(message.discards[0]);
      simulator.discard(region, message.discards[0]);
      regionTurnData.hasUsedFreeDiscard = true;
    }
    else
    {
      regionTurnData.actionsRemaining--;
      for (EnumPolicy discard : message.discards)
      {
        if (discard == null) continue;
        hand.remove(discard);
      }
      Collections.addAll(hand, simulator.drawCards(region));
    }
    handArray = hand.toArray(new EnumPolicy[hand.size()]);
    client.send(new ActionResponse(ActionResponseType.OK, handArray));
  }

  private void handleDraftMessage(ServerWorker client, DraftCard message)
  {
    final EnumRegion region = client.getRegion();
    if (getCurrentState() != ServerState.DRAFTING || region == null)
    {
      client.send(Response.INAPPROPRIATE);
      return;
    }
    client.send(Response.OK);
    List<EnumPolicy> hand = playerHands.get(region);
    EnumPolicy[] handArray = hand.toArray(new EnumPolicy[hand.size()]);
    final RegionTurnData regionTurnData = this.regionTurnData.get(region);
    if (regionTurnData.actionsRemaining < 1)
    {
      client.send(new ActionResponse(ActionResponseType.TOO_MANY_ACTIONS, handArray));
      return;
    }
    if (!hand.contains(message.policyCard.getCardType()))
    {
      client.send(new ActionResponse(ActionResponseType.NONEXISTENT_CARD, handArray));
      return;
    }
    String validationResponse = message.policyCard.validate();
    if (message.policyCard.getOwner() != client.getRegion())
    {
      validationResponse = "Tried to play a region card with the wrong owner";
    }
    if (validationResponse != null)
    {
      client.send(new ActionResponse(ActionResponseType.INVALID, handArray, validationResponse));
      return;
    }
    if (message.policyCard.votesRequired() > 0 && regionTurnData.hasPlayedVoteCard)
    {
      client.send(new ActionResponse(ActionResponseType.TOO_MANY_VOTING_CARDS, handArray));
      return;
    }
    regionTurnData.actionsRemaining--;
    if (message.policyCard.votesRequired() > 0) regionTurnData.hasPlayedVoteCard = true;
    draftedPolicyCards.add(message.policyCard);
    hand.remove(message.policyCard.getCardType());
    handArray = hand.toArray(new EnumPolicy[hand.size()]);
    client.send(new ActionResponse(ActionResponseType.OK, handArray));
  }

  private void handleChatMessage(ServerWorker client, ClientChatMessage message)
  {
    if (client.getRegion() == null)
    {
      client.send(Response.INAPPROPRIATE); //if you haven't been assigned a region, no reason to be able to send messages
      return;
    }
    client.send(Response.OK);
    Set<EnumRegion> recipientSet = new HashSet<>(Arrays.asList(message.messageRecipients));
    ServerChatMessage serverChatMessage = ServerChatMessage.constructFromClientMessage(message, client.getRegion());
    for (ServerWorker connectedClient : connectedClients)
    {
      if (connectedClient.getRegion() != null &&
          recipientSet.contains(connectedClient.getRegion()))
      {
        connectedClient.send(serverChatMessage);
      }
    }
  }

  private void handleRegionChoice(ServerWorker client, RegionChoice message)
  {
    if ((getCurrentState() != ServerState.LOGIN && getCurrentState() != ServerState.BEGINNING) ||
        passwordFile.regionMap != null)
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
    setServerState(ServerState.DRAWING);
    broadcast(new BeginGame(getTakenRegions()));
    simulator = new Simulator(Constant.FIRST_YEAR);
    for (EnumRegion region : EnumRegion.US_REGIONS)
    {
      playerHands.put(region, Arrays.asList(simulator.drawCards(region)));
    }
    broadcast(PhaseStart.constructPhaseStart(ServerState.DRAWING, -1));
    broadcastSimulatorState(simulator.init());
    enterDraftingPhase();
  }

  private void enterDraftingPhase()
  {
    enactedPolicyCards.clear();
    setServerState(ServerState.DRAFTING);
    broadcast(PhaseStart.constructPhaseStart(ServerState.DRAFTING, ServerConstants.DRAFTING_PHASE_TIME));
    for (EnumRegion region : EnumRegion.US_REGIONS)
    {
      regionTurnData.put(region, new RegionTurnData());
    }
    scheduledExecutorService.schedule(this::enterVotingPhase, ServerConstants.DRAFTING_PHASE_TIME, TimeUnit.MILLISECONDS);
  }

  private void enterVotingPhase()
  {
    setServerState(ServerState.VOTING);
    broadcast(PhaseStart.constructPhaseStart(ServerState.VOTING, ServerConstants.VOTING_PHASE_TIME));
    scheduledExecutorService.schedule(this::enterDrawingPhase, ServerConstants.VOTING_PHASE_TIME, TimeUnit.MILLISECONDS);
    regionsWhoVotedOnCards.clear();
    regionVoteRequiredCards.putAll(draftedPolicyCards.stream()
        .filter(c -> c.votesRequired() > 0)
        .collect(Collectors.toMap(PolicyCard::getOwner, Function.identity())));
    regionVoteRequiredCards.values().forEach(c -> regionsWhoVotedOnCards.put(c, new ArrayList<>()));
  }

  private void enterDrawingPhase()
  {
    setServerState(ServerState.DRAWING);
    broadcast(PhaseStart.constructPhaseStart(ServerState.DRAWING, -1));
    for(PolicyCard p: draftedPolicyCards)
    {
      if (p.votesRequired() > p.getEnactingRegionCount()) simulator.discard(p.getOwner(), p.getCardType());
      else enactedPolicyCards.add(p);
    }
    for (Map.Entry<EnumRegion, List<EnumPolicy>> entry : playerHands.entrySet())
    {
      entry.getValue().addAll(Arrays.asList(simulator.drawCards(entry.getKey())));
    }
    WorldData worldData = simulator.nextTurn(enactedPolicyCards);
    for (ServerWorker client : connectedClients)
    {
      if (client.getRegion() == null) continue;
      List<EnumPolicy> playerHand = playerHands.get(client.getRegion());
      client.send(new GameState(worldData, playerHand.toArray(new EnumPolicy[playerHand.size()])));
    }
    enterDraftingPhase();
  }

  private void broadcastSimulatorState(WorldData worldData)
  {
    for (ServerWorker client : connectedClients)
    {
      if (client.getRegion() == null) continue;
      final List<EnumPolicy> playerHand = playerHands.get(client.getRegion());
      client.send(new GameState(worldData, playerHand.toArray(new EnumPolicy[playerHand.size()])));
    }
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
      if (passwordFile.regionMap != null)
      {
        client.setRegion(passwordFile.regionMap.get(message.username));
      }
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
