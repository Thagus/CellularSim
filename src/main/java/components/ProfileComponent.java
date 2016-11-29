package components;

import com.badlogic.ashley.core.Component;
import dataObjects.Profile;
import javafx.scene.shape.Circle;
import utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Thagus on 19/10/16.
 */
public class ProfileComponent implements Component{
    public CityBlockComponent home, work;
    public Profile profile;

    public ProfileComponent(Profile profile, HashMap<CityBlockComponent.BlockType, ArrayList<CityBlockComponent>> cityBlocksIndex, Circle circleToReposition){
        this.profile = profile;
        Random random = new Random();

        //Select the home of the user
        home = cityBlocksIndex.get(CityBlockComponent.BlockType.RESIDENTIAL).get(random.nextInt(cityBlocksIndex.get(CityBlockComponent.BlockType.RESIDENTIAL).size()));
        circleToReposition.setCenterX(home.block.getX() + Constants.BLOCK_SIZE_PIXELS /2);
        circleToReposition.setCenterY(home.block.getY() + Constants.BLOCK_SIZE_PIXELS /2);

        //Define the work block
        if(profile.isHomeWorker()){ //If he/she works from home, the work block ant the home block are the same
            work = home;
        }
        else{
            switch(profile.getWorkArea()){  //We must select the work block according the profession
                case RESIDENTIAL:
                case BUSINESS:
                case SHOPPING:
                case PARK:
                    work = cityBlocksIndex.get(profile.getWorkArea()).get(random.nextInt(cityBlocksIndex.get(profile.getWorkArea()).size()));
                    break;
                case TRAFFIC:
                    work = null;
                    break;
            }
        }
    }
}
