package starvationevasion.ai;


import starvationevasion.ai.commands.Command;
import starvationevasion.ai.commands.Draft;
import starvationevasion.ai.commands.GameState;
import starvationevasion.ai.commands.Login;
import starvationevasion.ai.commands.Uptime;
import starvationevasion.ai.commands.Vote;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.RegionData;
import starvationevasion.common.SpecialEventData;
import starvationevasion.common.Util;
import starvationevasion.common.VoteData;
import starvationevasion.common.WorldData;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.common.gamecards.GameCard;
import starvationevasion.communication.Communication;
import starvationevasion.communication.ConcurrentCommModule;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.State;
import starvationevasion.server.model.Type;
import starvationevasion.server.model.User;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is just a test/proof-of-concept for the CommModule implementation of the
 * Communication interface.
 */
public class AI
{
  private final Communication COMM;
  private User u;
  private ArrayList<User> users = new ArrayList<>();
  private int numUsers;
  private State state = null;
  private ArrayList<WorldData> worldData;
  private List<GameCard> ballot;
  private List<GameCard> supportCards = new ArrayList<>();
  private Stack<Command> commands = new Stack<>();
  private volatile boolean isRunning = true;
  private volatile boolean aggregate=false;
  private VoteData ballotResults;
  private HashMap<EnumRegion,Integer> playerPolicyDrafts = new HashMap<>();
  private Random rand = new Random();
  //False if maps used in drafting phase not created yet.
  private AtomicBoolean mapsCreated=new AtomicBoolean(false);
  
  // time of server start
  private double startNanoSec = 0;

  public int numTurns = 0;
  public int worldDataSize = 0;

  /*
   * Factors used as map keys to keep track of and store events that are
   * happening in the world and in the region that this AI represents.
   */
  public enum WorldFactors
  {
    SEALEVEL, 
    REVENUEBALANCE, 
    POPULATION, 
    UNDERNOURISHED, 
    HDI, 
    FOODPRODUCED, 
    FOODINCOME, 
    FOODIMPORTED, 
    FOODEXPORTED, 
    ETHANOLTAXCREDIT, 
    FOODPRICE

  }
  /*
   * Represents card variables. As of now, some cards need a percentage or a dollar
   * amount assigned to them when they are drafted.
   */
  public enum CardVariableTypes
  {
    PERCENTAGE,
    MONEY
  }

  // This map is used to store information about the world and region that will
  // be used in selecting cards to play on each turn.
  public Map<WorldFactors, ArrayList<Object[]>> factorMap = new EnumMap<WorldFactors, ArrayList<Object[]>>(
      WorldFactors.class);
  
  //This map is used to store the various game card policies and their associated region, if they only affect one
  //region.
  public Map<EnumPolicy, EnumRegion> policyAndRegionMap = new EnumMap<EnumPolicy, EnumRegion>(EnumPolicy.class);
  //Used to store policies and their associated variables.
  public Map<EnumPolicy,CardVariableTypes> cardVariables=new EnumMap<EnumPolicy,CardVariableTypes>(EnumPolicy.class);
  
  //Cards that benefit many regions, not just a specific one.
  public ArrayList<EnumPolicy> cardsOfGeneralBenefit=new ArrayList<>();
  //Cards that benefit the AI's region monetarily.
  public ArrayList<EnumPolicy> moneyCards=new ArrayList<>();
  // The AI has a copy of the list of special events, if any occurred during the
  // last turn.
  public ArrayList<SpecialEventData> eventList = new ArrayList<>();

  // List of pairs of cards played in previous hands.
  public ArrayList<ArrayList<GameCard>> draftedCards = new ArrayList<>();

  // The region that this AI represents.
  String region = "";

  public AI(String host, int port)
  {
    COMM = new ConcurrentCommModule(host, port);
    COMM.connect();
    isRunning = COMM.isConnected();
    createMap();

    // Add the starting commands
    commands.add(new GameState(this));
    commands.add(new Uptime(this));
    commands.add(new Login(this));

    aiLoop();
    COMM.dispose();
  }

