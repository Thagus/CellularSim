package systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import components.CellComponent;
import components.UserComponent;
import java.util.HashMap;

/**
 * Created by Thagus on 24/11/16.
 * System to update the know location of users
 */
public class PagingSystem extends EntitySystem {
    private ImmutableArray<Entity> userEntities;
    private ImmutableArray<Entity> cellEntities;

    private HashMap<Integer, Entity> paging;

    private ComponentMapper<UserComponent> ucm = ComponentMapper.getFor(UserComponent.class);
    private ComponentMapper<CellComponent> ccm = ComponentMapper.getFor(CellComponent.class);

    public PagingSystem(){
        super();
        paging = new HashMap<>();
    }

    public void addedToEngine(Engine engine){
        userEntities = engine.getEntitiesFor(Family.all(UserComponent.class).get());
        cellEntities = engine.getEntitiesFor(Family.all(CellComponent.class).get());
    }

    public void update(float delta){
        for(Entity user : userEntities){
            for(Entity cell : cellEntities){
                UserComponent userComponent = ucm.get(user);
                CellComponent cellComponent = ccm.get(cell);

                if(cellComponent.cellCoverage.contains(userComponent.userPosition.getCenterX(), userComponent.userPosition.getCenterY())){
                    Entity actualCell = paging.get(userComponent.id);

                    if(actualCell == null || actualCell!=cell){
                        paging.put(userComponent.id, cell);
                    }
                }
            }
        }
    }

    /**
     * Check if the selected user is indeed inside the paged cell, and return the cell id if so
     * @param user The user we want to check if it is in the paged cell
     * @return the cell id if the user is within it
     */
    public int confirmPosition(Entity user){
        UserComponent userComponent = ucm.get(user);

        Entity cell = paging.get(userComponent.id);
        CellComponent cellComponent = ccm.get(cell);

        if(cellComponent.cellCoverage.contains(userComponent.userPosition.getCenterX(), userComponent.userPosition.getCenterY())){
            return cellComponent.id;
        }

        return -1;
    }

    /**
     * Get the cell containing an user
     * @param user the user
     * @return the cell id that contains the user
     */
    public int getUserPosition(Entity user){
        UserComponent userComponent = ucm.get(user);
        CellComponent cellComponent = ccm.get(paging.get(userComponent.id));

        if(cellComponent.cellCoverage.contains(userComponent.userPosition.getCenterX(), userComponent.userPosition.getCenterY())){
            return cellComponent.id;
        }
        else{
            for(Entity cell : cellEntities){
                cellComponent = ccm.get(cell);

                if(cellComponent.cellCoverage.contains(userComponent.userPosition.getCenterX(), userComponent.userPosition.getCenterY())){
                    return cellComponent.id;
                }
            }
        }
        return -1;
    }
}
