package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Toggle;
import view.View;

/**
 * Created by Thagus on 29/11/16.
 * Controller to change the currently used cellular technology in the simulator
 */
public class TechnologyToggleController implements ChangeListener<Toggle> {
    private View view;

    public TechnologyToggleController(View view) {
        this.view = view;
    }

    @Override
    public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        if(newValue!=null){
            int userData = Integer.parseInt(newValue.getUserData().toString());

            view.changeTechnology(userData);
        }
    }
}
