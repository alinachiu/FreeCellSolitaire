# freecell-solitaire
A text-based Java program that is modeled after the multi-move freecell solitaire card game. Follows the same basic rules as solitaire. Thoroughly unit tested using JUnit 4 and created using the Model-View-Controller pattern.

## How to Play
When running the Main.java file, the console will display a randomly generated set of piles of cards, including open, cascade, and free piles. A valid user input for a move is a sequence of three inputs (separated by spaces or newlines):
- The source pile (e.g., "C1" , as a single word). The pile number begins at 1.
- The card index, again with the index beginning at 1.
- The destination pile (e.g., "F2" , as a single word). The pile number is again indexed starting from 1.

Note: The controller will parse these inputs and pass the information on to the model to make the move. After a successful move, the controller will use the view to transmit the game state, plus a newline, to the output. See below for more detail. If a move is invalid, the console will let the user know, and they can attempt to move again.
