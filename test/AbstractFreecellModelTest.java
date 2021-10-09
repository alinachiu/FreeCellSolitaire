import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.FreecellModelCreator;
import cs3500.freecell.model.FreecellModelCreator.GameType;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.Suit;
import cs3500.freecell.model.hw02.Value;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

// NOTE: the SimpleFreecellModelTest class was replaced by the AbstractFreecellModelTest to test
// the basic features involved in both the SimpleFreecellModel and the MultiMoveFreecellModel
// (i.e.getDeck, startGame, etc.) to avoid repeated code

/**
 * Test class for methods in the {@link FreecellModel} class: unit tests to ensure that both the
 * SimpleFreecellModel and MultiMoveFreecellModel can be created and behave correctly for single
 * moves and all other methods.
 */
public abstract class AbstractFreecellModelTest {

  FreecellModel<ICard> freecellModel;
  FreecellModel<ICard> anotherFreecellModel;
  List<ICard> validDeck;
  List<ICard> emptyDeck;
  List<ICard> deckWithNullValue;
  List<ICard> deckWithDuplicates;
  List<ICard> deckInvalidSuitsNums;
  List<Value> listOfValues;
  List<Suit> listOfSuits;

  @Before
  public void initData() {
    this.freecellModel = this.makeModel();
    this.anotherFreecellModel = this.makeModel();
    this.validDeck = this.freecellModel.getDeck();
    this.emptyDeck = new ArrayList<>();
    this.deckWithDuplicates = this.freecellModel.getDeck();
    ICard dupCard = this.deckWithDuplicates.get(0);
    this.deckWithDuplicates.set(1, dupCard);
    this.deckWithNullValue = this.freecellModel.getDeck();
    this.deckWithNullValue.set(0, null);
    this.deckInvalidSuitsNums = this.freecellModel.getDeck();

    this.listOfValues = Arrays.asList(Value.values());
    this.listOfSuits = Arrays.asList(Suit.values());
  }

  // tests the method getDeck to make sure that the generated deck of cards has the correct length
  @Test
  public void testGetDeckLength() {
    assertEquals(52, this.freecellModel.getDeck().size());
  }

  // tests the method getDeck to make sure that it does not have any duplicate cards
  @Test
  public void testGetDeckDuplicates() {
    List<ICard> deck = this.freecellModel.getDeck();
    Set<ICard> setOfCards = new HashSet<>(deck);

    assertTrue(deck.size() == setOfCards.size());
  }

  // tests the method getDeck to make sure that it has no invalid suits or numbers
  @Test
  public void testGetDeckInvalidSuitsOrNums() {
    List<ICard> deck = this.freecellModel.getDeck();

    for (int i = 0; i < deck.size(); i = i + 1) {
      assertTrue(this.listOfValues.contains(deck.get(i).getValue()));
      assertTrue(this.listOfSuits.contains(deck.get(i).getSuit()));
    }
  }

  // tests startGame numbers of piles and cards in piles before and after
  @Test
  public void testStartGameNums() {
    assertEquals(-1, this.freecellModel.getNumOpenPiles());
    assertEquals(-1, this.freecellModel.getNumCascadePiles());

    this.freecellModel.startGame(this.validDeck, 4, 1, true);

    assertEquals(1, this.freecellModel.getNumOpenPiles());
    assertEquals(4, this.freecellModel.getNumCascadePiles());
    assertEquals(0, this.freecellModel.getNumCardsInOpenPile(0));
    assertEquals(13, this.freecellModel.getNumCardsInCascadePile(0));
    assertEquals(13, this.freecellModel.getNumCardsInCascadePile(1));
    assertEquals(13, this.freecellModel.getNumCardsInCascadePile(2));
    assertEquals(13, this.freecellModel.getNumCardsInCascadePile(3));
  }

  // tests startGame with too few numbers of cascade piles
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameTooFewCascadePiles() {
    this.freecellModel.startGame(this.validDeck, 1, 3, true);
  }

