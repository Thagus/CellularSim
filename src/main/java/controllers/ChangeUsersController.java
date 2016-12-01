package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputDialog;
import utils.Constants;
import view.View;

import java.util.Optional;

/**
 * Created by Thagus on 30/11/16.
 */
public class ChangeUsersController implements EventHandler<ActionEvent>{
    private View view;
    public ChangeUsersController(View view) {
        this.view = view;
    }

    @Override
    public void handle(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change number of users");
        dialog.setHeaderText("Change the number of users in the simulation");
        dialog.setContentText("Number of users:");

        //Get the response value
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(response -> {
            try {
                Constants.NUMBER_OF_USERS = Integer.parseInt(response);
                view.resetSimulation();
            } catch (NumberFormatException ignored){}
        });
    }
}
