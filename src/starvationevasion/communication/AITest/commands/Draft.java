package starvationevasion.communication.AITest.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.Util;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.common.gamecards.GameCard;
import starvationevasion.communication.AITest.AI;
import starvationevasion.communication.AITest.AI.WorldFactors;
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
  boolean moreThanOne = false;
  boolean pickThisRegion = false;
  double pickThisRegionChance = .75;
  int z = 0;
  int probModifier = 0;
  int draftIndex = 0;

  public Draft(AI client)
  {
    super(client);
    this.numTurns = client.numTurns;
  }

  @Override
  public String commandString()
  {
    return "Draft";
  }

  @Override
  public boolean run()
  {
    // System.out.println("Drafted: " + draftedCard +
    // "\nDiscarded: " + discarded +
    // "\nDrawn: " + drawn);

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

      if (!draftedCard)
      {
        randomlySetDraftedCard();
        return true;
      }

      if (!discarded && getClient().getUser().getHand().size() > 0)
      {
        if (!Util.rand.nextBoolean())
        {
          randomlyDiscard();
        }

        return true;
      }
      // getClient().getCommModule().send(new
      // RequestFactory().build(getClient().getStartNanoSec(), new Payload(),
      // Endpoint.DONE));
      getClient().getCommModule().send(Endpoint.DONE, new Payload(), null);

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
  private void randomlySetDraftedCard()
  {
    GameCard card = null;
    boolean draftSent = false;
    Random rand = new Random();
    System.out.println("Hand size:" + getClient().getUser().getHand().size());
    if (numTurns == 0)
    {
      int firstRandNum = rand.nextInt(7);
      int secondRandNum = 0;
      do
      {
        secondRandNum = rand.nextInt(7);
      } while (secondRandNum == firstRandNum);

      for (int i = 0; i < getClient().getUser().getHand().size(); i++)
      {
        if (i == firstRandNum || i == secondRandNum)
        {
          // TODO will have to change this in the future. Cards will be changed
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
            cardDrafted1 = card;
          }
          if (i == secondRandNum)
          {
            cardDrafted2 = card;
          }
          getClient().draftedCards.get(numTurns).add(card);
        }
      }
      tries = 2;
    } else
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
      draftCards(rand);
      draftedCard = true;
      draftSent = true;
      tries = 2;
    }
  }

  public void checkOtherFactors()
  {

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
  public void draftCards(Random rand)
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
      if (!pickThisRegion)
      {
        do
        {
          int policyIndex = rand.nextInt(getClient().policySampleSpace.size());
          policyString = getClient().policySampleSpace.get(policyIndex);
          currentPolicy = EnumPolicy.valueOf(policyString);
        } while (!policiesInHand.contains(EnumPolicy.valueOf(policyString)));
        policiesInHand.remove(currentPolicy);
      } else
      {
        double chance = rand.nextDouble();
        if (chance <= pickThisRegionChance)
        {
          for (int i = 0; i < policiesInHand.size(); i++)
          {
            card = GameCard.create(getClient().getUser().getRegion(),
                policiesInHand.get(i));
            ArrayList<String> regions = new ArrayList<String>();
            EnumRegion[] regionArray = card.getValidTargetRegions();
            if (regionArray != null)
            {
              Stream.of(regionArray)
                  .forEach(region -> regions.add(region.name()));
            }
            if (regions.contains(getClient().getUser().getRegion().name()))
            {
              currentPolicy = policiesInHand.get(i);
              regionFound = true;
              regionString = getClient().getUser().getRegion().name();
              policiesInHand.remove(currentPolicy);
              break;
            }
            regions.clear();
          }
          if (currentPolicy == null)
          {
            do
            {
              int policyIndex = rand
                  .nextInt(getClient().policySampleSpace.size());
              policyString = getClient().policySampleSpace.get(policyIndex);
              currentPolicy = EnumPolicy.valueOf(policyString);
            } while (!policiesInHand
                .contains(EnumPolicy.valueOf(policyString)));
            policiesInHand.remove(currentPolicy);
          }
        } else if (chance > pickThisRegionChance)
        {
          do
          {
            int policyIndex = rand
                .nextInt(getClient().policySampleSpace.size());
            policyString = getClient().policySampleSpace.get(policyIndex);
            currentPolicy = EnumPolicy.valueOf(policyString);
          } while (!policiesInHand.contains(EnumPolicy.valueOf(policyString)));
          policiesInHand.remove(currentPolicy);
          // TODO add a way to discard and redraw cards so that the AI can try
          // to
          // get different cards.
        }
      }
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
          int foodIndex = rand.nextInt(getClient().foodSampleSpace.size());
          food = getClient().foodSampleSpace.get(foodIndex);
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
          int regionIndex = rand.nextInt(getClient().regionSampleSpace.size());
          regionString = getClient().regionSampleSpace.get(regionIndex);
        } while (!validRegions.contains(regionString));
        currentRegion = regionMap.get(regionString);
      }
      if (regionFound)
      {
        currentRegion = regionMap.get(regionString);
      }
      setupCard(card, currentFood, currentRegion);
      getClient().getCommModule().send(Endpoint.DRAFT_CARD, card, null);
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
        for (int i = 0; i < getClient().foodSampleSpace.size(); i++)
        {
          if (card.getTargetFood().name()
              .equals(getClient().foodSampleSpace.get(i)))
          {
            z = 0;
            moreThanOne = false;
            getClient().foodSampleSpace.forEach(food ->
            {
              if (card.getTargetFood().name().equals(food))
              {
                z++;
              }
              if (z > 1)
              {
                moreThanOne = true;
              }
            });
            if (moreThanOne)
            {
              getClient().foodSampleSpace.remove(i);
            }
            return;
          }
        }
        if (type.equals("region"))
        {
          for (int i = 0; i < getClient().regionSampleSpace.size(); i++)
          {
            if (card.getTargetRegion().name()
                .equals(getClient().regionSampleSpace.get(i)))
            {
              z = 0;
              moreThanOne = false;
              getClient().regionSampleSpace.forEach(food ->
              {
                if (card.getTargetFood().name().equals(food))
                {
                  z++;
                }
                if (z > 1)
                {
                  moreThanOne = true;
                }
              });
              if (moreThanOne)
              {
                getClient().regionSampleSpace.remove(i);
              }
              return;
            }
          }
        }
        if (type.equals("policy"))
        {
          for (int i = 0; i < getClient().policySampleSpace.size(); i++)
          {
            if (card.getPolicyName()
                .equals(getClient().policySampleSpace.get(i)))
            {
              z = 0;
              moreThanOne = false;
              getClient().policySampleSpace.forEach(food ->
              {
                if (card.getTargetFood().name().equals(food))
                {
                  z++;
                }
                if (z > 1)
                {
                  moreThanOne = true;
                }
              });
              if (moreThanOne)
              {
                getClient().policySampleSpace.remove(i);
              }
              return;
            }
          }
        }
      }
    } else if (playAgain == 1)
    {
      if (type.equals("food"))
      {
        for (int i = 0; i < getClient().foodSampleSpace.size(); i++)
        {
          if (card.getTargetFood().name()
              .equals(getClient().foodSampleSpace.get(i)))
          {
            for (int h = 0; h < 12; h++)
            {
              getClient().foodSampleSpace.add(i, card.getTargetFood().name());
            }
            return;
          }
        }
        if (type.equals("region"))
        {
          for (int i = 0; i < getClient().regionSampleSpace.size(); i++)
          {
            if (card.getTargetRegion().name()
                .equals(getClient().regionSampleSpace.get(i)))
            {
              for (int h = 0; h < 12; h++)
              {
                getClient().regionSampleSpace.add(i,
                    card.getTargetRegion().name());
              }
              return;
            }
          }
        }
        if (type.equals("policy"))
        {
          for (int i = 0; i < getClient().policySampleSpace.size(); i++)
          {
            if (card.getPolicyName()
                .equals(getClient().policySampleSpace.get(i)))
            {
              for (int h = 0; h < 12; h++)
              {
                getClient().policySampleSpace.add(i, card.getPolicyName());
              }
              return;
            }
          }
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
      if (h == 1)
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
          } else if (i == 1)
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
          } else if (i == 2)
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
          } else if (i == 3 || i > 4)
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
          return 1;
        } else if (overallPercentDecrease > overallPercentIncrease)
        {
          if (overallPercentDecrease >= 20)
          {
            pickThisRegion = true;
          }
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
