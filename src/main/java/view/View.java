package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

/**
 * Created by Thagus on 28/11/16.
 */
public class View {
    Label receiverCellBlockedCalls, senderCellBlockedCalls, locationBlockedCalls, successfullyStartedCalls, successfullyEndedCalls;

    public Scene createScene(){
        VBox layout = new VBox();
        layout.setSpacing(5);

        layout.getChildren().add(createMenus());

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(new SimulationView(this));
        borderPane.setLeft(createLeftMenu());

        layout.getChildren().add(borderPane);


        return new Scene(layout, 1280, 720);
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

        receiverCellBlockedCalls = new Label("0");
        senderCellBlockedCalls = new Label("0");
        locationBlockedCalls = new Label("0");
        successfullyStartedCalls = new Label("0");
        successfullyEndedCalls = new Label("0");

        leftMenu.getChildren().addAll(successfullyStartedCalls, successfullyEndedCalls, locationBlockedCalls, senderCellBlockedCalls, receiverCellBlockedCalls);

        return leftMenu;
    }

    private void setLabelValues(int successfullyStartedCalls, int successfullyEndedCalls, int locationBlockedCalls, int senderCellBlockedCalls, int receiverCellBlockedCalls){
        this.successfullyStartedCalls.setText(successfullyStartedCalls + "");
        this.successfullyEndedCalls.setText(successfullyEndedCalls + "");
        this.locationBlockedCalls.setText(locationBlockedCalls + "");
        this.senderCellBlockedCalls.setText(senderCellBlockedCalls + "");
        this.receiverCellBlockedCalls.setText(receiverCellBlockedCalls + "");
    }
}
