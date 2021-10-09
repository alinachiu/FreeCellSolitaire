package cs3500.freecell.model.hw02;

import cs3500.freecell.model.hw02.Suit.CardColor;

/**
 * A class which represents a Cascade Pile in a Freecell game that is composed of a pile of face-up
 * cards. A build within a cascade pile is a subset of cards that has monotonically decreasing
 * values and suits of alternating colors.
 */
public class CascadePile extends AbstractPile {

  @Override
  public boolean canAddAdditionalCard(ICard card) {
    if (card == null) {
      throw new IllegalArgumentException("Card is null");
    }

    if (this.pileOfCards.isEmpty()) {
      return true;
    }

    ICard lastCard = this.pileOfCards.get(this.pileOfCards.size() - 1);
    CardColor newColor = card.getColor();
    CardColor oldColor = lastCard.getColor();

    return ((newColor != oldColor) && (lastCard.getValue()
        .isGreaterThanBy(1, card.getValue())));
  }

}
