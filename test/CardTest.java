// TODO add tests and add another card class that implements ICard
//  that would be considered an invalid card.

import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.Card;

import cs3500.freecell.model.hw02.Suit;
import cs3500.freecell.model.hw02.Suit.CardColor;
import cs3500.freecell.model.hw02.Value;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Test class for Card: unit tests to ensure that cards can be created and behave correctly.
 */
public class CardTest {

  ICard eightOfClubs = new Card(Value.EIGHT, Suit.CLUB);
  ICard eightOfClubsDuplicate = new Card(Value.EIGHT, Suit.CLUB);
  ICard aceOfHearts = new Card(Value.ACE, Suit.HEART);
  ICard jackOfDiamonds = new Card(Value.JACK, Suit.DIAMOND);
  ICard queenOfSpades = new Card(Value.QUEEN, Suit.SPADE);
  ICard kingOfClubs = new Card(Value.KING, Suit.CLUB);
  ICard kingOfHearts = new Card(Value.KING, Suit.HEART);

  // tests isGreaterThanBy for a ace of hearts and jack value, negative difference
  @Test
  public void testIsGreaterThanByAceOfHeartsJackNegative() {
    assertEquals(false, this.aceOfHearts.isGreaterThanBy(-11, Value.JACK));
  }

  // tests isGreaterThanBy for a ace of hearts and null value, negative difference
  @Test(expected = IllegalArgumentException.class)
  public void testIsGreaterThanNull() {
    this.aceOfHearts.isGreaterThanBy(-11, null);
  }

  // tests isGreaterThanBy for a king of clubs and queen value, 1 difference
  @Test
  public void testIsGreaterThanByKingOfClubsAndQueen1Diff() {
    assertEquals(true, this.kingOfClubs.isGreaterThanBy(1, Value.QUEEN));
  }

  // tests isGreaterThanBy for a king of clubs and queen value, 0 difference
  @Test
  public void testIsGreaterThanByKingOfClubsAndQueen0Diff() {
    assertEquals(false, this.kingOfClubs.isGreaterThanBy(0, Value.QUEEN));
  }

  // tests getColor for a normal valued card
  @Test
  public void testGetColorNormal() {
    assertEquals(CardColor.BLACK, this.eightOfClubs.getColor());
  }

  // tests getColor for an ace of hearts
  @Test
  public void testGetColorAceOfHearts() {
    assertEquals(CardColor.RED, this.aceOfHearts.getColor());
  }

  // tests getColor for a jack of diamonds
  @Test
  public void testGetColorJackOfDiamonds() {
    assertEquals(CardColor.RED, this.jackOfDiamonds.getColor());
  }

  // tests getColor for a queen of spades
  @Test
  public void testGetColorQueenOfSpades() {
    assertEquals(CardColor.BLACK, this.queenOfSpades.getColor());
  }

  // tests getColor for a king of clubs
  @Test
  public void testGetColorKingOfClubs() {
    assertEquals(CardColor.BLACK, this.kingOfClubs.getColor());
  }

  // tests getValue for a normal valued card
  @Test
  public void testGetValueNormal() {
    assertEquals(Value.EIGHT, this.eightOfClubs.getValue());
  }

  // tests getValue for an ace of hearts
  @Test
  public void testGetValueAceOfHearts() {
    assertEquals(Value.ACE, this.aceOfHearts.getValue());
  }

  // tests getValue for a jack of diamonds
  @Test
  public void testGetValueJackOfDiamonds() {
    assertEquals(Value.JACK, this.jackOfDiamonds.getValue());
  }

  // tests getValue for a queen of spades
  @Test
  public void testGetValueQueenOfSpades() {
    assertEquals(Value.QUEEN, this.queenOfSpades.getValue());
  }

  // tests getValue for a king of clubs
  @Test
  public void testGetValueKingOfClubs() {
    assertEquals(Value.KING, this.kingOfClubs.getValue());
  }

  // tests getSuit for a normal valued card
  @Test
  public void testGetSuitNormal() {
    assertEquals(Suit.CLUB, this.eightOfClubs.getSuit());
  }

  // tests getSuit for an ace of hearts
  @Test
  public void testGetSuitAceOfHearts() {
    assertEquals(Suit.HEART, this.aceOfHearts.getSuit());
  }

  // tests getSuit for a jack of diamonds
  @Test
  public void testGetSuitJackOfDiamonds() {
    assertEquals(Suit.DIAMOND, this.jackOfDiamonds.getSuit());
  }

  // tests getValue for a queen of spades
  @Test
  public void testGetSuitQueenOfSpades() {
    assertEquals(Suit.SPADE, this.queenOfSpades.getSuit());
  }

  // tests getSuit for a king of clubs
  @Test
  public void testGetSuitKingOfClubs() {
    assertEquals(Suit.CLUB, this.kingOfClubs.getSuit());
  }

  // tests toString for a normal valued card
  @Test
  public void testToStringNormalCard() {
    assertEquals("8♣", this.eightOfClubs.toString());
  }

  // tests toString for a card with an Ace value
  @Test
  public void testToStringAceOfHearts() {
    assertEquals("A♥", this.aceOfHearts.toString());
  }

  // tests toString for a card with a Jack value
  @Test
  public void testToStringJackOfDiamonds() {
    assertEquals("J♦", this.jackOfDiamonds.toString());
  }

  // tests toString for a card with a Queen value
  @Test
  public void testToStringQueenOfSpades() {
    assertEquals("Q♠", this.queenOfSpades.toString());
  }

  // tests toString for a card with a King value
  @Test
  public void testToStringKingOfClubs() {
    assertEquals("K♣", this.kingOfClubs.toString());
  }

  // tests the equals method on the same card
  @Test
  public void testEqualsSameCard() {
    assertTrue(this.kingOfClubs.equals(this.kingOfClubs));
  }

  // tests the equals method on two different objects that represent the same card
  @Test
  public void testEqualsSameCardDiffObject() {
    assertTrue(this.eightOfClubs.equals(this.eightOfClubsDuplicate));
  }

  // tests the equals method on two cards with the same value but different suits
  @Test
  public void testEqualsSameValuesDiffSuit() {
    assertFalse(this.kingOfHearts.equals(this.kingOfClubs));
  }

  // tests the equals method on two cards with the same value but different suits in reverse
  @Test
  public void testEqualsSameValuesDiffSuitReverse() {
    assertFalse(this.kingOfClubs.equals(this.kingOfHearts));
  }

  // tests the equals method on two cards with the same suit but different values
  @Test
  public void testEqualsSameSuitDiffValues() {
    assertFalse(this.eightOfClubs.equals(this.kingOfClubs));
  }

  // tests the equals method on two completely different cards
  @Test
  public void testEqualsCompletelyDifferentCards() {
    assertFalse(this.queenOfSpades.equals(this.eightOfClubs));
  }

  // tests for the hashCode method test for compatibility and non-injectivity based on the previous
  // equals method tests.
  // tests the compatibility rule on hashCode method on the same card
  @Test
  public void testHashCodeSameCard() {
    assertTrue((this.kingOfClubs.equals(this.kingOfClubs)) && (this.kingOfClubs.hashCode()
        == this.kingOfClubs.hashCode()));
  }

  // tests the compatibility rule on hashCode method on two different objects
  // that represent the same card
  @Test
  public void testHashCodeSameCardDiffObject() {
    assertTrue(
        (this.eightOfClubs.equals(this.eightOfClubsDuplicate)) && (this.eightOfClubs.hashCode()
            == this.eightOfClubsDuplicate.hashCode()));
  }
}
