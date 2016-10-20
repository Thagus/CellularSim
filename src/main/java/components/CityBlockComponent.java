package components;

import com.badlogic.ashley.core.Component;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Thagus on 18/10/16.
 */
public class CityBlockComponent implements Component {
    public enum BlockType {
        RESIDENTIAL, BUSINESS, SHOPPING, TRAFFIC, PARK
    }

    public Rectangle block;
    public BlockType type;

    public CityBlockComponent(Rectangle block, char type, HashMap<BlockType, ArrayList<CityBlockComponent>> cityBlocksIndex) {
        this.block = block;

        switch (type){
            case 'T':
                this.type = BlockType.TRAFFIC;
                this.block.setFill(Color.WHITE);
                break;
            case 'R':
                this.type = BlockType.RESIDENTIAL;
                this.block.setFill(Color.YELLOW);
                break;
            case 'B':
                this.type = BlockType.BUSINESS;
                this.block.setFill(Color.LIGHTBLUE);
                break;
            case 'S':
                this.type = BlockType.SHOPPING;
                this.block.setFill(Color.ORANGE);
                break;
            case 'P':
                this.type = BlockType.PARK;
                this.block.setFill(Color.LIGHTGREEN);
                break;
            default:
                this.type = BlockType.TRAFFIC;
                this.block.setFill(Color.WHITE);
        }

        this.block.setStroke(Color.BLACK);
        this.block.setStrokeWidth(1);

        ArrayList<CityBlockComponent> cityBlockComponentArrayList = cityBlocksIndex.get(this.type);
        cityBlockComponentArrayList.add(this);
    }
}
