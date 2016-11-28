package systems;

import actors.PlaceCall;
import akka.actor.ActorRef;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import components.UserCallComponent;
import utils.Variables;

import java.util.Random;

/**
 * Created by Thagus on 18/10/16.
 */
public class CallSystem extends EntitySystem {
    private ImmutableArray<Entity> userEntities;
    private Random rnd;
    private PagingSystem pagingSystem;
    private ActorRef managerRef;

    private ComponentMapper<UserCallComponent> uccm = ComponentMapper.getFor(UserCallComponent.class);

    public CallSystem(PagingSystem pagingSystem, ActorRef managerRef){
        super();
        this.rnd = new Random();
        this.pagingSystem = pagingSystem;
        this.managerRef = managerRef;
    }

    public void addedToEngine(Engine engine){
        userEntities = engine.getEntitiesFor(Family.all(UserCallComponent.class).get());
    }

    public void update(float delta){
        final int numberOfUsers = userEntities.size();

        for(int id = 0; id<numberOfUsers; id++){
            Entity user = userEntities.get(id);
            UserCallComponent userCallComponent = uccm.get(user);

            if(userCallComponent.callCooldown<=0){
                if(rnd.nextDouble()>0.8){   //20% probability
                    //Select the receiving user
                    int receivingUser = rnd.nextInt(numberOfUsers);
                    int receivingCellID = pagingSystem.confirmPosition(userEntities.get(receivingUser));

                    //Check if the receiving user is in the expected cell
                    if(receivingCellID!=-1){
                        //System.out.println("User making a call to " + receivingUser + " in cell " + receivingCellID);

                        int standingCell = pagingSystem.getUserPosition(user);
                        //Create an actor to handle the call
                        managerRef.tell(new PlaceCall(standingCell, id, receivingCellID, receivingUser), ActorRef.noSender());

                        //Set the cooldown to a random number between 5 and 15
                        userCallComponent.callCooldown = rnd.nextFloat()*15 + 5;
                    }
                    else {  //Call blocked due to wrong receiving location
                        Variables.locationBlockedCalls++;
                    }
                }
            }
            else {
                //Update the cooldown
                userCallComponent.callCooldown -= delta;
            }
        }
    }

}
