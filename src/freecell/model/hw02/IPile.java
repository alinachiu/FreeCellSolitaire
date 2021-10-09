package cs3500.freecell.model.hw02;

/**
 * This interface represents the different operations that piles of cards in a Freecell game can
 * perform.
 */
public interface IPile {

  /**
   * Checks if a card can be added to a specific pile.
   *
   * @param card a given card
   * @return true if the given card can be added to the pile, else false
   */
  boolean canAddAdditionalCard(ICard card);

  /**
   * Determines the size of this pile.
   *
   * @return the size of the pile in integers
   */
  int size();

  /**
   * Finds a card at a given index of a pile.
   *
   * @param index given index to get a card
   * @returns the card based on the index
   */
  ICard findCard(int index);

  /**
   * Adds a given card to a pile.
   *
   * @param card the given card that needs to be added to the pile
   * @throws IllegalArgumentException if card cannot be added to the pile.
   */
  void addCardToPile(ICard card);

  /**
   * Removes a given card from a pile.
   *
   * @param card the given card that needs to be removed from the pile
   */
  void removeCardFromPile(ICard card);

  /**
   * Determines if this pile is empty or not.
   *
   * @return true if the pile is empty, false if it is non-empty
   */
  boolean isEmpty();
}