  // tests startGame with too few numbers of open piles
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameTooFewOpenPiles() {
    this.freecellModel.startGame(this.validDeck, 4, 0, false);
  }

  // tests startGame with an invalid deck input (doesn't have 52 cards)
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidDeck() {
    this.freecellModel.startGame(this.emptyDeck, 4, 1, false);
  }

  // tests startGame with an invalid deck input (null deck)
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNullDeck() {
    this.freecellModel.startGame(null, 4, 1, false);
  }

  // tests startGame with an invalid deck input (deck that contains null)
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameDeckWithNullCard() {
    this.freecellModel.startGame(this.deckWithNullValue, 5, 2, true);
  }

  // tests startGame with an invalid deck input (deck that has duplicates)
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameDeckWithDuplicate() {
    this.freecellModel.startGame(this.deckWithDuplicates, 6, 3, false);
  }

  // tests startGame correctly shuffled, checks number of cards in each pile and compares unshuffled
  // to shuffled deck with same parameters
  @Test
  public void testStartGameCorrectlyShuffled() {
    this.freecellModel.startGame(this.validDeck, 4, 2, true);
    this.anotherFreecellModel
        .startGame(this.anotherFreecellModel.getDeck(), 4, 2, false);

    assertEquals(13, this.freecellModel.getNumCardsInCascadePile(0));
    assertEquals(13, this.freecellModel.getNumCardsInCascadePile(1));
    assertEquals(13, this.freecellModel.getNumCardsInCascadePile(2));
    assertEquals(13, this.freecellModel.getNumCardsInCascadePile(3));
    assertEquals(4, this.freecellModel.getNumCascadePiles());

    assertEquals(13, this.anotherFreecellModel.getNumCardsInCascadePile(0));
    assertEquals(13, this.anotherFreecellModel.getNumCardsInCascadePile(1));
    assertEquals(13, this.anotherFreecellModel.getNumCardsInCascadePile(2));
    assertEquals(13, this.anotherFreecellModel.getNumCardsInCascadePile(3));
    assertEquals(4, this.anotherFreecellModel.getNumCascadePiles());

    assertNotEquals(this.freecellModel.toString(),
        this.anotherFreecellModel.toString());
  }

  // tests startGame by calling it twice to make sure everything resets correctly
  @Test
  public void testStartGameCallTwice() {
    this.freecellModel.startGame(this.validDeck, 5,
        2, false);

    assertEquals(11, this.freecellModel.getNumCardsInCascadePile(0));
    assertEquals(11, this.freecellModel.getNumCardsInCascadePile(1));
    assertEquals(10, this.freecellModel.getNumCardsInCascadePile(2));
    assertEquals(10, this.freecellModel.getNumCardsInCascadePile(3));
    assertEquals(10, this.freecellModel.getNumCardsInCascadePile(4));

    assertEquals(this.freecellModel.getDeck().get(1),
        this.freecellModel.getCascadeCardAt(1, 0));
    assertEquals(this.freecellModel.getDeck().get(0),
        this.freecellModel.getCascadeCardAt(0, 0));
    assertEquals(this.freecellModel.getDeck().get(9),
        this.freecellModel.getCascadeCardAt(4, 1));

    this.freecellModel.startGame(this.validDeck, 4,
        2, false);

    assertEquals(13, this.freecellModel.getNumCardsInCascadePile(0));
    assertEquals(13, this.freecellModel.getNumCardsInCascadePile(1));
    assertEquals(13, this.freecellModel.getNumCardsInCascadePile(2));
    assertEquals(13, this.freecellModel.getNumCardsInCascadePile(3));

    assertEquals(this.freecellModel.getDeck().get(1),
        this.freecellModel.getCascadeCardAt(1, 0));
    assertEquals(this.freecellModel.getDeck().get(0),
        this.freecellModel.getCascadeCardAt(0, 0));
  }

