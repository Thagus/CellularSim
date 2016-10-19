package components;

import com.badlogic.ashley.core.Component;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by Thagus on 18/10/16.
 */
public class CellComponent implements Component {
    public Circle cellCoverage;

    public CellComponent(Circle cellCoverage) {
        this.cellCoverage = cellCoverage;
        this.cellCoverage.setFill(new Color(135, 206, 250, 0.3));
    }
}
