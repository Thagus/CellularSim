package dataObjects;

import components.CityBlockComponent;
import javafx.util.Pair;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Thagus on 20/10/16.
 */
public class Profile {
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private String profileName;
    private CityBlockComponent.BlockType workArea;
    private boolean homeWorker;

    private LinkedHashMap<Time, ArrayList<Pair<String, Double>>> schedule;

    public Profile(String name, String workArea){
        this.profileName = name;
        this.homeWorker = false;

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

        schedule = new LinkedHashMap<>();

        System.out.println(this.profileName + " " + this.workArea);
    }

    public void addScheduleEntry(String start, String end, ArrayList<Pair<String, Double>> places){
        ArrayList<Time> times = null;

        try {
            times = getTimesInRange(new Time(sdf.parse(start).getTime()), new Time(sdf.parse(end).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(times!=null){
            for(Time time : times){
                //Add for each time the schedule in that range
                schedule.put(time, places);
            }
        }
    }

    private ArrayList<Time> getTimesInRange(Time start, Time end){
        ArrayList<Time> times = new ArrayList<>();

        long time = start.getTime();
        times.add(start);

        //Divide the hours in intervals of 30 minutes
        while(time<end.getTime()){
            time += 1800000L;
            times.add(new Time(time));
        }

        return times;
    }

    public String getProfileName() {
        return profileName;
    }

    public CityBlockComponent.BlockType getWorkArea() {
        return workArea;
    }

    public LinkedHashMap<Time, ArrayList<Pair<String, Double>>> getSchedule() {
        return schedule;
    }

    public boolean isHomeWorker() {
        return homeWorker;
    }
}
