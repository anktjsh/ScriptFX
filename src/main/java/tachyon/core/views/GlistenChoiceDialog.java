/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tachyon.core.views;

import com.gluonhq.charm.glisten.control.Dialog;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 *
 * @author Aniket
 */
public class GlistenChoiceDialog {

    private final Dialog<String> dialog;
    private final ListView<String> choices;

    public GlistenChoiceDialog() {
        super();
        dialog = new Dialog<>();
        choices = new ListView<>();
        dialog.setContent(choices);
        Button yesButton = new Button("OK");
        Button noButton = new Button("Cancel");
        yesButton.setOnAction(event2 -> {
            dialog.setResult(choices.getSelectionModel().getSelectedItem());
            dialog.hide();
        });
        choices.getSelectionModel().selectedItemProperty().addListener(((ob, older, nweer) -> {
            if (nweer != null) {
                yesButton.setDisable(false);
            } else {
                yesButton.setDisable(true);
            }
        }));
        noButton.setOnAction(event2 -> {
            dialog.setResult(null);
            dialog.hide();
        });
        dialog.setOnShown((e) -> {
            Platform.runLater(() -> choices.requestFocus());
        });
        dialog.getButtons().addAll(yesButton, noButton);
    }

    public void setSelectedValue(String s) {
        choices.getSelectionModel().select(s);
    }

    public void setItems(List<String> al) {
        choices.getItems().clear();
        choices.getItems().addAll(al);
    }

    public void setTitle(String s) {
        dialog.setTitleText(s);
    }

    public Optional<String> showAndWait() {
        return dialog.showAndWait();
    }

}
