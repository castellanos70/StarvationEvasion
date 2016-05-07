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

import java.util.*;
import java.util.stream.Stream;

public class Draft extends AbstractCommand
{
  private boolean draftedCard = false;
  private boolean discarded = false;
  private boolean drawn = false;
  private GameCard cardDrafted1;
  private GameCard cardDrafted2;
  private GameCard[] draftedCards = new GameCard[2];
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
  boolean[][][] policyCardInfo=new boolean[EnumPolicy.values().length][EnumRegion.values().length][EnumFood.values().length];
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

  @Override
  public String commandString()
  {
    return "Draft";
  }
  /**
   * Fill the map of probability values with new ProbabilityLevel objects and
   * corresponding types of regions, foods and policies.
   */
  private void fillProbabilityMap()
  {
    for(EnumFood food:EnumFood.values())
    {
      probabilityMap.put(food.name(),rand.nextInt(20)+1);
      foodList.add(food);
    }
    for(EnumPolicy policy:getClient().getUser().getHand())
    {
      probabilityMap.put(policy.name(),rand.nextInt(20)+1);
    }
    for(EnumRegion region:EnumRegion.values())
    {
      probabilityMap.put(region.name(),rand.nextInt(20)+1);
      regionList.add(region);
    }
  }
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
   * James Perry Returns a method to be sent to other players requesting support
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

