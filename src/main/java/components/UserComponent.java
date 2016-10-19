package components;

import com.badlogic.ashley.core.Component;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by Thagus on 18/10/16.
 */
public class UserComponent implements Component {
    public Circle userPosition;
    public int speed;

    public UserComponent(Circle userPosition) {
        this.userPosition = userPosition;
        this.userPosition.setFill(Color.RED);
        this.speed = 1*60;  //60 is the base speed
    }
}
