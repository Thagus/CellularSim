package components;

import com.badlogic.ashley.core.Component;
import javafx.util.Pair;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Thagus on 19/10/16.
 */
public class ProfileComponent implements Component{
    public String profileName;

    public CityBlockComponent home, work;

    public HashMap<CityBlockComponent.BlockType, ArrayList<CityBlockComponent>> locations;  //For each block type we have a set of blocks where the user will be
    public ArrayList<Pair<Time, ArrayList<Pair<CityBlockComponent.BlockType, Double>>>> schedule;   //ArrayList of Time-Probabilities pair, where probabilities is an ArrayList of BlocType-Probability pair

    public ProfileComponent(String name, boolean movingProfession, ArrayList<ArrayList<String>> schedule, HashMap<CityBlockComponent.BlockType, ArrayList<CityBlockComponent>> cityBlocksIndex){
        this.profileName = name;

        Random random = new Random();

        home = cityBlocksIndex.get(CityBlockComponent.BlockType.RESIDENTIAL).get(random.nextInt(cityBlocksIndex.get(CityBlockComponent.BlockType.RESIDENTIAL).size()));

        //if work is null, it means that is a profession that has to move through the city
        if(!movingProfession)
            work = cityBlocksIndex.get(CityBlockComponent.BlockType.BUSINESS).get(random.nextInt(cityBlocksIndex.get(CityBlockComponent.BlockType.BUSINESS).size()));
        else
            work = null;



        //Select random locations for different cathegories

        /*for(ArrayList<String> arrayList : schedule){

        }*/
    }
}
