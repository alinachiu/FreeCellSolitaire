import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.FreecellModelCreator;
import cs3500.freecell.model.FreecellModelCreator.GameType;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the move method in the {@code MultiMoveFreecellModel} class to ensure that the
 * behavior of singular and multi moves is correct.
 */
public class MultiMoveFreecellModelMoveTest {

  FreecellModel<ICard> model;
  List<ICard> reverseDeck;
  List<ICard> multiMoveDeck;
  List<ICard> altMultiMoveDeck;

  @Before
  public void initData() {
    this.model = FreecellModelCreator.create(GameType.MULTIMOVE);
    this.reverseDeck = this.model.getDeck();
    Collections.reverse(this.reverseDeck);

    this.multiMoveDeck = this.model.getDeck();
    ICard switch1 = multiMoveDeck.get(0);
    ICard switch2 = multiMoveDeck.get(1);
    ICard switch3 = multiMoveDeck.get(8);
    ICard switch4 = multiMoveDeck.get(9);
    this.multiMoveDeck.set(1, switch1);
    this.multiMoveDeck.set(0, switch2);
    this.multiMoveDeck.set(9, switch3);
    this.multiMoveDeck.set(8, switch4);
    Collections.reverse(this.multiMoveDeck);

    this.altMultiMoveDeck = this.model.getDeck();
    ICard card1 = altMultiMoveDeck.get(51);
    ICard card2 = altMultiMoveDeck.get(50);
    this.altMultiMoveDeck.set(50, card1);
    this.altMultiMoveDeck.set(51, card2);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveBeforeGameStarted() {
    this.model.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void attemptMultiMoveFromCascadeToOpenPile() {
    this.model.startGame(this.model.getDeck(), 4, 1, false);
    this.model.move(PileType.CASCADE, 0, 3, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void attemptMultiMoveFromCascadeToFoundationPile() {
    this.model.startGame(this.model.getDeck(), 4, 1, false);
    this.model.move(PileType.CASCADE, 0, 3, PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void attemptMultiMoveFromFoundationToCascadePile() {
    this.model.startGame(this.model.getDeck(), 4, 1, false);
    this.model.move(PileType.FOUNDATION, 0, 3, PileType.CASCADE, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void attemptMultiMoveFromFoundationToOpenPile() {
    this.model.startGame(this.reverseDeck, 4, 2, false);
    this.model.move(PileType.CASCADE, 0, 13, PileType.FOUNDATION, 0);
    this.model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    this.model.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void attemptMultiMoveFromFoundationToFoundationPile() {
    this.model.startGame(this.reverseDeck, 4, 1, false);
    this.model.move(PileType.CASCADE, 0, 13, PileType.FOUNDATION, 0);
    this.model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    this.model.move(PileType.FOUNDATION, 0, 0, PileType.FOUNDATION, 1);
  }

  // has zero empty open piles and zero empty cascade piles
  @Test(expected = IllegalArgumentException.class)
  public void attemptToMoveThreeCardsFromCascadeToCascadeButMaxCardsMovedIsOneShouldFail() {
    this.model.startGame(this.multiMoveDeck, 4, 3, false);

    // move three cards to open piles to make room for multi move
    this.model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    this.model.move(PileType.CASCADE, 0, 11, PileType.OPEN, 1);
    this.model.move(PileType.CASCADE, 0, 10, PileType.OPEN, 2);

    // attempt multi move
    this.model.move(PileType.CASCADE, 3, 10, PileType.CASCADE, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void attemptMultiMoveFromEmptyCascadePile() {
    this.model.startGame(this.model.getDeck(), 52, 2, false);

    this.model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    this.model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 1);
  }

  // try to move an ace of diamonds to a heart pile
  @Test(expected = IllegalArgumentException.class)
  public void multiMoveButCardsThatAreBeingMovedAreTheWrongColorsShouldFail() {
    this.model.startGame(this.multiMoveDeck, 4, 10, false);

    this.model.move(PileType.CASCADE, 1, 12, PileType.OPEN, 0);
    this.model.move(PileType.CASCADE, 1, 11, PileType.OPEN, 1);

    // try to move 2♦, A♣ to the pile C2: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥
    // shouldn't be possible because, although in descending order, hearts and diamonds are both red
    this.model.move(PileType.CASCADE, 2, 11, PileType.CASCADE, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void multiMoveButCardsThatAreBeingMovedAreNotInDescendingOrderShouldFail() {
    this.model.startGame(this.altMultiMoveDeck, 4, 5, false);

    this.model.move(PileType.CASCADE, 1, 12, PileType.OPEN, 0);
    this.model.move(PileType.CASCADE, 1, 11, PileType.OPEN, 1);

    // try to move  Q♠, K♥ to the pile C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦.
    // shouldn't be possible because the cards are in ascending instead of descending order even
    // though they are in the correct order color-wise

    this.model.move(PileType.CASCADE, 3, 11, PileType.CASCADE, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void completelyWrongMultiMoveAttemptMoveOnePileToAnotherShouldFail() {
    this.model.startGame(this.altMultiMoveDeck, 4, 5, false);

    this.model.move(PileType.CASCADE, 1, 0, PileType.CASCADE, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void completelyWrongMultiMoveAttemptMoveHalfOnePileToAnotherTonsOfOpenPilesShouldFail() {
    this.model.startGame(this.altMultiMoveDeck, 4, 30, false);

    this.model.move(PileType.CASCADE, 1, 6, PileType.CASCADE, 0);
  }

  // move two cards in correct order value and color wise (two cards being added are black and red
  // to a red card on top in descending order)
  @Test
  public void testValidMoveTwoCardsFromOneCascadePileToAnotherShouldWork() {
    this.model.startGame(this.multiMoveDeck, 4, 6, false);

    assertEquals(13, this.model.getNumCardsInCascadePile(1));
    assertEquals(13, this.model.getNumCardsInCascadePile(3));
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
        + "C4: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♦, 2♣, A♦", this.model.toString());

    // move three cards to open piles to make room for multi move
    this.model.move(PileType.CASCADE, 1, 12, PileType.OPEN, 0);
    this.model.move(PileType.CASCADE, 1, 11, PileType.OPEN, 1);

    assertEquals(11, this.model.getNumCardsInCascadePile(1));
    assertEquals(13, this.model.getNumCardsInCascadePile(3));

    // attempt multi move
    this.model.move(PileType.CASCADE, 3, 11, PileType.CASCADE, 1);
    assertEquals(13, this.model.getNumCardsInCascadePile(1));
    assertEquals(11, this.model.getNumCardsInCascadePile(3));
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1: A♥\n"
        + "O2: 2♥\n"
        + "O3:\n"
        + "O4:\n"
        + "O5:\n"
        + "O6:\n"
        + "C1: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
        + "C2: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♣, A♦\n"
        + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♣, 2♦, A♣\n"
        + "C4: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♦", this.model.toString());
  }

  // move two cards in correct order value and color wise (three cards being added are red black
  // and red to a black card on top in descending order)
  @Test
  public void testValidMoveThreeCardsFromOneCascadePileToAnotherShouldWork() {
    this.model.startGame(this.multiMoveDeck, 4, 6, false);

    assertEquals(13, this.model.getNumCardsInCascadePile(0));
    assertEquals(13, this.model.getNumCardsInCascadePile(3));
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
        + "C4: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♦, 2♣, A♦", this.model.toString());

    // move three cards to open piles to make room for multi move
    this.model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    this.model.move(PileType.CASCADE, 0, 11, PileType.OPEN, 1);
    this.model.move(PileType.CASCADE, 0, 10, PileType.OPEN, 2);

    assertEquals(10, this.model.getNumCardsInCascadePile(0));
    assertEquals(13, this.model.getNumCardsInCascadePile(3));

    // attempt multi move
    this.model.move(PileType.CASCADE, 3, 10, PileType.CASCADE, 0);
    assertEquals(13, this.model.getNumCardsInCascadePile(1));
    assertEquals(10, this.model.getNumCardsInCascadePile(3));
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1: A♠\n"
        + "O2: 2♠\n"
        + "O3: 3♠\n"
        + "O4:\n"
        + "O5:\n"
        + "O6:\n"
        + "C1: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♦, 2♣, A♦\n"
        + "C2: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n"
        + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♣, 2♦, A♣\n"
        + "C4: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣", this.model.toString());
  }

  @Test
  public void multiMoveCardsWith2EmptyCascadesAndNoEmptyOpensShouldWork() {
    this.model.startGame(this.model.getDeck(), 52, 2, false);

    assertEquals(1, this.model.getNumCardsInCascadePile(0));
    assertEquals(1, this.model.getNumCardsInCascadePile(1));
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "C1: A♣\n"
        + "C2: A♦\n"
        + "C3: A♥\n"
        + "C4: A♠\n"
        + "C5: 2♣\n"
        + "C6: 2♦\n"
        + "C7: 2♥\n"
        + "C8: 2♠\n"
        + "C9: 3♣\n"
        + "C10: 3♦\n"
        + "C11: 3♥\n"
        + "C12: 3♠\n"
        + "C13: 4♣\n"
        + "C14: 4♦\n"
        + "C15: 4♥\n"
        + "C16: 4♠\n"
        + "C17: 5♣\n"
        + "C18: 5♦\n"
        + "C19: 5♥\n"
        + "C20: 5♠\n"
        + "C21: 6♣\n"
        + "C22: 6♦\n"
        + "C23: 6♥\n"
        + "C24: 6♠\n"
        + "C25: 7♣\n"
        + "C26: 7♦\n"
        + "C27: 7♥\n"
        + "C28: 7♠\n"
        + "C29: 8♣\n"
        + "C30: 8♦\n"
        + "C31: 8♥\n"
        + "C32: 8♠\n"
        + "C33: 9♣\n"
        + "C34: 9♦\n"
        + "C35: 9♥\n"
        + "C36: 9♠\n"
        + "C37: 10♣\n"
        + "C38: 10♦\n"
        + "C39: 10♥\n"
        + "C40: 10♠\n"
        + "C41: J♣\n"
        + "C42: J♦\n"
        + "C43: J♥\n"
        + "C44: J♠\n"
        + "C45: Q♣\n"
        + "C46: Q♦\n"
        + "C47: Q♥\n"
        + "C48: Q♠\n"
        + "C49: K♣\n"
        + "C50: K♦\n"
        + "C51: K♥\n"
        + "C52: K♠", this.model.toString());

    this.model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    this.model.move(PileType.CASCADE, 1, 0, PileType.OPEN, 1);

    assertEquals(0, this.model.getNumCardsInCascadePile(0));
    assertEquals(0, this.model.getNumCardsInCascadePile(1));
    assertEquals(1, this.model.getNumCardsInOpenPile(0));
    assertEquals(1, this.model.getNumCardsInOpenPile(1));

    this.model.move(PileType.CASCADE, 4, 0, PileType.CASCADE, 2);
    assertEquals(2, this.model.getNumCardsInCascadePile(2));
    assertEquals(0, this.model.getNumCardsInCascadePile(4));
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1: A♣\n"
        + "O2: A♦\n"
        + "C1:\n"
        + "C2:\n"
        + "C3: A♥, 2♣\n"
        + "C4: A♠\n"
        + "C5:\n"
        + "C6: 2♦\n"
        + "C7: 2♥\n"
        + "C8: 2♠\n"
        + "C9: 3♣\n"
        + "C10: 3♦\n"
        + "C11: 3♥\n"
        + "C12: 3♠\n"
        + "C13: 4♣\n"
        + "C14: 4♦\n"
        + "C15: 4♥\n"
        + "C16: 4♠\n"
        + "C17: 5♣\n"
        + "C18: 5♦\n"
        + "C19: 5♥\n"
        + "C20: 5♠\n"
        + "C21: 6♣\n"
        + "C22: 6♦\n"
        + "C23: 6♥\n"
        + "C24: 6♠\n"
        + "C25: 7♣\n"
        + "C26: 7♦\n"
        + "C27: 7♥\n"
        + "C28: 7♠\n"
        + "C29: 8♣\n"
        + "C30: 8♦\n"
        + "C31: 8♥\n"
        + "C32: 8♠\n"
        + "C33: 9♣\n"
        + "C34: 9♦\n"
        + "C35: 9♥\n"
        + "C36: 9♠\n"
        + "C37: 10♣\n"
        + "C38: 10♦\n"
        + "C39: 10♥\n"
        + "C40: 10♠\n"
        + "C41: J♣\n"
        + "C42: J♦\n"
        + "C43: J♥\n"
        + "C44: J♠\n"
        + "C45: Q♣\n"
        + "C46: Q♦\n"
        + "C47: Q♥\n"
        + "C48: Q♠\n"
        + "C49: K♣\n"
        + "C50: K♦\n"
        + "C51: K♥\n"
        + "C52: K♠", this.model.toString());
  }

  @Test
  public void multiMoveCardsWith2EmptyCascadesAnd1EmptyOpenShouldWork() {
    this.model.startGame(this.model.getDeck(), 52, 3, false);

    assertEquals(1, this.model.getNumCardsInCascadePile(0));
    assertEquals(1, this.model.getNumCardsInCascadePile(1));
    assertEquals(0, this.model.getNumCardsInOpenPile(0));
    assertEquals(0, this.model.getNumCardsInOpenPile(1));
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "C1: A♣\n"
        + "C2: A♦\n"
        + "C3: A♥\n"
        + "C4: A♠\n"
        + "C5: 2♣\n"
        + "C6: 2♦\n"
        + "C7: 2♥\n"
        + "C8: 2♠\n"
        + "C9: 3♣\n"
        + "C10: 3♦\n"
        + "C11: 3♥\n"
        + "C12: 3♠\n"
        + "C13: 4♣\n"
        + "C14: 4♦\n"
        + "C15: 4♥\n"
        + "C16: 4♠\n"
        + "C17: 5♣\n"
        + "C18: 5♦\n"
        + "C19: 5♥\n"
        + "C20: 5♠\n"
        + "C21: 6♣\n"
        + "C22: 6♦\n"
        + "C23: 6♥\n"
        + "C24: 6♠\n"
        + "C25: 7♣\n"
        + "C26: 7♦\n"
        + "C27: 7♥\n"
        + "C28: 7♠\n"
        + "C29: 8♣\n"
        + "C30: 8♦\n"
        + "C31: 8♥\n"
        + "C32: 8♠\n"
        + "C33: 9♣\n"
        + "C34: 9♦\n"
        + "C35: 9♥\n"
        + "C36: 9♠\n"
        + "C37: 10♣\n"
        + "C38: 10♦\n"
        + "C39: 10♥\n"
        + "C40: 10♠\n"
        + "C41: J♣\n"
        + "C42: J♦\n"
        + "C43: J♥\n"
        + "C44: J♠\n"
        + "C45: Q♣\n"
        + "C46: Q♦\n"
        + "C47: Q♥\n"
        + "C48: Q♠\n"
        + "C49: K♣\n"
        + "C50: K♦\n"
        + "C51: K♥\n"
        + "C52: K♠", this.model.toString());

    this.model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    this.model.move(PileType.CASCADE, 1, 0, PileType.OPEN, 1);

    assertEquals(0, this.model.getNumCardsInCascadePile(1));
    assertEquals(0, this.model.getNumCardsInCascadePile(1));
    assertEquals(1, this.model.getNumCardsInOpenPile(0));
    assertEquals(1, this.model.getNumCardsInOpenPile(1));

    this.model.move(PileType.CASCADE, 4, 0, PileType.CASCADE, 2);

    assertEquals(2, this.model.getNumCardsInCascadePile(2));
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1: A♣\n"
        + "O2: A♦\n"
        + "O3:\n"
        + "C1:\n"
        + "C2:\n"
        + "C3: A♥, 2♣\n"
        + "C4: A♠\n"
        + "C5:\n"
        + "C6: 2♦\n"
        + "C7: 2♥\n"
        + "C8: 2♠\n"
        + "C9: 3♣\n"
        + "C10: 3♦\n"
        + "C11: 3♥\n"
        + "C12: 3♠\n"
        + "C13: 4♣\n"
        + "C14: 4♦\n"
        + "C15: 4♥\n"
        + "C16: 4♠\n"
        + "C17: 5♣\n"
        + "C18: 5♦\n"
        + "C19: 5♥\n"
        + "C20: 5♠\n"
        + "C21: 6♣\n"
        + "C22: 6♦\n"
        + "C23: 6♥\n"
        + "C24: 6♠\n"
        + "C25: 7♣\n"
        + "C26: 7♦\n"
        + "C27: 7♥\n"
        + "C28: 7♠\n"
        + "C29: 8♣\n"
        + "C30: 8♦\n"
        + "C31: 8♥\n"
        + "C32: 8♠\n"
        + "C33: 9♣\n"
        + "C34: 9♦\n"
        + "C35: 9♥\n"
        + "C36: 9♠\n"
        + "C37: 10♣\n"
        + "C38: 10♦\n"
        + "C39: 10♥\n"
        + "C40: 10♠\n"
        + "C41: J♣\n"
        + "C42: J♦\n"
        + "C43: J♥\n"
        + "C44: J♠\n"
        + "C45: Q♣\n"
        + "C46: Q♦\n"
        + "C47: Q♥\n"
        + "C48: Q♠\n"
        + "C49: K♣\n"
        + "C50: K♦\n"
        + "C51: K♥\n"
        + "C52: K♠", this.model.toString());
  }
}
