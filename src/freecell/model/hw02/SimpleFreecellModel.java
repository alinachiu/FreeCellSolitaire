package cs3500.freecell.model.hw02;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a simple Freecell model which represents the state of the game internally and enforces
 * its rules, it uses the {@code ICard} representation and only allows one move at a time.
 */
public class SimpleFreecellModel implements FreecellModel<ICard> {

  // NOTE: private fields and helper methods were changed to protected methods as needed in order
  // to let {@code MultiMoveFreecellModel} access any necessary fields/methods for overriding
  // the move function
  protected List<IPile> cascadePiles;
  protected List<IPile> openPiles;
  protected List<IPile> foundationPiles;
  protected boolean gameStarted;

  /**
   * Constructs a {@code SimpleFreecellModel} object.
   */
  public SimpleFreecellModel() {
    this.cascadePiles = new ArrayList<>();
    this.openPiles = new ArrayList<>();
    this.foundationPiles = new ArrayList<IPile>(Arrays
        .asList(new FoundationPile(), new FoundationPile(), new FoundationPile(),
            new FoundationPile()));
    this.gameStarted = false;
  }

  @Override
  public List<ICard> getDeck() {
    List<ICard> deck = new ArrayList<>();

    for (Value val : Value.values()) {
      for (Suit suit : Suit.values()) {
        deck.add(new Card(val, suit));
      }
    }

    return deck;
  }

  @Override
  public void startGame(List<ICard> deck, int numCascadePiles, int numOpenPiles, boolean shuffle)
      throws IllegalArgumentException {

    this.resetGame();

    // checks to see if any given parameter is invalid
    if (!this.isValidDeck(deck) || numCascadePiles < 4 || numOpenPiles < 1) {
      throw new IllegalArgumentException("One or more of the given parameters is invalid");
    }

    this.initPiles(numCascadePiles, numOpenPiles);

    List<ICard> newDeck = deck;
    // shuffles the a deck, if necessary (no field for deck so that the game state
    // cannot be changed from another method)
    if (shuffle) {
      Collections.shuffle(newDeck);
    }

    this.roundRobin(newDeck, numCascadePiles);

    this.gameStarted = true;
  }

  /**
   * Initializes the piles to the correct amounts.
   */
  private void initPiles(int numCascadePiles, int numOpenPiles) {
    for (int i = 0; i < numCascadePiles; i = i + 1) {
      this.cascadePiles.add(new CascadePile());
    }

    for (int i = 0; i < numOpenPiles; i = i + 1) {
      this.openPiles.add(new OpenPile());
    }
  }

  /**
   * Resets the game back to default settings.
   */
  private void resetGame() {
    this.cascadePiles = new ArrayList<>();
    this.openPiles = new ArrayList<>();
    this.foundationPiles = new ArrayList<IPile>(Arrays
        .asList(new FoundationPile(), new FoundationPile(), new FoundationPile(),
            new FoundationPile()));
    this.gameStarted = false;
  }

  /**
   * Performs a round-robin style dealing on a given deck based on a given number of cascade piles.
   *
   * @param deck            the given deck of cards
   * @param numCascadePiles the given number of cascade piles
   */
  private void roundRobin(List<ICard> deck, int numCascadePiles) {
    for (int i = 0; i < numCascadePiles; i = i + 1) {
      for (int j = i; j < deck.size(); j = j + numCascadePiles) {
        this.cascadePiles.get(i).addCardToPile(deck.get(j));
      }
    }
  }

  /**
   * Determines whether or not a given deck is valid (must have 52 cards, no duplicates, and no
   * invalid cards (invalid suit or number)).
   *
   * @param deck the deck of cards
   * @return true if the given deck is valid, false if the given deck is invalid.
   */
  private boolean isValidDeck(List<ICard> deck) {
    if (deck == null) {
      return false;
    }

    // the set of cards in a deck, excluding duplicates
    Set<ICard> setOfCards = new HashSet<>(deck);

    return !(deck.size() != 52 || deck.size() != setOfCards.size() || deck
        .contains(null));
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
      int destPileNumber) throws IllegalArgumentException, IllegalStateException {

    // throws exception if game hasn't started yet
    if (!this.gameStarted) {
      throw new IllegalStateException("The game hasn't started yet!");
    }

    // throws exception if any arguments are invalid
    if ((source == null) || (destination == null) || (pileNumber < 0) || (
        pileNumber > (findPile(source).size() - 1) || (cardIndex < 0) || (
            destPileNumber < 0) || (destPileNumber > (findPile(destination).size() - 1)))) {
      throw new IllegalArgumentException(
          "One or more arguments is invalid (source pile or destination pile "
              + "are null/incorrect index/not last card). "
              + "The move cannot be made.");
    }

    IPile sourcePile = findPile(source).get(pileNumber);
    IPile destinationPile = findPile(destination).get(destPileNumber);

    if (sourcePile.size() <= cardIndex) {
      throw new IllegalArgumentException("Card Index out of bounds");
    }

    if (source == PileType.CASCADE && cardIndex != sourcePile.size() - 1) {
      throw new IllegalArgumentException("Can only move the last card of the cascade pile!");
    }

    if (source == PileType.FOUNDATION) {
      throw new IllegalArgumentException("Foundation cannot be the source pile!");
    }

    if (destination == PileType.OPEN && !destinationPile.isEmpty()) {
      throw new IllegalArgumentException("Open pile is already full!");
    }

    ICard card = sourcePile.findCard(cardIndex);

    if (destinationPile.canAddAdditionalCard(card)) {
      sourcePile.removeCardFromPile(card);
      destinationPile.addCardToPile(card);
    } else {
      throw new IllegalArgumentException("This move violates the rules of the "
          + "destination and/or source pile!!");
    }
  }

