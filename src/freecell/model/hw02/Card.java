package cs3500.freecell.model.hw02;

import cs3500.freecell.model.hw02.Suit.CardColor;

/**
 * Represents a card that has one of thirteen values (1-13) and one of four suits (clubs, diamonds,
 * hearts, and spades). Note: Ace - 1, Jack - 11, Queen - 12, King - 13
 */
public final class Card implements ICard {

  private final Value value;
  private final Suit suit;

  /**
   * Constructs a {@code Card} object.
   *
   * @param value the value of card from 1-13.
   * @param suit  the suit of the card (club, diamond, heart, or spade).
   * @throws IllegalArgumentException if any argument is invalid or null.
   */
  public Card(Value value, Suit suit) {
    if (value == null || suit == null) {
      throw new IllegalArgumentException("Invalid Input.");
    }

    this.value = value;
    this.suit = suit;
  }

  @Override
  public CardColor getColor() {
    return this.suit.getColor();
  }

  @Override
  public boolean isGreaterThanBy(int byNum, Value val) {
    return this.value.isGreaterThanBy(byNum, val);
  }

  @Override
  public Suit getSuit() {
    return this.suit;
  }

  @Override
  public Value getValue() {
    return this.value;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ICard)) {
      return false;
    }

    Card that = (Card) obj;

    return (this.value == that.value) && (this.suit == that.suit);

  }

  @Override
  public int hashCode() {
    return 2704 * this.value.hashCode() + this.suit.hashCode();
  }

  @Override
  public String toString() {
    return this.value.toString() + this.suit.toString();
  }

}
