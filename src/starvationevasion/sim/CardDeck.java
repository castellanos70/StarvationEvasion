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
   * then the player's discard is shuffled back into the deck.
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
   * Each player region has a unique deck to be read from
   * .cvs file: playerRegion(String), card(String), count(int).<br><br>
   *   In future versions, players will be able to produce and save
   *   custom decks.<br><br>
   *
   * In this pre-test version, cards are just randomly picked with no difference
   * for different player regions.
   *
   */
  public CardDeck(EnumRegion playerRegion)
  {
    if (!playerRegion.isUS())
    {
      throw new IllegalArgumentException("CardDeck(="+playerRegion+" must be " +
        "a player region.");
    }

    PolicyManager policyManager = PolicyManager.getPolicyManager();
    Collection<PolicyCard> cardTypes = policyManager.getCardTypes();

    while (drawPile.size() < CARDS_IN_PLAYER_DECK)
    {
      for (PolicyCard card : cardTypes)
      {
        drawPile.add(card);
        if (drawPile.size() >= CARDS_IN_PLAYER_DECK) break;
      }
    }

    shuffle();
  }


  /**
   * Shuffles the deck of cards.
  */
  private void shuffle()
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
  public PolicyCard[] drawCards(int count)
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
   * @return The number of cards remaining in the draw pile.
   */
  public int cardsRemainingInDrawPile()
  {
    return drawPile.size();
  }



  public static void main(String[] args)
  {
    CardDeck deck = new CardDeck(EnumRegion.CALIFORNIA);

    PolicyManager policyManager = PolicyManager.getPolicyManager();
    // Draw cards, instantiating each.
    //
    PolicyCard[] hand = deck.drawCards(7);
    System.out.println("Drew 7 " + deck.cardsRemainingInDrawPile() + " remaining in draw pile.");
    for (PolicyCard card : hand)
    {
      String name = card.name();

      Policy policy = policyManager.createPolicy(name, EnumRegion.CALIFORNIA);
      System.out.println("Policy (" + name + ",typeId="+card.cardTypeId()+") : " + policy.getTitle() + " / " + policy.getGameText());
    }
  }
}

