import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import view.View;

/**
 * Created by Thagus on 18/10/16.
 */
public class Main extends Application{
    private Stage window;

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cellular simulator");
        this.window = primaryStage;

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        View view = new View();

        primaryStage.setScene(view.createScene());
        primaryStage.show();
    }

    /**
     * Method to close the program after checking with the user
     */
    private void closeProgram(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to quit?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.setTitle("Exit");
        alert.setHeaderText(null);

        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES){
            window.close();
            System.exit(0);
        }
    }
}
