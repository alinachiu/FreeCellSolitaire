package cs3500.freecell.model.hw02;

import cs3500.freecell.model.hw02.Suit.CardColor;

/**
 * This interface represents the different operations that cards that can appear in a Freecell game
 * can perform.
 */
public interface ICard {

  /**
   * Gets the color of the card.
   *
   * @return the color of the current card.
   */
  CardColor getColor();

  /**
   * Determines if this{@code Value} is greater than a given {@code Value} by a given number.
   *
   * @param byNum the amount by which this {@code Value} should be greater than the other one by
   * @param val   the given {@code Value} that is being compared to
   * @return true if this {@code Value} is greater than the given one, false if it
   * @throws IllegalArgumentException if the given {@code Value} is null
   */
  boolean isGreaterThanBy(int byNum, Value val);

  /**
   * Gets the suit of the card.
   *
   * @return the suit of the current card.
   */
  Suit getSuit();

  /**
   * Gets the value of the card.
   *
   * @return the suit of the current card.
   */
  Value getValue();
}
