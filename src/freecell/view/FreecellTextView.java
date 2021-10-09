package cs3500.freecell.view;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.FreecellModelState;
import java.io.IOException;

/**
 * Represents the text view for a Freecell game which displays the game interface to the user
 * based on a given {@code FreecellModelState} and appends it to an {@link Appendable} if one
 * is given.
 */
public class FreecellTextView implements FreecellView {

  private final FreecellModelState<?> model;
  private final Appendable ap;

  /**
   * Constructs a {@code FreecellTextView} object.
   *
   * @param model the model for a freecell game
   * @throws IllegalArgumentException if the model passed through is null.
   */
  public FreecellTextView(FreecellModelState<?> model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }

    this.model = model;
    this.ap = null;
  }

  /**
   * Constructs a {@code FreecellTextView} object based on a given model and appendable value.
   *
   * @param model the model for a freecell game
   * @param ap    an {@link Appendable} object that this view uses as its destination
   * @throws IllegalArgumentException if the model is null or the Appendable is null
   */
  public <K> FreecellTextView(FreecellModel<K> model, Appendable ap) {
    if (model == null || ap == null) {
      throw new IllegalArgumentException("Model/Appendable cannot be null");
    }

    this.model = model;
    this.ap = ap;
  }


  @Override
  public void renderBoard() throws IOException {
    this.ap.append(this.toString());
  }

  @Override
  public void renderMessage(String message) throws IOException {
    this.ap.append(message);
  }

  @Override
  public String toString() {
    return this.model.toString();
  }
}
