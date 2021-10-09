package cs3500.freecell.model.hw02;

/**
 * Represents the suit of a card (club, heart, spade, or diamond) and its associated color.
 */
public enum Suit {
  CLUB(CardColor.BLACK), DIAMOND(CardColor.RED), HEART(CardColor.RED), SPADE(CardColor.BLACK);
  private final CardColor color;

  /**
   * Represents an enumeration of a card color (red or black).
   */
  public enum CardColor { RED, BLACK }

  /**
   * Constructs a {@link Suit} enum.
   *
   * @param color the color associated with this particular Suit enum
   */
  Suit(CardColor color) {
    this.color = color;
  }

  /**
   * Gets the color of this suit.
   *
   * @return the color associated with a particular suit
   */
  public CardColor getColor() {
    return this.color;
  }

  @Override
  public String toString() {
    switch (this) {
      case CLUB:
        return "♣";
      case DIAMOND:
        return "♦";
      case HEART:
        return "♥";
      case SPADE:
        return "♠";
      default:
        return "";
    }
  }
}
