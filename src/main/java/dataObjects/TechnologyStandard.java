package dataObjects;

/**
 * Created by Thagus on 28/11/16.
 * Represents a technology standard from the list
 */
public class TechnologyStandard {
    private String technologyName;  //Technology name
    private float cellRadius;       //Cell radius in kilometers
    private int usersPerCell;       //Number of users per cell considering duplex channels
    private float dataRate;         //Data rate in kbps
    private String multipleAccess;  //Multiple access technology name

    public TechnologyStandard(String technologyName, String multipleAccess, float dataRate, float cellRadius, int usersPerCell) {
        this.technologyName = technologyName;
        this.cellRadius = cellRadius;
        this.usersPerCell = usersPerCell;
        this.dataRate = dataRate;
        this.multipleAccess = multipleAccess;
    }

    public String getTechnologyName() {
        return technologyName;
    }

    public float getCellRadius() {
        return cellRadius;
    }

    public int getUsersPerCell() {
        return usersPerCell;
    }

    public float getDataRate() {
        return dataRate;
    }

    public String getMultipleAccess() {
        return multipleAccess;
    }
}
