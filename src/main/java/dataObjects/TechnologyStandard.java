package dataObjects;

/**
 * Created by Thagus on 28/11/16.
 */
public class TechnologyStandard {
    private String technologyName;
    private float cellRadius;
    private int usersPerCell;
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
