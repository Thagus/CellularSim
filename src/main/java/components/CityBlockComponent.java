package components;

import com.badlogic.ashley.core.Component;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Thagus on 18/10/16.
 */
public class CityBlockComponent implements Component {
    public enum BlockType {
        RESIDENTIAL, BUSINESS, SHOPPING, TRAFFIC, PARK
    }
    public Rectangle block;
    public BlockType type;

    public CityBlockComponent(Rectangle block, BlockType type) {
        this.block = block;
        this.type = type;
        this.block.setFill(Color.WHITE);
    }

    public CityBlockComponent(int i, int j, BlockType type) {
        this.type = type;
        this.block = new Rectangle(i*10, j*10, 10, 10);
        this.block.setFill(Color.WHITE);
    }
}