  /**
   * Determines the pile based on a given PileType.
   *
   * @param pileType the given PileType of the desired {@link IPile}
   * @return the corresponding list of piles
   * @throws IllegalArgumentException if no such pile type exists
   */
  protected List<IPile> findPile(PileType pileType) throws IllegalArgumentException {
    switch (pileType) {
      case CASCADE:
        return this.cascadePiles;
      case OPEN:
        return this.openPiles;
      case FOUNDATION:
        return this.foundationPiles;
      default:
        throw new IllegalArgumentException("No such pile type exists");
    }
  }

  @Override
  public boolean isGameOver() {
    int numCardsInFoundation = 0;

    for (int i = 0; i < this.foundationPiles.size(); i = i + 1) {
      numCardsInFoundation = numCardsInFoundation + this.foundationPiles.get(i).size();
    }

    return numCardsInFoundation == 52;
  }

  @Override
  public int getNumCardsInFoundationPile(int index)
      throws IllegalArgumentException, IllegalStateException {
    this.checkExceptionsGetNumCards(this.foundationPiles, index);

    return this.foundationPiles.get(index).size();
  }

  @Override
  public int getNumCascadePiles() {
    if (!this.gameStarted) {
      return -1;
    }

    return this.cascadePiles.size();
  }

  @Override
  public int getNumCardsInCascadePile(int index)
      throws IllegalArgumentException, IllegalStateException {
    this.checkExceptionsGetNumCards(this.cascadePiles, index);

    return this.cascadePiles.get(index).size();
  }

  @Override
  public int getNumCardsInOpenPile(int index)
      throws IllegalArgumentException, IllegalStateException {
    this.checkExceptionsGetNumCards(this.openPiles, index);

    if (this.openPiles.get(index) == null) {
      throw new IllegalArgumentException("Pile is invalid.");
    }

    return this.openPiles.get(index).size();
  }

  /**
   * Checks potential exceptions when attempting to get the number of cards in a given pile.
   *
   * @param pile  the given pile
   * @param index the index of the given pile
   * @throws IllegalArgumentException if the provided index is invalid
   * @throws IllegalStateException    if the game has not started
   */
  private void checkExceptionsGetNumCards(List<IPile> pile, int index)
      throws IllegalArgumentException, IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game has not started yet!");
    }

    if ((index < 0) || (index > pile.size() - 1)) {
      throw new IllegalArgumentException("Invalid index.");
    }
  }

  @Override
  public int getNumOpenPiles() {
    if (!this.gameStarted) {
      return -1;
    }

    return this.openPiles.size();
  }

  @Override
  public ICard getFoundationCardAt(int pileIndex, int cardIndex)
      throws IllegalArgumentException, IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game hasn't started.");
    }

    // checks invalid pile index
    if (pileIndex < 0 || pileIndex > (this.foundationPiles.size() - 1) || cardIndex < 0
        || cardIndex > (this.foundationPiles.get(pileIndex).size() - 1)) {
      throw new IllegalArgumentException("Invalid pile/card index.");
    }

    return this.foundationPiles.get(pileIndex).findCard(cardIndex);
  }

  @Override
  public ICard getCascadeCardAt(int pileIndex, int cardIndex)
      throws IllegalArgumentException, IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game hasn't started.");
    }

    // checks invalid pile index
    if (pileIndex < 0 || pileIndex > (this.cascadePiles.size() - 1) || cardIndex < 0
        || cardIndex > (this.cascadePiles.get(pileIndex).size() - 1)) {
      throw new IllegalArgumentException("Invalid pile/card index.");
    }

    return this.cascadePiles.get(pileIndex).findCard(cardIndex);
  }

  @Override
  public ICard getOpenCardAt(int pileIndex)
      throws IllegalArgumentException, IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game has not started yet!");
    }

    if (pileIndex > this.openPiles.size() - 1 || pileIndex < 0 || this.openPiles == null
        || this.openPiles
        .isEmpty()) {
      throw new IllegalArgumentException("Invalid index for pile and/or card.");
    }

    if (this.openPiles.get(pileIndex).isEmpty()) {
      return null;
    } else {
      return this.openPiles.get(pileIndex).findCard(0);
    }
  }


  @Override
  public String toString() {
    if (!this.gameStarted) {
      return "";
    }

    String str = "";

    str = this.toStringGenericBuilder(this.foundationPiles, str, 'F');
    str = this.toStringGenericBuilder(this.openPiles, str, 'O');
    str = this.toStringGenericBuilder(this.cascadePiles, str, 'C');

    str = str.substring(0, str.lastIndexOf("\n"));

    return str;
  }

  /**
   * Assists in creating the toString method by taking a given pile, current string, and letter.
   *
   * @param piles      the given pile that will be converted into a string
   * @param currString the toString in its current state so far
   * @param letter     the letter that signifies the correct letter for the toString
   * @return the formatted string
   */
  private String toStringGenericBuilder(List<IPile> piles, String currString, char letter) {
    for (int i = 0; i < piles.size(); i++) {
      IPile currPile = piles.get(i);

      if (currPile.isEmpty()) {
        currString = currString + letter + (i + 1) + ":\n" + currPile.toString();
      } else {
        currString = currString + letter + (i + 1) + ": " + currPile.toString();
      }
    }

    return currString;
  }
}
