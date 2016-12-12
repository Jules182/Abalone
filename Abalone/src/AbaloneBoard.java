import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

//draw Hexagonal Grid

// Pieces can move to â€œvalidâ€� locations determined by a complex set of rules.
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
			HBox hbox = new HBox();
			hbox.setAlignment(Pos.CENTER);
			hboxes[i] =  hbox;
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
				cells[i][j] = new Cell(CellType.EMPTY);
			}
		}
		createGutters();
		createOutsides();
	}

	private void createGutters() {
		createGutterInLine(0, new int[] { 0, 1, 2, 3, 4, 5, 6 });
		createGutterInLine(1, new int[] { 0, 6, 7 });
		createGutterInLine(2, new int[] { 0, 7, 8 });
		createGutterInLine(3, new int[] { 0, 8, 9 });
		createGutterInLine(4, new int[] { 0, 9, 10 });
		createGutterInLine(5, new int[] { 0, 10 });
		createGutterInLine(6, new int[] { 0, 1, 10 });
		createGutterInLine(7, new int[] { 1, 2, 10 });
		createGutterInLine(8, new int[] { 2, 3, 10 });
		createGutterInLine(9, new int[] { 3, 4, 10 });
		createGutterInLine(10, new int[] { 4, 5, 6, 7, 8, 9, 10 });

	}

	private void createGutterInLine(int line, int[] place) {
		for (int i : place) {
			cells[line][i] = new Cell(CellType.GUTTER);
		}
	}

	private void createOutsides() {
		createOutsideInLine(0, new int[] { 7, 8, 9, 10 });
		createOutsideInLine(1, new int[] { 8, 9, 10 });
		createOutsideInLine(2, new int[] { 9, 10 });
		createOutsideInLine(3, new int[] { 10 });
		createOutsideInLine(7, new int[] { 0 });
		createOutsideInLine(8, new int[] { 0, 1 });
		createOutsideInLine(9, new int[] { 0, 1, 2 });
		createOutsideInLine(10, new int[] { 0, 1, 2, 3 });

	}

	private void createOutsideInLine(int line, int[] place) {
		for (int i : place) {
			cells[line][i] = new Cell(CellType.OUTSIDE);
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
