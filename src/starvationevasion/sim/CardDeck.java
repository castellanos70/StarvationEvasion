package starvationevasion.sim;

import starvationevasion.common.*;

import java.util.*;

/**
 * Each player region as a unique collection of 80 policy cards that make up that player's
 * deck.<br><br>
 * Each player's deck is shuffled at the start of the simulation. The deck is an <b>ordered</b>
 * collection. <br><br>
 *
 * There is no add() method because a card can never be added to a deck.
 *
*/
public class CardDeck
{

  /**
   * Each player starts with a unique deck of cards. A deck will, in general, contain
   * duplicate cards. If the player needs to draw a card and his, her or its deck is empty
   * then the player's discard is shuffeled back into the deck.
   */
  public static final int CARDS_IN_PLAYER_DECK = 80;
  /**
   *  Must be an ordered set of cards.
  */
  private ArrayList<PolicyCard> drawPile = new ArrayList<>(CARDS_IN_PLAYER_DECK);

  /**
   *  Must be an ordered set of cards.
  */
  private ArrayList<PolicyCard> discardPile = new ArrayList<>(CARDS_IN_PLAYER_DECK);

  /**
   * read from .cvs file: playerRegion(String), card(String), count(int)
   */
  public CardDeck(EnumRegion playerRegion)
  {

  }


  /**
   * Shuffles the deck of cards.
  */
  public void shuffle()
  {
    Collections.shuffle(drawPile, Util.rand);
  }

  /**
   * Returns an array of the top count cards from the players, draw pile.
   * If there are insufficient cards remaining, then the player's discard
   * pile is shuffled back into the deck.
   *
   * @param count The number of cards requested.
   * @return The cards drawn from the deck.
   */
  public PolicyCard[] deal(int count)
  {
    if (count < 1 || count > Constant.MAX_HAND_SIZE)
    {
      throw new IllegalArgumentException("deal(count="+count+") count must be " +
        "a positive integer no larger than " + Constant.MAX_HAND_SIZE);
    }
    PolicyCard[] cards = new PolicyCard[count];
    for (int i = 0 ; i < count ; i++)
    {
      if (drawPile.size() == 0)
      {
        ArrayList<PolicyCard> tmp = drawPile;
        drawPile = discardPile;
        discardPile = tmp;
        shuffle();
      }

      cards[i] = drawPile.get(drawPile.size()-1);
      drawPile.remove(drawPile.size()-1);
    }
    return cards;
  }

  /**
   * @return The number of cards remaining in the shuffled deck.
   */
  public int remaining()
  {
    return drawPile.size();
  }

  /* The queue element adapter for the PriorityQueue.
  */
  private static class ShuffledCard
  { final PolicyCard card;
    final int order;

    /**
     * Creates a new shuffleable card for insertion into the shuffled queue.
     *
     * @param card The card being shuffled into the deck.
     * @param order The relative position of the card in the deck.
     */
    public ShuffledCard(PolicyCard card, int order)
    { this.card = card;
      this.order = order;
    }

    public int compareTo(ShuffledCard other)
    { return order - other.order;
    }
  }

  /* The comparator adapter for the PriorityQueue.
  */
  private static class OrderComparator implements Comparator<ShuffledCard>
  { @Override
    public int compare(ShuffledCard o1, ShuffledCard o2)
    { return o1.compareTo(o2);
    }
  }

  /*
  public static void main(String[] args)
  {

    PolicyManager pm = PolicyManager.getPolicyManager();
    Collection<PolicyCard> cards = pm.getCardTypes();
    CardDeck deck = new CardDeck();



    // Shuffle the deck.
    //
    Random rnd = new Random();
    deck.shuffle(rnd);

    // Draw cards, instantiating each.
    //
    int count = cards.size();
    for (int i = 0 ; i < count ; i += 1)
    {
      ArrayList<PolicyCard> draw = deck.deal(4);
      System.out.println("Drew 4 " + deck.remaining() + " remaining");
      for (PolicyCard card : draw)
      { String name = card.name();

        Policy policy = pm.createPolicy(name, EnumRegion.MOUNTAIN);
        System.out.println("Policy " + i + " (" + name + ") : " + policy.getTitle() + " / " + policy.getGameText());
      }
    }
  }
  */
}

