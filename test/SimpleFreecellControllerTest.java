import cs3500.freecell.controller.FailingAppendable;
import cs3500.freecell.controller.FailingReadable;
import cs3500.freecell.controller.FreecellController;
import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for SimpleFreecellController: ensures that the class can be created and behave
 * correctly.
 */
public class SimpleFreecellControllerTest {

  // TODO MAKE A BUNCH OF READABLES
  FreecellModel<ICard> model;
  Appendable out;
  FreecellController<ICard> defaultController;
  List<ICard> validDeck;
  List<ICard> emptyDeck;
  List<ICard> deckWithNullValue;
  List<ICard> deckWithDuplicates;
  List<ICard> deckWithExtraCard;
  List<ICard> reverseDeck;

  @Before
  public void initData() {
    this.model = new SimpleFreecellModel();
    this.out = new StringBuilder();
    this.defaultController = new SimpleFreecellController(model, new InputStreamReader(System.in),
        out);
    this.validDeck = this.model.getDeck();
    this.emptyDeck = new ArrayList<>();
    this.deckWithDuplicates = this.model.getDeck();
    ICard dupCard = this.deckWithDuplicates.get(0);
    this.deckWithDuplicates.set(1, dupCard);
    this.deckWithNullValue = this.model.getDeck();
    this.deckWithNullValue.set(0, null);
    this.deckWithExtraCard = this.model.getDeck();
    this.deckWithExtraCard.add(dupCard);
    this.reverseDeck = this.model.getDeck();
    Collections.reverse(this.reverseDeck);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorExceptionNullModel() {
    FreecellController<ICard> nullModelController = new SimpleFreecellController(null,
        new StringReader(""), new StringBuilder());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorExceptionNullReadable() {
    FreecellController<ICard> nullReadableController = new SimpleFreecellController(null,
        null, new StringBuilder());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorExceptionNullAppendable() {
    FreecellController<ICard> nullAppendableController = new SimpleFreecellController(null,
        new StringReader(""), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayGameExceptionDeckNull() {
    this.defaultController.playGame(null, 4, 1, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameFailingAppendable() {
    StringReader input = new StringReader("C1 13 O1");
    Appendable gameLog = new FailingAppendable();
    FreecellController<ICard> c = new SimpleFreecellController(model, input, gameLog);
    c.playGame(this.validDeck, 4, 1, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameFailingReadable() {
    Readable fail = new FailingReadable();
    StringBuilder app = new StringBuilder();
    FreecellController<ICard> c = new SimpleFreecellController(model, fail, app);
    c.playGame(this.validDeck, 4, 1, false);
  }

  @Test
  public void testPlayGameFailedStartGameInvalidDeckWithDuplicates() {
    this.defaultController.playGame(this.deckWithDuplicates, 4, 1, true);
    assertEquals("Could not start game.", out.toString());
  }

  @Test
  public void testPlayGameFailedStartGameInvalidDeckWithTooFewCards() {
    this.defaultController.playGame(this.emptyDeck, 4, 1, true);
    assertEquals("Could not start game.", out.toString());
  }

  @Test
  public void testPlayGameFailedStartGameInvalidDeckWithTooManyCards() {
    this.defaultController.playGame(this.deckWithExtraCard, 4, 1, true);
    assertEquals("Could not start game.", out.toString());
  }

  @Test
  public void testPlayGameFailedStartGameInvalidDeckWithANullCard() {
    this.defaultController.playGame(this.deckWithNullValue, 4, 1, true);
    assertEquals("Could not start game.", out.toString());
  }

  @Test
  public void testPlayGameFailedStartGameInvalidNumCascades() {
    this.defaultController.playGame(this.validDeck, 3, 1, true);
    assertEquals("Could not start game.", out.toString());
  }

  @Test
  public void testPlayGameFailedStartGameInvalidNumOpens() {
    this.defaultController.playGame(this.validDeck, 4, 0, true);
    assertEquals("Could not start game.", out.toString());
  }

  @Test
  public void testPlayGameShufflingCorrectly() {
    Readable in = new StringReader("q");
    StringBuilder builder = new StringBuilder();
    FreecellController<ICard> c = new SimpleFreecellController(model, in, builder);
    FreecellController<ICard> alternateController = new SimpleFreecellController(model, in, out);

    c.playGame(this.validDeck, 4, 1, false);

    assertNotEquals(builder.toString(), out.toString());
  }

  @Test
  public void testPlayGameQuitWithSpacesAndEnters() {
    Readable in = new StringReader("\n\n\n\n    \n\n\n\n q");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testPlayGameInvalidInputPileCardIndexAndDestinationPileInput() {
    Readable in = new StringReader("hello hello hello\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
        + "Invalid Input: hello, try again\n"
        + "Invalid Input: hello, try again\n"
        + "Invalid Input: hello, try again\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testPlayGameInvalidCardIndexTooLarge() {
    Readable in = new StringReader("c1 1293 o1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("Invalid Move: Card Index out of bounds",
        splitLines[splitLines.length - 2]);
  }

  @Test
  public void testPlayGameQForSourcePileQuitsProperly() {
    Readable in = new StringReader("q 1 o1");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testPlayGameQForCardIndexQuitsProperly() {
    Readable in = new StringReader("c1 q o1");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testPlayGameQForDestinationPileQuitsProperly() {
    Readable in = new StringReader("c1 1 q");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testPlayGameInvalidCardIndexTooSmall() {
    Readable in = new StringReader("c1 -1 o1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals(
        "Invalid Move: One or more arguments is invalid "
            + "(source pile or destination pile are null/incorrect "
            + "index/not last card). The move cannot be made.",
        splitLines[splitLines.length - 2]);
  }

  @Test
  public void testPlayGameTryToPlayAfterQuitting() {
    Readable in = new StringReader("c1 1293 o1\nq\nc1 13 o1");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("Invalid Move: Card Index out of bounds",
        splitLines[splitLines.length - 2]);
  }

  @Test
  public void testPlayGameInvalidSourcePileName() {
    Readable in = new StringReader("a1 13 o1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("Invalid Input: a1, try again",
        splitLines[splitLines.length - 3]);
  }

  @Test
  public void testPlayGameInvalidDestinationPileName() {
    Readable in = new StringReader("c1 13 b1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("Invalid Input: b1, try again",
        splitLines[splitLines.length - 2]);
  }

  @Test
  public void testMissingDestinationPileIndex() {
    Readable in = new StringReader("c1 13 f\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
            + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "Invalid Input: , try again\n"
            + "Game quit prematurely.",
        this.out.toString());
  }

  @Test
  public void testPlayGameMoveAllCaps() {
    Readable in = new StringReader("C1 13 O1\nQ");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1: K♣\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
        + "Game quit prematurely.", this.out.toString());
  }

  @Test
  public void testPlayGameToggleCaps() {
    Readable in = new StringReader("C1 13 o1\nQ");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1: K♣\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
        + "Game quit prematurely.", this.out.toString());
  }


  @Test
  public void testPlayGameInvalidSourcePileIndexTooLarge() {
    Readable in = new StringReader("c100 13 o1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("Invalid Move: One or more arguments is invalid "
            + "(source pile or destination pile are null/incorrect index/not last card). "
            + "The move cannot be made.",
        splitLines[splitLines.length - 2]);
  }

  @Test
  public void testPlayGameInvalidSourcePileIndexTooSmall() {
    Readable in = new StringReader("c-1 13 o1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
            + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "Invalid Move: One or more arguments is invalid (source "
            + "pile or destination pile are null/incorrect index/not last card). "
            + "The move cannot be made.\n"
            + "Game quit prematurely.",
        this.out.toString());
  }

  @Test
  public void testPlayGameInvalidMoveDestinationPileIndexTooSmall() {
    Readable in = new StringReader("c1 13 o-1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
            + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "Invalid Move: One or more arguments is invalid "
            + "(source pile or destination pile are null/incorrect "
            + "index/not last card). The move cannot be made.\n"
            + "Game quit prematurely.",
        this.out.toString());
  }

  @Test
  public void testPlayGameInvalidMoveDestinationPileIndexTooLarge() {
    Readable in = new StringReader("c1 13 o119\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
            + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "Invalid Move: One or more arguments is invalid (source pile "
            + "or destination pile are null/incorrect index/not last card). "
            + "The move cannot be made.\n"
            + "Game quit prematurely.",
        this.out.toString());
  }

  // checks to make sure that it only asks for the index again
  @Test
  public void testPlayGameInvalidDestinationPileIndexInvalidIndex() {
    Readable in = new StringReader("c1 13 of\n1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 2, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
            + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "Invalid Input: f, try again\n"
            + "F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1: K♣\n"
            + "O2:\n"
            + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣\n"
            + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
            + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "Game quit prematurely.",
        this.out.toString());
  }

  @Test
  public void testPlayGameInvalidSourcePileIndexInvalidIndex() {
    Readable in = new StringReader("cf 13 o1\n13 o2\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 2, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
            + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "Invalid Input: f, try again\n"
            + "Invalid Input: o1, try again\n"
            + "F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2: K♥\n"
            + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥\n"
            + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "Game quit prematurely.",
        this.out.toString());
  }

  @Test
  public void testPlayGameNotLastCardOfCascade() {
    Readable in = new StringReader("c1 3 o1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("Invalid Move: Can only move the last card of the cascade pile!",
        splitLines[splitLines.length - 2]);
  }

  @Test
  public void testPlayGameTryToMoveFromFoundationPile() {
    Readable in = new StringReader("c1 13 f1\nf1 1 o1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.reverseDeck, 4, 8, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("Invalid Move: Foundation cannot be the source pile!",
        splitLines[splitLines.length - 2]);
  }

  @Test
  public void testPlayGamePutAnotherCardInAFullOpenPile() {
    Readable in = new StringReader("c1 13 o1\nc1 12 o1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.reverseDeck, 4, 8, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("Invalid Move: Open pile is already full!", splitLines[splitLines.length - 2]);
  }

  @Test
  public void testMoveToSamePile() {
    Readable in = new StringReader("c1 13 c1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
            + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "Invalid Move: This move violates the rules of the destination and/or source pile!!\n"
            + "Game quit prematurely.",
        this.out.toString());
  }

  @Test
  public void testPlayGameAfterTwoFalseInputsForCardIndexAndDestinationPile() {
    Readable in = new StringReader("c1 hello hello\n11 o1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 5, 1, false);

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "C1: A♣, 2♦, 3♥, 4♠, 6♣, 7♦, 8♥, 9♠, J♣, Q♦, K♥\n"
        + "C2: A♦, 2♥, 3♠, 5♣, 6♦, 7♥, 8♠, 10♣, J♦, Q♥, K♠\n"
        + "C3: A♥, 2♠, 4♣, 5♦, 6♥, 7♠, 9♣, 10♦, J♥, Q♠\n"
        + "C4: A♠, 3♣, 4♦, 5♥, 6♠, 8♣, 9♦, 10♥, J♠, K♣\n"
        + "C5: 2♣, 3♦, 4♥, 5♠, 7♣, 8♦, 9♥, 10♠, Q♣, K♦\n"
        + "Invalid Input: hello, try again\n"
        + "Invalid Input: hello, try again\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1: K♥\n"
        + "C1: A♣, 2♦, 3♥, 4♠, 6♣, 7♦, 8♥, 9♠, J♣, Q♦\n"
        + "C2: A♦, 2♥, 3♠, 5♣, 6♦, 7♥, 8♠, 10♣, J♦, Q♥, K♠\n"
        + "C3: A♥, 2♠, 4♣, 5♦, 6♥, 7♠, 9♣, 10♦, J♥, Q♠\n"
        + "C4: A♠, 3♣, 4♦, 5♥, 6♠, 8♣, 9♦, 10♥, J♠, K♣\n"
        + "C5: 2♣, 3♦, 4♥, 5♠, 7♣, 8♦, 9♥, 10♠, Q♣, K♦\n"
        + "Game quit prematurely.", this.out.toString());
  }

  @Test
  public void testPlayGameAfterWrongSourcePileName() {
    Readable in = new StringReader("a1 hello bye\nc1 11 o1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 5, 1, false);

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "C1: A♣, 2♦, 3♥, 4♠, 6♣, 7♦, 8♥, 9♠, J♣, Q♦, K♥\n"
        + "C2: A♦, 2♥, 3♠, 5♣, 6♦, 7♥, 8♠, 10♣, J♦, Q♥, K♠\n"
        + "C3: A♥, 2♠, 4♣, 5♦, 6♥, 7♠, 9♣, 10♦, J♥, Q♠\n"
        + "C4: A♠, 3♣, 4♦, 5♥, 6♠, 8♣, 9♦, 10♥, J♠, K♣\n"
        + "C5: 2♣, 3♦, 4♥, 5♠, 7♣, 8♦, 9♥, 10♠, Q♣, K♦\n"
        + "Invalid Input: a1, try again\n"
        + "Invalid Input: hello, try again\n"
        + "Invalid Input: bye, try again\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1: K♥\n"
        + "C1: A♣, 2♦, 3♥, 4♠, 6♣, 7♦, 8♥, 9♠, J♣, Q♦\n"
        + "C2: A♦, 2♥, 3♠, 5♣, 6♦, 7♥, 8♠, 10♣, J♦, Q♥, K♠\n"
        + "C3: A♥, 2♠, 4♣, 5♦, 6♥, 7♠, 9♣, 10♦, J♥, Q♠\n"
        + "C4: A♠, 3♣, 4♦, 5♥, 6♠, 8♣, 9♦, 10♥, J♠, K♣\n"
        + "C5: 2♣, 3♦, 4♥, 5♠, 7♣, 8♦, 9♥, 10♠, Q♣, K♦\n"
        + "Game quit prematurely.", this.out.toString());
  }

  @Test
  public void testPlayGameWordThatStartsWithO() {
    Readable in = new StringReader("open foundation cascade\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 5, 1, false);

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "C1: A♣, 2♦, 3♥, 4♠, 6♣, 7♦, 8♥, 9♠, J♣, Q♦, K♥\n"
        + "C2: A♦, 2♥, 3♠, 5♣, 6♦, 7♥, 8♠, 10♣, J♦, Q♥, K♠\n"
        + "C3: A♥, 2♠, 4♣, 5♦, 6♥, 7♠, 9♣, 10♦, J♥, Q♠\n"
        + "C4: A♠, 3♣, 4♦, 5♥, 6♠, 8♣, 9♦, 10♥, J♠, K♣\n"
        + "C5: 2♣, 3♦, 4♥, 5♠, 7♣, 8♦, 9♥, 10♠, Q♣, K♦\n"
        + "Invalid Input: pen, try again\n"
        + "Invalid Input: oundation, try again\n"
        + "Invalid Input: ascade, try again\n"
        + "Game quit prematurely.", this.out.toString());
  }

  @Test
  public void testPlayGameLineByLine() {
    Readable in = new StringReader("c1\n13\no1\nq");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.validDeck, 4, 1, false);

    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
            + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1: K♣\n"
            + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣\n"
            + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
            + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "Game quit prematurely.",
        out.toString());
  }

  @Test
  public void testPlayGameCorrectlyOneBlock() {
    Readable in = new StringReader("c1 13 f1 c1 12 f1 c1 11 f1 c1 10 f1 c1 9 f1 c1 8 "
        + "f1 c1 7 f1 c1 6 f1 c1 5 f1 c1 4 f1 c1 3 f1 c1 2 f1 c1 1 f1 "
        + "c2 13 f2 c2 12 f2 c2 11 f2 c2 10 f2 c2 9 f2 c2 8 "
        + "f2 c2 7 f2 c2 6 f2 c2 5 f2 c2 4 f2 c2 3 f2 c2 2 f2 c2 1 f2 "
        + "c3 13 f3 c3 12 f3 c3 11 f3 c3 10 f3 c3 9 f3 c3 8 "
        + "f3 c3 7 f3 c3 6 f3 c3 5 f3 c3 4 f3 c3 3 f3 c3 2 f3 c3 1 f3 "
        + "c4 13 f4 c4 12 f4 c4 11 f4 c4 10 f4 c4 9 f4 c4 8 "
        + "f4 c4 7 f4 c4 6 f4 c4 5 f4 c4 4 f4 c4 3 f4 c4 2 f4 c4 1 f4\n");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.reverseDeck, 4, 8, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("Game over.", splitLines[splitLines.length - 1]);
  }

  @Test
  public void testPlayGameCorrectlySplitByLine() {
    Readable in = new StringReader("c1 13 f1\nc1 12 f1\nc1 11 f1\nc1 10 f1\nc1 9 f1\nc1 8 "
        + "f1\nc1 7 f1\nc1 6 f1\nc1 5 f1\nc1 4 f1\nc1 3 f1\nc1 2 f1\nc1 1 f1\n"
        + "c2 13 f2\nc2 12 f2\nc2 11 f2\nc2 10 f2\nc2 9 f2\nc2 8 "
        + "f2\nc2 7 f2\nc2 6 f2\nc2 5 f2\nc2 4 f2\nc2 3 f2\nc2 2 f2\nc2 1 f2\n"
        + "c3 13 f3\nc3 12 f3\nc3 11 f3\nc3 10 f3\nc3 9 f3\nc3 8 "
        + "f3\nc3 7 f3\nc3 6 f3\nc3 5 f3\nc3 4 f3\nc3 3 f3\nc3 2 f3\nc3 1 f3\n"
        + "c4 13 f4\nc4 12 f4\nc4 11 f4\nc4 10 f4\nc4 9 f4\nc4 8 "
        + "f4\nc4 7 f4\nc4 6 f4\nc4 5 f4\nc4 4 f4\nc4 3 f4\nc4 2 f4\nc4 1 f4\n");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.reverseDeck, 4, 8, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("Game over.", splitLines[splitLines.length - 1]);
  }

  @Test
  public void testPlayGameCorrectlySplitByLineOneWrongMoveAfterWon() {
    Readable in = new StringReader("c1 13 f1\nc1 12 f1\nc1 11 f1\nc1 10 f1\nc1 9 f1\nc1 8 "
        + "f1\nc1 7 f1\nc1 6 f1\nc1 5 f1\nc1 4 f1\nc1 3 f1\nc1 2 f1\nc1 1 f1\n"
        + "c2 13 f2\nc2 12 f2\nc2 11 f2\nc2 10 f2\nc2 9 f2\nc2 8 "
        + "f2\nc2 7 f2\nc2 6 f2\nc2 5 f2\nc2 4 f2\nc2 3 f2\nc2 2 f2\nc2 1 f2\n"
        + "c3 13 f3\nc3 12 f3\nc3 11 f3\nc3 10 f3\nc3 9 f3\nc3 8 "
        + "f3\nc3 7 f3\nc3 6 f3\nc3 5 f3\nc3 4 f3\nc3 3 f3\nc3 2 f3\nc3 1 f3\n"
        + "c4 13 f4\nc4 12 f4\nc4 11 f4\nc4 10 f4\nc4 9 f4\nc4 8 "
        + "f4\nc4 7 f4\nc4 6 f4\nc4 5 f4\nc4 4 f4\nc4 3 f4\nc4 2 f4\nc4 1 f4\nc1 1 f1\n");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.reverseDeck, 4, 8, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("Game over.", splitLines[splitLines.length - 1]);
  }

  @Test
  public void testPlayGameCorrectlySplitByLineOneWrongMoveBeforeWinning() {
    Readable in = new StringReader("c1 13 f1\nc1 12 f1\nc1 11 f1\nc1 10 f1\nc1 9 f1\nc1 8 "
        + "f1\nc1 7 f1\nc1 6 f1\nc1 5 f1\nc1 4 f1\nc1 3 f1\nc1 2 f1\nc1 1 f1\n"
        + "c2 13 f2\nc2 12 f2\nc2 11 f2\nc2 10 f2\nc2 9 f2\nc2 8 "
        + "f2\nc2 7 f2\nc2 6 f2\nc2 5 f2\nc2 4 f2\nc2 3 f2\nc2 2 f2\nc2 1 f2\n"
        + "c3 13 f3\nc3 12 f3\nc3 11 f3\nc3 10 f3\nc3 9 f3\nc3 8 "
        + "f3\nc3 7 f3\nc3 6 f3\nc3 5 f3\nc3 4 f3\nc3 3 f3\nc3 2 f3\nc3 1 f3\n"
        + "c4 13 f4\nc4 12 f4\nc4 11 f4\nc4 10 f4\nc4 9 f4\nc4 8 "
        + "f4\nc4 7 f4\nc4 6 f4\nc4 5 f4\nc4 4 f4\nc4 3 f4\nc4 0 f4\nc4 2 f4\nc4 1 f4\n");
    FreecellController<ICard> c = new SimpleFreecellController(model, in, out);
    c.playGame(this.reverseDeck, 4, 8, false);

    String[] splitLines = this.out.toString().split("\n");

    assertEquals("Game over.", splitLines[splitLines.length - 1]);
  }

}
