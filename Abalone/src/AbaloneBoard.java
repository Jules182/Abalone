import javafx.scene.layout.Pane;

//draw Hexagonal Grid

// Pieces can move to “valid” locations determined by a complex set of rules.
// Valid moves are defined in the Piece/GameLogic class.

// maps mouse click coordinates to hexagonal grid.

public class AbaloneBoard extends Pane {

	private Board board;
	private Cell testCell;
	private double cell_width, cell_height; // width and height of a cell

	public AbaloneBoard() {
		// initialise the cells
		board = new Board();

		for (Cell[] row : board.getCells()) {
			for (Cell cell : row) {
				getChildren().add(cell);
			}
		}

	}

	@Override
	public void resize(double width, double height) {
		// TODO Auto-generated method stub
		super.resize(width, height);

		// figure out the width and height of a cell
		cell_width = width / 9.0;
		cell_height = height / 9.0;

		// we need to reset the sizes and positions of all XOPieces that were placed
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board.getCell(i, j).relocate(i * cell_width, j * cell_height);
				board.getCell(i, j).resize(cell_width, cell_height);
			}
		}
	}

}
