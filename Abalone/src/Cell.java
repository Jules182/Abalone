import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * This is an element of the Hexagonal board which will have neighbours
 */

public class Cell extends Control {

	private GameLogic game;
	private int sides = 6;
	private int radius = 30;
	private Polygon polygon;
	private Piece piece;
	private int xLocation;
	private CellType celltype;

	private int yLocation;

	public Cell(CellType celltype, GameLogic game, int xLocation, int yLocation) {

		super();

		this.setSkin(new CellSkin(this));
		this.game = game;
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		this.celltype = celltype;

		polygon = new Polygon();
		piece = new Piece(PieceType.DEFAULT);
		if (celltype == CellType.EMPTY)
			createHexagon();

		setOnMouseClicked((MouseEvent event) -> {
			System.out.println("Clicked x=" + xLocation + " y=" + yLocation);
			// if Cell is selectable, not already selected and holds a piece of
			// the current player
			if (game.isSelectable(this) && (!game.isSelected(this)) && (hasPieceOf(game.getCurrentPlayer()))) {
				// select
				game.select(this);

				unmarkDestinations();
				game.checkDestinations();
				markDestinations();
			}
			// if Cell was recently selected
			else if (game.isLastSelected(this)) {
				// deselect
				game.deselect(this);

				unmarkDestinations();
				if (game.getNumberOfSelectedCells() > 0) {
					game.checkDestinations();
					markDestinations();
				}
			} else if (game.isDestination(this) && (game.getNumberOfSelectedCells() != 0)
					&& (getPiece().getPlayer() != game.getCurrentPlayer())) {
				// move here
				game.move(this);
				game.checkForWinner();

				unmarkDestinations();
			}

		});
	}

	private void markDestinations() {
		for (Cell destination : game.getDestinations()) {
			destination.polygon.setFill(Color.LIGHTSTEELBLUE);
		}
	}

	private void unmarkDestinations() {
		for (Cell destination : game.getDestinations()) {
			destination.polygon.setFill(Color.LIGHTBLUE);
		}
	}

	public void addPiece(Piece piece) {
		this.piece = piece;
		if (piece.getPlayer() != PieceType.DEFAULT)
			getChildren().add(this.piece);
	}

	public Piece removePiece() {
		Piece oldPiece = this.piece;
		getChildren().remove(this.piece);
		this.piece = new Piece(PieceType.DEFAULT);
		return oldPiece;
	}

	public boolean hasPieceOf(PieceType player) {
			return (player == piece.getPlayer());
	}
	
	public boolean isEmptyCell() {
		return ((celltype == CellType.EMPTY) && hasPieceOf(PieceType.DEFAULT));
	}
	
	public boolean isPlayerCell() {
		return ((celltype == CellType.EMPTY) && (!hasPieceOf(PieceType.DEFAULT)));
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