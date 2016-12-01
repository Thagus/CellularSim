package dataObjects;

import components.CityBlockComponent;
import javafx.util.Pair;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Thagus on 20/10/16.
 * Represents an user profile
 */
public class Profile {
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private String profileName;
    private CityBlockComponent.BlockType workArea;
    private boolean homeWorker;

    private int timeBlocksCount;

    private LinkedHashMap<Integer, TimeRange> timeBlocks;
    private HashMap<Integer, ArrayList<Pair<String, Double>>> schedule;



    public Profile(String name, String workArea){
        this.profileName = name;
        this.homeWorker = false;
        this.timeBlocksCount = 0;

        switch(workArea){
            case "business":
                this.workArea = CityBlockComponent.BlockType.BUSINESS;
                break;
            case "residential":
                this.workArea = CityBlockComponent.BlockType.RESIDENTIAL;
                break;
            case "home":
                this.workArea = CityBlockComponent.BlockType.RESIDENTIAL;
                this.homeWorker = true;
                break;
            case "shopping":
                this.workArea = CityBlockComponent.BlockType.SHOPPING;
                break;
            case "traffic":
                this.workArea = CityBlockComponent.BlockType.TRAFFIC;
                break;
            case "park":
                this.workArea = CityBlockComponent.BlockType.PARK;
                break;
        }

        timeBlocks = new LinkedHashMap<>();
        schedule = new HashMap<>();
    }

    public void addScheduleEntry(String start, String end, ArrayList<Pair<String, Double>> places){

        try {
            TimeRange timeRange = new TimeRange(new Time(sdf.parse(start).getTime()), new Time(sdf.parse(end).getTime()));

            timeBlocks.put(timeBlocksCount, timeRange);
            schedule.put(timeBlocksCount, places);

            timeBlocksCount++;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getProfileName() {
        return profileName;
    }

    public CityBlockComponent.BlockType getWorkArea() {
        return workArea;
    }

    public boolean isHomeWorker() {
        return homeWorker;
    }

    public int getTimeBlocksCount() {
        return timeBlocksCount;
    }

    public LinkedHashMap<Integer, TimeRange> getTimeBlocks() {
        return timeBlocks;
    }

    public HashMap<Integer, ArrayList<Pair<String, Double>>> getSchedule() {
        return schedule;
    }
}
