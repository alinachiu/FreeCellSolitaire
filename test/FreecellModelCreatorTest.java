import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.FreecellModelCreator.GameType;
import cs3500.freecell.model.FreecellModelCreator;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the method create in the class {@code FreecellModelCreator} to make sure that the
 * proper objects are returned.
 */
public class FreecellModelCreatorTest {

  // tests to make sure that create creates a single move object that can move one card at a time
  @Test
  public void testCreateSingleMoveValidMove() {
    FreecellModel<ICard> singleMove = FreecellModelCreator.create(GameType.SINGLEMOVE);

    singleMove.startGame(singleMove.getDeck(), 8, 7, false);
    singleMove.move(PileType.CASCADE, 0, 6, PileType.OPEN, 0);

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1: K♣\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "O5:\n"
        + "O6:\n"
        + "O7:\n"
        + "C1: A♣, 3♣, 5♣, 7♣, 9♣, J♣\n"
        + "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n"
        + "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n"
        + "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠\n"
        + "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n"
        + "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n"
        + "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n"
        + "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠", singleMove.toString());
  }

  // tests to make sure that create creates a single move object that can't move more than one
  // card at a time
  @Test(expected = IllegalArgumentException.class)
  public void testCreateSingleMoveCannotMoveMoreThanOneCard() {
    FreecellModel<ICard> singleMove = FreecellModelCreator.create(GameType.SINGLEMOVE);

    singleMove.startGame(singleMove.getDeck(), 4, 1, false);
    singleMove.move(PileType.CASCADE, 0, 11, PileType.OPEN, 0);
  }

  // checks to make sure that using the FreecellModelCreator static method create for a MultiMove
  // by making sure that moving multiple cards works and doesn't throw an error
  @Test
  public void testCreateMultiMoveCorrect() {
    FreecellModel<ICard> multiMove = FreecellModelCreator.create(GameType.MULTIMOVE);

    List<ICard> multiMoveDeck = multiMove.getDeck();
    ICard switch1 = multiMoveDeck.get(0);
    ICard switch2 = multiMoveDeck.get(1);
    ICard switch3 = multiMoveDeck.get(8);
    ICard switch4 = multiMoveDeck.get(9);
    multiMoveDeck.set(1, switch1);
    multiMoveDeck.set(0, switch2);
    multiMoveDeck.set(9, switch3);
    multiMoveDeck.set(8, switch4);
    Collections.reverse(multiMoveDeck);

    multiMove.startGame(multiMoveDeck, 4, 6, false);

    assertEquals(13, multiMove.getNumCardsInCascadePile(1));
    assertEquals(13, multiMove.getNumCardsInCascadePile(3));
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "O5:\n"
        + "O6:\n"
        + "C1: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
        + "C2: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n"
        + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♣, 2♦, A♣\n"
        + "C4: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♦, 2♣, A♦", multiMove.toString());

    // move three cards to open piles to make room for multi move
    multiMove.move(PileType.CASCADE, 1, 12, PileType.OPEN, 0);
    multiMove.move(PileType.CASCADE, 1, 11, PileType.OPEN, 1);

    assertEquals(11, multiMove.getNumCardsInCascadePile(1));
    assertEquals(13, multiMove.getNumCardsInCascadePile(3));

    // attempt multi move
    multiMove.move(PileType.CASCADE, 3, 11, PileType.CASCADE, 1);
  }

  // makes sure that creating a multi move object will throw an error if an incorrect move is made
  @Test(expected = IllegalArgumentException.class)
  public void testCreateMultiMoveError() {
    FreecellModel<ICard> multiMove = FreecellModelCreator.create(GameType.MULTIMOVE);

    multiMove.startGame(multiMove.getDeck(), 4, 30, false);
    multiMove.move(PileType.CASCADE, 1, 6, PileType.CASCADE, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateInvalidGameType() {
    FreecellModel<ICard> singleMove = FreecellModelCreator.create(null);
  }
}