  /*
   * Jeffrey McCall 
   * Create the EnumMap that will be used by Draft.java which
   * will aid the AI in making decisions about which card to draft.
   */
  private void createMap()
  {
    for (int i = 0; i < WorldFactors.values().length; i++)
    {
      factorMap.put(WorldFactors.values()[i], new ArrayList<Object[]>());
    }
  }
  /*
   * Jeffrey McCall
   * In this method I'm adding items to policyAndRegionMap, which stores policy cards and
   * their corresponding regions. I'm also adding policies to a couple lists. I'm also filling
   * cardVariables which stores policies and their associated variable types. These maps and lists
   * are all used in the drafting phase by the AI.
   */
  private void createMapsAndLists()
  {
    policyAndRegionMap.put(EnumPolicy.Policy_CleanRiverIncentive, getUser().getRegion());
    policyAndRegionMap.put(EnumPolicy.Policy_EfficientIrrigationIncentive, getUser().getRegion());
    policyAndRegionMap.put(EnumPolicy.Policy_EthanolTaxCreditChange, getUser().getRegion());
    policyAndRegionMap.put(EnumPolicy.Policy_FarmInfrastructureSubSaharan, EnumRegion.SUB_SAHARAN);
    policyAndRegionMap.put(EnumPolicy.Policy_FertilizerAidCentralAsia, EnumRegion.CENTRAL_ASIA);
    policyAndRegionMap.put(EnumPolicy.Policy_FertilizerAidMiddleAmerica, EnumRegion.MIDDLE_AMERICA);
    policyAndRegionMap.put(EnumPolicy.Policy_FertilizerAidOceania, EnumRegion.OCEANIA);
    policyAndRegionMap.put(EnumPolicy.Policy_FertilizerAidSouthAsia, EnumRegion.SOUTH_ASIA);
    policyAndRegionMap.put(EnumPolicy.Policy_FertilizerAidSubSaharan, EnumRegion.SUB_SAHARAN);
    policyAndRegionMap.put(EnumPolicy.Policy_FertilizerSubsidy, getUser().getRegion());
    policyAndRegionMap.put(EnumPolicy.Policy_FoodReliefCentralAsia, EnumRegion.CENTRAL_ASIA);
    policyAndRegionMap.put(EnumPolicy.Policy_FoodReliefMiddleAmerica,EnumRegion.MIDDLE_AMERICA);
    policyAndRegionMap.put(EnumPolicy.Policy_FoodReliefOceania, EnumRegion.OCEANIA);
    policyAndRegionMap.put(EnumPolicy.Policy_FoodReliefSouthAsia, EnumRegion.SOUTH_ASIA);
    policyAndRegionMap.put(EnumPolicy.Policy_FoodReliefSubSaharan, EnumRegion.SUB_SAHARAN);
    policyAndRegionMap.put(EnumPolicy.Policy_DivertFunds, getUser().getRegion());
    policyAndRegionMap.put(EnumPolicy.Policy_Fundraiser, getUser().getRegion());
    policyAndRegionMap.put(EnumPolicy.Policy_Loan, getUser().getRegion());
    cardsOfGeneralBenefit.add(EnumPolicy.Policy_InternationalFoodRelief);
    cardsOfGeneralBenefit.add(EnumPolicy.Policy_MyPlatePromotionCampaign);
    cardsOfGeneralBenefit.add(EnumPolicy.Policy_ResearchInsectResistanceGrain);
    moneyCards.add(EnumPolicy.valueOf("Policy_Loan"));
    moneyCards.add(EnumPolicy.valueOf("Policy_DivertFunds"));
    moneyCards.add(EnumPolicy.valueOf("Policy_Fundraiser"));
    cardVariables.put(EnumPolicy.Policy_CleanRiverIncentive, CardVariableTypes.PERCENTAGE);
    cardVariables.put(EnumPolicy.Policy_EfficientIrrigationIncentive, CardVariableTypes.PERCENTAGE);
    cardVariables.put(EnumPolicy.Policy_EthanolTaxCreditChange, CardVariableTypes.PERCENTAGE);
    cardVariables.put(EnumPolicy.Policy_FarmInfrastructureSubSaharan, CardVariableTypes.MONEY);
    cardVariables.put(EnumPolicy.Policy_FertilizerAidCentralAsia, CardVariableTypes.MONEY);
    cardVariables.put(EnumPolicy.Policy_FertilizerAidMiddleAmerica, CardVariableTypes.MONEY);
    cardVariables.put(EnumPolicy.Policy_FertilizerAidOceania, CardVariableTypes.MONEY);
    cardVariables.put(EnumPolicy.Policy_FertilizerAidSouthAsia, CardVariableTypes.MONEY);
    cardVariables.put(EnumPolicy.Policy_FertilizerAidSubSaharan,CardVariableTypes.MONEY);
    cardVariables.put(EnumPolicy.Policy_InternationalFoodRelief,CardVariableTypes.MONEY);
    cardVariables.put(EnumPolicy.Policy_MyPlatePromotionCampaign, CardVariableTypes.MONEY);
    cardVariables.put(EnumPolicy.Policy_ResearchInsectResistanceGrain,CardVariableTypes.MONEY);
  }

