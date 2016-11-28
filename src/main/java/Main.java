import javafx.application.Application;
import javafx.stage.Stage;
import view.View;

/**
 * Created by Thagus on 18/10/16.
 */
public class Main extends Application{
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cellular simulator");

        View view = new View();

        primaryStage.setScene(view.createScene());
        primaryStage.show();
    }
}