  /**
   * Jeffrey McCall If this is the first turn, then randomly choose the card to
   * draft. If this is not the first turn, then evaluate choice of next card to
   * draft according to how things turned out after the last turn. If after the
   * last turn, there was an overall decrease in factors that are relevant to
   * this AI's region, then it is less likely that the AI will again choose that
   * food, region or policy.
   */
  private boolean setDraftedCards()
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
      if(getClient().draftedCards.get(numTurns).size()>1)
      {
        lastCard2 = getClient().draftedCards.get(numTurns - 1).get(1);
      }
      int playAgain = 0;
      if(actionsRemaining==2)
      {
        playAgain = checkLastPlay();
        if (lastCard1.getTargetFood() != null)
        {
          distributeProbabilities(playAgain, lastCard1, "food");
        }
        if (lastCard1.getTargetRegion() != null)
        {
          distributeProbabilities(playAgain, lastCard1, "region");
        }
        distributeProbabilities(playAgain, lastCard1, "policy");
        if(lastCard2!=null)
        {
          if (lastCard2.getTargetFood() != null)
          {
            distributeProbabilities(playAgain, lastCard2, "food");
          }
          if (lastCard2.getTargetRegion() != null)
          {
            distributeProbabilities(playAgain, lastCard2, "region");
          }
          distributeProbabilities(playAgain, lastCard2, "policy");
        }
      }
      if(actionsRemaining==2)
      {
        checkOtherFactors();
        //If cards were discarded and re-drawn, then call method again with new hand.
        if(cardsDiscarded)
        {
          actionsRemaining--;
          policiesInHand.clear();
          votesRequired.clear();
          noVotesRequired.clear();
          probabilityMap.clear();
          foodList.clear();
          regionList.clear();
          return false;
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
  /**
   * Check factors such as the level of people undernourished in other regions, the revenue for
   * this AI's region and foods in this region. Prioritize different regions, foods and policies
   * based on this information. Also, make it more likely for this region to be picked if 
   * pickThisRegion is set to true.
   */
  public void checkOtherFactors()
  {
    z=0;
    //First check other regions for impending starvation events.
    for(RegionData data:getClient().getWorldData().get(1).regionData)
    {
      if(data.undernourished>40 && 
          data.undernourished>=getClient().getWorldData().get(0).regionData[data.region.ordinal()].undernourished)
      {
        amtToAdjust=probabilityMap.get(data.region.name());
        amtToAdjust*=data.undernourished/10;
        adjustProbability(amtToAdjust,data.region.name());
        amtToAdjust=0;
      }
      //If a region is doing well, make it less likely to get picked.
      else if(data.undernourished<10 &&
          data.undernourished<=getClient().getWorldData().get(0).regionData[data.region.ordinal()].undernourished)
      {
        adjustProbability(rand.nextInt(5)+1,data.region.name());
      }
      if(data.region.name().equals(getClient().getUser().getRegion().name()))
      {
        thisRegion=data;
      }
    }
    //If there has been an overall decrease of factors greater than 20%, then it is
    //75% more likely that this region will be picked.
    if(pickThisRegion)
    {
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
      });
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
    }
    //Check relevant factors in AI's own region.
    int revenueDiff=percentChangeInt(thisRegion.revenueBalance, 
        getClient().getWorldData().get(0).regionData[thisRegion.region.ordinal()].revenueBalance);
    //If revenue severely down, increase likelihood of playing a card that will get the region more 
    //money.
    if(revenueDiff<-50)
    {
      ArrayList<EnumPolicy> moneyCards=new ArrayList<>();
      moneyCards.add(EnumPolicy.valueOf("Policy_Loan"));
      moneyCards.add(EnumPolicy.valueOf("Policy_DiverttheFunds"));
      moneyCards.add(EnumPolicy.valueOf("Policy_Fundraiser"));
      if(!(getClient().getUser().getHand().contains(moneyCards.get(0)) ||
          getClient().getUser().getHand().contains(moneyCards.get(1))||
          getClient().getUser().getHand().contains(moneyCards.get(2))))
      {
        if(!cardsDiscarded)
        {
          discardAndRedraw();
        }
      }
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
    //If the region doesn't need the money, don't select getting a loan.
    else if(revenueDiff>0)
    {
      if(getClient().getUser().getHand().contains(EnumPolicy.valueOf("Policy_Loan")))
      {
        policyCardInfo[EnumPolicy.valueOf("Policy_Loan").ordinal()][getClient().getUser().getRegion().ordinal()][0]=false;
      }
    }
    int foodsInTrouble=0;
    //If production of a certain food in this region has gone down by more than 30%, take note of this
    //and take actions to fix it.
    for(p=0;p<thisRegion.foodProduced.length;p++)
    {
      long difference=percentChangeLong(getClient().getWorldData().get(0).regionData[thisRegion.region.ordinal()].foodProduced[p],
          thisRegion.foodProduced[p]);
      if(difference<-30)
      {
        foodsInTrouble++;
        foodNeedingAttention=EnumFood.values()[p];
        adjustProbability((int) (probabilityMap.get(foodNeedingAttention.name())*(difference/10)),foodNeedingAttention.name());
        getClient().getUser().getHand().forEach(policy->
        {
          card = GameCard.create(getClient().getUser().getRegion(),policy);
          if(card.getValidTargetFoods()!=null)
          {
            if(Arrays.asList(card.getValidTargetFoods()).contains(foodNeedingAttention)
                && Arrays.asList(card.getValidTargetRegions()).contains(getClient().getUser().getRegion()) )
            {
              foodsForThisRegion=true;
            }
          }
        });
        if(foodsInTrouble==2 && !foodsForThisRegion && !cardsDiscarded)
        {
          discardAndRedraw();
        }
      }
      //This food is doing fine, it doesn't need any attention this turn.
      else if(difference>10)
      {
        getClient().getUser().getHand().forEach(policy->
        {
          policyCardInfo[policy.ordinal()][getClient().getUser().getRegion().ordinal()][EnumFood.values()[p].ordinal()]=false;
        });
      }
    }
  }
  private void discardAndRedraw()
  {
    System.out.println("Discarding up to 3 cards");
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
  /**
   * Jeffrey McCall
   * Adjust the probability that an item will be selected. Either increase or
   * decrease the probability by a certain amount.
   * @param size
   *        The size to increase or decrease.
   * @param type
   *        The type of the item, like food, region etc.
   */
  private void adjustProbability(int size,String type)
  {
    Integer oldVal=probabilityMap.get(type);
    probabilityMap.replace(type, oldVal, size);
  }
  /**
   * Jeffrey McCall This method drafts 2 cards. The policy, region, and food
   * sample space ArrayLists from AI.java are accessed to pick which policy,
   * region and foods should be used when drafting the 2 cards. If the decrease
   * of factor's in the AI's region are greater than 20% after the last turn,
   * then pickThisRegion is true and the AI is much more likely to draft a card
   * that affects it's own region.
   * 
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
      finalIndexChosen=i;
      int regionIndex=0;
      int foodIndex=0;
      if(card.getValidTargetRegions()!=null)
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
        }while(!Arrays.asList(card.getValidTargetRegions()).contains(currentRegion));
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
    } while (!checkCardVals(currentPolicy,currentRegion,currentFood));
    indexOfLastCard=finalIndexChosen;
    secondCardToDraft=true;
    if(card.votesRequired()>0)
    {
      cardsNeedingSupport++;
    }
    setupCard(card, currentFood, currentRegion);
    getClient().getCommModule().send(Endpoint.DRAFT_CARD, card, null);
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
    if(!discarded && rand.nextBoolean())
    {
      randomlyDiscard();
    }
    return true;
  }
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
    else if(region==null && food!=null)
    {
      card=GameCard.create(getClient().getUser().getRegion(), policy);
      return true;
    }
    return false;
  }
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
   * Jeffrey McCall This method will change the probability values in an array
   * list of probability values. The probability is decreased if playAgain==-1.
   * It is increased if playAgain==1. This is done for foods, regions and
   * policies.
   * 
   * @param playAgain
   *          An int value which determines how the probability is altered.
   * @param card
   *          The policy card that was drafted last turn that we are checking.
   * @param type
   *          The type of item that the probabilities relate to.
   */
  public void distributeProbabilities(int playAgain, GameCard card, String type)
  {
    if (playAgain == -1)
    {
      if(card.getTargetFood()!=null && card.getTargetRegion()!=null)
      {
        policyCardInfo[card.getCardType().ordinal()][card.getTargetRegion().ordinal()][card.getTargetFood().ordinal()]=false;
      }
      else if(card.getTargetRegion()!=null && card.getTargetFood()==null)
      {
        policyCardInfo[card.getCardType().ordinal()][card.getTargetRegion().ordinal()][0]=false;
      }
      else if(card.getTargetFood()==null && card.getTargetRegion()==null)
      {
        policyCardInfo[card.getCardType().ordinal()][0][0]=false;
      }
    } 
  }

  /**
   * Jeffrey McCall Go through the relevant regional factors to determine how
   * things have worsened or improved since the last turn. If the amount of
   * decrease of factors is greater than the increase, then this method will
   * return -1 and the policy in question will be less likely to get played this
   * turn. If the amount of decrease is equal to the increase, than 0 is
   * returned and the likelihood of the card getting played this turn is
   * unaffected. If there was an overall improvement of factors greater than the
   * decrease, then it is more likely that the policy will get drafted again.
   * 
   * @return -1, 0 or 1
   */
  private int checkLastPlay()
  {
    int overallPercentDecrease = 0;
    int overallPercentIncrease = 0;
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
        if (overallPercentIncrease > overallPercentDecrease)
        {
          //If there has been an increase of over 20%, make it less likely to pick
          //this region again.
          if((overallPercentIncrease-overallPercentDecrease)>20)
          {
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
            pickThisRegion = true;
          }
          totalDecrease=overallPercentDecrease;
          return -1;
        }
      }
    }
    return 0;
  }

  /**
   * Returns the percent change between 2 ints.
   * 
   * @param originalVal
   *          First int.
   * @param newVal
   *          Second int.
   * @return The percent change between 2 ints.
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

  /**
   * Get the percent change between 2 doubles.
   * 
   * @param originalVal
   *          The first double.
   * @param newVal
   *          The second double.
   * @return The percentage change.
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

  /**
   * The percent change between longs.
   * 
   * @param originalVal
   * @param newVal
   * @return The percentage change.
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
      card.setX(0);
    else
    {
      card.setX(legalValueList.get(Util.rand.nextInt(legalValueList.size())));
    }
  }

}
