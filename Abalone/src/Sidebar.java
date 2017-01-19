import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

	public Sidebar(GameLogic game) {
		setFillWidth(true);

		// Label currentPlayer = new Label("Current Player:");
		// currentPlayer.setId("currentPlayer");
		// currentPlayer.getStyleClass().add("heading");
		Label currentPlayerDisplay = new Label();
		currentPlayerDisplay.setId("playerDisplay");
		Label player1 = new Label("Player 1");
		player1.setId("player1");
		player1.getStyleClass().add("heading");

		Label player2 = new Label("Player 2");
		player2.setId("player2");
		player2.getStyleClass().add("heading");

		Label piecesTakenP1 = new Label("Pieces taken: ");
		Label piecesTakenP1Display = new Label();
		Label piecesLeftP1 = new Label("Pieces left: ");
		Label piecesLeftP1Display = new Label();
		Label piecesTakenP2 = new Label("Pieces taken: ");
		Label piecesTakenP2Display = new Label();
		Label piecesLeftP2 = new Label("Pieces left: ");
		Label piecesLeftP2Display = new Label("14");

		currentPlayerDisplay.textProperty().bind(game.playerNameProperty());
		currentPlayerDisplay.styleProperty().bind(game.playerColorProperty());
		piecesLeftP1Display.textProperty().bind(game.getP1PiecesLeftProperty());
		piecesLeftP2Display.textProperty().bind(game.getP2PiecesLeftProperty());
		piecesTakenP1Display.textProperty().bind(game.getP1PiecesTakenProperty());
		piecesTakenP2Display.textProperty().bind(game.getP2PiecesTakenProperty());

		getChildren().addAll(currentPlayerDisplay, player1, piecesTakenP1, piecesTakenP1Display, piecesLeftP1,
				piecesLeftP1Display, player2, piecesTakenP2, piecesTakenP2Display, piecesLeftP2, piecesLeftP2Display);
	}
}
