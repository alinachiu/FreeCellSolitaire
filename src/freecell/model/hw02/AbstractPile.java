package cs3500.freecell.model.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an abstract class for a pile of cards in a Freecell game.
 */
public abstract class AbstractPile implements IPile {

  protected List<ICard> pileOfCards;

  /**
   * Constructs a {@link IPile} in a manner selected by concrete subclasses of this class.
   */
  protected AbstractPile() {
    this.pileOfCards = new ArrayList<>();
  }

  public abstract boolean canAddAdditionalCard(ICard card);

  @Override
  public int size() {
    return this.pileOfCards.size();
  }

  @Override
  public ICard findCard(int index) {
    return this.pileOfCards.get(index);
  }

  @Override
  public void addCardToPile(ICard card) {
    this.pileOfCards.add(card);
  }

  @Override
  public void removeCardFromPile(ICard card) {
    this.pileOfCards.remove(card);
  }

  @Override
  public boolean isEmpty() {
    return this.pileOfCards.isEmpty();
  }

  @Override
  public String toString() {
    String pileString = "";

    for (int i = 0; i < this.pileOfCards.size(); i++) {
      if (i == this.pileOfCards.size() - 1) {
        pileString = pileString
            + this.pileOfCards.get(i).toString() + "\n";
      } else {
        pileString = pileString
            + this.pileOfCards.get(i).toString() + ", ";
      }
    }
    return pileString;
  }
}
