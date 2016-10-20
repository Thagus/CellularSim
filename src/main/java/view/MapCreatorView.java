package view;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Created by Thagus on 19/10/16.
 */
public class MapCreatorView extends Pane {

    private int WIDTH = 800, HEIGHT = 600;

    public MapCreatorView(){
        setMaxSize(WIDTH, HEIGHT);
        setMinSize(WIDTH, HEIGHT);
        setClip(new Rectangle(WIDTH, HEIGHT));
    }
}
