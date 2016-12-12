import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Class which defines a hexagonal button
 * 
 * This is an element of the Hexagonal board which will have neighbours
 * 
 * @author alexcronin
 *
 */
public class Cell extends Control {

	// Tutorial to make this button
	// http://stackoverflow.com/questions/26850828/how-to-make-a-javafx-button-with-circle-shape-of-3xp-diameter

	public Cell(CellType celltype) {
		super();
		this.setSkin(new CellSkin(this));
		polygon = new Polygon();
		if (celltype == CellType.PLAYER1 || celltype == CellType.PLAYER2 || celltype == CellType.EMPTY){
			polygon.getPoints().addAll(makeVertices(this.radius, this.sides));
			polygon.setStroke(Color.RED);
			this.setShape(polygon);
			this.setPickOnBounds(false);
			getChildren().add(polygon);
			
			// add some listeners for clicks
			setOnMouseClicked((MouseEvent event) -> {
				polygon.setFill(Color.GREEN);
			});
		}
	}

	// Tutorial to make this method
	// http://stackoverflow.com/questions/7198144/how-to-draw-a-n-sided-regular-circle-in-cartesian-coordinates
	public Double[] makeVertices(int radius, int sides) {
		Double[] vertices = new Double[sides * 2];
		int indexInVerticesArray = 0;
		for (int i = 1; i <= sides; i++) {
			vertices[indexInVerticesArray++] = radius * Math.cos((2 * Math.PI * i) / sides);// x coordinate
			vertices[indexInVerticesArray++] = radius * Math.sin((2 * Math.PI * i) / sides);// y coordinate
		}
		return vertices;
	}

	public void resize(double width, double height) {
		// update the size of the rectangle
		super.resize(width, height);
		this.polygon.resize(width, height);
	}

	public void relocate(double x, double y) {
		// update the size of the rectangle
		super.relocate(x, y);
		this.polygon.relocate(x, y);
	}

	private int sides = 6;
	private int radius = 20;
	Polygon polygon;
}