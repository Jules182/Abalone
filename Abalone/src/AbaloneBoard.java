import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

//draw Hexagonal Grid

// Pieces can move to “valid” locations determined by a complex set of rules.
// Valid moves are defined in the Piece/GameLogic class.

// maps mouse click coordinates to hexagonal grid.

public class AbaloneBoard extends Pane {

	private double cell_width, cell_height; // width and height of a cell
	private VBox vbox;
	private HBox[] hboxes;
	private Cell[][] cells = new Cell[11][11];

	public AbaloneBoard() {
		
		createCells();
		
		vbox = new VBox(); // VBox with 10px spacing
		hboxes = new HBox[11];
		for (int i = 0; i < hboxes.length; i++) {
			hboxes[i] = new HBox();
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
				cells[i][j] = new Cell();
			}
		}
	}


	@Override
	public void resize(double width, double height) {
		// TODO Auto-generated method stub
		super.resize(width, height);

		// figure out the width and height of a cell
		cell_width = 20;
		cell_height = 20;

			// we need to reset the sizes and positions of all Cells
			for (int i = 0; i < cells.length; i++) {
				for (int j = 0; j < cells[i].length; j++) {
					cells[i][j].relocate(i * cell_width, j * cell_height);
					cells[i][j].resize(cell_width, cell_height);
			}
			}
			
		
	}
}
