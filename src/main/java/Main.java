import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import components.CityBlockComponent;
import components.UserComponent;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
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


        BorderPane pane = new BorderPane();

        initialize();
        pane.getChildren().addAll(createCityBlocks());
        pane.getChildren().addAll(createUsers());
        addSystems();


        final Scene scene = new Scene(pane, 802, 604, Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.show();

        startTick();
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
        int width = 73;
        int height = 55;

        ArrayList<Node> nodes = new ArrayList<>();

        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                Rectangle rectangle = new Rectangle(i*10+i, j*10+j, 10, 10);
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
