package starvationevasion.ai.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import starvationevasion.ai.AI;
import starvationevasion.ai.AI.CardVariableTypes;
import starvationevasion.ai.AI.WorldFactors;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.RegionData;
import starvationevasion.common.Util;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.common.gamecards.GameCard;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.server.model.State;

public class Draft extends AbstractCommand
{
  private boolean draftedCard = false;
  private boolean discarded = false;
  private GameCard cardDrafted1;
  private GameCard cardDrafted2;
  private int tries = 2;
  private int numTurns = 0;
  // The region that the AI represents.
  String region = "";
  Long foodProduced = new Long(0);
  Integer foodIncome = new Integer(0);
  Long foodImported = new Long(0);
  Long foodExported = new Long(0);
  Long lastFoodProduced = new Long(0);
  Integer lastFoodIncome = new Integer(0);
  Long lastFoodImported = new Long(0);
  Long lastFoodExported = new Long(0);
  Random rand=new Random();
  int numCardsDrafted=0;
  boolean moreThanOne = false;
  boolean pickThisRegion = false;
  double pickThisRegionChance = .75;
  int z = 0;
  GameCard card = null;
  int probModifier = 0;
  int draftIndex = 0;
  private Map<String,Integer> probabilityMap=new LinkedHashMap<>();
  private List<EnumRegion> regionList=new LinkedList<>();
  private List<EnumFood> foodList=new LinkedList<>();
  ArrayList<EnumPolicy> votesRequired=new ArrayList<>();
  ArrayList<EnumPolicy> noVotesRequired=new ArrayList<>();
  int totalIncrease=0;
  int totalDecrease=0;
  int amtToAdjust=0;
  boolean draftAgain=false;
  int cardsNeedingSupport=0;
  ArrayList<EnumPolicy> policiesInHand = new ArrayList<>();
  boolean cardsForThisRegion=false;
  boolean cardsDiscarded=false;
  boolean foodsForThisRegion=false;
  EnumFood foodNeedingAttention=null;
  int count=0;
  
  //If set to false, then that combination of policy, region and food should not be picked this turn.
  boolean[][][] policyCardInfo=new boolean[EnumPolicy.values().length][EnumRegion.values().length][EnumFood.values().length];
  
  /*
   * Jeffrey McCall
   * Arrays used to partition the elements of the sample space used in the probability distribution that the AI uses to pick
   * cards, regions and policies. The second dimension of each array is a lower and upper bound between 2 numbers. The larger
   * the difference between these 2 numbers, the higher the chance that that policy, region or food will get selected to be drafted
   * that turn.
   */
  int[][] policyBounds=new int[7][2];
  int[][] regionBounds=new int[EnumRegion.values().length][2];
  int[][] foodBounds=new int[EnumFood.values().length][2];
  
  int totalPolicyVal=0;
  int totalRegionVal=0;
  int totalFoodVal=0;
  int p=0;
  boolean secondCardToDraft=false;
  int indexOfLastCard=0;
  int actionsRemaining=2;
  RegionData thisRegion=null;
  boolean cardOfGeneralBenefit=false;
  int revenueDiff=0;
  double avgUndernourished=0;
  boolean tryForMoneyCards=false;
  int revenueBalance=0;
  
