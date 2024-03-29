import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * This is an element of the Hexagonal board which will have neighbours
 */

public class Cell extends Control {

	private int sides = 6;
	private int radius = 30;
	Polygon polygon;
	private Piece piece;
	private int xLocation;
	private CellType celltype;

	private int yLocation;

	public Cell(CellType celltype, GameLogic game, int xLocation, int yLocation) {

		super();

		this.setSkin(new CellSkin(this));
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		this.celltype = celltype;

		polygon = new Polygon();
		piece = new Piece(PieceType.EMPTY);
		if (celltype == CellType.CELL)
			createHexagon();

		setOnMouseClicked((MouseEvent event) -> {
			// if Cell is selectable, not already selected and holds a piece of the current player, select it
			if (game.isSelectable(this) && (!game.isSelected(this)) && (hasPieceOf(game.getCurrentPlayer()))) {
				game.select(this);
				game.checkDestinations();
			}
			// if Cell was recently selected, deselect it
			else if (game.isLastSelected(this)) {
				game.deselect(this);
				game.checkDestinations();
				// if it is a destination, try to move it
			} else if (game.isDestination(this) && (game.getNumberOfSelectedCells() != 0)) {
				// move here
				game.move(this);
			}
		});
	}

	public void addPiece(Piece piece) {
		this.piece = piece;
		if (piece.getPlayer() != PieceType.EMPTY)
			getChildren().add(this.piece);
	}

	public Piece removePiece() {
		Piece oldPiece = this.piece;
		getChildren().remove(this.piece);
		this.piece = new Piece(PieceType.EMPTY);
		return oldPiece;
	}

	public boolean hasPieceOf(PieceType player) {
		return (celltype == CellType.CELL && player == piece.getPlayer());
	}

	public boolean isEmptyCell() {
		return ((celltype == CellType.CELL) && hasPieceOf(PieceType.EMPTY));
	}

	public boolean isPlayerCell() {
		return ((celltype == CellType.CELL) && (!hasPieceOf(PieceType.EMPTY)));
	}

	public boolean isGutter() {
		return (celltype == CellType.GUTTER);
	}

	// Tutorial to make this method:
	// http://stackoverflow.com/questions/7198144/how-to-draw-a-n-sided-regular-circle-in-cartesian-coordinates
	private Double[] makeVertices(int radius, int sides) {
		Double[] vertices = new Double[sides * 2];
		int indexInVerticesArray = 0;
		for (int n = 1; n <= sides; n++) {
			vertices[indexInVerticesArray++] = radius * Math.cos((2 * Math.PI * n + Math.PI) / sides); // x
			vertices[indexInVerticesArray++] = radius * Math.sin((2 * Math.PI * n + Math.PI) / sides); // y
		}
		return vertices;
	}

	private void createHexagon() {
		polygon.getPoints().addAll(makeVertices(this.radius, this.sides));
		polygon.setStroke(Color.BLACK);
		polygon.setFill(Color.LIGHTBLUE);
		this.setShape(polygon);
		getChildren().add(polygon);
	}

	public int getxLocation() {
		return xLocation;
	}

	public int getyLocation() {
		return yLocation;
	}

	public Piece getPiece() {
		return piece;
	}

	public CellType getCellType() {
		return celltype;
	}

}