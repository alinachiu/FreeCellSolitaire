import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.CascadePile;
import cs3500.freecell.model.hw02.FoundationPile;
import cs3500.freecell.model.hw02.IPile;
import cs3500.freecell.model.hw02.OpenPile;
import cs3500.freecell.model.hw02.Suit;
import cs3500.freecell.model.hw02.Value;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for AbstractPile: unit tests to ensure that abstract piles can be created and behave
 * correctly.
 */
public class AbstractPileTest {

  IPile cascadePile;
  IPile openPile;
  IPile foundationPile;

  @Before
  public void initData() {
    this.cascadePile = new CascadePile();
    this.openPile = new OpenPile();
    this.foundationPile = new FoundationPile();
  }

  // tests canAddAdditionalCard for empty open pile
  @Test
  public void testCanAddAdditionalCardEmptyOpen() {
    assertTrue(this.openPile.canAddAdditionalCard(new Card(Value.TEN, Suit.CLUB)));
  }

  // tests canAddAdditionalCard for non-empty open pile
  @Test
  public void testCanAddAdditionalCardNonEmptyOpen() {
    this.openPile.addCardToPile(new Card(Value.FOUR, Suit.CLUB));
    assertFalse(this.openPile.canAddAdditionalCard(new Card(Value.TEN, Suit.CLUB)));
  }

  // tests canAddAdditionalCard for empty foundation pile, ace input card
  @Test
  public void testCanAddAdditionalCardEmptyFoundationAceCard() {
    assertTrue(this.foundationPile.canAddAdditionalCard(new Card(Value.ACE, Suit.SPADE)));
  }

  // tests canAddAdditionalCard for empty foundation pile, non-ace input card
  @Test
  public void testCanAddAdditionalCardEmptyFoundationNonAceCard() {
    assertFalse(this.foundationPile.canAddAdditionalCard(new Card(Value.JACK, Suit.DIAMOND)));
  }

  // tests canAddAdditionalCard for a foundation pile with an ace, incorrect input (wrong value)
  @Test
  public void testCanAddAdditionalCardEmptyFoundationWithAceIncorrectValue() {
    this.foundationPile.addCardToPile(new Card(Value.ACE, Suit.DIAMOND));
    assertFalse(this.foundationPile.canAddAdditionalCard(new Card(Value.JACK, Suit.DIAMOND)));
  }

  // tests canAddAdditionalCard for a foundation pile with an ace, incorrect input (wrong suit)
  @Test
  public void testCanAddAdditionalCardEmptyFoundationWithAceIncorrectSuit() {
    this.foundationPile.addCardToPile(new Card(Value.ACE, Suit.DIAMOND));
    assertFalse(this.foundationPile.canAddAdditionalCard(new Card(Value.TWO, Suit.HEART)));
  }

  // tests canAddAdditionalCard for a foundation pile with an ace, correct input
  @Test
  public void testCanAddAdditionalCardEmptyFoundationWithAceCorrect() {
    this.foundationPile.addCardToPile(new Card(Value.ACE, Suit.DIAMOND));
    this.foundationPile.addCardToPile(new Card(Value.TWO, Suit.DIAMOND));
    this.foundationPile.addCardToPile(new Card(Value.THREE, Suit.DIAMOND));
    this.foundationPile.addCardToPile(new Card(Value.FOUR, Suit.DIAMOND));
    this.foundationPile.addCardToPile(new Card(Value.FIVE, Suit.DIAMOND));
    this.foundationPile.addCardToPile(new Card(Value.SIX, Suit.DIAMOND));
    this.foundationPile.addCardToPile(new Card(Value.SEVEN, Suit.DIAMOND));
    this.foundationPile.addCardToPile(new Card(Value.EIGHT, Suit.DIAMOND));
    this.foundationPile.addCardToPile(new Card(Value.NINE, Suit.DIAMOND));
    this.foundationPile.addCardToPile(new Card(Value.TEN, Suit.DIAMOND));
    this.foundationPile.addCardToPile(new Card(Value.JACK, Suit.DIAMOND));
    this.foundationPile.addCardToPile(new Card(Value.QUEEN, Suit.DIAMOND));
    this.foundationPile.addCardToPile(new Card(Value.KING, Suit.DIAMOND));

    assertFalse(this.foundationPile.canAddAdditionalCard(new Card(Value.ACE, Suit.DIAMOND)));
  }

  // tests canAddAdditionalCard for a foundation pile with an ace, no more room finished pile
  @Test
  public void testCanAddAdditionalCardEmptyFoundationNoMoreRoom() {
    this.foundationPile.addCardToPile(new Card(Value.ACE, Suit.DIAMOND));
    assertFalse(this.foundationPile.canAddAdditionalCard(new Card(Value.ACE, Suit.SPADE)));
  }

