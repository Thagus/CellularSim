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
import systems.UserAgentSystem;
import systems.UserMovementSystem;
import utils.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Thagus on 19/10/16.
 */
public class SimulationView extends Pane{
    private Engine engine;
    private ArrayList<EntitySystem> systemArrayList;

    private int WIDTH = 800, HEIGHT = 600;

    private HashMap<CityBlockComponent.BlockType, ArrayList<CityBlockComponent>> cityBlocksIndex;
    private char[][] cityMap;

    public SimulationView(File file){
        setMaxSize(WIDTH, HEIGHT);
        setMinSize(WIDTH, HEIGHT);
        setClip(new Rectangle(WIDTH, HEIGHT));

        initialize();

        if(file==null) {
            ClassLoader classLoader = getClass().getClassLoader();
            file = new File(classLoader.getResource("map.txt").getFile());
        }

        getChildren().addAll(loadCityBlocks(file));
        getChildren().addAll(createCells());
        getChildren().addAll(createUsers());

        startTick();
    }

    private void initialize(){
        engine = new Engine();

        cityBlocksIndex = new HashMap<>();
        for(CityBlockComponent.BlockType b : CityBlockComponent.BlockType.values()){
            cityBlocksIndex.put(b, new ArrayList<>());
        }

        addSystems();
    }

    private void addSystems(){
        systemArrayList = new ArrayList<>();

        systemArrayList.add(new UserAgentSystem());
        systemArrayList.add(new UserMovementSystem(WIDTH, HEIGHT));

        for(EntitySystem es : systemArrayList){
            engine.addSystem(es);
        }
    }

    private ArrayList<Node> loadCityBlocks(File file){
        int width = WIDTH/Constants.BLOCK_SIZE;
        int height = HEIGHT/Constants.BLOCK_SIZE;

        cityMap = new char[height][width];
        ArrayList<Node> nodes = new ArrayList<>();


        for(int h=0; h<height; h++){
            for(int w=0; w<width; w++){
                cityMap[h][w] = 'T';
            }
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = null;
            int i = 0;
            while ((line = reader.readLine()) != null){
                int j = 0;
                for(char letter : line.toCharArray()){
                    if(i<height && j<width){
                        cityMap[i][j] = letter;     //i row, j column
                    }
                    j++;
                }
                i++;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                //Create entity
                Rectangle rectangle = new Rectangle(j*Constants.BLOCK_SIZE, i*Constants.BLOCK_SIZE, Constants.BLOCK_SIZE, Constants.BLOCK_SIZE);
                CityBlockComponent cityBlockComponent = new CityBlockComponent(rectangle, cityMap[i][j], cityBlocksIndex);
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