  /**
   * Creates new AI object. Fills policyCardInfo array with booleans.
   * @param client
   *        The AI object that is creating this Draft object.
   */
  public Draft(AI client)
  {
    super(client);
    this.numTurns = client.numTurns;
    for(int i=0;i<EnumPolicy.values().length;i++)
    {
      for(int h=0;h<EnumRegion.values().length;h++)
      {
        for(int j=0;j<EnumFood.values().length;j++)
        {
          policyCardInfo[i][h][j]=true;
        }
      }
    }  
  }
  /**
   *Get the String that represents which command this is.
   */
  @Override
  public String commandString()
  {
    return "Draft";
  }
  /**
   * Called to draft cards each turn for the AI.
   * @return False if done drafting cards, true otherwise.
   */
  @Override
  public boolean run()
  {
    if (!getClient().getState().equals(State.DRAFTING) && tries > 0)
    {
      tries--;
      if (tries <= 0)
      {
        return false;
      }
    }
    if (getClient().getState().equals(State.DRAFTING))
    {
      synchronized(getClient())
      {
        if (!draftedCard)
        {
          if (setDraftedCards())
          {
            getClient().getCommModule().send(Endpoint.DONE, new Payload(), null);
            return true;
          } 
          else
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * James Perry 
   * Returns a method to be sent to other players requesting support
   * for a policy card. This method is called by draftCards() after the card has
   * been set up.
   * 
   * @param card
   *          Policy Card needing support
   * @param food
   *          the Card's target food, if any
   * @param region
   *          the Card's target region, if any
   * @return a request String to be sent in a chat message
   */
  private String requestSupportMessage(GameCard card, EnumFood food,
      EnumRegion region)
  {
    if (food == null && region != null)
    {
      return "I'm going to draft a card of type: " + card.getType() + " for "
          + card.getTargetRegion() + " . Can anyone support it?";
    } else if (food != null && region != null)
    {
      return "I'm going to draft a card of type " + card.getType() + " with "
          + card.getTargetFood() + " for " + card.getTargetRegion()
          + " . Can anyone support it?";
    } else
      return "I'm going to draft a card of type " + card.getType()
          + " . Can anyone support it?";
  }
  /**
   * Random chance that a card will get discarded. can only be called once
   * in a turn.
   */
  private void randomlyDiscard()
  {
    EnumPolicy discard = null;

    for (EnumPolicy policy : getClient().getUser().getHand())
    {
      GameCard card = GameCard.create(getClient().getUser().getRegion(),
          policy);
      // dont remove the card we just drafted!!!
      if (cardDrafted1 != null && policy != cardDrafted1.getCardType()&&
          cardDrafted2 != null && policy != cardDrafted2.getCardType()
          && Util.rand.nextBoolean())
      {
        discard = policy;
        break;
      }
        
    }
    int idx = getClient().getUser().getHand().indexOf(discard);
    if (idx >= 0)
    {

      getClient().getCommModule().send(Endpoint.DELETE_CARD, discard, null);
      System.out.println("Card discarded");
    }
    discarded = true;
  }
  /*
   * Jeffrey McCall
   * Fill the map of probability values with Strings that represent policies,
   * regions and foods and Integers that represent the probability that they
   * will get picked. These are random at first, but will be modified in the course
   * of the code in this class being called.
   */
  private void fillProbabilityMap()
  {
    for(EnumFood food:EnumFood.values())
    {
      probabilityMap.put(food.name(),rand.nextInt(10)+1);
      foodList.add(food);
    }
    for(EnumPolicy policy:getClient().getUser().getHand())
    {
      probabilityMap.put(policy.name(),rand.nextInt(10)+1);
    }
    for(EnumRegion region:EnumRegion.values())
    {
      probabilityMap.put(region.name(),rand.nextInt(10)+1);
      regionList.add(region);
    }
  }
  /*
   * Jeffrey McCall
   * For every probability value mapped to a policy, region or food in the 
   * probability map, add these values to the arrays that hold all of the
   * lower and upper bounds of numbers that represent the percentage chance of
   * a policy, region or food getting selected in the card drafting phase in
   * the method draftCards(). This method is called after all operations have been
   * performed on the probability map to create an appropriate probability distribution
   * for selecting cards this turn. 
   */
  private void createProbabilityDistribution()
  {
    probabilityMap.forEach((string,integer)->
    {
      EnumPolicy currPolicy=null;
      EnumRegion currRegion=null;
      EnumFood currFood=null;
      try
      {
        currPolicy=EnumPolicy.valueOf(string);
      }catch(IllegalArgumentException e){};
      if(currPolicy==null)
      {
        try
        {
          currRegion=EnumRegion.valueOf(string);
        }catch(IllegalArgumentException e){};
      }
      if(currRegion==null)
      {
        try
        {
          currFood=EnumFood.valueOf(string);
        }catch(IllegalArgumentException e){};
      }
      if(currPolicy!=null)
      {
        totalPolicyVal+=integer;
        policyBounds[getClient().getUser().getHand().indexOf(currPolicy)][0]=totalPolicyVal-integer;
        policyBounds[getClient().getUser().getHand().indexOf(currPolicy)][1]=totalPolicyVal;
      }
      else if(currRegion!=null)
      {
        totalRegionVal+=integer;
        regionBounds[regionList.indexOf(currRegion)][0]=totalRegionVal-integer;
        regionBounds[regionList.indexOf(currRegion)][1]=totalRegionVal;
      }
      else if(currFood!=null)
      {
        totalFoodVal+=integer;
        foodBounds[foodList.indexOf(currFood)][0]=totalFoodVal-integer;
        foodBounds[foodList.indexOf(currFood)][1]=totalFoodVal;
      }
    });
  }
  /**
   * Jeffrey McCall 
   * First, the cards are divided into piles with cards that require no votes,
   * and cards that require votes. Only one card that requires votes can be selected in
   * a turn. On the first turn, one card is selected randomly from the no votes list,
   * and the other selected randomly from the votes list. If the votes list is empty, than
   * 2 are selected from the no votes list. If the no votes list is empty, than only 1 can be 
   * selected for drafting. On the next and all subsequent turns, a checkLastPlay() is called to
   * evaluate how factors have changed in the player's own region after the last turn. The cards
   * drafted last turn, along with their corresponding regions and foods, are then either set to
   * not be drafted this turn if there was a significant decrease of factors, or they are set to
   * be more likely to get drafted again if there was an increase of factors. The method 
   * checkOtherFactors() is then called to check other relevant factors that would affect the AI's
   * choice about which policies to draft. In that method, there is a possibility that the AI will
   * discard up to three cards and re-draw 3 new cards. If this happens, then this method will return
   * false and get called again with a new set of cards. However, the AI will then only be able to
   * draft 1 card. If the boolean tryForMoneyCards is set to true, that means that revenue is in trouble
   * in this AI's region and the method adjustMoneyCards() is called. This makes it more likely that a
   * card that benefits this AI's region monetarily will be called. The probability distribution will then
   * be created now that all of the probabilities have been set and modified in the previous methods that
   * were called. draftCards() is then called to draft cards for this turn. There is also checking done
   * to see if a card has been drafted that requires monetary support, but this region's money is running
   * low. If that is the case, then it is made more likely that the Special Interests card is drafted if
   * it is in the player's hand. The probability distribution is then re-created. The actionsRemaining
   * int is kept track of and represents how many actions the AI has available to it such as drafting
   * a card or discarding and re-drawing up to three cards. 
   * @return True if cards drafted or if no actions remaining for AI. False if not all cards drafted yet
   * or actions remaining has not hit 0. 
   */
  public boolean setDraftedCards()
  {
    if(probabilityMap.size()==0 && foodList.size()==0 && regionList.size()==0)
    {
      fillProbabilityMap();
    }
    if(policiesInHand.size()==0)
    {
      for (int i = 0; i < getClient().getUser().getHand().size(); i++)
      {
        policiesInHand.add(getClient().getUser().getHand().get(i));
      } 
    }
    GameCard card = null;
    System.out.println("Hand size:" + getClient().getUser().getHand().size());
    if(noVotesRequired.size()==0 && votesRequired.size()==0)
    {
      for (int i = 0; i < getClient().getUser().getHand().size(); i++)
      {
        card = GameCard.create(getClient().getUser().getRegion(),
            getClient().getUser().getHand().get(i));
        if(card.votesRequired()==0)
        {
          noVotesRequired.add(getClient().getUser().getHand().get(i));
        }
        else
        {
          votesRequired.add(getClient().getUser().getHand().get(i)); 
        }
      }
      //System.out.println("Hand:"+getClient().getUser().getHand().get(i).name());
    }
    if (getClient().getUser().getHand().size() == 0)
    {
      return false;
    }
    if (numTurns == 0)
    {
      getRevenueBalance();
      if(votesRequired.size()>0 && noVotesRequired.size()>0)
      {
        card = GameCard.create(getClient().getUser().getRegion(),
            votesRequired.get(rand.nextInt(votesRequired.size())));
        cardDrafted1=card;
        EnumFood[] foods=card.getValidTargetFoods();
        EnumRegion[] regions=card.getValidTargetRegions();
        EnumFood food=null;
        if(foods!=null)
        {
          food=foods[rand.nextInt(foods.length)];
        }
        EnumRegion region=null;
        if(regions!=null)
        {
          region=regions[rand.nextInt(regions.length)];
        }
        setupCard(card,food,region);
        getClient().getCommModule().send(Endpoint.DRAFT_CARD, card, null);
        actionsRemaining--;
        System.out.println("Card drafted:"+card.getPolicyName());
        getClient().draftedCards.get(numTurns).add(card);
        card = GameCard.create(getClient().getUser().getRegion(),
            noVotesRequired.get(rand.nextInt(noVotesRequired.size())));
        cardDrafted2=card;
        EnumFood[] foods2=card.getValidTargetFoods();
        EnumRegion[] regions2=card.getValidTargetRegions();
        EnumFood food2=null;
        if(foods2!=null)
        {
          food2=foods2[rand.nextInt(foods2.length)];
        }
        EnumRegion region2=null;
        if(regions2!=null)
        {
          region2=regions2[rand.nextInt(regions2.length)];
        }
        setupCard(card,food2,region2);
        getClient().getCommModule().send(Endpoint.DRAFT_CARD, card, null);
        actionsRemaining--;
        System.out.println("Card drafted:"+card.getPolicyName());
        getClient().draftedCards.get(numTurns).add(card);
      }
      else if(votesRequired.size()==0)
      {
        int randNum1=rand.nextInt(noVotesRequired.size());
        int randNum2=0;
        do
        {
          randNum2=rand.nextInt(noVotesRequired.size());
        }while(randNum1==randNum2);
        card = GameCard.create(getClient().getUser().getRegion(),
            noVotesRequired.get(randNum1));
        cardDrafted1=card;
        EnumFood[] foods=card.getValidTargetFoods();
        EnumRegion[] regions=card.getValidTargetRegions();
        EnumFood food=null;
        if(foods!=null)
        {
          food=foods[rand.nextInt(foods.length)];
        }
        EnumRegion region=null;
        if(regions!=null)
        {
          region=regions[rand.nextInt(regions.length)];
        }
        setupCard(card,food,region);
        getClient().getCommModule().send(Endpoint.DRAFT_CARD, card, null);
        actionsRemaining--;
        System.out.println("Card drafted:"+card.getPolicyName());
        getClient().draftedCards.get(numTurns).add(card);
        card = GameCard.create(getClient().getUser().getRegion(),
            noVotesRequired.get(randNum2));
        cardDrafted2=card;
        EnumFood[] foods2=card.getValidTargetFoods();
        EnumRegion[] regions2=card.getValidTargetRegions();
        EnumFood food2=null;
        if(foods2!=null)
        {
          food2=foods2[rand.nextInt(foods2.length)];
        }
        EnumRegion region2=null;
        if(regions2!=null)
        {
          region2=regions2[rand.nextInt(regions2.length)];
        }
        setupCard(card,food2,region2);
        getClient().getCommModule().send(Endpoint.DRAFT_CARD, card, null);
        actionsRemaining--;
        System.out.println("Card drafted:"+card.getPolicyName());
        getClient().draftedCards.get(numTurns).add(card);
      }
      else if(noVotesRequired.size()==0)
      {
        card = GameCard.create(getClient().getUser().getRegion(),
            votesRequired.get(rand.nextInt(votesRequired.size())));
        cardDrafted1=card;
        EnumFood[] foods=card.getValidTargetFoods();
        EnumRegion[] regions=card.getValidTargetRegions();
        EnumFood food=null;
        if(foods!=null)
        {
          food=foods[rand.nextInt(foods.length)];
        }
        EnumRegion region=null;
        if(regions!=null)
        {
          region=regions[rand.nextInt(regions.length)];
        }
        setupCard(card,food,region);
        getClient().getCommModule().send(Endpoint.DRAFT_CARD, card, null);
        actionsRemaining--;
        System.out.println("Card drafted:"+card.getPolicyName());
        getClient().draftedCards.get(numTurns).add(card);
      }
      if(!discarded)
      {
        randomlyDiscard();
      }
      draftedCard=true;
      tries=2;
      return true;
    } 
    else
    {
      pickThisRegion = false;
      GameCard lastCard1=getClient().draftedCards.get(numTurns - 1).get(0);
      GameCard lastCard2=null;
      if(getClient().draftedCards.get(numTurns-1).size()>1)
      {
        lastCard2 = getClient().draftedCards.get(numTurns - 1).get(1);
      }
      int playAgain = 0;
      if(actionsRemaining==2)
      {
        playAgain = checkLastPlay();
        distributeProbabilities(playAgain, lastCard1);
        if(lastCard2!=null)
        {
          distributeProbabilities(playAgain, lastCard2);
        }
      }
      if(actionsRemaining==2)
      {
        checkOtherFactors();
        //If cards were discarded and re-drawn, then call method again with new hand.
        if(cardsDiscarded)
        {
          if(tryForMoneyCards)
          {
            //System.out.println("Discarded cards, try re-drafting for money cards.");
            adjustMoneyCards();
            tryForMoneyCards=false;
          }
          actionsRemaining--;
          policiesInHand.clear();
          votesRequired.clear();
          noVotesRequired.clear();
          probabilityMap.clear();
          foodList.clear();
          regionList.clear();
          return false;
        }
        if(tryForMoneyCards)
        {
          adjustMoneyCards();
          tryForMoneyCards=false;
        }
      }
      createProbabilityDistribution();
      boolean drafted=false;
      if(actionsRemaining>0)
      {
        do
        {
          drafted=draftCards();
        }while(!drafted);
      }
      if(revenueDiff<-30)
      {
        if(getClient().getUser().getHand().contains(EnumPolicy.Policy_SpecialInterests)
            && cardDrafted1!=null && getClient().cardVariables.containsKey(cardDrafted1.getCardType())
            && getClient().cardVariables.get(cardDrafted1.getCardType()).equals(CardVariableTypes.MONEY)
            && !cardDrafted1.getCardType().equals(EnumPolicy.Policy_SpecialInterests))
        {
          //System.out.println("Possibly draw special interests card.");
          adjustProbability(probabilityMap.get("Policy_SpecialInterests")*(revenueDiff/10),"Policy_SpecialInterests");
          getClient().getUser().getHand().forEach(policy->
          {
            if(!policy.equals(EnumPolicy.Policy_SpecialInterests))
            {
              adjustProbability(rand.nextInt(5)+1,policy.name());
            }
          });
          totalPolicyVal=0;
          totalRegionVal=0;
          totalFoodVal=0;
          createProbabilityDistribution();
        }
      }
      drafted=false;
      //System.out.println("Drafted a card");
      draftAgain=true;
      //If you've drafted a card that requires support, and the only cards left in
      //your hand are other ones that require support, then you can't draft any more cards.
      if(cardsNeedingSupport>0 && noVotesRequired.size()==0)
      {
        draftedCard=true;
        return true;
      }
      if(actionsRemaining>0)
      {
        do
        {
          drafted=draftCards();
        }while(!drafted);
      }
      draftedCard=true;
      if(!discarded)
      {
        randomlyDiscard();
      }
      tries=2;
      return true;
    }
  }
  //On the first turn, get the starting revenue balance for this region. In future turns,
  //this information is set in the checkOtherFactors() method.
  private void getRevenueBalance()
  {
    for(RegionData data:getClient().getWorldData().get(1).regionData)
    {
      if(data.region.equals(getClient().getUser().getRegion()))
      {
        revenueBalance=data.revenueBalance;
      }
    }
  }
  /**
   * Jeffrey McCall
   * Check factors such as the level of people undernourished in other regions, the revenue for
   * this AI's region and foods in this and other regions. Prioritize different regions, foods and policies
   * based on this information. Also, make it more likely for this region to be picked if 
   * pickThisRegion is set to true.
   */
  public void checkOtherFactors()
  {
    z=0;
    double undernourishedPercent=0;
    double lastUndernourishedPercent=0;
    System.out.println("World data size:"+getClient().getWorldData().size());
    //First check other regions for impending starvation events.
    for(RegionData data:getClient().getWorldData().get(1).regionData)
    {
      undernourishedPercent=(data.undernourished/data.population)*100;
      lastUndernourishedPercent=(getClient().getWorldData().get(0).regionData[data.region.ordinal()].undernourished/
          getClient().getWorldData().get(0).regionData[data.region.ordinal()].population)*100;
      //System.out.println(data.region.name()+" undernourished level "+data.undernourished);
      if(undernourishedPercent>40 && 
          undernourishedPercent>=lastUndernourishedPercent)
      {
        //System.out.println(data.region.name()+" is undernourished, I'm more likely to pick a card that helps them.");
        amtToAdjust=probabilityMap.get(data.region.name());
        amtToAdjust*=undernourishedPercent/10;
        adjustProbability(amtToAdjust,data.region.name());
        amtToAdjust=0;
      }
      //If a region is doing well, make it less likely to get picked.
      else if(undernourishedPercent<10 &&
          lastUndernourishedPercent>=undernourishedPercent)
      {
        //System.out.println(data.region.name()+" is doing well, I'm less likely to help them.");
        adjustProbability(rand.nextInt(5)+1,data.region.name());
      }
      if(data.region.name().equals(getClient().getUser().getRegion().name()))
      {
        thisRegion=data;
        revenueBalance=data.revenueBalance;
      }
      avgUndernourished+=undernourishedPercent;
      //If production of a certain food in this region has gone down by more than 30%, take note of this
      //and take actions to fix it. Make it more likely for that food and region to get selected this turn.
      for(p=0;p<data.foodProduced.length;p++)
      {
        long difference=percentChangeLong(getClient().getWorldData().get(0).regionData[data.region.ordinal()].foodProduced[p],
            data.foodProduced[p]);
        if(difference<-30)
        {
          foodNeedingAttention=EnumFood.values()[p];
          //System.out.println(foodNeedingAttention.name()+" is doing poorly in "+data.region.name()+ "\nI'm more likely to pick it.");
          adjustProbability((int) (probabilityMap.get(foodNeedingAttention.name())*(difference/10)),foodNeedingAttention.name());
          adjustProbability((int) (probabilityMap.get(data.region.name())*(difference/10)),data.region.name());
        }
        //This food is doing fine, it doesn't need any attention this turn.
        else if(difference>0)
        {
          getClient().getUser().getHand().forEach(policy->
          {
            policyCardInfo[policy.ordinal()][data.region.ordinal()][EnumFood.values()[p].ordinal()]=false;
          });
        }
      }
    }
    avgUndernourished/=getClient().getWorldData().get(1).regionData.length;
    //If the average percent of undernourished people around the world is over 30%, then make it
    //more likely to pick a card that has general benefits across the whole world.
    if(avgUndernourished>=30)
    {
      //System.out.println("Average number of people undernourished is greater than 30%.");
      //System.out.println("More likely to pick a card that has broad benefits.");
      getClient().cardsOfGeneralBenefit.forEach(policy->
      {
        adjustProbability(probabilityMap.get(policy.name())*((int)avgUndernourished/(10)),policy.name());
      });
    }
    //If the average rate of undernourishment is fairly low, make it less likely to pick a card that has
    //general benefits.
    else if(avgUndernourished<=10)
    {
      //System.out.println("Average number of people undernourished is less than 10%.");
      //System.out.println("Less likely to pick a card that has broad benefits.");
      getClient().cardsOfGeneralBenefit.forEach(policy->
      {
        adjustProbability(rand.nextInt(5)+1,policy.name());
      });
    }
    //If there has been an overall decrease of factors greater than 20%, then it is
    //75% more likely that this region will be picked.
    if(pickThisRegion)
    {
      //System.out.println("Things have been declining in my region, therefore I'm");
      //System.out.println("75% more likely to pick a card that helps my region.");
      getClient().getUser().getHand().forEach(policy->
      {
        card = GameCard.create(getClient().getUser().getRegion(),policy);
        if(card.getValidTargetRegions()!=null)
        {
          if(Arrays.asList(card.getValidTargetRegions()).contains(getClient().getUser().getRegion()))
          {
            cardsForThisRegion=true;
          }
        }
        else if(card.getValidTargetRegions()==null && getClient().policyAndRegionMap.containsKey(policy)
            && getClient().policyAndRegionMap.get(policy).equals(getClient().getUser().getRegion()))
        {
          cardsForThisRegion=true;
        }
      });
      //If no cards in hand that help AI's region, discard up to three cards and re-draw.
      if(!cardsForThisRegion && !cardsDiscarded)
      {
        discardAndRedraw();
      }
      amtToAdjust=0;
      probabilityMap.forEach((key,val)->
      {
        try
        {
          if(EnumRegion.valueOf(key)!=null)
          {
            amtToAdjust+=probabilityMap.get(key);
          }
        }
        catch(IllegalArgumentException e){}
      });
      amtToAdjust*=.75;
      adjustProbability(amtToAdjust,getClient().getUser().getRegion().name());
      getClient().policyAndRegionMap.forEach((policy,region)->
      {
        if(region.equals(getClient().getUser().getRegion()))
        {
          adjustProbability(amtToAdjust,policy.name());
        }
        else
        {
          adjustProbability(rand.nextInt(5)+1,policy.name());
        }
      });
    }
    //Check revenue in AI's region. 
    revenueDiff=percentChangeInt(thisRegion.revenueBalance, 
        getClient().getWorldData().get(0).regionData[thisRegion.region.ordinal()].revenueBalance);
    //If revenue severely down, increase likelihood of playing a card that will get the region more 
    //money.
    if(revenueDiff<-50)
    {
      if(!(getClient().getUser().getHand().contains(getClient().moneyCards.get(0)) ||
          getClient().getUser().getHand().contains(getClient().moneyCards.get(1))||
          getClient().getUser().getHand().contains(getClient().moneyCards.get(2))))
      {
        if(!cardsDiscarded)
        {
          discardAndRedraw();
          tryForMoneyCards=true;
          return;
        }
        tryForMoneyCards=true;
      }
    }
    //If the region doesn't need the money, don't select getting a loan.
    else if(revenueDiff>0)
    {
      if(getClient().getUser().getHand().contains(EnumPolicy.valueOf("Policy_Loan")))
      {
        policyCardInfo[EnumPolicy.valueOf("Policy_Loan").ordinal()][getClient().getUser().getRegion().ordinal()][0]=false;
      }
    }
  }
  /*
   * Jeffrey McCall
   * Make it more likely to select a card that benefits the AI's region monetarily.
   * This method is called if the AI region's revenue has gone down severely.
   */
  private void adjustMoneyCards()
  {
    if(getClient().getUser().getHand().contains(EnumPolicy.valueOf("Policy_Loan")))
    {
      adjustProbability(probabilityMap.get("Policy_Loan")*2,"Policy_Loan");
    }
    if(getClient().getUser().getHand().contains(EnumPolicy.valueOf("Policy_DiverttheFunds")))
    {
      adjustProbability(probabilityMap.get("Policy_DiverttheFunds")*2,"Policy_DiverttheFunds");
    }
    if(getClient().getUser().getHand().contains(EnumPolicy.valueOf("Policy_Fundraiser")))
    {
      adjustProbability(probabilityMap.get("Policy_Fundraiser")*2,"Policy_Fundraiser");
    }
  }
  /*
   * Jeffrey McCall
   * Called to discard up to three cards from the AI's hand. Three new
   * cards are automatically re-drawn and added back to the AI's hand.
   */
  private void discardAndRedraw()
  {
    ArrayList<EnumPolicy> cardsToDiscard=new ArrayList<>();
    int i=0;
    for(EnumPolicy policy:getClient().getUser().getHand())
    {
      if(rand.nextBoolean())
      {
        i++;
        cardsToDiscard.add(policy);
      }
      if(i==3)break;
    }
    Payload discardData=new Payload(cardsToDiscard);
    getClient().getCommModule().send(Endpoint.DELETE_AND_DRAW_CARDS, discardData, null);
    cardsDiscarded=true;
  }
  /*
   * Jeffrey McCall
   * Adjust the probability that an item will be selected. This method
   * is called when a probability for a policy, region or food is being altered.
   */
  private void adjustProbability(int size,String type)
  {
    Integer oldVal=probabilityMap.get(type);
    probabilityMap.replace(type, oldVal, size);
  }
  /**
   * Jeffrey McCall 
   * This method drafts 2 cards. Random numbers are selected and used to select
   * a random policy, region and food from the sample space. These are done in do while
   * loops inside a larger do while loop. It is set up that way since there is condition
   * checking I do for selecting the policy, region and food to make sure of certain things such
   * as not selecting the wrong region for whichever policy card I chose. Overall condition 
   * checking is done to make sure that the policy, region and food have not been set to false
   * in the policyCardInfo array. A check is also done to ensure that only one card that requires
   * votes is drafted each turn. If DivertFunds is drafted, then all the rest of the cards in
   * the AI's hand must be discarded and no more policy cards can be drafted and this method returns
   * true.
   * @return True if card is drafted, false if not.
   */
  public boolean draftCards()
  {
    GameCard card=null;
    EnumRegion currentRegion = null;
    String regionString = "";
    boolean regionFound = false;
    String policyString = "";
    EnumPolicy currentPolicy = null;
    EnumFood currentFood = null;
    int finalIndexChosen=0;
    int secondCardChosen=0;
    boolean draftedSecondCard=false;
    do
    {
      int i=0;
      do
      {
        int policyIndex = rand.nextInt(totalPolicyVal);
        for(i=0;i<7;i++)
        {
          if(policyIndex>=policyBounds[i][0] && policyIndex<policyBounds[i][1])
          {
            currentPolicy=policiesInHand.get(i);
            break;
          }
        }
        card = GameCard.create(getClient().getUser().getRegion(), currentPolicy);
        if(card==null || currentPolicy==null)
        {
          return false;
        }
      }while((secondCardToDraft  &&  i==indexOfLastCard) || !checkCardsWithVotes(card));
      if(!secondCardToDraft)
      {
        finalIndexChosen=i;
      }
      if(secondCardToDraft)
      {
        secondCardChosen=i;
        draftedSecondCard=true;
      }
      int regionIndex=0;
      int foodIndex=0;
      if(card.getValidTargetRegions()!=null || getClient().policyAndRegionMap.containsKey(card.getCardType()))
      {
        do
        {
          regionIndex=rand.nextInt(totalRegionVal);
          for(i=0;i<regionList.size();i++)
          { 
            if(regionIndex>=regionBounds[i][0] && regionIndex<regionBounds[i][1])
            {
              currentRegion=regionList.get(i);
              break;
            }
          }
        }while(card.getValidTargetRegions()!=null &&(!Arrays.asList(card.getValidTargetRegions()).contains(currentRegion))
            || (getClient().policyAndRegionMap.containsKey(card.getCardType())&&
                !getClient().policyAndRegionMap.get(card.getCardType()).equals(currentRegion)));
      }
      if(card.getValidTargetFoods()!=null)
      {
        do
        {
          foodIndex=rand.nextInt(totalFoodVal);
          for(i=0;i<foodList.size();i++)
          { 
            if(foodIndex>=foodBounds[i][0] && foodIndex<foodBounds[i][1])
            {
              currentFood=foodList.get(i);
              break;
            }
          }
        }while(!Arrays.asList(card.getValidTargetFoods()).contains(currentFood));
      }
      //This will be true if this card benefits a number of regions as opposed to only 1 region.
      if(currentRegion==null && currentFood!=null)
      {
        cardOfGeneralBenefit=true;
      }
    } while (!checkCardVals(currentPolicy,currentRegion,currentFood));
    indexOfLastCard=finalIndexChosen;
    secondCardToDraft=true;
    cardOfGeneralBenefit=false;
    if(card.votesRequired()>0)
    {
      cardsNeedingSupport++;
    }
    setupCard(card, currentFood, currentRegion);
    getClient().getCommModule().send(Endpoint.DRAFT_CARD, card, null);
    //If the policy DivertFunds is drafted, then the rest of hand must be discarded and
    //that will end the drafting phase for this turn. 
    if(card.getCardType().equals(EnumPolicy.Policy_DivertFunds))
    {
      System.out.println("Divert funds drafted.");
      for(int i=0;i<getClient().getUser().getHand().size();i++)
      {
        if(i!=indexOfLastCard || !(draftedSecondCard && i==secondCardChosen))
        {
          getClient().getCommModule().send(Endpoint.DELETE_CARD, getClient().getUser().getHand().get(i), null);
        }
      }
      actionsRemaining=0;
      getClient().draftedCards.get(numTurns).add(card);
      return true;
    }
    actionsRemaining--;
    System.out.println("Card drafted:"+card.getPolicyName());
    //System.out.println("Votes required:"+card.votesRequired());
    if (card.votesRequired() != 0)
    {
      String message = "I am drafing " + card.getTitle()
          + ". Will you support it?";
      // getClient().send(new
      // RequestFactory().chat(getClient().getStartNanoSec(),
      // "ALL", message, card));
    }
    if (!draftAgain)
    {
      cardDrafted1 = card;
    }
    if (draftAgain)
    {
      cardDrafted2 = card;
    }
    getClient().draftedCards.get(numTurns).add(card);
    return true;
  }
  /*
   * Jeffrey McCall
   * Ensure that this combination of policy, region and food has not been set to false
   * in the policyCardInfo array. 
   */
  private boolean checkCardVals(EnumPolicy policy,EnumRegion region,EnumFood food)
  {
    if(region!=null && food!=null)
    {
      return policyCardInfo[policy.ordinal()][region.ordinal()][food.ordinal()];
    }
    else if(region==null && food==null)
    {
      return policyCardInfo[policy.ordinal()][0][0];
    }
    else if(region!=null && food==null)
    {
      return policyCardInfo[policy.ordinal()][region.ordinal()][0];
    }
    else if(cardOfGeneralBenefit)
    {
      return true;
    }
    return false;
  }
  /*
   * Ensure that more than 1 card requiring votes doesn't get drafted.
   */
  private boolean checkCardsWithVotes(GameCard card)
  {
    if(cardsNeedingSupport>0 && card.votesRequired()>0)
    {
      return false;
    }
    else if(cardsNeedingSupport==0 && card.votesRequired()>0)
    {
      return true;
    }
    else if(card.votesRequired()==0)
    {
      return true;
    }
    return false;
  }
  /**
   * Jeffrey McCall 
   * If playAgain=-1, then don't play that combination of card, region and food again this turn.
   * If playAgain==1, then make it more likely that that policy will get drafted again.
   * 
   * @param playAgain
   *          An int value which determines how the probability is altered.
   * @param card
   *          The policy card that was drafted last turn that we are checking.
   */
  public void distributeProbabilities(int playAgain, GameCard card)
  {
    if (playAgain == -1)
    {
      //System.out.println("Overall decrease of factors, I'm less likely to draft "+card.getCardType().name()+" again.");
      if(card.getTargetFood()!=null && card.getTargetRegion()!=null)
      {
        //System.out.println("Will also not assign that policy to "+card.getTargetRegion().name()+" this turn.");
        //System.out.println("Will not assign "+card.getTargetFood().toString()+" to that region and policy this turn.");
        policyCardInfo[card.getCardType().ordinal()][card.getTargetRegion().ordinal()][card.getTargetFood().ordinal()]=false;
      }
      else if(card.getTargetRegion()!=null && card.getTargetFood()==null)
      {
        //System.out.println("Will also not assign that policy to "+card.getTargetRegion().name()+" this turn.");
        policyCardInfo[card.getCardType().ordinal()][card.getTargetRegion().ordinal()][0]=false;
      }
      else if(card.getTargetFood()==null && card.getTargetRegion()==null)
      {
        policyCardInfo[card.getCardType().ordinal()][0][0]=false;
      }
    } 
    else if(playAgain==1)
    {
      //System.out.println("Overall improvement in factors, therefore I'm more likely to pick "+card.getCardType().name()+" again.");
      if(getClient().getUser().getHand().contains(card.getCardType()))
      {
        adjustProbability(probabilityMap.get(card.getCardType().name())*2,card.getCardType().name());
      }
    }
  }

  /**
   * Jeffrey McCall 
   * Go through the relevant regional factors to determine how
   * things have worsened or improved since the last turn. If the amount of
   * decrease of factors is greater than the increase, then this method will
   * return -1 and the combination of policy, region and food played last turn will
   * not be drafted again this turn. If the amount of decrease is equal to the increase, than 0 is
   * returned and the likelihood of the card getting played this turn is
   * unaffected. If there was an overall improvement of factors greater than the
   * decrease, then it is more likely that the policy will get drafted again.
   * If food production or overall factors have decreased by more than 20% in the AI's own
   * region, than pickThisRegion is set to true and it is 75% more likely that a card that benefits
   * the AI's own region exclusively will be drafted this turn. 
   * 
   * @return -1, 0 or 1
   */
  private int checkLastPlay()
  {
    int overallPercentDecrease = 0;
    int overallPercentIncrease = 0;
    long firstYearProduced=0;
    long secondYearProduced=0;
    Integer revenueBalance = new Integer(0);
    Double undernourished = new Double(0);
    Double hdi = new Double(0);
    Integer lastRevenueBalance = new Integer(0);
    Double lastUndernourished = new Double(0);
    Double lastHdi = new Double(0);
    for (int h = getClient().worldDataSize
        - 4; h < getClient().worldDataSize; h++)
    {
      if (h == getClient().worldDataSize - 4
          || h == getClient().worldDataSize - 3)
      {
        lastRevenueBalance += (Integer) getClient().factorMap
            .get(WorldFactors.REVENUEBALANCE).get(h)[0];
        lastUndernourished += (Double) getClient().factorMap
            .get(WorldFactors.UNDERNOURISHED).get(h)[0];
        lastHdi += (Double) getClient().factorMap.get(WorldFactors.HDI)
            .get(h)[0];
        Stream.of(getClient().factorMap.get(WorldFactors.FOODPRODUCED).get(h))
            .forEach(val ->
            {
              lastFoodProduced += (Long) val;
            });
        Stream.of(getClient().factorMap.get(WorldFactors.FOODINCOME).get(h))
            .forEach(val ->
            {
              lastFoodIncome += (Integer) val;
            });
        Stream.of(getClient().factorMap.get(WorldFactors.FOODIMPORTED).get(h))
            .forEach(val ->
            {
              lastFoodImported += (Long) val;
            });
        Stream.of(getClient().factorMap.get(WorldFactors.FOODEXPORTED).get(h))
            .forEach(val ->
            {
              lastFoodExported += (Long) val;
            });
      }
      if (h == getClient().worldDataSize - 2
          || h == getClient().worldDataSize - 1)
      {
        revenueBalance += (Integer) getClient().factorMap
            .get(WorldFactors.REVENUEBALANCE).get(h)[0];
        undernourished += (Double) getClient().factorMap
            .get(WorldFactors.UNDERNOURISHED).get(h)[0];
        hdi += (Double) getClient().factorMap.get(WorldFactors.HDI).get(h)[0];
        Stream.of(getClient().factorMap.get(WorldFactors.FOODPRODUCED).get(h))
            .forEach(val ->
            {
              foodProduced += (Long) val;
            });
        Stream.of(getClient().factorMap.get(WorldFactors.FOODINCOME).get(h))
            .forEach(val ->
            {
              foodIncome += (Integer) val;
            });
        Stream.of(getClient().factorMap.get(WorldFactors.FOODIMPORTED).get(h))
            .forEach(val ->
            {
              foodImported += (Long) val;
            });
        Stream.of(getClient().factorMap.get(WorldFactors.FOODEXPORTED).get(h))
            .forEach(val ->
            {
              foodExported += (Long) val;
            });
        if(h == getClient().worldDataSize - 2)
        {
          firstYearProduced=foodProduced;
        }
        if(h==getClient().worldDataSize-1)
        {
          secondYearProduced=foodProduced-firstYearProduced;
        }
        //Check if the amount of food being produced in AI's region is decreasing.
        if(h == getClient().worldDataSize - 1)
        {
          if(percentChangeLong(firstYearProduced,secondYearProduced)<-20)
          {
            //System.out.println("I'm more likely to pick my region this time since food production is down.");
            pickThisRegion=true;
          }
        }
        Object[][] numArray =
        {
            { lastRevenueBalance, revenueBalance },
            { lastUndernourished, undernourished },
            { lastHdi, hdi },
            { lastFoodProduced, foodProduced },
            { lastFoodIncome, foodIncome },
            { lastFoodImported, foodImported },
            { lastFoodExported, foodExported } };
        for (int i = 0; i < 7; i++)
        {
          if (i == 0 || i == 4)
          {
            int percentChange = percentChangeInt((int) numArray[i][0],
                (int) numArray[i][1]);
            if (percentChange < 0)
            {
              overallPercentDecrease += (percentChange * -1);
            } else if (percentChange > 0)
            {
              overallPercentIncrease += percentChange;
            }
          }
          else if (i == 1)
          {
            double percentChange = percentChangeDouble((double) numArray[i][0],
                (double) numArray[i][1]);
            if (percentChange > 0)
            {
              overallPercentDecrease += percentChange;
            } else if (percentChange < 0)
            {
              overallPercentIncrease += (percentChange * -1);
            }
          } 
          else if (i == 2)
          {
            double percentChange = percentChangeDouble((double) numArray[i][0],
                (double) numArray[i][1]);
            if (percentChange < 0)
            {
              overallPercentDecrease += (percentChange * -1);
            } else if (percentChange > 0)
            {
              overallPercentIncrease += percentChange;
            }
          } 
          else if (i == 3 || i > 4)
          {
            long percentChange = percentChangeLong((long) numArray[i][0],
                (long) numArray[i][1]);
            if (percentChange < 0)
            {
              overallPercentDecrease += (percentChange * -1);
            } else if (percentChange > 0)
            {
              overallPercentIncrease += percentChange;
            }
          }
        }
        //System.out.println("Overall percent decrease:"+overallPercentDecrease);
        //System.out.println("Overall percent increase:"+overallPercentIncrease);
        if (overallPercentIncrease > overallPercentDecrease)
        {
          //If there has been an increase of over 20%, make it less likely to pick
          //this region again.
          if((overallPercentIncrease-overallPercentDecrease)>20)
          {
            //System.out.println("Things are going well in my region, I'll more likely help another region out.");
            adjustProbability(rand.nextInt(5)+1,getClient().getUser().getRegion().name());
          }
          totalIncrease=overallPercentIncrease;
          return 1;
        } 
        else if (overallPercentDecrease > overallPercentIncrease)
        {
          //If there has been a decrease of over 20%, make it more likely to pick this region.
          if ((overallPercentDecrease-overallPercentIncrease)>20)
          {
            //System.out.println("I'm more likely to pick my region this time since things are starting to go poorly for it.");
            pickThisRegion = true;
          }
          totalDecrease=overallPercentDecrease;
          return -1;
        }
      }
    }
    return 0;
  }

  /*
   * Jeffrey McCall
   * Returns the percent change between 2 ints.
   */
  private int percentChangeInt(int originalVal, int newVal)
  {
    if (originalVal != 0)
    {
      return ((originalVal - newVal) / originalVal) * 100;
    } else
    {
      return 0;
    }
  }

  /*
   * Jeffrey McCall
   * Get the percent change between 2 doubles.
   */
  private double percentChangeDouble(double originalVal, double newVal)
  {
    if (originalVal != 0)
    {
      return ((originalVal - newVal) / originalVal) * 100;
    } else
    {
      return 0;
    }
  }

  /*
   * Jeffrey McCall
   * The percent change between longs.
   */
  private long percentChangeLong(long originalVal, long newVal)
  {
    if (originalVal != 0)
    {
      return ((originalVal - newVal) / originalVal) * 100;
    } else
    {
      return 0;
    }
  }
  /*
   * Jeffrey McCall
   * Assign region, food and variable to card. If the variable to be assigned
   * is millions of dollars, no value more than 10% of the region's revenue for the
   * last year is allotted to this policy, unless the lowest value is more than 10% of
   * the revenue, in which case that's what assigned to the card. If the variable to be
   * assigned is a percentage tax break, then if the revenue is doing poorly, the lowest
   * tax break is assigned. If it's not doing poorly, then a higher tax break is assigned.
   */
  private void setupCard(GameCard card, EnumFood food, EnumRegion region)
  {
    if (card == null)
    {
      return;
    }

    if (food != null)
    {
      card.setTargetFood(food);
    }
    if (region != null)
    {
      card.setTargetRegion(region);
    }

    ArrayList<Integer> legalValueList = card.getOptionsOfVariable();
    
    if (legalValueList == null)
    {
      card.setX(0);
    }
    else
    {
      if(getClient().cardVariables.containsKey(card.getCardType()))
      {
        if(getClient().cardVariables.get(card.getCardType()).equals(CardVariableTypes.MONEY))
        {
          for(int i=0;i<legalValueList.size();i++)
          {
            //Don't assign the X variable to be more than 10% of your income if possible.
            if(legalValueList.get(i)<=(revenueBalance/10))
            {
              continue;
            }
            if(i!=0 && legalValueList.get(i)>(revenueBalance/10))
            {
              card.setX(legalValueList.get(i-1));
              return;
            }
            if(i==legalValueList.size()-1)
            {
              card.setX(legalValueList.get(i));
              return;
            }
          }
          card.setX(legalValueList.get(0));
        }
        else if(getClient().cardVariables.get(card.getCardType()).equals(CardVariableTypes.PERCENTAGE))
        {
          //If revenue doing well, will be more likely to give farmers a bigger tax break. I revenue not doing
          //well, will give them the bare minimum tax break.
          if(revenueDiff<=0 && numTurns>0)
          {
            card.setX(legalValueList.get(0));
          }
          else if(numTurns>0 && revenueDiff>0)
          {
            card.setX(legalValueList.get(rand.nextInt(legalValueList.size()-1)+1));
          }
          else if(numTurns==0)
          {
            card.setX(legalValueList.get(rand.nextInt(legalValueList.size())));
          }
        }
      }
      else
      {
        card.setX(legalValueList.get(rand.nextInt(legalValueList.size())));
      }
    }
  }

}