  // tests canAddAdditionalCard for empty cascade pile
  @Test
  public void testCanAddAdditionalCardEmptyCascade() {
    assertTrue(this.cascadePile.canAddAdditionalCard(new Card(Value.KING, Suit.DIAMOND)));
  }

  // tests canAddAdditionalCard for empty cascade pile try adding same color, wrong value
  @Test
  public void testCanAddAdditionalCardCascadeTryAddingSameColorWrongVal() {
    this.cascadePile.addCardToPile(new Card(Value.ACE, Suit.DIAMOND));
    this.cascadePile.addCardToPile(new Card(Value.TEN, Suit.HEART));

    assertFalse(this.cascadePile.canAddAdditionalCard(new Card(Value.KING, Suit.DIAMOND)));
  }

  // tests canAddAdditionalCard for empty cascade pile try adding different color, wrong value
  @Test
  public void testCanAddAdditionalCardCascadeTryAddingDiffColorWrongVal() {
    this.cascadePile.addCardToPile(new Card(Value.ACE, Suit.DIAMOND));
    this.cascadePile.addCardToPile(new Card(Value.TEN, Suit.HEART));

    assertFalse(this.cascadePile.canAddAdditionalCard(new Card(Value.KING, Suit.CLUB)));
  }

  // tests canAddAdditionalCard for empty cascade pile try adding same color, right value
  @Test
  public void testCanAddAdditionalCardCascadeTryAddingSameColorRightVal() {
    this.cascadePile.addCardToPile(new Card(Value.ACE, Suit.DIAMOND));
    this.cascadePile.addCardToPile(new Card(Value.TEN, Suit.HEART));

    assertTrue(this.cascadePile.canAddAdditionalCard(new Card(Value.NINE, Suit.CLUB)));
  }

  // tests canAddAdditionalCard for empty cascade pile try adding different color, right value
  @Test
  public void testCanAddAdditionalCardCascadeTryAddingDiffColorRightVal() {
    this.cascadePile.addCardToPile(new Card(Value.ACE, Suit.DIAMOND));
    this.cascadePile.addCardToPile(new Card(Value.TEN, Suit.HEART));

    assertFalse(this.cascadePile.canAddAdditionalCard(new Card(Value.NINE, Suit.HEART)));
  }

  // tests canAddAdditionalCard null card for cascade
  @Test(expected = IllegalArgumentException.class)
  public void testCanAddAdditionalCardNullCardCascade() {
    this.cascadePile.canAddAdditionalCard(null);
  }

  // tests canAddAdditionalCard null card for open
  @Test(expected = IllegalArgumentException.class)
  public void testCanAddAdditionalCardNullCardOpen() {
    this.openPile.canAddAdditionalCard(null);
  }

  // tests canAddAdditionalCard null card for foundation
  @Test(expected = IllegalArgumentException.class)
  public void testCanAddAdditionalCardNullCardFoundation() {
    this.foundationPile.canAddAdditionalCard(null);
  }

  // tests size for an empty cascade pile
  @Test
  public void testSizeEmptyCascade() {
    assertEquals(0, this.cascadePile.size());
  }

  // tests size for an empty open pile
  @Test
  public void testSizeEmptyOpen() {
    assertEquals(0, this.openPile.size());
  }

  // tests size for an empty foundation pile
  @Test
  public void testSizeEmptyFoundation() {
    assertEquals(0, this.foundationPile.size());
  }

  // tests size for a cascade pile with cards in it
  @Test
  public void testSizeCascadeContents() {
    this.cascadePile.addCardToPile(new Card(Value.ACE, Suit.CLUB));
    assertEquals(1, this.cascadePile.size());
  }

  // tests size for an open pile with cards in it
  @Test
  public void testSizeOpenContents() {
    this.openPile.addCardToPile(new Card(Value.QUEEN, Suit.SPADE));

    assertEquals(1, this.openPile.size());
  }

  // tests size for a foundation pile with cards in it
  @Test
  public void testSizeFoundationContents() {
    this.foundationPile.addCardToPile(new Card(Value.ACE, Suit.HEART));
    assertEquals(1, this.foundationPile.size());
  }

  // tests findCard of an empty cascade pile
  @Test(expected = IndexOutOfBoundsException.class)
  public void testFindCardEmptyCascade() {
    this.cascadePile.findCard(0);
  }

  // tests findCard of an empty foundation pile
  @Test(expected = IndexOutOfBoundsException.class)
  public void testFindCardEmptyFoundation() {
    this.foundationPile.findCard(0);
  }

