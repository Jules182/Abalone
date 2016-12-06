import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;

//draw Hexagonal Grid

// Pieces can move to “valid” locations determined by a complex set of rules.
// Valid moves are defined in the Piece/GameLogic class.

// maps mouse click coordinates to hexagonal grid.

public class AbaloneBoard extends Pane {

	private Board board;
	private Cell testCell;
	private double cell_width, cell_height; // width and height of a cell
	private VBox vbox;
	private HBox[] hboxes;
	private Button button;
	private Cell[][] cells = new Cell[11][11];

	public AbaloneBoard() {
		// initialise the cells
//		board = new Board();
		//not using board container anymore
		
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				cells[i][j] = new Cell();
			}
		}
		
		vbox = new VBox(5); // VBox with 10px spacing
		hboxes = new HBox[11];
		for (int i = 0; i < hboxes.length; i++) {
			hboxes[i] = new HBox(5);
		}

		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				Button btn = new Button("haha, java");
				Polygon triangle = createStartingTriangle();
				btn.setShape(new Cell().getShape());
				Cell cell = cells[i][j];
				// TODO: why can I add the btn but not the Cell (Control)
				hboxes[i].getChildren().add(btn);
			}
		}		
		
		for (int i = 0; i < hboxes.length; i++) {
			vbox.getChildren().add(hboxes[i]);
		}
		getChildren().add(vbox);
	}


	@Override
	public void resize(double width, double height) {
		// TODO Auto-generated method stub
		super.resize(width, height);

		// figure out the width and height of a cell
		cell_width = 50;
		cell_height = 50;

		// we need to reset the sizes and positions of all Cells
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				cells[i][j].relocate(i * cell_width, j * cell_height);
				cells[i][j].resize(cell_width, cell_height);
			}
		}
	}
	
	
	//copied from online to test button shaping
	private Polygon createStartingTriangle() {
	    Polygon triangle = new Polygon();

	    triangle.getPoints().setAll(
	        100d, 100d,
	        150d, 50d,
	        250d, 150d
	    );

	    triangle.setStroke(Color.FORESTGREEN);
	    triangle.setStrokeWidth(4);
	    triangle.setStrokeLineCap(StrokeLineCap.ROUND);
	    triangle.setFill(Color.CORNSILK.deriveColor(0, 1.2, 1, 0.6));

	    return triangle;
	  }

}
