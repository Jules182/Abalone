
import javafx.application.Platform;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

//class definition for a custom control
class CustomControl extends Control {
	// constructor for the class
	public CustomControl() {
		// set a default skin and generate a game board
		setSkin(new CustomControlSkin(this));
		board = new AbaloneBoard();
		getChildren().addAll(board);

		setOnKeyPressed((KeyEvent event) -> {
			if (event.getCode() == KeyCode.SPACE) {
				resetBoard();
			}
			if (event.getCode() == KeyCode.ESCAPE)
				Platform.exit();
		});

	}

	public void resetBoard() {
		getChildren().remove(board);
		board = new AbaloneBoard();
		getChildren().add(board);
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