package view;

import actors.CallManager;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import components.*;
import dataObjects.Profile;
import dataObjects.TechnologyStandard;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import systems.CallSystem;
import systems.PagingSystem;
import systems.UserAgentSystem;
import systems.UserMovementSystem;
import utils.Constants;
import utils.Variables;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Thagus on 19/10/16.
 */
public class SimulationView extends Pane{
    private Engine engine;
    private View view;
    private TechnologyStandard technology;
    private ArrayList<EntitySystem> systemArrayList;

    private ArrayList<Profile> profiles;

    private int WIDTH = 800, HEIGHT = 600;

    private HashMap<CityBlockComponent.BlockType, ArrayList<CityBlockComponent>> cityBlocksIndex;
    private char[][] cityMap;

    public SimulationView(View view, TechnologyStandard technology){
        this.view = view;
        this.technology = technology;

        //Set constants based on technology
        if(technology!=null){
            Constants.CELL_CALL_LIMIT = technology.getUsersPerCell();
            Constants.CELL_BLOCK_RADIUS = (int) ((technology.getCellRadius()*1000)/(Constants.BLOCK_SIZE_METERS));
        }

        setMaxSize(WIDTH, HEIGHT);
        setMinSize(WIDTH, HEIGHT);
        setClip(new Rectangle(WIDTH, HEIGHT));

        initialize();

        InputStream mapStream = getClass().getResourceAsStream("/map.txt");

        getChildren().addAll(loadCityBlocks(mapStream));
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

        long time = 0;

        try {
            time = new SimpleDateFormat("HH:mm:ss").parse("00:00:00").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Initialize clock
        Constants.clock = new Time(time);

        loadProfiles();
        addSystems();
    }

    private void addSystems(){
        systemArrayList = new ArrayList<>();

        systemArrayList.add(new UserAgentSystem(cityBlocksIndex));
        systemArrayList.add(new UserMovementSystem(WIDTH, HEIGHT));
        PagingSystem pagingSystem = new PagingSystem();
        systemArrayList.add(pagingSystem);

        final ActorSystem system = ActorSystem.create("System");
        ActorRef manager = system.actorOf(Props.create(CallManager.class));

        systemArrayList.add(new CallSystem(pagingSystem, manager));

        for(EntitySystem es : systemArrayList){
            engine.addSystem(es);
        }
    }

    private ArrayList<Node> loadCityBlocks(InputStream file){
        int width = WIDTH/Constants.BLOCK_SIZE_PIXELS;
        int height = HEIGHT/Constants.BLOCK_SIZE_PIXELS;

        cityMap = new char[height][width];
        ArrayList<Node> nodes = new ArrayList<>();


        for(int h=0; h<height; h++){
            for(int w=0; w<width; w++){
                cityMap[h][w] = 'T';
            }
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));

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
                Rectangle rectangle = new Rectangle(j*Constants.BLOCK_SIZE_PIXELS, i*Constants.BLOCK_SIZE_PIXELS, Constants.BLOCK_SIZE_PIXELS, Constants.BLOCK_SIZE_PIXELS);
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

        int xBlocks = WIDTH/Constants.BLOCK_SIZE_PIXELS;
        int yBlocks = HEIGHT/Constants.BLOCK_SIZE_PIXELS;

        double s = Constants.CELL_BLOCK_RADIUS *Constants.BLOCK_SIZE_PIXELS;
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

                CellComponent cellComponent = new CellComponent(circle, i+j);

                Entity cellTower = new Entity();
                cellTower.add(cellComponent);

                engine.addEntity(cellTower);

                nodes.add(circle);
            }
        }
        return nodes;
    }

    private void loadProfiles(){
        profiles = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(getClass().getResourceAsStream("/profiles.xml"));

            doc.getDocumentElement().normalize();

            NodeList profilesList = doc.getElementsByTagName("profile");

            for (int prof = 0; prof < profilesList.getLength(); prof++) {
                org.w3c.dom.Node nNode = profilesList.item(prof);

                if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    //Create profile object
                    Profile profile = new Profile(
                            eElement.getAttribute("name"),
                            eElement.getElementsByTagName("workArea").item(0).getTextContent()
                    );

                    org.w3c.dom.Node scheduleNode = eElement.getElementsByTagName("schedule").item(0);  //Obtain schedule tag, get the 0 as there is only one schedule tag per profile

                    if (scheduleNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        NodeList entry = ((Element) scheduleNode).getElementsByTagName("entry");

                        for(int entryCount=0; entryCount<entry.getLength(); entryCount++){
                            org.w3c.dom.Node entryNode = entry.item(entryCount);

                            if (entryNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                Element entryElement = (Element) entryNode; //Treat the node as an element
                                org.w3c.dom.Node placesNode = entryElement.getElementsByTagName("places").item(0);  //There is only one places tag per entry tag

                                ArrayList<Pair<String, Double>> places = new ArrayList<>();

                                if(placesNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE){
                                    NodeList place = ((Element)placesNode).getElementsByTagName("place");

                                    for(int placeCount=0; placeCount<place.getLength(); placeCount++){
                                        org.w3c.dom.Node placeNode = place.item(placeCount);

                                        if(placeNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE){
                                            Element placeElement = (Element) placeNode;

                                            places.add(new Pair<>(
                                                    placeElement.getElementsByTagName("area").item(0).getTextContent(),
                                                    Double.parseDouble(placeElement.getElementsByTagName("probability").item(0).getTextContent())
                                            ));
                                        }
                                    }
                                }

                                profile.addScheduleEntry(
                                        entryElement.getElementsByTagName("start").item(0).getTextContent(),
                                        entryElement.getElementsByTagName("end").item(0).getTextContent(),
                                        places
                                );
                            }
                        }
                    }

                    profiles.add(profile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private ArrayList<Node> createUsers(){
        ArrayList<Node> nodes = new ArrayList<>();

        Random random = new Random();

        int usersPerProfile = Constants.NUMBER_OF_USERS / profiles.size();

        int currentProfileChange = usersPerProfile;
        int profile = 0;

        for(int i=0; i<Constants.NUMBER_OF_USERS; i++){
            Circle circle = new Circle(4);
            UserComponent userComponent = new UserComponent(circle, i);
            UserCommitmentComponent userCommitmentComponent = new UserCommitmentComponent();
            UserCallComponent userCallComponent = new UserCallComponent(random.nextFloat()*15);

            if(i == currentProfileChange){
                currentProfileChange = i + usersPerProfile;
                profile++;
            }

            //Select random profile
            ProfileComponent profileComponent = new ProfileComponent(profiles.get(profile), cityBlocksIndex, circle);

            Entity user = new Entity();
            user.add(userComponent);
            user.add(userCommitmentComponent);
            user.add(profileComponent);
            user.add(userCallComponent);

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

                //Update clock
                Constants.clock.setTime((Constants.clock.getTime()+(long)(delta*60000*12)));
                System.out.println("\t" + Constants.clock);
                System.out.println("\t\tSuccessfully started: " + Variables.successfullyStartedCalls);
                System.out.println("\t\tSuccessfully ended: " + Variables.successfullyEndedCalls);
                System.out.println("\t\tLocation blocked: " + Variables.locationBlockedCalls);
                System.out.println("\t\tSender blocked: " + Variables.senderCellBlockedCalls);
                System.out.println("\t\tReceiver blocked: " + Variables.receiverCellBlockedCalls);

                view.setLabelValues(
                        Variables.successfullyStartedCalls,
                        Variables.successfullyEndedCalls,
                        Variables.locationBlockedCalls,
                        Variables.senderCellBlockedCalls,
                        Variables.receiverCellBlockedCalls
                );

                for(EntitySystem es : systemArrayList){
                    es.update(delta);
                }
            }
        }.start();
    }
}
