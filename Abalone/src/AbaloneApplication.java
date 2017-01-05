import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AbaloneApplication extends Application {
	private VBox mainlayout;
	private CustomControl cc_custom;
	// menu
	public MenuBar menubar;
	private MenuItem miReset;
	private MenuItem miQuit;
	private Menu menuGame;

	@Override
	public void init() {
		// initialise the pane, add menu and CustomControl
		mainlayout = new VBox();
		cc_custom = new CustomControl();

		menubar = new MenuBar();
		menuGame = new Menu("Game");
		menubar.getMenus().addAll(menuGame);

		miReset = new MenuItem("Reset");
		miQuit = new MenuItem("Quit");

		menuGame.getItems().addAll(miReset, miQuit);
		mainlayout.getChildren().addAll(menubar, cc_custom);

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
		primaryStage.setScene(new Scene(mainlayout, 600, 600));
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
