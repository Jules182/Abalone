import javafx.scene.layout.Pane;

public class AbaloneBoard extends Pane {
	
	private Cell[][] cells;

	public AbaloneBoard() {
		// initialise the cells
				cells = new Cell[12][12];
				for (int i = 0; i < 3; i++)
					for (int j = 0; j < 3; j++) {
						// render the XO Boards and pass the GameLogic instance to them
						cells[i][j] = new Cell();
						getChildren().add(cells[i][j]);
					}	
				}

	// draw Hexagonal Grid

	// Pieces can move to “valid” locations determined by a complex set of rules.
	// Valid moves are defined in the Piece/GameLogic class.

	// maps mouse click coordinates to hexagonal grid.

	@Override
	public void resize(double width, double height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
	}

	@Override
	public void relocate(double x, double y) {
		// TODO Auto-generated method stub
		super.relocate(x, y);
	}

}
