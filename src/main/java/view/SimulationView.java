package view;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import components.CellComponent;
import components.CityBlockComponent;
import components.UserCommitmentComponent;
import components.UserComponent;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import systems.UserMovementSystem;
import utils.Constants;

import java.util.ArrayList;

/**
 * Created by Thagus on 19/10/16.
 */
public class SimulationView extends Pane{
    private Engine engine;
    private ArrayList<EntitySystem> systemArrayList;

    private int WIDTH = 800, HEIGHT = 600;

    public SimulationView(){
        setMaxSize(WIDTH, HEIGHT);
        setMinSize(WIDTH, HEIGHT);
        setClip(new Rectangle(WIDTH, HEIGHT));

        initialize();

        getChildren().addAll(createCityBlocks());
        getChildren().addAll(createCells());
        getChildren().addAll(createUsers());

        addSystems();
        startTick();
    }

    private void initialize(){
        engine = new Engine();
    }

    private void addSystems(){
        systemArrayList = new ArrayList<>();

        systemArrayList.add(new UserMovementSystem(WIDTH, HEIGHT));

        for(EntitySystem es : systemArrayList){
            engine.addSystem(es);
        }
    }

    private ArrayList<Node> createCityBlocks(){
        int width = WIDTH/Constants.BLOCK_SIZE;
        int height = HEIGHT/Constants.BLOCK_SIZE;

        ArrayList<Node> nodes = new ArrayList<>();

        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                Rectangle rectangle = new Rectangle(i*Constants.BLOCK_SIZE, j*Constants.BLOCK_SIZE, Constants.BLOCK_SIZE, Constants.BLOCK_SIZE);
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

        int xBlocks = WIDTH/Constants.BLOCK_SIZE;
        int yBlocks = HEIGHT/Constants.BLOCK_SIZE;

        double s = Constants.CELL_BLOCK_RADIUS *Constants.BLOCK_SIZE;
        double r = Math.cos(Math.toRadians(30))*s;
        double h = Math.sin(Math.toRadians(30))*s;

        double blocksPerCell = ((2*Math.cos(Math.toRadians(30))*Constants.CELL_BLOCK_RADIUS));

        int numRows = 0, numColumns = 0;

        //Calculate how many cell rows
        int yBlocksCovered = 0;
        while(yBlocksCovered<yBlocks){
            if(numRows == 0){
                yBlocksCovered += Constants.CELL_BLOCK_RADIUS/2;
            }
            else if(numRows%2==0){
                yBlocksCovered += Constants.CELL_BLOCK_RADIUS;
            }
            else {
                yBlocksCovered += Constants.CELL_BLOCK_RADIUS*2;
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
                    es.update(delta);
                }
            }
        }.start();
    }
}