  // tests startGame correctly no shuffle
  @Test
  public void testStartGameNoShuffle() {
    this.freecellModel.startGame(this.validDeck, 5,
        2, false);

    assertEquals(11, this.freecellModel.getNumCardsInCascadePile(0));
    assertEquals(11, this.freecellModel.getNumCardsInCascadePile(1));
    assertEquals(10, this.freecellModel.getNumCardsInCascadePile(2));
    assertEquals(10, this.freecellModel.getNumCardsInCascadePile(3));
    assertEquals(10, this.freecellModel.getNumCardsInCascadePile(4));

    // check to make sure shuffled deck is the same as unshuffled deck using getDeck
    assertEquals(this.freecellModel.getDeck().get(1),
        this.freecellModel.getCascadeCardAt(1, 0));
    assertEquals(this.freecellModel.getDeck().get(0),
        this.freecellModel.getCascadeCardAt(0, 0));
    assertEquals(this.freecellModel.getDeck().get(9),
        this.freecellModel.getCascadeCardAt(4, 1));
  }

  // tests move when game hasn't started
  @Test(expected = IllegalStateException.class)
  public void testMoveGameNotStarted() {
    this.freecellModel.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 0);
  }

  // tests move when source is null
  @Test(expected = IllegalArgumentException.class)
  public void testMoveGameSourceNull() {
    this.freecellModel.startGame(this.validDeck, 4, 1, false);
    this.freecellModel.move(null, 0, 0, PileType.CASCADE, 0);
  }

  // tests move when destination is null
  @Test(expected = IllegalArgumentException.class)
  public void testMoveGameDestinationNull() {
    this.freecellModel.startGame(this.validDeck, 4, 1, false);
    this.freecellModel.move(PileType.CASCADE, 0, 0, null, 0);
  }

  // tests move when pileNumber is less than 0
  @Test(expected = IllegalArgumentException.class)
  public void testMovePileNumberLessThan0() {
    this.freecellModel.startGame(this.validDeck, 4, 1, false);
    this.freecellModel.move(PileType.CASCADE, -1, 0, PileType.OPEN, 0);
  }

  // tests move when cardIndex is less than 0
  @Test(expected = IllegalArgumentException.class)
  public void testMoveCardIndexLessThan0() {
    this.freecellModel.startGame(this.validDeck, 4, 1, false);
    this.freecellModel.move(PileType.CASCADE, 0, -1, PileType.OPEN, 0);
  }

  // tests move when destPileNumber is less than 0
  @Test(expected = IllegalArgumentException.class)
  public void testMoveDestPileNumberLessThan0() {
    this.freecellModel.startGame(this.validDeck, 4, 1, false);
    this.freecellModel.move(PileType.CASCADE, 0, 0, PileType.OPEN, -1);
  }

  // tests move when source type is cascade and card index isn't the last card or pileNumber
  // is greater than the number of cascade piles
  @Test(expected = IllegalArgumentException.class)
  public void testMoveCascadeSourceExceptions() {
    this.freecellModel.startGame(this.validDeck, 4, 2, false);
    this.freecellModel.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    this.freecellModel.move(PileType.CASCADE, 12, 0, PileType.OPEN, 1);
  }

  // tests move when source type is foundation for any possible exceptions
  @Test(expected = IllegalArgumentException.class)
  public void testMoveFoundationSourceException() {
    this.freecellModel.startGame(this.validDeck, 5, 2, true);
    this.freecellModel.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 1);
  }

  // tests move when source type is open for any possible exceptions
  @Test(expected = IllegalArgumentException.class)
  public void testMoveOpenSourceException() {
    this.freecellModel.startGame(this.validDeck, 5, 2, true);
    this.freecellModel.move(PileType.OPEN, 0, 1, PileType.OPEN, 1);
    this.freecellModel.move(PileType.OPEN, 4, 0, PileType.OPEN, 1);
  }

  // test move when source type is null to get default case
  @Test(expected = IllegalArgumentException.class)
  public void testMoveSourceNull() {
    this.freecellModel.startGame(this.validDeck, 5, 2, true);
    this.freecellModel.move(null, 0, 0, PileType.OPEN, 1);
  }

  // test move when destination type is cascade exceptions
  @Test(expected = IllegalArgumentException.class)
  public void testMoveDestinationCascadeDestPileNumberLarge() {
    this.freecellModel.startGame(this.validDeck, 5, 2, false);
    this.freecellModel.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 10);
    // for adding a card with same color as the current pile
    this.freecellModel.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 2);
  }

  // test move when destination type is foundation for any exceptions
  @Test(expected = IllegalArgumentException.class)
  public void testMoveDestinationFoundationException() {
    this.freecellModel.startGame(this.validDeck, 4, 2, false);
    this.freecellModel.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 10);
    // for adding a card with same color as the current pile
    this.freecellModel.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 2);
  }

  // test move when destination type is open for any exceptions
  @Test(expected = IllegalArgumentException.class)
  public void testMoveDestinationOpenException() {
    this.freecellModel.startGame(this.validDeck, 4, 2, false);
    this.freecellModel.move(PileType.CASCADE, 0, 0, PileType.OPEN, 10);
  }

  // test move when destination type is null for any exceptions
  @Test(expected = IllegalArgumentException.class)
  public void testMoveDestinationNull() {
    this.freecellModel.startGame(this.validDeck, 5, 2, true);
    this.freecellModel.move(PileType.CASCADE, 0, 0, null, 1);
  }

  // test move into an open pile that already has something there
  @Test(expected = IllegalArgumentException.class)
  public void testMoveIntoAlreadyFullPile() {
    this.freecellModel.startGame(this.validDeck, 4, 2, false);

    this.freecellModel.move(PileType.CASCADE, 0, 12, PileType.OPEN, 1);
    this.freecellModel.move(PileType.CASCADE, 0, 11, PileType.OPEN, 1);
  }

  //  tests move a card with wrong color from open to cascade
  @Test(expected = IllegalArgumentException.class)
  public void testMoveCardWrongColorOpenCascade() {
    this.freecellModel.startGame(this.validDeck, 4, 1, false);

    this.freecellModel.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    this.freecellModel.move(PileType.OPEN, 3, 12, PileType.CASCADE, 0);
  }

  // tests move a card with wrong value from open to cascade
  @Test(expected = IllegalArgumentException.class)
  public void testMoveCardWrongValueOpenCascade() {
    this.freecellModel.startGame(this.validDeck, 4, 1, false);

    this.freecellModel.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    this.freecellModel.move(PileType.OPEN, 2, 12, PileType.CASCADE, 0);
  }

  @Test
  public void testMoveFromCascadeToEmptyOpen() {
    this.freecellModel.startGame(this.validDeck, 4, 1, false);

    assertEquals(0, this.freecellModel.getNumCardsInOpenPile(0));
    assertEquals(13, this.freecellModel.getNumCardsInCascadePile(0));
    this.freecellModel.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    assertEquals(1, this.freecellModel.getNumCardsInOpenPile(0));
    assertEquals(12, this.freecellModel.getNumCardsInCascadePile(0));
  }

  // tests getNumCardsInFoundationPile before starting game
  @Test(expected = IllegalStateException.class)
  public void testGetNumCardsInFoundationPileBeforeGame() {
    this.freecellModel.getNumCardsInFoundationPile(0);
  }

  // tests getNumCardsInFoundationPile index too low
  @Test(expected = IllegalArgumentException.class)
  public void testGetNumCardsInFoundationPileIndexTooLow() {
    this.freecellModel.startGame(this.validDeck, 4, 2, false);
    this.freecellModel.getNumCardsInFoundationPile(-1);
  }

  // tests getNumCardsInFoundationPile index too high
  @Test(expected = IllegalArgumentException.class)
  public void testGetNumCardsInFoundationPileIndexTooHigh() {
    this.freecellModel.startGame(this.validDeck, 4, 2, false);
    this.freecellModel.getNumCardsInFoundationPile(100);
  }

  // tests correct getNumCardsInFoundationPile
  @Test
  public void testGetNumCardsInFoundationPileCorrect() {
    this.freecellModel.startGame(this.validDeck, 4, 2, false);
    assertEquals(0, this.freecellModel.getNumCardsInFoundationPile(1));
  }

  // tests getNumCardsInCascadePile before starting game
  @Test(expected = IllegalStateException.class)
  public void testGetNumCardsInCascadePileBeforeGame() {
    this.freecellModel.getNumCardsInCascadePile(0);
  }

  // tests getNumCardsInCascadePile index too low
  @Test(expected = IllegalArgumentException.class)
  public void testGetNumCardsInCascadePileIndexTooLow() {
    this.freecellModel.startGame(this.validDeck, 4, 2, false);
    this.freecellModel.getNumCardsInCascadePile(-1);
  }

  // tests getNumCardsInCascadePile index too high
  @Test(expected = IllegalArgumentException.class)
  public void testGetNumCardsInCascadePileIndexTooHigh() {
    this.freecellModel.startGame(this.validDeck, 4, 2, false);
    this.freecellModel.getNumCardsInCascadePile(100);
  }

  // tests correct getNumCardsInCascadePile
  @Test
  public void testGetNumCardsInCascadePileCorrect() {
    this.freecellModel.startGame(this.validDeck, 4, 2, false);
    assertEquals(13, this.freecellModel.getNumCardsInCascadePile(1));
  }


  // tests getNumCardsInOpenPile before starting game
  @Test(expected = IllegalStateException.class)
  public void testGetNumCardsInOpenPileBeforeGame() {
    this.freecellModel.getNumCardsInOpenPile(0);
  }

  // tests getNumCardsInOpenPile index too low
  @Test(expected = IllegalArgumentException.class)
  public void testGetNumCardsInOpenPileIndexTooLow() {
    this.freecellModel.startGame(this.validDeck, 4, 2, false);
    this.freecellModel.getNumCardsInOpenPile(-1);
  }

  // tests getNumCardsInOpenPile index too high
  @Test(expected = IllegalArgumentException.class)
  public void testGetNumCardsInOpenPileIndexTooHigh() {
    this.freecellModel.startGame(this.validDeck, 4, 2, false);
    this.freecellModel.getNumCardsInOpenPile(100);
  }

  // tests correct getNumCardsInOpenPile
  @Test
  public void testGetNumCardsInOpenPileCorrect() {
    this.freecellModel.startGame(this.validDeck, 4, 2, false);
    assertEquals(0, this.freecellModel.getNumCardsInOpenPile(1));
  }

  // tests correct getNumCardsInOpenPile with move
  @Test
  public void testGetNumCardsInOpenPileCorrectWithMove() {
    this.freecellModel.startGame(this.validDeck, 4, 2, false);
    this.freecellModel.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);

    assertEquals(1, this.freecellModel.getNumCardsInOpenPile(0));
  }

  // test getNumCascadePiles before game has started
  @Test
  public void testGetNumCascadePilesBefore() {
    assertEquals(-1, this.freecellModel.getNumCascadePiles());
  }

  // test getNumCascadePiles after game has started
  @Test
  public void testGetNumCascadePilesAfter() {
    this.freecellModel.startGame(this.validDeck, 4, 2, true);
    assertEquals(4, this.freecellModel.getNumCascadePiles());
  }

  // test getNumOpenPiles before game has started
  @Test
  public void testGetNumOpenPilesBefore() {
    assertEquals(-1, this.freecellModel.getNumOpenPiles());
  }

  // test getNumOpenPiles after game has started
  @Test
  public void testGetNumOpenPilesAfter() {
    this.freecellModel.startGame(this.validDeck, 4, 2, true);
    assertEquals(2, this.freecellModel.getNumOpenPiles());
  }

  // test getFoundationCardAt before the game has started
  @Test(expected = IllegalStateException.class)
  public void testGetFoundationCardAtBeforeGame() {
    this.freecellModel.getFoundationCardAt(0, 0);
  }

  // test getFoundationCardAt where the pile index is greater than the number of foundation piles
  @Test(expected = IllegalArgumentException.class)
  public void testGetFoundationCardAtPileIndexTooBig() {
    this.freecellModel.startGame(this.validDeck, 4, 2, true);

    this.freecellModel.getFoundationCardAt(10, 11);
  }

  // test getFoundationCardAt where the pile index is smaller than the number of foundation piles
  @Test(expected = IllegalArgumentException.class)
  public void testGetFoundationCardAtPileIndexTooSmall() {
    this.freecellModel.startGame(this.validDeck, 4, 2, true);

    this.freecellModel.getFoundationCardAt(-1, 11);
  }

  // test getFoundationCardAt where the card index is too big
  @Test(expected = IllegalArgumentException.class)
  public void testGetFoundationCardAtCardIndexTooBig() {
    this.freecellModel.startGame(this.validDeck, 4, 2, true);

    this.freecellModel.getFoundationCardAt(1, 1);
  }


  // test getFoundationCardAt where the card index is too small
  @Test(expected = IllegalArgumentException.class)
  public void testGetFoundationCardAtCardIndexTooSmall() {
    this.freecellModel.startGame(this.validDeck, 4, 2, true);

    this.freecellModel.getFoundationCardAt(1, -1);
  }

  // test getCascadeCardAt before the game has started
  @Test(expected = IllegalStateException.class)
  public void testGetCascadeCardAtBeforeGame() {
    this.freecellModel.getCascadeCardAt(0, 0);
  }

  // test getCascadeCardAt where the pile index is greater than the number of foundation piles
  @Test(expected = IllegalArgumentException.class)
  public void testGetCascadeCardAtPileIndexTooBig() {
    this.freecellModel.startGame(this.validDeck, 4, 2, true);

    this.freecellModel.getCascadeCardAt(10, 11);
  }

  // test getCascadeCardAt where the pile index is smaller than the number of foundation piles
  @Test(expected = IllegalArgumentException.class)
  public void testGetCascadeCardAtPileIndexTooSmall() {
    this.freecellModel.startGame(this.validDeck, 4, 2, true);

    this.freecellModel.getCascadeCardAt(-1, 11);
  }

  // test getCascadeCardAt where the card index is too big
  @Test(expected = IllegalArgumentException.class)
  public void testGetCascadeCardAtCardIndexTooBig() {
    this.freecellModel.startGame(this.validDeck, 4, 2, true);

    this.freecellModel.getCascadeCardAt(1, 13);
  }


  // test getCascadeCardAt where the card index is too small
  @Test(expected = IllegalArgumentException.class)
  public void testGetCascadeCardAtCardIndexTooSmall() {
    this.freecellModel.startGame(this.validDeck, 4, 2, true);

    this.freecellModel.getCascadeCardAt(1, -1);
  }


  // test getOpenCardAt before the game has started
  @Test(expected = IllegalStateException.class)
  public void testGetOpenCardAtBeforeGame() {
    this.freecellModel.getOpenCardAt(0);
  }

  // test getOpenCardAt where the pile index is greater than the number of foundation piles
  @Test(expected = IllegalArgumentException.class)
  public void testGetOpenCardAtPileIndexTooBig() {
    this.freecellModel.startGame(this.validDeck, 4, 2, true);

    this.freecellModel.getOpenCardAt(2);
  }

  // test getOpenCardAt where the pile index is smaller than the number of foundation piles
  @Test(expected = IllegalArgumentException.class)
  public void testGetOpenCardAtPileIndexTooSmall() {
    this.freecellModel.startGame(this.validDeck, 4, 2, true);

    this.freecellModel.getOpenCardAt(-1);
  }

  // tests isGameOver on a game that isn't over
  @Test
  public void testIsGameOverNot() {
    assertFalse(this.freecellModel.isGameOver());
  }

  @Test
  public void testIsGameOverYes() {
    this.freecellModel.startGame(this.validDeck, 52, 1, false);
    this.freecellModel.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
    this.freecellModel.move(PileType.CASCADE, 4, 0, PileType.FOUNDATION, 0);
    this.freecellModel.move(PileType.CASCADE, 8, 0, PileType.FOUNDATION, 0);
    this.freecellModel.move(PileType.CASCADE, 12, 0, PileType.FOUNDATION, 0);
    this.freecellModel.move(PileType.CASCADE, 16, 0, PileType.FOUNDATION, 0);
    this.freecellModel.move(PileType.CASCADE, 20, 0, PileType.FOUNDATION, 0);
    this.freecellModel.move(PileType.CASCADE, 24, 0, PileType.FOUNDATION, 0);
    this.freecellModel.move(PileType.CASCADE, 28, 0, PileType.FOUNDATION, 0);
    this.freecellModel.move(PileType.CASCADE, 32, 0, PileType.FOUNDATION, 0);
    this.freecellModel.move(PileType.CASCADE, 36, 0, PileType.FOUNDATION, 0);
    this.freecellModel.move(PileType.CASCADE, 40, 0, PileType.FOUNDATION, 0);
    this.freecellModel.move(PileType.CASCADE, 44, 0, PileType.FOUNDATION, 0);
    this.freecellModel.move(PileType.CASCADE, 48, 0, PileType.FOUNDATION, 0);

    this.freecellModel.move(PileType.CASCADE, 1, 0, PileType.FOUNDATION, 1);
    this.freecellModel.move(PileType.CASCADE, 5, 0, PileType.FOUNDATION, 1);
    this.freecellModel.move(PileType.CASCADE, 9, 0, PileType.FOUNDATION, 1);
    this.freecellModel.move(PileType.CASCADE, 13, 0, PileType.FOUNDATION, 1);
    this.freecellModel.move(PileType.CASCADE, 17, 0, PileType.FOUNDATION, 1);
    this.freecellModel.move(PileType.CASCADE, 21, 0, PileType.FOUNDATION, 1);
    this.freecellModel.move(PileType.CASCADE, 25, 0, PileType.FOUNDATION, 1);
    this.freecellModel.move(PileType.CASCADE, 29, 0, PileType.FOUNDATION, 1);
    this.freecellModel.move(PileType.CASCADE, 33, 0, PileType.FOUNDATION, 1);
    this.freecellModel.move(PileType.CASCADE, 37, 0, PileType.FOUNDATION, 1);
    this.freecellModel.move(PileType.CASCADE, 41, 0, PileType.FOUNDATION, 1);
    this.freecellModel.move(PileType.CASCADE, 45, 0, PileType.FOUNDATION, 1);
    this.freecellModel.move(PileType.CASCADE, 49, 0, PileType.FOUNDATION, 1);

    this.freecellModel.move(PileType.CASCADE, 2, 0, PileType.FOUNDATION, 2);
    this.freecellModel.move(PileType.CASCADE, 6, 0, PileType.FOUNDATION, 2);
    this.freecellModel.move(PileType.CASCADE, 10, 0, PileType.FOUNDATION, 2);
    this.freecellModel.move(PileType.CASCADE, 14, 0, PileType.FOUNDATION, 2);
    this.freecellModel.move(PileType.CASCADE, 18, 0, PileType.FOUNDATION, 2);
    this.freecellModel.move(PileType.CASCADE, 22, 0, PileType.FOUNDATION, 2);
    this.freecellModel.move(PileType.CASCADE, 26, 0, PileType.FOUNDATION, 2);
    this.freecellModel.move(PileType.CASCADE, 30, 0, PileType.FOUNDATION, 2);
    this.freecellModel.move(PileType.CASCADE, 34, 0, PileType.FOUNDATION, 2);
    this.freecellModel.move(PileType.CASCADE, 38, 0, PileType.FOUNDATION, 2);
    this.freecellModel.move(PileType.CASCADE, 42, 0, PileType.FOUNDATION, 2);
    this.freecellModel.move(PileType.CASCADE, 46, 0, PileType.FOUNDATION, 2);
    this.freecellModel.move(PileType.CASCADE, 50, 0, PileType.FOUNDATION, 2);

    this.freecellModel.move(PileType.CASCADE, 3, 0, PileType.FOUNDATION, 3);
    this.freecellModel.move(PileType.CASCADE, 7, 0, PileType.FOUNDATION, 3);
    this.freecellModel.move(PileType.CASCADE, 11, 0, PileType.FOUNDATION, 3);
    this.freecellModel.move(PileType.CASCADE, 15, 0, PileType.FOUNDATION, 3);
    this.freecellModel.move(PileType.CASCADE, 19, 0, PileType.FOUNDATION, 3);
    this.freecellModel.move(PileType.CASCADE, 23, 0, PileType.FOUNDATION, 3);
    this.freecellModel.move(PileType.CASCADE, 27, 0, PileType.FOUNDATION, 3);
    this.freecellModel.move(PileType.CASCADE, 31, 0, PileType.FOUNDATION, 3);
    this.freecellModel.move(PileType.CASCADE, 35, 0, PileType.FOUNDATION, 3);
    this.freecellModel.move(PileType.CASCADE, 39, 0, PileType.FOUNDATION, 3);
    this.freecellModel.move(PileType.CASCADE, 43, 0, PileType.FOUNDATION, 3);
    this.freecellModel.move(PileType.CASCADE, 47, 0, PileType.FOUNDATION, 3);
    this.freecellModel.move(PileType.CASCADE, 51, 0, PileType.FOUNDATION, 3);

    assertTrue(this.freecellModel.isGameOver());
  }

  // tests toString before startGame is run
  @Test
  public void testToStringPrior() {
    assertEquals("", this.freecellModel.toString());
  }

  // tests toString after startGame is run
  @Test
  public void testToStringAfter() {
    this.freecellModel.startGame(this.validDeck, 4, 1, false);
    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
            + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠",
        this.freecellModel.toString());
  }

  // tests toString after startGame is run with 8 cascade piles and 4 open piles
  @Test
  public void testToStringAfter8Cascade4Open() {
    this.freecellModel.startGame(this.validDeck, 8, 4, false);
    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "C1: A♣, 3♣, 5♣, 7♣, 9♣, J♣, K♣\n"
            + "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n"
            + "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n"
            + "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠\n"
            + "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n"
            + "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n"
            + "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n"
            + "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠",
        this.freecellModel.toString());
  }

  // tests toString after something has been moved to the foundation and open piles on
  // unshuffled deck
  @Test
  public void testToStringMovedFoundationOpenUnshuffled() {
    this.freecellModel.startGame(this.validDeck, 8, 7, false);
    this.freecellModel.move(PileType.CASCADE, 0, 6, PileType.OPEN, 0);
    this.freecellModel.move(PileType.CASCADE, 0, 5, PileType.OPEN, 1);
    this.freecellModel.move(PileType.CASCADE, 0, 4, PileType.OPEN, 2);
    this.freecellModel.move(PileType.CASCADE, 0, 3, PileType.OPEN, 3);
    this.freecellModel.move(PileType.CASCADE, 0, 2, PileType.OPEN, 4);
    this.freecellModel.move(PileType.CASCADE, 0, 1, PileType.OPEN, 5);
    this.freecellModel.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);

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
        + "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠", this.freecellModel.toString());
  }

  /**
   * Constructs an instance of the class under test representing the Freecell Model.
   *
   * @return an instance of the class under test
   */
  protected abstract FreecellModel<ICard> makeModel();

  /**
   * Concrete class for testing the {@code SimpleFreecellModel} implementation of {@code
   * FreecellModel<ICard>}.
   */
  public static final class SimpleFreecellModelTest extends AbstractFreecellModelTest {

    @Override
    protected FreecellModel<ICard> makeModel() {
      return FreecellModelCreator.create(GameType.SINGLEMOVE);
    }
  }

  /**
   * Concrete class for testing the {@code MultiMoveFreecellModel} implementation of {@code
   * FreecellModel<ICard>}.
   */
  public static final class MultiMoveFreecellModelTest extends AbstractFreecellModelTest {

    @Override
    protected FreecellModel<ICard> makeModel() {
      return FreecellModelCreator.create(GameType.MULTIMOVE);
    }
  }
}
