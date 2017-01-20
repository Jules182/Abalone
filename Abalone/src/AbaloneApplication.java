import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AbaloneApplication extends Application {

	private CustomControl customControl;

	@Override
	public void init() {
		customControl = new CustomControl();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Abalone Game Team Markus&Julian");
		Scene scene = new Scene(customControl, 750, 530);
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
