import javafx.scene.paint.Color;

public enum PlayerType {

	PLAYER1(Color.GREEN), PLAYER2(Color.RED), PLAYER3(Color.BLUE);
	
	private Color color;

	PlayerType(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
	}


}
