package starvationevasion.ai.commands;

import java.util.ArrayList;
import java.util.HashMap;
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
  boolean moreThanOne = false;
  boolean pickThisRegion = false;
  double pickThisRegionChance = .75;
  int z = 0;
  int probModifier = 0;
  int draftIndex = 0;
  public ArrayList<String> policySampleSpace = new ArrayList<String>();
  public ArrayList<String> foodSampleSpace = new ArrayList<String>();
  public ArrayList<String> regionSampleSpace = new ArrayList<String>();
  private Map<String,Integer> probabilityMap=new HashMap<>();
  int totalIncrease=0;
  int totalDecrease=0;
  int amtToAdjust=0;
  
  public Draft(AI client)
  {
    super(client);
    this.numTurns = client.numTurns;
    fillProbabilityMap();
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
  public void fillProbabilityMap()
  {
    for(EnumFood food:EnumFood.values())
    {
      probabilityMap.put(food.name(),rand.nextInt(20)+1);
    }
    for(EnumPolicy policy:EnumPolicy.values())
    {
      probabilityMap.put(policy.name(),rand.nextInt(20)+1);
    }
    for(EnumRegion region:EnumRegion.values())
    {
      probabilityMap.put(region.name(),rand.nextInt(20)+1);
    }
  }
  
  public void createLists()
  {
    for(EnumFood food:EnumFood.values())
    {
      for(int i=0;i<probabilityMap.get(food.name());i++)
      {
        foodSampleSpace.add(food.name());
      }
    }
    for(EnumRegion region:EnumRegion.values())
    {
      for(int i=0;i<probabilityMap.get(region.name());i++)
      {
        regionSampleSpace.add(region.name());
      }
    }
    for(EnumPolicy policy:EnumPolicy.values())
    {
      for(int i=0;i<probabilityMap.get(policy.name());i++)
      {
        policySampleSpace.add(policy.name());
      }
    }
  }
  @Override
  public boolean run()
  {
    // System.out.println("Drafted: " + draftedCard +
    // "\nDiscarded: " + discarded +
    // "\nDrawn: " + drawn);

    // if (!getClient().getState().equals(State.DRAFTING) && tries > 0)
    // {
    // tries--;
    // if (tries <= 0)
    // {
    // return false;
    // }
    // }

    if (getClient().getState().equals(State.DRAFTING))
    {

      if (!draftedCard)
      {
        if (setDraftedCards())
        {
          getClient().getCommModule().send(Endpoint.DONE, new Payload(), null);
          return false;
        } else
        {
          return true;
        }
      }

      // if (!discarded && getClient().getUser().getHand().size() > 0)
      // {
      // if (!Util.rand.nextBoolean())
      // {
      // randomlyDiscard();
      // }
      //
      // return true;
      // }
      // getClient().getCommModule().send(new
      // RequestFactory().build(getClient().getStartNanoSec(), new Payload(),
      // Endpoint.DONE));
      // getClient().getCommModule().send(Endpoint.DONE, new Payload(), null);

      // if (!drawn && getClient().getUser().getHand().size() < 7)
      // {
      // Request request = new Request(getClient().getStartNanoSec(),
      // Endpoint.DRAW_CARD);
      // getClient().send(request);
      // drawn = true;
      // return true;
      // }

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
      if (cardDrafted1 != null && policy != cardDrafted1.getCardType()
          && cardDrafted2 != null && policy != cardDrafted2.getCardType()
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
    GameCard card = null;
    boolean draftSent = false;
    System.out.println("Hand size:" + getClient().getUser().getHand().size());
    ArrayList<EnumPolicy> currentHand = new ArrayList<>();
    for (int i = 0; i < getClient().getUser().getHand().size(); i++)
    {
      System.out.println("Hand:"+getClient().getUser().getHand().get(i).name());
    }
    if (getClient().getUser().getHand().size() == 0)
    {
      getClient().getUser().reset();
      return false;
    }
    if (numTurns == 0)
    {
      int firstRandNum = rand.nextInt(7);
      int secondRandNum = 0;
      do
      {
        secondRandNum = rand.nextInt(7);
      } while (secondRandNum == firstRandNum);
      EnumPolicy policy1 = null;
      EnumPolicy policy2 = null;
      for (int i = 0; i < getClient().getUser().getHand().size(); i++)
      {
        if (i == firstRandNum || i == secondRandNum)
        {
          // TODO will have to change this in the future. Cards will be
          // changed
          // to not have BOTH a
          // target region and target food to set, it will be one of each.
          card = GameCard.create(getClient().getUser().getRegion(),
              getClient().getUser().getHand().get(i));
          EnumFood[] foods = card.getValidTargetFoods();
          EnumRegion[] regions = card.getValidTargetRegions();
          EnumFood food = null;
          if (foods != null)
          {
            food = foods[rand.nextInt(foods.length)];
          }
          EnumRegion region = null;
          if (regions != null)
          {
            region = regions[rand.nextInt(regions.length)];
          }
          setupCard(card, food, region);
          getClient().getCommModule().send(Endpoint.DRAFT_CARD, card, null);
          draftedCard = true;
          draftSent = true;
          if (i == firstRandNum)
          {
            policy1 = getClient().getUser().getHand().get(i);
            cardDrafted1 = card;
          }
          if (i == secondRandNum)
          {
            policy2 = getClient().getUser().getHand().get(i);
            cardDrafted2 = card;
          }
          getClient().draftedCards.get(numTurns).add(card);
        }

      }
      tries = 2;
      return true;
    } 
    else
    {
      pickThisRegion = false;
      GameCard lastCard1 = getClient().draftedCards.get(numTurns - 1).get(0);
      GameCard lastCard2 = getClient().draftedCards.get(numTurns - 1).get(1);
      int playAgain = 0;
      playAgain = checkLastPlay();
      foodProduced = (long) 0;
      foodIncome = 0;
      foodImported = (long) 0;
      foodExported = (long) 0;
      lastFoodProduced = (long) 0;
      lastFoodIncome = 0;
      lastFoodImported = (long) 0;
      lastFoodExported = (long) 0;
      if (lastCard1.getTargetFood() != null)
      {
        distributeProbabilities(playAgain, lastCard1, "food");
      }
      if (lastCard1.getTargetRegion() != null)
      {
        distributeProbabilities(playAgain, lastCard1, "region");
      }
      distributeProbabilities(playAgain, lastCard1, "policy");
      if (lastCard2.getTargetFood() != null)
      {
        distributeProbabilities(playAgain, lastCard2, "food");
      }
      if (lastCard2.getTargetRegion() != null)
      {
        distributeProbabilities(playAgain, lastCard2, "region");
      }
      distributeProbabilities(playAgain, lastCard2, "policy");
      checkOtherFactors();
      createLists();
      draftCards();
      draftedCard = true;
      draftSent = true;
      tries = 2;
      return true;
    }
  }

  public void checkOtherFactors()
  {
    z=0;
    RegionData thisRegion=null;
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
      if(data.region.name().equals(getClient().getUser().getRegion().name()))
      {
        thisRegion=data;
      }
    }
    //If there has been an overall decrease of factors greater than 20%, then it is
    //75% more likely that this region will be picked.
    if(pickThisRegion)
    {
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
      adjustProbability(probabilityMap.get("Policy_Loan")*2,"Policy_Loan");
      adjustProbability(probabilityMap.get("Policy_DiverttheFunds")*2,"Policy_DiverttheFunds");
      adjustProbability(probabilityMap.get("Policy_Fundraiser")*2,"Policy_Fundraiser");
    }
    //If the region doesn't need the money, make it very unlikely to select getting a loan.
    else if(revenueDiff>0)
    {
      adjustProbability(1,"Policy_Loan");
    }
    EnumFood foodNeedingAttention=null;
  }
  /**
   * Jeffrey McCall
   * Adjust the probability that an item will be selected. Either increase or
   * decrease the probability by a certain amount.
   * @param increase
   *        Increase probability.
   * @param decrease
   *        Decrease probability.
   * @param size
   *        The size to increase or decrease.
   * @param type
   *        The type of the item, like food, region etc.
   */
  private void adjustProbability(int size,String type)
  {
    probabilityMap.remove(type);
    probabilityMap.put(type, size);
  }
  /**
   * Jeffrey McCall This method drafts 2 cards. The policy, region, and food
   * sample space ArrayLists from AI.java are accessed to pick which policy,
   * region and foods should be used when drafting the 2 cards. If the decrease
   * of factor's in the AI's region are greater than 20% after the last turn,
   * then pickThisRegion is true and the AI is much more likely to draft a card
   * that affects it's own region.
   * 
   * @param rand
   *          A Random object.
   */
  public void draftCards()
  {
    ArrayList<EnumPolicy> policiesInHand = new ArrayList<>();
    for (int i = 0; i < getClient().getUser().getHand().size(); i++)
    {
      policiesInHand.add(getClient().getUser().getHand().get(i));
    }
    for (int h = 0; h < 2; h++)
    {
      GameCard card = null;
      EnumRegion currentRegion = null;
      String regionString = "";
      boolean regionFound = false;
      String policyString = "";
      EnumPolicy currentPolicy = null;
      do
      {
        int policyIndex = rand.nextInt(policySampleSpace.size());
        policyString = policySampleSpace.get(policyIndex);
        currentPolicy = EnumPolicy.valueOf(policyString);
      } while (!policiesInHand.contains(currentPolicy));
      policiesInHand.remove(currentPolicy);
      card = GameCard.create(getClient().getUser().getRegion(), currentPolicy);
      EnumFood[] foods = card.getValidTargetFoods();
      ArrayList<String> validFoods = new ArrayList<>();
      Map<String, EnumFood> foodMap = new HashMap<>();
      EnumFood currentFood = null;
      z = 0;
      if (foods != null)
      {
        Stream.of(foods).forEach(food ->
        {
          validFoods.add(food.name());
          foodMap.put(food.name(), foods[z]);
          z++;
        });
        z = 0;
        String food = "";
        do
        {
          int foodIndex = rand.nextInt(foodSampleSpace.size());
          food = foodSampleSpace.get(foodIndex);
        } while (!validFoods.contains(food));
        currentFood = foodMap.get(food);
      }
      EnumRegion[] regions = card.getValidTargetRegions();
      ArrayList<String> validRegions = new ArrayList<String>();
      Map<String, EnumRegion> regionMap = new HashMap<>();
      if (regions != null)
      {
        Stream.of(regions).forEach(region ->
        {
          validRegions.add(region.name());
          regionMap.put(region.name(), regions[z]);
          z++;
        });
        z = 0;
      }
      if (regions != null && !regionFound)
      {
        do
        {
          int regionIndex = rand.nextInt(regionSampleSpace.size());
          regionString = regionSampleSpace.get(regionIndex);
        } while (!validRegions.contains(regionString));
        currentRegion = regionMap.get(regionString);
      }
      if (regionFound)
      {
        currentRegion = regionMap.get(regionString);
      }
      setupCard(card, currentFood, currentRegion);
      getClient().getCommModule().send(Endpoint.DRAFT_CARD, card, null);
      System.out.println("Card drafted:"+card.getPolicyName());
      if (card.votesRequired() != 0)
      {
        String message = "I am drafing " + card.getTitle()
            + ". Will you support it?";
        // getClient().send(new
        // RequestFactory().chat(getClient().getStartNanoSec(),
        // "ALL", message, card));
      }
      if (h == 0)
      {
        cardDrafted1 = card;
      }
      if (h == 1)
      {
        cardDrafted2 = card;
      }
      getClient().draftedCards.get(numTurns).add(card);
    }
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
      if (type.equals("food"))
      {
        if(totalDecrease<10)
        {
          adjustProbability(9,card.getTargetFood().name());
        }
        else
        {
          adjustProbability(10-(totalDecrease/10),card.getTargetFood().name());
        }
      }
      if (type.equals("region"))
      {
        if(totalDecrease<10)
        {
          adjustProbability(9,card.getTargetRegion().name());
        }
        else
        {
          adjustProbability(10-(totalDecrease/10),card.getTargetRegion().name());
        }
      }
      if (type.equals("policy"))
      {
        if(totalDecrease<10)
        {
          adjustProbability(9,card.getPolicyName());
        }
        else
        {
          adjustProbability(10-(totalDecrease/10),card.getPolicyName());
        }
      }
    } 
    else if (playAgain == 1)
    {
      if (type.equals("food"))
      {
        if(totalIncrease<10)
        {
          adjustProbability(21,card.getTargetFood().name());
        }
        else
        {
          adjustProbability(20+((int)Math.pow(((totalIncrease/10)+1),2)),card.getTargetFood().name());
        }
      }
      if (type.equals("region"))
      {
        if(totalIncrease<10)
        {
          adjustProbability(21,card.getTargetRegion().name());
        }
        else
        {
          adjustProbability(20+((int)Math.pow(((totalIncrease/10)+1),2)),card.getTargetRegion().name());
        }
      }
      if (type.equals("policy"))
      {
        if(totalIncrease<10)
        {
          adjustProbability(21,card.getPolicyName());
        }
        else
        {
          adjustProbability(20+((int)Math.pow(((totalIncrease/10)+1),2)),card.getPolicyName());
        }
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
          totalIncrease=overallPercentIncrease;
          return 1;
        } 
        else if (overallPercentDecrease > overallPercentIncrease)
        {
          if (overallPercentDecrease >= 20)
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
