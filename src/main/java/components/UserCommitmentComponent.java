package components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Thagus on 18/10/16.
 * Component that holds user commitments
 */
public class UserCommitmentComponent implements Component {
    public float xCommitment, yCommitment;  //The current commitment to reach the destination
    public CityBlockComponent destination;  //The destination city block

    //Each schedule block has a timeBlock identifier, therefore we can store the time block corresponding to the current/last commitment so we don't make many decisions per time block
    public int commitmentTimeBlock = -1;    //The time block where the last commitment was made
}
