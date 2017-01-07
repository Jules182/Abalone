
import javafx.scene.Group;
import javafx.scene.shape.Circle;

class Piece extends Group {
	// constructor for the class
	public Piece(PieceType player) {
		this.player = player;
		stone = new Circle(20);
		getChildren().addAll(stone);
		stone.setFill(player.getPieceColor());
	}

	public PieceType getPlayer() {
		return player;
	}
	
	public void setSelectColor() {
		stone.setFill(player.getSelectColor());
	}
	
	public void setDeselectColor() {
		stone.setFill(player.getPieceColor());
	}

	// private fields of the class
	private Circle stone; // ellipse for rendering the piece
	private PieceType player; // maintain a copy of the piece type we have
	

}