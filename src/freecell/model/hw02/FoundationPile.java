package cs3500.freecell.model.hw02;

/**
 * A class which represents a Foundation Pile in a Freecell game which
 * is initially empty. There are four foundation piles for each suit that
 * collect cards in increasing value (Ace is the lowest).
 */
public class FoundationPile extends AbstractPile {

  @Override
  public boolean canAddAdditionalCard(ICard card) {
    if (card == null) {
      throw new IllegalArgumentException("Card is null");
    }

    if (this.pileOfCards.isEmpty() && (card.getValue() == Value.ACE)) {
      return true;
    }

    if (this.pileOfCards.isEmpty()) {
      return false;
    }

    ICard lastCard = this.pileOfCards.get(this.pileOfCards.size() - 1);
    return (this.pileOfCards.size() <= 13 && ((lastCard.getSuit()
            == card.getSuit()) && (card.isGreaterThanBy(1, lastCard.getValue()))));
  }
}
