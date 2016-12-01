
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;

//class definition for a custom control
class CustomControl extends Control {
	// constructor for the class
	public CustomControl() {
		// set a default skin and generate a game board
		setSkin(new CustomControlSkin(this));
		board = new AbaloneBoard();
		getChildren().add(board);

		// add a mouse clicked listener that will try to place a piece
		setOnMouseClicked((MouseEvent event) -> {
			//board.placePiece(event.getX(), event.getY());
		});

		setOnKeyPressed((KeyEvent event) -> {
			if (event.getCode() == KeyCode.SPACE) {
				//board.resetGame();
			}
			if (event.getCode() == KeyCode.ESCAPE)
				Platform.exit();
		});

	}

	// override the resize method
	@Override
	public void resize(double width, double height) {
		// update size of rect
		super.resize(width, height);
		board.resize(width, height);
	}

	// private fields of the class
	private AbaloneBoard board;
}