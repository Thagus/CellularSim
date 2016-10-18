package systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import components.UserComponent;

import java.util.Random;

/**
 * Created by Thagus on 18/10/16.
 */
public class UserMovementSystem extends IteratingSystem {
    private ComponentMapper<UserComponent> uc = ComponentMapper.getFor(UserComponent.class);
    private Random rnd;
    private int maxX, maxY;

    public UserMovementSystem(int maxX, int maxY) {
        super(Family.all(UserComponent.class).get());
        rnd = new Random();
        this.maxX = maxX;
        this.maxY = maxY;
    }



    @Override
    protected void processEntity(Entity entity, float delta) {
        UserComponent userComponent = uc.get(entity);
        int speed = userComponent.speed;

        double newXValue, newYValue;

        if(rnd.nextBoolean()) { //Decide if we move in Y
            double currY = userComponent.user.getCenterY();
            if (rnd.nextDouble()>0.2) {  //Decide if we move up or down
                newYValue = currY + speed*delta;
            } else {
                newYValue = currY - speed*delta;
            }

            //newYValue = currY + speed*delta;
            userComponent.user.setCenterY(newYValue);
        }
        else{   //We move in X
            double currX = userComponent.user.getCenterX();
            if(rnd.nextDouble()>0.2){  //Decide if we go right or left
                newXValue = currX + speed*delta;
            }
            else {
                newXValue = currX - speed*delta;
            }

            //newXValue = currX + speed*delta;
            userComponent.user.setCenterX(newXValue);
        }

        //The new value must always be aligned with the city blocks


        if(userComponent.user.getCenterX()<5){
            userComponent.user.setCenterX(5);
        }
        else if(userComponent.user.getCenterX()>maxX-5){
            userComponent.user.setCenterX(maxX-5);
        }

        if(userComponent.user.getCenterY()<5){
            userComponent.user.setCenterY(5);
        }
        else if (userComponent.user.getCenterY()>maxY-5){
            userComponent.user.setCenterY(maxY-5);
        }
    }
}
