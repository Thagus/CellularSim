package utils;

import java.sql.Time;

/**
 * Created by Thagus on 19/10/16.
 */
public class Constants {
    public static int BLOCK_SIZE_PIXELS = 10;
    public static int BLOCK_SIZE_METERS = 500;
    public static int NUMBER_OF_USERS = 1000;

    /**
     * Technology dependant variables
     */
    public static int CELL_CALL_LIMIT = 100;
    public static int CELL_BLOCK_RADIUS = 5;

    public static volatile Time clock;


}
