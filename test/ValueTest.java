import cs3500.freecell.model.hw02.Value;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the enum, Value: unit tests to ensure that values can be created and behave
 * correctly.
 */
public class ValueTest {

  Value ace = Value.ACE;
  Value two = Value.TWO;
  Value jack = Value.JACK;
  Value queen = Value.QUEEN;
  Value king = Value.KING;

  // tests isGreaterThanBy, negative difference
  @Test
  public void testIsGreaterThanByNegative() {
    assertEquals(false, this.ace.isGreaterThanBy(-1, this.king));
  }

  // tests isGreaterThanBy, zero difference
  @Test
  public void testIsGreaterThanByZero() {
    assertEquals(true, this.queen.isGreaterThanBy(0, this.queen));
  }

  // tests isGreaterThanBy, positive difference
  @Test
  public void testIsGreaterThanByPositive() {
    assertEquals(false, this.ace.isGreaterThanBy(1, this.jack));
  }

  // tests isGreaterThanBy, null value exception
  @Test(expected = IllegalArgumentException.class)
  public void testIsGreaterThanByNull() {
    this.ace.isGreaterThanBy(123, null);
  }

  // tests toString for ace
  @Test
  public void testToStringAce() {
    assertEquals("A", this.ace.toString());
  }

  // tests toString for two
  @Test
  public void testToStringTwo() {
    assertEquals("2", this.two.toString());
  }

  // tests toString for jack
  @Test
  public void testToStringJack() {
    assertEquals("J", this.jack.toString());
  }

  // tests toString for queen
  @Test
  public void testToStringQueen() {
    assertEquals("Q", this.queen.toString());
  }

  // tests toString for king
  @Test
  public void testToStringKing() {
    assertEquals("K", this.king.toString());
  }

}
