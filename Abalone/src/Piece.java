
import javafx.scene.Group;
import javafx.scene.shape.Circle;

class Piece extends Group {
	// constructor for the class
	public Piece(PieceType player) {
		this.player = player;
		ball = new Circle(20);
		getChildren().addAll(ball);
		ball.setFill(player.getPieceColor());
	}

	public PieceType getPlayer() {
		return player;
	}
	
	public void setSelectColor() {
		ball.setFill(player.getSelectColor());
	}
	
	public void setPieceColor() {
		ball.setFill(player.getPieceColor());
	}

	// private fields of the class
	private Circle ball; // ellipse for rendering the piece
	private PieceType player; // maintain a copy of the piece type we have
	

}