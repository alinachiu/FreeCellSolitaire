package cs3500.freecell.model;

import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw04.MultiMoveFreecellModel;

/**
 * Represents a FreecellModelCreator which uses the factory method design pattern to create the
 * correct Freecell model associated with the game that the user wants to play.
 */
public class FreecellModelCreator {

  /**
   * Represents a Freecell game type of either a single-move game, where only one move can be made
   * at a time, or a mutli-move game, where multiple cards can be moved in one move.
   */
  public enum GameType { SINGLEMOVE, MULTIMOVE }

  /**
   * Returns the correct {@code FreecellModel} based on a given {@link GameType}.
   *
   * @param type represents the type of Freecell game that the user wants to play
   * @return the correct {@code FreecellModel} associated with the given type
   * @throws IllegalArgumentException if an invalid {@code GameType} is given (i.e. a null {@code
   *                                  GameType})
   */
  public static FreecellModel<ICard> create(GameType type) throws IllegalArgumentException {
    if (type == null) {
      throw new IllegalArgumentException("Invaid game type!");
    }

    switch (type) {
      case SINGLEMOVE:
        return new SimpleFreecellModel();
      case MULTIMOVE:
        return new MultiMoveFreecellModel();
      default:
        throw new IllegalArgumentException("Invalid game type!");
    }
  }
}
