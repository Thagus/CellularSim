import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import components.CityBlockComponent;
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

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cellular simulator");

        VBox layout = new VBox();
        layout.setSpacing(5);

        layout.getChildren().add(createMenus());
        layout.getChildren().add(createCanvas());


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
        simPane.getChildren().addAll(createUsers());
        addSystems();


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
                Rectangle rectangle = new Rectangle(i*10, j*10, 10, 10);
                CityBlockComponent cityBlockComponent = new CityBlockComponent(rectangle, CityBlockComponent.BlockType.TRAFFIC);
                Entity cityBlock = new Entity();
                cityBlock.add(cityBlockComponent);

                engine.addEntity(cityBlock);
                nodes.add(rectangle);
            }
        }

        return nodes;
    }

    private void createCells(){

    }

    private ArrayList<Node> createUsers(){
        int numUsers = 10;
        ArrayList<Node> nodes = new ArrayList<>();


        for(int i=0; i<numUsers; i++){
            Circle circle = new Circle(5);
            UserComponent userComponent = new UserComponent(circle);

            Entity user = new Entity();
            user.add(userComponent);

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
                    es.update(delta);
                }

            }
        }.start();
    }
}
