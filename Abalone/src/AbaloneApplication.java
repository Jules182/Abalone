import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AbaloneApplication extends Application {

	private GameLogic game;

	private BorderPane mainlayout;
	private CustomControl cc_custom;
	// menu
	public MenuBar menubar;
	private MenuItem miReset;
	private MenuItem miQuit;
	private Menu menuGame;

	private Sidebar sidebar;

	@Override
	public void init() {
		// initialise the pane, add menu and CustomControl
		game = new GameLogic();
		mainlayout = new BorderPane();
		cc_custom = new CustomControl(game);
		sidebar = new Sidebar(game);
		sidebar.setPrefWidth(150);

		// menu bar
		menubar = new MenuBar();
		menuGame = new Menu("Game");
		menubar.getMenus().addAll(menuGame);

		miReset = new MenuItem("Reset [SPACE]");
		miQuit = new MenuItem("Quit [ESC]");

		menuGame.getItems().addAll(miReset, miQuit);

		// add panes
		mainlayout.setTop(menubar);
		mainlayout.setCenter(cc_custom);
		mainlayout.setRight(sidebar);

		miReset.setOnAction((ActionEvent event) -> {
			cc_custom.resetBoard();
		});

		miQuit.setOnAction((ActionEvent event) -> {
			Platform.exit();
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Abalone Game Team Markus&Julian");
		Scene scene = new Scene(mainlayout, 750, 600);
		scene.getStylesheets().add("style.css");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void stop() throws Exception {

	}

}
