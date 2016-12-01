package components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Thagus on 24/11/16.
 * Component that holds the call cooldown for users
 */
public class UserCallComponent implements Component {
    public float callCooldown;

    public UserCallComponent(float initialCooldown){
        this.callCooldown = initialCooldown;
    }
}
