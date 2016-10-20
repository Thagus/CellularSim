package components;

import com.badlogic.ashley.core.Component;
import dataObjects.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Thagus on 19/10/16.
 */
public class ProfileComponent implements Component{
    public CityBlockComponent home, work;

    public Profile profile;

    public HashMap<CityBlockComponent.BlockType, ArrayList<CityBlockComponent>> locations;  //For each block type we have a set of blocks where the user will be

    //movingProfession might not be necessary, if we use the WORK field of the Excel to determine if he works on the Traffic

    public ProfileComponent(Profile profile, HashMap<CityBlockComponent.BlockType, ArrayList<CityBlockComponent>> cityBlocksIndex){
        this.profile = profile;

        Random random = new Random();

        home = cityBlocksIndex.get(CityBlockComponent.BlockType.RESIDENTIAL).get(random.nextInt(cityBlocksIndex.get(CityBlockComponent.BlockType.RESIDENTIAL).size()));

        //Select a work area based on the workArea attribute of the profile
        work = cityBlocksIndex.get(CityBlockComponent.BlockType.BUSINESS).get(random.nextInt(cityBlocksIndex.get(CityBlockComponent.BlockType.BUSINESS).size()));



        //Select random locations for different categories

        /*for(ArrayList<String> arrayList : schedule){

        }*/
    }
}
