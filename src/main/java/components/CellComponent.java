package components;

import com.badlogic.ashley.core.Component;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by Thagus on 18/10/16.
 * Component that every cell entity must have
 */
public class CellComponent implements Component {
    public Circle cellCoverage; //A circle that represents the cell coverage
    public int id;              //The cell ID

    public CellComponent(Circle cellCoverage, int id) {
        this.cellCoverage = cellCoverage;
        this.cellCoverage.setFill(new Color(135/255, 206/255, 250/255, 0.3));
        this.id = id;
    }
}
