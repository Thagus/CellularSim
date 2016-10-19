package components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Thagus on 18/10/16.
 */
public class UserCommitmentComponent implements Component {
    public float xCommitment, yCommitment;
    public int xDestination, yDestination;
    public CityBlockComponent.BlockType destination;
}
