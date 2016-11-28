package utils;

/**
 * Created by Thagus on 28/11/16.
 */
public class Variables {
    /**
     * Counters for calls
     */
    public static volatile Integer receiverCellBlockedCalls = 0;
    public static volatile Integer senderCellBlockedCalls = 0;
    public static volatile Integer locationBlockedCalls = 0;
    public static volatile Integer successfullyStartedCalls = 0;
    public static volatile Integer successfullyEndedCalls = 0;
}
