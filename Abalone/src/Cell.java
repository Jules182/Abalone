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

	private int yLocation;

	public Cell(CellType celltype, GameLogic game, int xLocation, int yLocation) {

		super();

		this.setSkin(new CellSkin(this));
		this.game = game;
		this.xLocation = xLocation;
		this.yLocation = yLocation;

		polygon = new Polygon();
		piece = new Piece(PieceType.DEFAULT);
		if (celltype == CellType.EMPTY)
			createHexagon();

		setOnMouseClicked((MouseEvent event) -> {
			unmarkDestinations();
			System.out.println("Clicked x=" + xLocation + " y=" + yLocation);
			// if Cell is selectable and not already selected
			if (game.isSelectable(this) && !game.isSelected(this)) {
				// if it holds a piece of the current player
				if (hasPieceOf(game.getCurrentPlayer())) {

					// update selectablePieces properly depending on how many pieces are selected
					// if 1st piece:
					if (game.getNumberOfSelectedCells() == 0) {
						game.select(this);
						game.findAllNeighbours();
					}

					// if 2nd piece:
					else if (game.getNumberOfSelectedCells() == 1) {
						if (game.isSelectable(this)) {
							game.select(this);
						}
						game.findThirdInLine();
					}

					// if 3rd piece:
					else if (game.getNumberOfSelectedCells() == 2) {
						if (game.isSelectable(this)) {
							game.select(this);
						}
						game.emptySelectableCells();
					}

					game.checkDestinations();
					markDestinations();
				}
			} else if (game.isLastSelected(this)) {
				// deselect this piece
				game.deselect(this);

				if (game.getNumberOfSelectedCells() == 0)
					game.initializeSelectable();
				else if (game.getNumberOfSelectedCells() == 1)
					game.findAllNeighbours();
				else if (game.getNumberOfSelectedCells() == 2)
					game.findThirdInLine();

				if (game.getNumberOfSelectedCells() > 0) {
					game.checkDestinations();
					markDestinations();
				}

			}
			// move here
			else if (game.isDestination(this) && game.getNumberOfSelectedCells() != 0
					&& getPiece().getPlayer() == PieceType.DEFAULT) {
				game.move(this);
				game.changePlayer();

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
		Piece piece = this.piece;
		getChildren().remove(this.piece);
		this.piece = new Piece(PieceType.DEFAULT);
		return piece;
	}

	public boolean hasPieceOf(PieceType player) {
		return (player == piece.getPlayer());
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

	public void setxLocation(int xLocation) {
		this.xLocation = xLocation;
	}

	public int getyLocation() {
		return yLocation;
	}

	public void setyLocation(int yLocation) {
		this.yLocation = yLocation;
	}

	public Piece getPiece() {
		return piece;
	}

	public void setBall(Piece ball) {
		this.piece = ball;
	}
}