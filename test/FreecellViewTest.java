import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for FreecellView: ensures that the class can be created and behave correctly.
 */
public class FreecellViewTest {

  FreecellModel<ICard> simpleFreecell;
  FreecellView view;

  @Before
  public void initData() {
    this.simpleFreecell = new SimpleFreecellModel();
    this.view = new FreecellTextView(simpleFreecell);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException() {
    FreecellView invalidView = new FreecellTextView(null);
  }

  @Test
  public void testToStringBeforeGameStart() {
    assertEquals("", this.view.toString());
  }

  @Test
  public void testToStringAfterGameStartAndMove() {
    this.simpleFreecell.startGame(this.simpleFreecell.getDeck(), 8, 7, false);
    this.simpleFreecell.move(PileType.CASCADE, 0, 6, PileType.OPEN, 0);
    this.simpleFreecell.move(PileType.CASCADE, 0, 5, PileType.OPEN, 1);
    this.simpleFreecell.move(PileType.CASCADE, 0, 4, PileType.OPEN, 2);
    this.simpleFreecell.move(PileType.CASCADE, 0, 3, PileType.OPEN, 3);
    this.simpleFreecell.move(PileType.CASCADE, 0, 2, PileType.OPEN, 4);
    this.simpleFreecell.move(PileType.CASCADE, 0, 1, PileType.OPEN, 5);
    this.simpleFreecell.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);

    assertEquals("F1: A♣\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1: K♣\n"
        + "O2: J♣\n"
        + "O3: 9♣\n"
        + "O4: 7♣\n"
        + "O5: 5♣\n"
        + "O6: 3♣\n"
        + "O7:\n"
        + "C1:\n"
        + "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n"
        + "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n"
        + "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠\n"
        + "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n"
        + "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n"
        + "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n"
        + "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠", this.view.toString());
  }

  @Test
  public void testToStringStartGameError() {
    try {
      this.simpleFreecell.startGame(this.simpleFreecell.getDeck(), 3, 1, false);
    } catch (IllegalArgumentException e) {
      assertEquals("", this.view.toString());
    }
  }
}
