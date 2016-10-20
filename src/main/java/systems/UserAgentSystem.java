package systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
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
}
