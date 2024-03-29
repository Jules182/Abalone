import javafx.scene.paint.Color;

public enum PieceType {

	EMPTY(Color.BLACK, Color.BLACK),
	PLAYER1(Color.GREEN, Color.LIMEGREEN), 
	PLAYER2(Color.RED, Color.LIGHTCORAL);

	private Color pieceColor;
	private Color selectColor;

	PieceType(Color pieceColor, Color selectColor) {
		this.pieceColor = pieceColor;
		this.selectColor = selectColor;
	}

	public Color getPieceColor() {
		return this.pieceColor;
	}

	public Color getSelectColor() {
		return this.selectColor;
	}

}
