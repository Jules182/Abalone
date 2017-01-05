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
	private boolean piece;
	private int sides = 6;
	private int radius = 30;
	private Polygon polygon;
	private Piece stone;
	private int xLocation;

	private int yLocation;

	// http://stackoverflow.com/questions/26850828/how-to-make-a-javafx-button-with-circle-shape-of-3xp-diameter

	public Cell(CellType celltype, GameLogic game, int xLocation, int yLocation) {

		super();

		this.setSkin(new CellSkin(this));
		this.game = game;
		this.piece = false;
		this.xLocation = xLocation;
		this.yLocation = yLocation;

		polygon = new Polygon();
		stone = new Piece(PieceType.DEFAULT);
		if (celltype == CellType.EMPTY)
			createHexagon();

		// add some listeners for clicks
		setOnMouseClicked((MouseEvent event) -> {
			if (game.isSelectable(this) && !game.isSelected(this)) {
				// if it holds a piece
				if (hasPiece(game.getCurrentPlayer())) {

					if (!game.isSelected(this)) {

						// selected in order to update selectablePieces properly
						// if 1st piece:
						if (game.getNumberOfSelectedCells() == 0) {
							game.setSelected(this);
							stone.setSelectColor();
							game.findAllNeighbours();
						}

						// if 2nd piece:
						else if (game.getNumberOfSelectedCells() == 1) {
							if (game.isSelectable(this)) {
								game.setSelected(this);
								stone.setSelectColor();
							}
							game.findThirdInLine();
						}

						// if 3rd piece:
						else if (game.getNumberOfSelectedCells() == 2) {
							if (game.isSelectable(this)) {
								game.setSelected(this);
								stone.setSelectColor();
							}
							game.emptySelectableCells();
						}
					}
				} else {
					// TODO if possible, move selected pieces one step into the
					// direction of this piece
					polygon.setFill(game.getCurrentPlayer().getSelectColor());
				}
			} else if (game.isLastSelected(this)) {
				// unselect this piece
				game.deSelect(this);
				stone.setDeselectColor();

				if (game.getNumberOfSelectedCells() == 0)
					game.initializeSelectable();
				else if (game.getNumberOfSelectedCells() == 1)
					game.findAllNeighbours();
				else if (game.getNumberOfSelectedCells() == 2)
					game.findThirdInLine();
			}

		});
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
		this.piece = true;
	}

	public boolean hasPiece(PieceType player) {
		// TODO: in piece variable gleich den Player storen??
		return piece && (player == stone.getPlayer());
	}

	public void setHasPiece(boolean hasPiece) {
		this.piece = hasPiece;
	}

	// Tutorial to make this method
	// http://stackoverflow.com/questions/7198144/how-to-draw-a-n-sided-regular-circle-in-cartesian-coordinates
	private Double[] makeVertices(int radius, int sides) {
		Double[] vertices = new Double[sides * 2];
		int indexInVerticesArray = 0;
		for (int n = 1; n <= sides; n++) {
			vertices[indexInVerticesArray++] = radius * Math.cos((2 * Math.PI * n + Math.PI) / sides);// x
																										// coordinate
			vertices[indexInVerticesArray++] = radius * Math.sin((2 * Math.PI * n + Math.PI) / sides);// y
																										// coordinate
		}
		return vertices;
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
}