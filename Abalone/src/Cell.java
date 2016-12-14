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
	
	private GameLogic game;
	private boolean isSelectable;

	// http://stackoverflow.com/questions/26850828/how-to-make-a-javafx-button-with-circle-shape-of-3xp-diameter

	public Cell(CellType celltype, GameLogic game) {
		super();
		this.setSkin(new CellSkin(this));
		this.game = game;
		this.isSelectable = false;

		polygon = new Polygon();
		stone = new Piece(PieceType.PLAYER1);
		if (celltype == CellType.EMPTY)
			createHexagon();

		// add some listeners for clicks
		setOnMouseClicked((MouseEvent event) -> {
			if (isSelectable)
				if (getChildren().contains(stone))
				stone.setSelectColor();
				else 
				//TODO: validity of selection
				polygon.setFill(game.getCurrentPlayer().getSelectColor());
		}
	);
	}

	private void createHexagon() {
		polygon.getPoints().addAll(makeVertices(this.radius, this.sides));
		polygon.setStroke(Color.BLACK);
		polygon.setFill(Color.LIGHTBLUE);
		this.setShape(polygon);
		getChildren().add(polygon);
	}

	public void setPiece(PieceType player) {
		stone = new Piece(player);
		getChildren().add(stone);
		setSelectable(true);
	}

	// Tutorial to make this method
	// http://stackoverflow.com/questions/7198144/how-to-draw-a-n-sided-regular-circle-in-cartesian-coordinates
	public Double[] makeVertices(int radius, int sides) {
		Double[] vertices = new Double[sides * 2];
		int indexInVerticesArray = 0;
		for (int n = 1; n <= sides; n++) {
			vertices[indexInVerticesArray++] = radius * Math.cos((2 * Math.PI * n + Math.PI) / sides);// x coordinate
			vertices[indexInVerticesArray++] = radius * Math.sin((2 * Math.PI * n + Math.PI) / sides);// y coordinate
		}
		return vertices;
	}
	
	public boolean isSelectable() {
		return this.isSelectable;
	}
	

	public void resize(double width, double height) {
		// update the size of the rectangle
		super.resize(width, height);
		// polygon.getPoints().removeAll(makeVertices(this.radius, this.sides));
		// this.radius = (int) width;
		// polygon.getPoints().addAll(makeVertices(this.radius, this.sides));
		//// this.polygon.resize(width, height);
	}

	public void relocate(double x, double y) {
		super.relocate(x, y);
		this.polygon.relocate(x, y);
	}

	private int sides = 6;
	private int radius = 30;
	Polygon polygon;
	private Piece stone;

	public void setSelectable(boolean isSelectable) {
		this.isSelectable = isSelectable;
	}
}