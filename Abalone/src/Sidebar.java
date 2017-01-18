import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Sidebar extends VBox {

	public Sidebar(GameLogic game) {
		setFillWidth(true);

		Label player1 = new Label("Player 1");
		Label player2 = new Label("Player 2");
		player1.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		player2.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

		Label currentPlayer = new Label("Current Player:");
		Label currentPlayerDisplay = new Label();

		Label piecesTakenP1 = new Label("Pieces taken: ");
		Label piecesTakenP1Display = new Label();
		Label piecesLeftP1 = new Label("Pieces left: ");
		Label piecesLeftP1Display = new Label();
		Label piecesTakenP2 = new Label("Pieces taken: ");
		Label piecesTakenP2Display = new Label();
		Label piecesLeftP2 = new Label("Pieces left: ");
		Label piecesLeftP2Display = new Label("14");

		currentPlayerDisplay.textProperty().bind(game.playerNameProperty());
		piecesLeftP1Display.textProperty().bind(game.getP1PiecesLeftProperty());
		piecesLeftP2Display.textProperty().bind(game.getP2PiecesLeftProperty());
		piecesTakenP1Display.textProperty().bind(game.getP1PiecesTakenProperty());
		piecesTakenP2Display.textProperty().bind(game.getP2PiecesTakenProperty());
		

		getChildren().addAll(currentPlayer, currentPlayerDisplay, 
				player1, piecesTakenP1, piecesTakenP1Display, piecesLeftP1, piecesLeftP1Display,
				player2, piecesTakenP2, piecesTakenP2Display, piecesLeftP2, piecesLeftP2Display);
	}
}
