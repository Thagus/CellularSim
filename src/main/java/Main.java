import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import components.CellComponent;
import components.CityBlockComponent;
import components.UserCommitmentComponent;
import components.UserComponent;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import systems.UserMovementSystem;

import java.util.ArrayList;

/**
 * Created by Thagus on 18/10/16.
 */
public class Main extends Application{
    private Engine engine;
    private ArrayList<EntitySystem> systemArrayList;

    private int BLOCK_SIZE = 10;

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cellular simulator");

        VBox layout = new VBox();
        layout.setSpacing(5);

        layout.getChildren().add(createMenus());
        layout.getChildren().add(createCanvas());

        addSystems();

        Scene scene = new Scene(layout, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();

        startTick();
    }

    private BorderPane createCanvas() {
        BorderPane simPane = new BorderPane();
        simPane.setMaxSize(800, 600);

        initialize();
        simPane.getChildren().addAll(createCityBlocks());
        simPane.getChildren().addAll(createCells());
        simPane.getChildren().addAll(createUsers());


        return simPane;
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

    private void initialize(){
        engine = new Engine();


    }

    private void addSystems(){
        systemArrayList = new ArrayList<>();

        systemArrayList.add(new UserMovementSystem(800, 600));

        for(EntitySystem es : systemArrayList){
            engine.addSystem(es);
        }
    }

    private ArrayList<Node> createCityBlocks(){
        int width = 80;
        int height = 60;

        ArrayList<Node> nodes = new ArrayList<>();

        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                Rectangle rectangle = new Rectangle(i*BLOCK_SIZE, j*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                CityBlockComponent cityBlockComponent = new CityBlockComponent(rectangle, CityBlockComponent.BlockType.TRAFFIC);
                Entity cityBlock = new Entity();
                cityBlock.add(cityBlockComponent);

                engine.addEntity(cityBlock);
                nodes.add(rectangle);
            }
        }

        return nodes;
    }

    private ArrayList<Node> createCells(){
        ArrayList<Node> nodes = new ArrayList<>();

        int xBlocks = 80;
        int yBlocks = 60;

        int cellBlockRadius = 5;

        double s = cellBlockRadius*BLOCK_SIZE;
        double r = Math.cos(Math.toRadians(30))*s;
        double h = Math.sin(Math.toRadians(30))*s;

        double blocksPerCell = ((2*Math.cos(Math.toRadians(30))*cellBlockRadius));

        int numRows = 0, numColumns = 0;

        //Calculate how many cell rows
        int yBlocksCovered = 0;
        while(yBlocksCovered<yBlocks){
            if(numRows == 0){
                yBlocksCovered += cellBlockRadius/2;
            }
            else if(numRows%2==0){
                yBlocksCovered += cellBlockRadius;
            }
            else {
                yBlocksCovered += cellBlockRadius*2;
            }
            numRows++;
        }

        //Calculate how many cell columns
        double xBlocksCovered = 0;
        while(xBlocksCovered<xBlocks){
            if(numColumns == 0){
                xBlocksCovered += blocksPerCell/2;
            }
            else {
                xBlocksCovered += blocksPerCell;
            }
            numColumns++;
        }

        for(int i=0; i<numColumns; i++){
            for(int j=0; j<numRows; j++){
                Circle circle;

                if(j%2==0){ //Even row
                    circle = new Circle(i*2*r, j*(h+s), s);
                }
                else{   //Odd row
                    circle = new Circle(i*2*r+r, j*(h+s), s);
                }

                CellComponent cellComponent = new CellComponent(circle);

                Entity cellTower = new Entity();
                cellTower.add(cellComponent);

                engine.addEntity(cellTower);

                nodes.add(circle);
            }
        }
        return nodes;
    }

    private ArrayList<Node> createUsers(){
        int numUsers = 100;
        ArrayList<Node> nodes = new ArrayList<>();


        for(int i=0; i<numUsers; i++){
            Circle circle = new Circle(5, 5, 4);
            UserComponent userComponent = new UserComponent(circle);
            UserCommitmentComponent userCommitmentComponent = new UserCommitmentComponent();

            Entity user = new Entity();
            user.add(userComponent);
            user.add(userCommitmentComponent);

            engine.addEntity(user);

            nodes.add(circle);
        }

        return nodes;
    }

    private void startTick(){
        //Update canvas
        new AnimationTimer() {
            private long lastTime = System.currentTimeMillis();

            @Override
            public void handle(long now) {
                long currentTime = System.currentTimeMillis();
                float delta = (currentTime-lastTime)/1000f;
                lastTime = currentTime;
                //System.out.println(delta);

                for(EntitySystem es : systemArrayList){
                    //es.update(delta);
                }

            }
        }.start();
    }
}
