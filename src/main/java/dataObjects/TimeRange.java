package dataObjects;

import java.sql.Time;
import java.util.Objects;

/**
 * Created by Thagus on 27/11/16.
 * Holds two times that define a range
 */
public class TimeRange {
    private Time lowerBound;
    private Time upperBound;

    public TimeRange(Time lowerBound, Time upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public Time getLowerBound() {
        return lowerBound;
    }

    public Time getUpperBound() {
        return upperBound;
    }

    public boolean isWithinRange(Time timeToTest){
        return (timeToTest.after(lowerBound) || timeToTest.equals(lowerBound)) && (timeToTest.before(upperBound) || timeToTest.equals(upperBound));
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        TimeRange other = (TimeRange) obj;
        return other.getLowerBound().equals(lowerBound) && other.getUpperBound().equals(upperBound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.lowerBound, this.upperBound);
    }
}
