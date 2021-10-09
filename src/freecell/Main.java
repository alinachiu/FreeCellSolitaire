package cs3500.freecell;

import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Represents the main class for this Freecell game where the user can play a Freecell game by
 * entering in a source pile, card index, and destination pile.
 */
public class Main {

  /**
   * A static method that runs arguments to the console so the user can directly play the game.
   *
   * @param args the arguments passed through main
   */
  public static void main(String[] args) {
    SimpleFreecellModel model = new SimpleFreecellModel();
    Appendable out = System.out;
    Readable in = new InputStreamReader(System.in);
    SimpleFreecellController controller = new SimpleFreecellController(model, in, out);

    List<ICard> deck = model.getDeck();

    controller.playGame(deck, 4, 5, false);
  }
}
