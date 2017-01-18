import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

//draw Hexagonal Grid

// Pieces can move to “valid” locations determined by a complex set of rules.
// Valid moves are defined in the Piece/GameLogic class.

// maps mouse click coordinates to hexagonal grid.

public class AbaloneBoard extends Pane {

	private VBox vbox;
	private HBox[] hboxes;
	private Cell[][] cells = new Cell[11][11];
	private GameLogic game;

	public AbaloneBoard() {
		game = new GameLogic();
		game.setCurrentPlayer(PieceType.PLAYER1);
		createCells();
		game.setCells(cells);
		game.initializeSelectable();

		Label label = new Label("displaying player");
		label.textProperty().bind(game.playerNameProperty());
		
		vbox = new VBox(-15);
		vbox.getChildren().add(label);
		vbox.setPadding(new Insets(50));
		vbox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		hboxes = new HBox[11];
		for (int i = 0; i < hboxes.length; i++) {
			HBox hbox = new HBox(2);
			hbox.setAlignment(Pos.CENTER);
			hboxes[i] = hbox;
		}

		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				hboxes[i].getChildren().add(cells[i][j]);
			}
		}

		for (int i = 0; i < hboxes.length; i++) {
			vbox.getChildren().add(hboxes[i]);
		}
		getChildren().add(vbox);
	}

	private void createCells() {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				cells[i][j] = new Cell(CellType.EMPTY, this.game, j, i);
			}
		}
		createOutsides();
		createGutters();

		// create Players
		createPlayer1();
		createPlayer2();

	}

	private void createPieceTypeInLine(int line, int[] place, CellType celltype) {
		for (int i : place) {
			cells[line][i] = new Cell(celltype, this.game, i, line);
		}
	}

	private void createGutters() {
		createPieceTypeInLine(0, new int[] { 0, 1, 2, 3, 4, 5, 6 }, CellType.GUTTER);
		createPieceTypeInLine(1, new int[] { 0, 6, 7 }, CellType.GUTTER);
		createPieceTypeInLine(2, new int[] { 0, 7, 8 }, CellType.GUTTER);
		createPieceTypeInLine(3, new int[] { 0, 8, 9 }, CellType.GUTTER);
		createPieceTypeInLine(4, new int[] { 0, 9, 10 }, CellType.GUTTER);
		createPieceTypeInLine(5, new int[] { 0, 10 }, CellType.GUTTER);
		createPieceTypeInLine(6, new int[] { 0, 1, 10 }, CellType.GUTTER);
		createPieceTypeInLine(7, new int[] { 1, 2, 10 }, CellType.GUTTER);
		createPieceTypeInLine(8, new int[] { 2, 3, 10 }, CellType.GUTTER);
		createPieceTypeInLine(9, new int[] { 3, 4, 10 }, CellType.GUTTER);
		createPieceTypeInLine(10, new int[] { 4, 5, 6, 7, 8, 9, 10 }, CellType.GUTTER);

	}

	private void createOutsides() {
		createPieceTypeInLine(0, new int[] { 7, 8, 9, 10 }, CellType.OUTSIDE);
		createPieceTypeInLine(1, new int[] { 8, 9, 10 }, CellType.OUTSIDE);
		createPieceTypeInLine(2, new int[] { 9, 10 }, CellType.OUTSIDE);
		createPieceTypeInLine(3, new int[] { 10 }, CellType.OUTSIDE);
		createPieceTypeInLine(7, new int[] { 0 }, CellType.OUTSIDE);
		createPieceTypeInLine(8, new int[] { 0, 1 }, CellType.OUTSIDE);
		createPieceTypeInLine(9, new int[] { 0, 1, 2 }, CellType.OUTSIDE);
		createPieceTypeInLine(10, new int[] { 0, 1, 2, 3 }, CellType.OUTSIDE);
	}

	private void setPlayerInLine(int line, int[] place, PieceType player) {
		for (int i : place) {
			cells[line][i].addPiece(new Piece(player));
		}
	}

	private void createPlayer1() {
		setPlayerInLine(1, new int[] { 1, 2, 3, 4, 5 }, PieceType.PLAYER1);
		setPlayerInLine(2, new int[] { 1, 2, 3, 4, 5, 6 }, PieceType.PLAYER1);
		setPlayerInLine(3, new int[] { 3, 4, 5 }, PieceType.PLAYER1);
	}

	private void createPlayer2() {
		setPlayerInLine(7, new int[] { 5, 6, 7 }, PieceType.PLAYER2);
		setPlayerInLine(8, new int[] { 4, 5, 6, 7, 8, 9 }, PieceType.PLAYER2);
		setPlayerInLine(9, new int[] { 5, 6, 7, 8, 9 }, PieceType.PLAYER2);
	}

}
