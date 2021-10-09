package cs3500.freecell.controller;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// NOTE: abstracted the playGame method as per HW03 feedback.

/**
 * Represents a simple freecell controller with basic implementations which takes input from the
 * user and decides what to do. This controller uses the {@code ICard} representation, takes in
 * input from a {@link Readable}, and decides what to do before appending the result to an {@link
 * Appendable}.
 */
public class SimpleFreecellController implements FreecellController<ICard> {

  private final FreecellModel<ICard> model;
  private final Readable rd;
  private final FreecellView view;
  private PileType source = null;
  private int sourceIndex = -1;
  private int cardIndex = -1;
  private PileType destination = null;
  private int destinationIndex = -1;

  /**
   * Constructs a {@code SimpleFreecellController} object.
   *
   * @param model the freecell model that the user will play with.
   * @param rd    represents the user input
   * @param ap    represents the user output
   * @throws IllegalArgumentException if any of its arguments are null
   */
  public SimpleFreecellController(FreecellModel<ICard> model, Readable rd,
      Appendable ap) throws IllegalArgumentException {
    if (model == null || rd == null || ap == null) {
      throw new IllegalArgumentException("Arguments cannot be null.");
    }

    this.model = model;
    this.rd = rd;
    this.view = new FreecellTextView(this.model, ap);
  }

  @Override
  public void playGame(List<ICard> deck, int numCascades, int numOpens, boolean shuffle)
      throws IllegalStateException, IllegalArgumentException {
    if (deck == null) {
      throw new IllegalArgumentException("Deck cannot be null");
    }

    try {
      this.model.startGame(deck, numCascades, numOpens, shuffle);
    } catch (IllegalArgumentException e) {
      this.attemptAppend("Could not start game.");
      return;
    }

    boolean sourceFound = false;
    boolean cardIndexFound = false;
    boolean anyIndexInvalid = false;
    Scanner scan = new Scanner(this.rd);
    this.displayBoard();

    while (scan.hasNext()) {
      String str = scan.next().toLowerCase();

      if (str.equals("q")) {
        this.attemptAppend("Game quit prematurely.");
        return;
      }

      anyIndexInvalid = this.determineParameters(str, sourceFound, cardIndexFound, anyIndexInvalid);

      this.tryMove();

      if (this.model.isGameOver()) {
        this.displayBoard();
        this.attemptAppend("\nGame over.");
        return;
      }

      sourceFound = (source != null && sourceIndex != -1);
      cardIndexFound = (cardIndex != -1);
    }

    if (!this.model.isGameOver()) {
      throw new IllegalStateException("Readable failed!");
    }

  }

  /**
   * Determines the parameters for the move in terms of source, source index, card index,
   * destination, and destination index. Sets them accordingly based on a given string and a set of
   * booleans and returns whether any indices were entered incorrectly.
   *
   * @param str             the given user input to determine the move
   * @param sourceFound     represents whether or not the source has been found yet
   * @param cardIndexFound  represents whether or not the card index has been found yet
   * @param anyIndexInvalid represents whether or not any indices were entered incorrectly
   * @return true if any indices were entered incorrectly, false if they were entered correctly
   */
  private boolean determineParameters(String str, boolean sourceFound, boolean cardIndexFound,
      boolean anyIndexInvalid) {
    if (source == null && sourceIndex == -1) {
      source = this.handleUserInput(str);
    }

    if (source != null && sourceIndex == -1) {
      sourceIndex = this.handlePotentialInt(str.substring(1));
    }

    if (source != null && sourceIndex != -1 && cardIndex == -1 && sourceFound) {
      cardIndex = this.handlePotentialInt(str);
    }

    if (source != null && sourceIndex != -1 && cardIndex != -1 && sourceFound && cardIndexFound
        && !anyIndexInvalid) {
      destination = this.handleUserInput(str);
    }

    if (source != null && sourceIndex != -1 && cardIndex != -1 && destination != null
        && sourceFound && cardIndexFound) {
      if (anyIndexInvalid) {
        destinationIndex = this.handlePotentialInt(str);
      } else {
        destinationIndex = this.handlePotentialInt(str.substring(1));
      }

      if (destinationIndex != -1) {
        anyIndexInvalid = false;
      } else {
        anyIndexInvalid = true;
      }
    }

    return anyIndexInvalid;
  }

  /**
   * Tries to move a card from its source pile if possible based on the class's fields.
   */
  public void tryMove() {
    if (source != null && sourceIndex != -1 && cardIndex != -1 && destination != null
        && destinationIndex != -1) {
      try {
        this.model.move(source, sourceIndex, cardIndex, destination, destinationIndex);
        this.displayBoard();
      } catch (IllegalArgumentException e) {
        this.attemptAppend("Invalid Move: " + e.getLocalizedMessage() + "\n");
      }
      this.resetFields();
    }
  }

  /**
   * Resets the fields of this class back to default (null or -1).
   */
  private void resetFields() {
    source = null;
    sourceIndex = -1;
    cardIndex = -1;
    destination = null;
    destinationIndex = -1;
  }

  /**
   * Handles the user input character based on whether or not it is valid ('c', 'f', 'o') and
   * returns the correct PileType associated with the input.
   *
   * @param userInput the string that the user inputs to the controller
   */
  private PileType handleUserInput(String userInput) {
    char firstChar = userInput.charAt(0);

    switch (firstChar) {
      case 'c':
        return PileType.CASCADE;
      case 'f':
        return PileType.FOUNDATION;
      case 'o':
        return PileType.OPEN;
      default:
        this.attemptAppend("Invalid Input: " + userInput + ", try again\n");
        return null;
    }
  }

  /**
   * Handles the user input character based on whether or not it is valid (an integer) and returns
   * the correct value associated with the input. Returns -1 if invalid input
   *
   * @param userInput the string that the user inputs to the controller
   */
  private int handlePotentialInt(String userInput) {
    try {
      int userInt = Integer.parseInt(userInput) - 1;

      if (userInt == -1) {
        return userInt - 1;
      } else {
        return userInt;
      }
    } catch (NumberFormatException num) {
      this.attemptAppend("Invalid Input: " + userInput + ", try again\n");
    }
    return -1;
  }


  /**
   * Displays the current state of the game board.
   *
   * @throws IllegalStateException if writing to the Appendable throws an IOException
   */
  private void displayBoard() throws IllegalStateException {
    try {
      this.view.renderBoard();
      this.attemptAppend("\n");
    } catch (IOException e) {
      throw new IllegalStateException("Writing to the Appendable object used by it fails");
    }
  }

  /**
   * Tries to append a given string to this appendable, if possible.
   *
   * @param str the given string to be appended
   * @throws IllegalStateException if writing to the Appendable throws an IOException
   */
  private void attemptAppend(String str) throws IllegalStateException {
    try {
      this.view.renderMessage(str);
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Writing to the Appendable object used by it fails");
    }
  }
}
