import cs3500.freecell.model.hw02.Suit;
import cs3500.freecell.model.hw02.Suit.CardColor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the enum, Suit: unit tests to ensure that suits can be created and behave
 * correctly.
 */
public class SuitTest {

  Suit club = Suit.CLUB;
  Suit diamond = Suit.DIAMOND;
  Suit heart = Suit.HEART;
  Suit spade = Suit.SPADE;
  Suit invalidSuit = null;

  // tests getColor on a club suit
  @Test
  public void testGetColorClub() {
    assertEquals(CardColor.BLACK, this.club.getColor());
  }

  // tests getColor on a diamond suit
  @Test
  public void testGetColorDiamond() {
    assertEquals(CardColor.RED, this.diamond.getColor());
  }

  // tests getColor on a heart suit
  @Test
  public void testGetColorHeart() {
    assertEquals(CardColor.RED, this.heart.getColor());
  }

  // tests getColor on a spade suit
  @Test
  public void testGetColorSpade() {
    assertEquals(CardColor.BLACK, this.spade.getColor());
  }

  // tests toString on a club suit
  @Test
  public void testToStringClub() {
    assertEquals("♣", this.club.toString());
  }

  // tests toString on a diamond suit
  @Test
  public void testToStringDiamond() {
    assertEquals("♦", this.diamond.toString());
  }

  // tests toString on a heart suit
  @Test
  public void testToStringHeart() {
    assertEquals("♥", this.heart.toString());
  }

  // tests toString on a spade suit
  @Test
  public void testToStringSpade() {
    assertEquals("♠", this.spade.toString());
  }
}
