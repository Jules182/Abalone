
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Control;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

//class definition for a custom control
class CustomControl extends Control {
	// private fields of the class
	private GameLogic game;
	private AbaloneBoard board;
	private BorderPane mainlayout;
	private Sidebar sidebar;
	// menu
	public MenuBar menubar;
	private MenuItem miReset;
	private MenuItem miQuit;
	private Menu menuGame;

	// constructor for the class
	public CustomControl() {
		// set a default skin and generate a game board
		setSkin(new CustomControlSkin(this));
		mainlayout = new BorderPane();

		menubar = new MenuBar();
		menuGame = new Menu("Game");
		menubar.getMenus().addAll(menuGame);
		miReset = new MenuItem("Reset [SPACE]");
		miQuit = new MenuItem("Quit [ESC]");
		menuGame.getItems().addAll(miReset, miQuit);

		// initialize layout
		game = new GameLogic(this);
		board = new AbaloneBoard(game);
		sidebar = new Sidebar(game);
		sidebar.setPrefWidth(150);

		mainlayout.setTop(menubar);
		mainlayout.setCenter(board);
		mainlayout.setRight(sidebar);
		getChildren().addAll(mainlayout);

		miReset.setOnAction((ActionEvent event) -> {
			resetGame();
		});

		miQuit.setOnAction((ActionEvent event) -> {
			Platform.exit();
		});

		setOnKeyPressed((KeyEvent event) -> {
			if (event.getCode() == KeyCode.SPACE) {
				resetGame();
			}

			if (event.getCode() == KeyCode.ESCAPE)
				Platform.exit();
		});
	}

	public void resetGame() {
		mainlayout.getChildren().removeAll(sidebar, board);

		game = new GameLogic(this);
		sidebar = new Sidebar(game);
		sidebar.setPrefWidth(150);
		board = new AbaloneBoard(game);

		mainlayout.setCenter(board);
		mainlayout.setRight(sidebar);
	}

	// override the resize method
	@Override
	public void resize(double width, double height) {
		// update size of rect
		super.resize(width, height);
		board.resize(width, height);
	}

}