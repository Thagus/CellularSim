package dataObjects;

import components.CityBlockComponent;
import javafx.util.Pair;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Thagus on 20/10/16.
 */
public class Profile {
    private String profileName;
    private CityBlockComponent.BlockType workArea;

    private LinkedHashMap<Time, ArrayList<Pair<CityBlockComponent.BlockType, Double>>> schedule;

    public Profile(String name, String workArea){
        this.profileName = name;

        switch(workArea){
            case "business":
                this.workArea = CityBlockComponent.BlockType.BUSINESS;
                break;
            case "residential":
                this.workArea = CityBlockComponent.BlockType.RESIDENTIAL;
                break;
            case "shopping":
                this.workArea = CityBlockComponent.BlockType.SHOPPING;
                break;
            case "home":
                this.workArea = CityBlockComponent.BlockType.RESIDENTIAL;
                break;
            case "traffic":
                this.workArea = CityBlockComponent.BlockType.TRAFFIC;
                break;
            case "park":
                this.workArea = CityBlockComponent.BlockType.PARK;
                break;
        }

        System.out.println(this.profileName + " " + this.workArea);
    }

    public void addScheduleEntry(String start, String end, ArrayList<Pair<String, Double>> places){
        //Receive start and end
        System.out.println("\t\t" + start + " - " + end);
        //Receive places
        for(Pair<String, Double> place : places){
            System.out.println("\t\t\t" + place.getKey() + " - " + place.getValue());
        }
    }

}
