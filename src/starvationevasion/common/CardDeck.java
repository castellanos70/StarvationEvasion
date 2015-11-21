package starvationevasion.common;

import java.util.*;

/** The CardDeck class
*/
public class CardDeck
{
  /* The master collection of cards.  This can be thought of as the cards in a new deck.
  */
  private Collection<PolicyCard> cards = new ArrayList<>();

  /* The shuffled cards from which cards are drawn.
  */
  private final Queue<ShuffledCard> shuffled = new PriorityQueue<>(new OrderComparator());

  /**
   * Adds a card to the deck.
  */
  public void add(PolicyCard card)
  {
    cards.add(card);
  }

  /**
   * Shuffles the deck of cards using the provided random number generator.
   * @param rnd The random number generator.
  */
  public void shuffle(Random rnd)
  {
    shuffled.clear();
    for (PolicyCard card : cards)
    { shuffled.add(new ShuffledCard(card, rnd.nextInt(10000)));
    }
  }

  /**
   * Returns a collection of cards from the shuffled deck.  If there are insufficient cards remaining,
   * then the count is adjusted.
   *
   * @param count The number of cards requested.
   * @return The cards drawn from the deck.
   */
  public Collection<PolicyCard> deal(int count)
  {
    if (shuffled.size() == 0) throw new IllegalArgumentException("The deck is empty.");

    if (shuffled.size() < count) count = shuffled.size();
    Collection<PolicyCard> cards = new ArrayList<>();
    for (int i = 0 ; i < count ; i += 1) cards.add(shuffled.remove().card);

    return cards;
  }

  /**
   * @return The number of cards remaining in the shuffled deck.
   */
  public int remaining()
  {
    return shuffled.size();
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

  /**
   * @param args Not used.
   * Used only for testing this class.
   */
  public static void main(String[] args)
  {
    PolicyManager pm = null;
    try
    { pm = PolicyManager.getInstance();
    }
    catch (ServiceConfigurationError | PolicyManager.DuplicatePolicyException ex)
    {
      ex.printStackTrace();
      System.exit(1);
    }

    Collection<PolicyCard> cards = pm.getCards();
    CardDeck deck = new CardDeck();

    for (PolicyCard c : cards)
    {
      // TODO : PAB : How many of each card should go into the deck?
      //
      for (int i = 0 ; i < 4 ; i += 1) deck.add(c);
    }

    // Shuffle the deck.
    //
    Random rnd = new Random();
    deck.shuffle(rnd);

    // Draw cards, instantiating each.
    //
    int count = cards.size();
    for (int i = 0 ; i < count ; i += 1)
    {
      Collection<PolicyCard> draw = deck.deal(4);
      System.out.println("Drew 4 " + deck.remaining() + " remaining");
      for (PolicyCard card : draw)
      { String name = card.name();

        Policy policy = pm.createPolicy(name, EnumRegion.MOUNTAIN);
        System.out.println("Policy " + i + " (" + name + ") : " + policy.getTitle() + " / " + policy.getGameText());
      }
    }
  }
}