  // tests findCard of an empty open pile
  @Test(expected = IndexOutOfBoundsException.class)
  public void testFindCardEmptyOpen() {
    this.openPile.findCard(0);
  }

  // tests findCard and addCardToPile for a cascade pile with cards in it
  @Test
  public void testFindCardAddToPileCascadeContent() {
    assertTrue(this.cascadePile.isEmpty());
    this.cascadePile.addCardToPile(new Card(Value.ACE, Suit.CLUB));
    assertEquals(new Card(Value.ACE, Suit.CLUB), this.cascadePile.findCard(0));
    assertFalse(this.cascadePile.isEmpty());
  }

  // tests findCard and addCardToPile for a open pile with cards in it
  @Test
  public void testFindCardAddToPileOpenContents() {
    assertTrue(this.openPile.isEmpty());
    this.openPile.addCardToPile(new Card(Value.QUEEN, Suit.SPADE));

    assertEquals(new Card(Value.QUEEN, Suit.SPADE), this.openPile.findCard(0));
    assertFalse(this.openPile.isEmpty());
  }

  // tests findCard and addCardToPile for a foundation pile with cards in it
  @Test
  public void testFindCardAddToPileFoundationContents() {
    assertTrue(this.foundationPile.isEmpty());
    this.foundationPile.addCardToPile(new Card(Value.ACE, Suit.HEART));
    assertEquals(new Card(Value.ACE, Suit.HEART), this.foundationPile.findCard(0));
    assertFalse(this.foundationPile.isEmpty());
  }

  // tests removeCardFromPile, removes card from empty pile
  @Test
  public void testRemoveCardFromPileEmpty() {
    assertTrue(this.openPile.isEmpty());
    this.openPile.removeCardFromPile(new Card(Value.TWO, Suit.SPADE));
    assertTrue(this.openPile.isEmpty());
  }

  // tests removeCardFromPile, removes card from pile with cards in it
  @Test
  public void testRemoveCardFromPileContents() {
    assertTrue(this.foundationPile.isEmpty());
    this.foundationPile.addCardToPile(new Card(Value.ACE, Suit.HEART));
    assertEquals(new Card(Value.ACE, Suit.HEART), this.foundationPile.findCard(0));
    assertFalse(this.foundationPile.isEmpty());
    this.foundationPile.removeCardFromPile(new Card(Value.ACE, Suit.HEART));
    assertTrue(this.foundationPile.isEmpty());
  }

  // tests isEmpty on empty piles
  @Test
  public void testIsEmptyEmptyPiles() {
    assertTrue(this.foundationPile.isEmpty());
    assertTrue(this.openPile.isEmpty());
    assertTrue(this.cascadePile.isEmpty());
  }

  // tests isEmpty on non-empty piles
  @Test
  public void testIsEmptyNonEmptyPiles() {
    this.openPile.addCardToPile(new Card(Value.TEN, Suit.SPADE));
    this.cascadePile.addCardToPile(new Card(Value.TEN, Suit.HEART));
    this.foundationPile.addCardToPile(new Card(Value.ACE, Suit.DIAMOND));

    assertFalse(this.foundationPile.isEmpty());
    assertFalse(this.openPile.isEmpty());
    assertFalse(this.cascadePile.isEmpty());
  }

  // tests toString on an empty cascade pile
  @Test
  public void testToStringEmptyPileCascade() {
    assertEquals("", this.cascadePile.toString());
  }

  // tests toString on an empty open pile
  @Test
  public void testToStringEmptyPileOpen() {
    assertEquals("", this.openPile.toString());
    assertEquals("", this.foundationPile.toString());
  }

  // tests toString on an empty foundation pile
  @Test
  public void testToStringEmptyPileFoundation() {
    assertEquals("", this.foundationPile.toString());
  }

  // tests toString on a cascade pile with cards
  @Test
  public void testToStringCascadePileWithCards() {
    this.cascadePile.addCardToPile(new Card(Value.QUEEN, Suit.CLUB));
    this.cascadePile.addCardToPile(new Card(Value.JACK, Suit.HEART));

    assertEquals("Q♣, J♥\n", this.cascadePile.toString());
  }

  // tests toString on an open pile with cards
  @Test
  public void testToStringOpenPileWithCards() {
    this.openPile.addCardToPile(new Card(Value.ACE, Suit.DIAMOND));

    assertEquals("A♦\n", this.openPile.toString());
  }

  // tests toString on a foundation pile with cards
  @Test
  public void testToStringFoundationPileWithCards() {
    this.foundationPile.addCardToPile(new Card(Value.ACE, Suit.DIAMOND));

    assertEquals("A♦\n", this.foundationPile.toString());
  }
}