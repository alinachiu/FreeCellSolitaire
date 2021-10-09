package cs3500.freecell.model.hw02;

/**
 * A class which represents a Open Pile in a Freecell game which can
 * hold only one card and acts as a temporary buffer.
 */
public class OpenPile extends AbstractPile {

  @Override
  public boolean canAddAdditionalCard(ICard card) {
    if (card == null) {
      throw new IllegalArgumentException("Card is null");
    }

    return this.pileOfCards.isEmpty();
  }
}