  /*
   * Jeffrey McCall 
   * Collect all of the important data about the world and region
   * that has been updated since the last turn. Put this data in the factorMap
   * which is used in decision making by the AI regarding which cards it wants
   * to draft.
   */
  private void aggregateData()
  {
    worldDataSize += 2;
    region = u.getRegion().name();
    for (int i = worldData.size() - 2; i < worldData.size(); i++)
    {
      Double[] seaLevel =
      { worldData.get(i).seaLevel };
      factorMap.get(WorldFactors.SEALEVEL).add(seaLevel);
      eventList.clear();
      if (worldData.get(i).eventList.size() > 0)
      {
        for (SpecialEventData event : worldData.get(numTurns).eventList)
        {
          eventList.add(event);
        }
      }
      RegionData thisRegion = null;
      for (RegionData data : worldData.get(i).regionData)
      {
        if (data.region.name().equals(u.getRegion().name()))
        {
          thisRegion = data;
        }
      }
      Integer[] revenueBalance =
      { thisRegion.revenueBalance };
      factorMap.get(WorldFactors.REVENUEBALANCE).add(revenueBalance);
      Integer[] population =
      { thisRegion.population };
      factorMap.get(WorldFactors.POPULATION).add(population);
      Double[] undernourished =
      { thisRegion.undernourished };
      factorMap.get(WorldFactors.UNDERNOURISHED).add(undernourished);
      Double[] hdi =
      { thisRegion.humanDevelopmentIndex };
      factorMap.get(WorldFactors.HDI).add(hdi);
      Long[] foodProduced = new Long[EnumFood.SIZE];
      for (int h = 0; h < EnumFood.SIZE; h++)
      {
        foodProduced[h] = thisRegion.foodProduced[h];
      }
      // Food produced over the last year in metric tons.
      factorMap.get(WorldFactors.FOODPRODUCED).add(foodProduced);
      Integer[] foodIncome = new Integer[EnumFood.SIZE];
      for (int h = 0; h < EnumFood.SIZE; h++)
      {
        foodIncome[h] = thisRegion.foodIncome[h];
      }
      factorMap.get(WorldFactors.FOODINCOME).add(foodIncome);
      Long[] foodImported = new Long[EnumFood.SIZE];
      for (int h = 0; h < EnumFood.SIZE; h++)
      {
        foodImported[h] = thisRegion.foodImported[h];
      }
      factorMap.get(WorldFactors.FOODIMPORTED).add(foodImported);
      Long[] foodExported = new Long[EnumFood.SIZE];
      for (int h = 0; h < EnumFood.SIZE; h++)
      {
        foodExported[h] = thisRegion.foodExported[h];
      }
      factorMap.get(WorldFactors.FOODEXPORTED).add(foodExported);
      Integer[] ethanolCredit =
      { thisRegion.ethanolProducerTaxCredit };
      factorMap.get(WorldFactors.ETHANOLTAXCREDIT).add(ethanolCredit);
      Integer[] foodPrice = new Integer[EnumFood.SIZE];
      for (int h = 0; h < EnumFood.SIZE; h++)
      {
        foodPrice[h] = worldData.get(i).foodPrice[h];
      }
      factorMap.get(WorldFactors.FOODPRICE).add(foodPrice);
    }
  }

