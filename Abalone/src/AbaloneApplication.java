import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AbaloneApplication extends Application {
	private VBox sp_mainlayout;
	private CustomControl cc_custom;
	//menu
	private MenuBar menubar;
	private MenuItem mi_quit;
	private MenuItem mi_hello;
	private Menu menuFile;
	private Menu menuHelp;

	@Override
	public void init() {
		// initialise the pane, add menu and CustomControl
		sp_mainlayout = new VBox();
		cc_custom = new CustomControl();
		
		menubar = new MenuBar();
		menuFile = new Menu("File");
		menuHelp = new Menu("Help");
		menubar.getMenus().addAll(menuFile, menuHelp);
		mi_hello = new MenuItem("Hello");
		mi_quit = new MenuItem("Quit");
		menuFile.getItems().addAll(mi_hello, mi_quit);
		sp_mainlayout.getChildren().addAll(menubar, cc_custom);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Abalone Game Team Markus&Julian");
		primaryStage.setScene(new Scene(sp_mainlayout, 600, 600));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void stop() throws Exception {
		
	}
	
}
