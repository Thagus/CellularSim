package systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import components.UserCommitmentComponent;
import components.UserComponent;

import java.util.Random;

/**
 * Created by Thagus on 18/10/16.
 * System to process the movement of users by checking the commitment they have stabilised and moving the user's circle towards the target
 */
public class UserMovementSystem extends IteratingSystem {
    private ComponentMapper<UserComponent> uc = ComponentMapper.getFor(UserComponent.class);
    private ComponentMapper<UserCommitmentComponent> ucc = ComponentMapper.getFor(UserCommitmentComponent.class);

    private int maxX, maxY;

    public UserMovementSystem(int maxX, int maxY) {
        super(Family.all(UserComponent.class).get());
        this.maxX = maxX;
        this.maxY = maxY;
    }



    @Override
    protected void processEntity(Entity entity, float delta) {
        UserComponent userComponent = uc.get(entity);
        UserCommitmentComponent userCommitmentComponent = ucc.get(entity);

        int speed = userComponent.speed;

        double newXValue, newYValue;

        if(userCommitmentComponent.xCommitment!=0){ //We have a current commitment for the X axis
            double currX = userComponent.userPosition.getCenterX();

            int direction = (userCommitmentComponent.xCommitment>0)?1:-1;

            float movement = direction*speed*delta;

            float newCommitment = userCommitmentComponent.xCommitment - direction*movement;
            int direction2 = (newCommitment>0)?1:-1;

            if(direction!=direction2){  //We passed the commitment goal
                newXValue = currX + userCommitmentComponent.xCommitment;
                userCommitmentComponent.xCommitment = 0;
            }
            else {  //Everything is fine
                newXValue = currX + movement;
                userCommitmentComponent.xCommitment -= movement;
            }

            userComponent.userPosition.setCenterX(newXValue);
        }
        else if (userCommitmentComponent.yCommitment!=0){   //We have a commitment for Y axis
            double currY = userComponent.userPosition.getCenterY();

            int direction = (userCommitmentComponent.yCommitment>0)?1:-1;

            float movement = direction*speed*delta;

            float newCommitment = userCommitmentComponent.yCommitment - direction*movement;
            int direction2 = (newCommitment>0)?1:-1;

            if(direction!=direction2){  //We passed the commitment goal
                newYValue = currY + userCommitmentComponent.yCommitment;
                userCommitmentComponent.yCommitment = 0;
            }
            else {  //Everything is fine
                newYValue = currY + movement;
                userCommitmentComponent.yCommitment = newCommitment;
            }

            userComponent.userPosition.setCenterY(newYValue);
        }


        if(userComponent.userPosition.getCenterX()<5){
            userComponent.userPosition.setCenterX(5);
            userCommitmentComponent.xCommitment = 0;
        }
        else if(userComponent.userPosition.getCenterX()>maxX-5){
            userComponent.userPosition.setCenterX(maxX-5);
            userCommitmentComponent.xCommitment = 0;
        }

        if(userComponent.userPosition.getCenterY()<5){
            userComponent.userPosition.setCenterY(5);
            userCommitmentComponent.yCommitment = 0;
        }
        else if (userComponent.userPosition.getCenterY()>maxY-5){
            userComponent.userPosition.setCenterY(maxY-5);
            userCommitmentComponent.yCommitment = 0;
        }
    }
}
