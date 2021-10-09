package cs3500.freecell.model.hw04;

import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.IPile;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Freecell model which represents the state of the game internally and enforces its
 * rules, it uses the {@code ICard} representation and allows for the user to move multiple cards at
 * once if they obey two conditions: the cards should form a valid build , i.e., they should be
 * arranged in alternating colors and consecutive, descending values in the cascade pile that they
 * are moving from and, for any move to a cascade pile, these cards should form a build with the
 * last card in the destination cascade pile. A mutli-card move may be illegal even if valid if
 * there aren't enough of these intermediate slots available.
 */
public class MultiMoveFreecellModel extends SimpleFreecellModel {

  /**
   * Constructs a {@code MultiMoveFreecellModel} with all the fields inherited by its parent class,
   * {@code SimpleFreecellModel}.
   */
  public MultiMoveFreecellModel() {
    super();
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
      int destPileNumber) throws IllegalArgumentException, IllegalStateException {

    // throws exception if game hasn't started yet
    if (!super.gameStarted) {
      throw new IllegalStateException("The game hasn't started yet!");
    }

    // throws exception if any arguments are invalid
    if ((source == null) || (destination == null) || (pileNumber < 0) || (
        pileNumber > (super.findPile(source).size() - 1) || (cardIndex < 0) || (
            destPileNumber < 0) || (destPileNumber > (super.findPile(destination).size() - 1)))) {
      throw new IllegalArgumentException(
          "One or more arguments is invalid (source pile or destination pile are null/incorrect "
              + "index/not last card). The move cannot be made.");
    }

    // determines the source pile and destination pile as well as whether they are valid and
    // the first card to be moved/list of cards to be moved depending on the type of move desired
    IPile sourcePile = super.findPile(source).get(pileNumber);
    IPile destinationPile = super.findPile(destination).get(destPileNumber);
    this.checkValidSourceDestination(source, destination, cardIndex, sourcePile, destinationPile);

    List<ICard> listOfCards = this.createListOfCards(cardIndex, sourcePile);

    if (cardIndex == sourcePile.size() - 1) {
      sourcePile.removeCardFromPile(listOfCards.get(0));
      destinationPile.addCardToPile(listOfCards.get(0));
    } else if ((source == PileType.CASCADE) && (destination == PileType.CASCADE)
        && this.canAddAdditionalCards(destinationPile, listOfCards) && this
        .validIntermediateSlot(sourcePile.size() - cardIndex)) {
      this.moveFromPileToPile(sourcePile, destinationPile, listOfCards);
    } else {
      throw new IllegalArgumentException("Invalid move!");
    }
  }

  /**
   * Adds the given list of cards to the given destination pile and removes them from the given
   * source pile.
   *
   * @param sourcePile      the given source pile that a move will be made from
   * @param destinationPile the given destination pile that a card/cards will be moved to
   * @param listOfCards     the list of cards that will be added to the given destination pile and
   *                        removed from the given source pile
   */
  private void moveFromPileToPile(IPile sourcePile, IPile destinationPile,
      List<ICard> listOfCards) {
    // add cards to the pile
    for (int i = 0; i < listOfCards.size(); i = i + 1) {
      destinationPile.addCardToPile(listOfCards.get(i));
    }

    // remove cards from the pile
    for (int i = 0; i < listOfCards.size(); i = i + 1) {
      sourcePile.removeCardFromPile(listOfCards.get(i));
    }
  }

  /**
   * Determines whether or not the given destination pile can add a given list of cards to it based
   * on the rules of Multi-Move Freecell.
   *
   * @param destinationPile the pile that the cards are potentially being moved to
   * @param cards           the list of cards that a user wants to move
   * @return true if the move is valid, false if it is invalid
   */
  private boolean canAddAdditionalCards(IPile destinationPile, List<ICard> cards) {
    // checks to see if the list of cards arranged in alternating colors and consecutive,
    // descending values in the cascade pile that they are moving from
    if (destinationPile.canAddAdditionalCard(cards.get(0))) {
      for (int i = 0; i < cards.size() - 1; i++) {
        ICard currCard = cards.get(i);
        ICard nextCard = cards.get(i + 1);

        if ((currCard.getColor() == nextCard.getColor()) || (nextCard.getValue()
            .isGreaterThanBy(1, currCard.getValue()))) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   * Determines if the number of cards the user wants to move is greater than the maximum number of
   * cards that can be moved.
   *
   * @param numCards the number of cards in a cascade pile the user is trying to move
   * @return true if the intermediate slot is valid, false if its invalid
   */
  private boolean validIntermediateSlot(int numCards) {
    int freeOpenPiles = 0;
    int freeCascadePiles = 0;

    // number of free open piles
    for (int i = 0; i < super.openPiles.size(); i++) {
      IPile currPile = super.openPiles.get(i);

      if (currPile.isEmpty()) {
        freeOpenPiles = freeOpenPiles + 1;
      }
    }

    // number of free cascade piles
    for (int i = 0; i < super.cascadePiles.size(); i++) {
      IPile currPile = super.cascadePiles.get(i);

      if (currPile.isEmpty()) {
        freeCascadePiles = freeCascadePiles + 1;
      }
    }

    double maximumCardsMoved = ((freeOpenPiles + 1) * Math.pow(2, freeCascadePiles));
    return numCards <= maximumCardsMoved;
  }

  /**
   * Creates a list of cards from a given starter index to the end of the given pile of cards.
   *
   * @param cardIndex  the starter card index to be grabbed from the pile of cards
   * @param sourcePile the source pile that the cards will be grabbed from
   * @return a list of cards from the starter index to the end of the source pile
   */
  private List<ICard> createListOfCards(int cardIndex, IPile sourcePile) {
    List<ICard> listOfCards = new ArrayList<>();

    for (int i = cardIndex; i < sourcePile.size(); i = i + 1) {
      listOfCards.add(sourcePile.findCard(i));
    }

    return listOfCards;
  }

  /**
   * Throws any necessary exceptions based on the given parameters and Multi-Move Freecell's rules.
   *
   * @param source          the pile type associated with the source for a move in a Freecell game
   * @param destination     the destination type associated with the destination pile for a move in
   *                        a Freecell game
   * @param sourcePile      the correct pile associated with the source type for a move in a
   *                        Freecell game
   * @param cardIndex       the card index associated with the desired card from the source pile
   *                        that the user wants to move
   * @param destinationPile the destination pile for a move in a Freecell game
   * @throws IllegalArgumentException if any parameters are considered to be invalid based on
   *                                  Freecell's rules
   */
  private void checkValidSourceDestination(PileType source, PileType destination, int cardIndex,
      IPile sourcePile, IPile destinationPile)
      throws IllegalArgumentException {
    if (sourcePile.size() <= cardIndex) {
      throw new IllegalArgumentException("Card Index out of bounds");
    }

    if (source == PileType.FOUNDATION) {
      throw new IllegalArgumentException("Foundation cannot be the source pile!");
    }

    if (destination == PileType.OPEN && !destinationPile.isEmpty()) {
      throw new IllegalArgumentException("Open pile is already full!");
    }
  }

}
