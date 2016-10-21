package systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import components.CityBlockComponent;
import components.ProfileComponent;
import components.UserCommitmentComponent;
import components.UserComponent;
import javafx.util.Pair;
import utils.Constants;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Thagus on 19/10/16.
 */
public class UserAgentSystem extends IteratingSystem {
    private ComponentMapper<UserCommitmentComponent> ucc = ComponentMapper.getFor(UserCommitmentComponent.class);
    private ComponentMapper<ProfileComponent> pc = ComponentMapper.getFor(ProfileComponent.class);
    private ComponentMapper<UserComponent> uc = ComponentMapper.getFor(UserComponent.class);

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    private HashMap<CityBlockComponent.BlockType, ArrayList<CityBlockComponent>> cityBlocksIndex;
    private char[] possibleMovements = {'U', 'D', 'R', 'L'};
    private Random rnd;

    public UserAgentSystem(HashMap<CityBlockComponent.BlockType, ArrayList<CityBlockComponent>> cityBlocksIndex) {
        super(Family.all(UserCommitmentComponent.class, ProfileComponent.class, UserComponent.class).get());
        rnd = new Random();
        this.cityBlocksIndex = cityBlocksIndex;
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        UserCommitmentComponent userCommitmentComponent = ucc.get(entity);

        if(userCommitmentComponent.xCommitment==0 && userCommitmentComponent.yCommitment==0) {
            //If we dont have a selected destination, select one based on the schedule
            if(userCommitmentComponent.destination==null){
                ProfileComponent profileComponent = pc.get(entity);

                //Identify the current hour interval
                String[] hours = Constants.clock.toString().split(":");
                int hour = Integer.parseInt(hours[0]);
                int minutes = Integer.parseInt(hours[1]);

                String currentInterval;
                currentInterval = (minutes>=30)? hour+":30:00" : hour+":00:00"; //If the minutes is greater than 30, the block is 30, if not the time interval is the 00

                ArrayList<Pair<String, Double>> placesProbabilities;

                try {
                    placesProbabilities = profileComponent.profile.getSchedule().get(new Time(sdf.parse(currentInterval).getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }

                for(Pair<String, Double> placeProbability : placesProbabilities){
                    if(rnd.nextDouble()<placeProbability.getValue()){   //if the random value is smaller than the wanted probability, we found the place we want to be :)
                        switch(placeProbability.getKey()){
                            case "home":
                                userCommitmentComponent.destination = profileComponent.home;
                                break;
                            case "work":
                                if(profileComponent.work==null){    //If the work is null, the profession involves moving arround the city
                                    //Select a random location
                                    ArrayList<CityBlockComponent> blockComponents = cityBlocksIndex.get(CityBlockComponent.BlockType.randomBlock());
                                    userCommitmentComponent.destination = blockComponents.get(rnd.nextInt(blockComponents.size()));
                                }
                                else {
                                    userCommitmentComponent.destination = profileComponent.work;
                                }
                                break;
                            case "business":
                                userCommitmentComponent.destination = cityBlocksIndex.get(CityBlockComponent.BlockType.BUSINESS).get(rnd.nextInt(cityBlocksIndex.get(CityBlockComponent.BlockType.BUSINESS).size()));
                                break;
                            case "residential":
                                userCommitmentComponent.destination = cityBlocksIndex.get(CityBlockComponent.BlockType.RESIDENTIAL).get(rnd.nextInt(cityBlocksIndex.get(CityBlockComponent.BlockType.RESIDENTIAL).size()));
                                break;
                            case "shopping":
                                userCommitmentComponent.destination = cityBlocksIndex.get(CityBlockComponent.BlockType.SHOPPING).get(rnd.nextInt(cityBlocksIndex.get(CityBlockComponent.BlockType.SHOPPING).size()));
                                break;
                            case "park":
                                userCommitmentComponent.destination = cityBlocksIndex.get(CityBlockComponent.BlockType.PARK).get(rnd.nextInt(cityBlocksIndex.get(CityBlockComponent.BlockType.PARK).size()));
                                break;
                        }
                    }
                }
            }
            else {   //We already have a selected destination, make a specific commitment to improve Manhattan distance
                //Get the current position
                UserComponent userComponent = uc.get(entity);

                //Check if we satisfied the commitment
                if(manhattanDistance(userCommitmentComponent.destination.block.getX() + Constants.BLOCK_SIZE / 2, userComponent.userPosition.getCenterX(), userCommitmentComponent.destination.block.getY() + Constants.BLOCK_SIZE / 2, userComponent.userPosition.getCenterY())==0){
                    userCommitmentComponent.destination = null;
                    return;
                }

                //Calculate possible moves
                char bestCommitment = 'D';
                int bestDistance = -1;

                for (char move : possibleMovements) {
                    int movementY = 0;
                    int movementX = 0;
                    switch (move) {
                        case 'U':
                            movementY = -10;
                            break;
                        case 'D':
                            movementY = 10;
                            break;
                        case 'R':
                            movementX = 10;
                            break;
                        case 'L':
                            movementX = -10;
                            break;
                    }

                    int newX = (int) (userComponent.userPosition.getCenterX() + movementX);
                    int newY = (int) (userComponent.userPosition.getCenterY() + movementY);

                    int distance = manhattanDistance(userCommitmentComponent.destination.block.getX() + Constants.BLOCK_SIZE / 2, newX, userCommitmentComponent.destination.block.getY() + Constants.BLOCK_SIZE / 2, newY);

                    if (bestDistance == -1 || bestDistance > distance) {
                        bestDistance = distance;
                        bestCommitment = move;
                    }
                    if(bestDistance==distance && rnd.nextBoolean()){
                        bestDistance = distance;
                        bestCommitment = move;
                    }
                }

                switch (bestCommitment) {
                    case 'U':
                        userCommitmentComponent.yCommitment = -10;
                        break;
                    case 'D':
                        userCommitmentComponent.yCommitment = 10;
                        break;
                    case 'R':
                        userCommitmentComponent.xCommitment = 10;
                        break;
                    case 'L':
                        userCommitmentComponent.xCommitment = -10;
                        break;
                }
            }
        }
    }

    /**
     * A method to calculate the Manhattan distance
     * @param targetX the X coordinate of destination
     * @param currentX the current X coordinate
     * @param targetY the Y coordinate of the destination
     * @param currentY the current Y coordinate
     * @return the Manhattan distance
     */
    private static int manhattanDistance(double targetX, double currentX, double targetY, double currentY){
        return (int) (Math.abs(targetX-currentX) + Math.abs(targetY-currentY));
    }
}