  private void aiLoop()
  {
    while(isRunning)
    {
      try
      {
        isRunning = COMM.isConnected();

        // Ask the communication module to give us any server response events it has received
        // since the last call
        ArrayList<Response> responses = COMM.pollMessages();
        processServerInput(responses);
        
        // if commands is empty check again
        if (commands.size() == 0) continue;

        // take off the top of the stack
        Command c = commands.peek();
        //If the first drafting phase is about to happen and the maps used in drafting haven't been
        //created, call the method to create them.
        if(!mapsCreated.get() && c.commandString().equals("Draft"))
        {
          createMapsAndLists();
          mapsCreated.set(true);
        }
        boolean runAgain = c.run();

        // if it does not need to run again pop
        if (!runAgain) commands.pop();
        // wait a little
        Thread.sleep(1000);
      }
      catch(InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }

  private void processServerInput(ArrayList<Response> responses)
  {
    for (Response response : responses)
    {
      Type type = response.getType();
      Object data = response.getPayload().getData();

      if (type == Type.AUTH_SUCCESS)
      {
        u = (User)data;
        COMM.sendChat("ALL", u.getUsername()+": Hi, I am " + u.getUsername() + ". I'll be playing using kind of okay AI.", null);
      }
      else if (type == Type.USER)
      {
        u = (User)data;
        COMM.sendChat("ALL", "User updated: " + u.getUsername(), null);
      }
      else if (type == Type.WORLD_DATA_LIST) worldData = (ArrayList<WorldData>)data;
      else if (type == Type.WORLD_DATA) worldData.add((WorldData)data);
      else if (type == Type.USERS_LOGGED_IN_LIST)
      {
        users = (ArrayList<User>)data;
        numUsers = users.size();
        for(User user: users)
        {
          if(!(playerPolicyDrafts.keySet().contains(user)))
          {
            playerPolicyDrafts.put(user.getRegion(),0);
          }
        }
      }  
      else if (type == Type.VOTE_BALLOT) ballot = (List<GameCard>)data;
      else if (type == Type.VOTE_RESULTS)
      {
        ballotResults = (VoteData) data;
        for(GameCard card: ballotResults.getEnacted())
        {
          if(!playerPolicyDrafts.containsKey(card.getOwner()))
          {
            playerPolicyDrafts.put(card.getOwner(),0);
          }
          if(card.votesRequired()>1)
          {
            int tally = playerPolicyDrafts.get(card.getOwner());
            playerPolicyDrafts.put(card.getOwner(),tally++);
          }
        }
      }
      else if (type == Type.CHAT)
      {
        String msg = (String) response.getPayload().get("text");
        System.out.println(msg);
        if(msg.contains("I'm going to draft"))
        {
          GameCard card = (GameCard) response.getPayload().get("card");
          Integer sum = 0;
          for(Integer playerTally:playerPolicyDrafts.values())
          {
            sum+=playerTally;
          }
          Integer average = sum/numUsers;
          String[] words = msg.split(" ");
          String username = words[0].substring(0,words[0].length()-1);
          if(u.getUsername().equals(username)) continue;
          for(User user: users)
          {
            if(playerPolicyDrafts.get(user.getRegion())>=average)
            {
              if(Util.likeliness(0.65f))
              {
                supportCards.add(card); 
                int reply = rand.nextInt(2);
                switch(reply)
                {
                  case 0:
                  COMM.sendChat(username, 
                                u.getUsername()+": I'll support it, "+username+".", 
                                null);
                  break;
                  case 1:
                  COMM.sendChat(username, 
                                u.getUsername()+": I think I can help, "+username+".", 
                                null);
                  break;
                }
              }
            }
          }
        }
        else if(msg.contains("Hi,")) sendGreeting(msg);
      }
      else if (type == Type.GAME_STATE)
      {
        state = (State)data;
        if (state == starvationevasion.server.model.State.VOTING)
        {
          AI.this.commands.add(new Vote(AI.this));
        }
        else if (state == starvationevasion.server.model.State.DRAFTING)
        {
          aggregateData();
          draftedCards.add(new ArrayList<GameCard>());
          Draft newDraft = new Draft(AI.this);
          AI.this.commands.add(newDraft);
          numTurns++;
        }
        else if (state == starvationevasion.server.model.State.DRAWING)
        {
          // AI.this.commands.add(new Draft(AI.this));
          commands.clear();
        }
      }
    }
  }
  /**
   * James Perry
   * AI bots use this method to greet new players
   * @param msg Greeting message sent from player who has 
   *            just entered the game.
   */
  private void sendGreeting(String msg) 
  {
    try
	  {
	    Thread.sleep(1000);
	  }
	  catch(InterruptedException e)
	  {
	    System.out.println("Chat Response was interrupted");
	  }
	  int reply = rand.nextInt(3);
	  
	  String[] words = msg.split(" ");
	  String username = words[0].substring(0,words[0].length()-1);
	  if(u.getUsername().equals(username)) reply = -1;
	  numUsers++;
	  switch(reply)
	  {
	    case -1:
	    break;
	    case 0:
	    COMM.sendChat(username, u.getUsername()+": Hi There!", null);	  
	    break;
	    case 1:
      COMM.sendChat(username, u.getUsername()+": Good Day, "+username+"!", null);
	    break;
      case 2:
      COMM.sendChat(username, u.getUsername()+": Hello, "+username, null);
      break;
	  }
  }
  public Communication getCommModule()
  {
    return COMM;
  }
  public List<GameCard> getSupportCards()
  {
    return supportCards;
  }

  public ArrayList<WorldData> getWorldData()
  {
    return worldData;
  }

  public double getStartNanoSec()
  {
    return COMM.getStartNanoTime();
  }

  public State getState()
  {
    return state;
  }

  public User getUser()
  {
    return u;
  }

  public Stack<Command> getCommands()
  {
    return commands;
  }

  public List<GameCard> getBallot()
  {
    return ballot;
  }

  public ArrayList<User> getUsers()
  {
    return users;
  }

  public static void main(String[] args)
  {

    String host = null;
    int port = 0;

    try
    {
      host = args[0];
      port = Integer.parseInt(args[1]);
      System.out.println("host: " + host + "\tport: " + port);
      if (port < 1)
        throw new Exception();
    } catch (Exception e)
    {
      System.exit(0);
    }

    System.out.println("---- CREATING NEW AI ----");
    new AI(host, port);
    System.out.println("---- AI COMPLETE ----");
  }
}
