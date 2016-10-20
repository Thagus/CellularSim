package systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import components.UserCommitmentComponent;
import components.UserComponent;

import java.util.Random;

/**
 * Created by Thagus on 19/10/16.
 */
public class UserAgentSystem extends IteratingSystem {
    private ComponentMapper<UserCommitmentComponent> ucc = ComponentMapper.getFor(UserCommitmentComponent.class);

    private Random rnd;

    public UserAgentSystem() {
        super(Family.all(UserComponent.class).get());
        rnd = new Random();
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        UserCommitmentComponent userCommitmentComponent = ucc.get(entity);

        //if no commitments, check if there is a commited destination
        //  if there is a commited destination, move to calculate best commitment
        //  if there is no destination
        //      based on the hour, identify the place where it should be
        //      if the place is in traffic, identify the next destination

        //Calculate possible movement (up,down,right,left) ending position
        //Compute the Manhattan distance for each possible movement
        //Commit to the best movement

        if(userCommitmentComponent.xCommitment==0 && userCommitmentComponent.yCommitment==0) {
            if (rnd.nextBoolean()) {  //We move in X
                if (rnd.nextDouble() > 0.2) {   //We move to the right
                    userCommitmentComponent.xCommitment = 10;
                } else {   //We move to the left
                    userCommitmentComponent.xCommitment = -10;
                }
            } else {  //We move in Y
                if (rnd.nextDouble() > 0.2) {   //We move down
                    userCommitmentComponent.yCommitment = 10;
                } else {   //We move up
                    userCommitmentComponent.yCommitment = -10;
                }
            }

            //When in traffic, random wandering, unless we have a speccific destination
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
    private static int manhattanDistance(int targetX, int currentX, int targetY, int currentY){
        return Math.abs(targetX-currentX) + Math.abs(targetY-currentY);
    }
}
