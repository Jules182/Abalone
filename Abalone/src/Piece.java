
import javafx.scene.Group;
import javafx.scene.shape.Circle;

// class definition for an X or O piece
class Piece extends Group {
	// constructor for the class
	public Piece(PieceType player) {
		this.player = player;
		// create a new translate object and take a copy of the type
		stone = new Circle(20);
		getChildren().addAll(stone);
		stone.setFill(player.getPieceColor());

	}
	
	public void setSelectColor() {
		stone.setFill(player.getSelectColor());
	}
	
	public void setDeselectColor() {
		stone.setFill(player.getPieceColor());
	}

	// private fields of the class
	private Circle stone; // ellipse for rendering the O piece
	private PieceType player; // maintain a copy of the piece type we have
	public PieceType getPlayer() {
		return player;
	}

}