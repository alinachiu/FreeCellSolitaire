package cs3500.freecell.model.hw02;

/**
 * Represents a card value from 1-13 where Ace - 1, Jack - 11, Queen - 12, and King - 13.
 */
public enum Value {
  ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(
      11), QUEEN(12), KING(13);
  private final int value;

  /**
   * Constructs a {@code Value} class.
   *
   * @param value the value of a card.
   * @throws IllegalArgumentException if the input value is less than 1 or greater than 13
   */
  Value(int value) {
    if (value < 1 || value > 13) {
      throw new IllegalArgumentException("Card values must be between 1 and 13");
    }

    this.value = value;
  }

  /**
   * Determines if this{@code Value} is greater than a given {@code Value} by a given number.
   *
   * @param byNum the amount by which this {@code Value} should be greater than the other one by
   * @param val   the given {@code Value} that is being compared to
   * @return true if this {@code Value} is greater than the given one, false if it
   * @throws IllegalArgumentException if the given {@code Value} is null
   */
  public boolean isGreaterThanBy(int byNum, Value val) {
    if (val == null) {
      throw new IllegalArgumentException("Value is null.");
    }

    return (this.value - byNum) == val.value;
  }

  @Override
  public String toString() {
    switch (this.value) {
      case 1:
        return "A";
      case 11:
        return "J";
      case 12:
        return "Q";
      case 13:
        return "K";
      default:
        return Integer.toString(this.value);
    }
  }
}
