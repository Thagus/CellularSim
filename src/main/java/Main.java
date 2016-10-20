import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import view.SimulationView;

/**
 * Created by Thagus on 18/10/16.
 */
public class Main extends Application{


    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cellular simulator");

        VBox layout = new VBox();
        layout.setSpacing(5);

        layout.getChildren().add(createMenus());

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(new SimulationView(null));
        borderPane.setLeft(createLeftMenu());

        layout.getChildren().add(borderPane);


        Scene scene = new Scene(layout, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenus(){
        MenuBar menuBar = new MenuBar();

        //File menu
        Menu menuFile = new Menu("_File");

        MenuItem importMap = new MenuItem("Import map...");
        menuFile.getItems().add(importMap);

        menuBar.getMenus().add(menuFile);
        return  menuBar;
    }

    private VBox createLeftMenu(){
        VBox leftMenu = new VBox();
        leftMenu.setAlignment(Pos.TOP_LEFT);
        leftMenu.setSpacing(10);
        leftMenu.setPadding(new Insets(10, 7, 5, 7));

        BorderStrokeStyle boderStyle = new BorderStrokeStyle(StrokeType.INSIDE, StrokeLineJoin.MITER, StrokeLineCap.BUTT, 10, 0, null);
        BorderStroke borderStroke = new BorderStroke(Color.LIGHTGRAY, boderStyle, CornerRadii.EMPTY, new BorderWidths(2), new Insets(2));
        leftMenu.setBorder(new Border(borderStroke));

        leftMenu.setPrefSize(256, 500);


        return leftMenu;
    }
}
