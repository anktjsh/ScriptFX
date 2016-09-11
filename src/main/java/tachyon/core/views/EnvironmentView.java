package tachyon.core.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import tachyon.core.Tachyon;

public class EnvironmentView extends View {

    private boolean added = false;
    private final ScriptWriter writer;

    public EnvironmentView(String name) {
        super(name);
        getStylesheets().add(EnvironmentView.class.getResource("environment.css").toExternalForm());
        writer = new ScriptWriter();
        setCenter(writer);
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Tachyon.MENU_LAYER)));
        appBar.setTitleText("Environment");
        appBar.getActionItems().add(MaterialDesignIcon.UNDO.button((e) -> {
            writer.undo();
        }));
        appBar.getActionItems().add(MaterialDesignIcon.REDO.button((e) -> {
            writer.redo();
        }));
        appBar.getActionItems().add(MaterialDesignIcon.SAVE.button((e) -> {
            writer.saveAll();
        }));
        appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button((e) -> {
            writer.replace();
        }));
        appBar.getActionItems().add(MaterialDesignIcon.PLAY_ARROW.button((e) -> {
            writer.run();
        }));
        TabPane b = (TabPane) writer.getCenter();
        if (b.getSelectionModel().getSelectedItem() != null) {
            if (b.getSelectionModel().getSelectedItem() instanceof ScriptTab) {
                ScriptTab ba = (ScriptTab) b.getSelectionModel().getSelectedItem();
                appBar.setTitleText(ba.getScript().getProject().getProjectName());
                if (ba instanceof Editor) {
                    for (Node n : appBar.getActionItems()) {
                        n.setDisable(false);
                    }
                } else if (appBar.getActionItems().size() == 5) {
                    appBar.getActionItems().get(0).setDisable(true);
                    appBar.getActionItems().get(1).setDisable(true);
                    appBar.getActionItems().get(2).setDisable(false);
                    appBar.getActionItems().get(3).setDisable(true);
                    appBar.getActionItems().get(4).setDisable(false);
                }
            } else {
                for (Node n : appBar.getActionItems()) {
                    n.setDisable(true);
                }
            }
        } else {
            for (Node n : appBar.getActionItems()) {
                n.setDisable(true);
            }
        }
        if (!added) {
            added = true;
            b.getSelectionModel().selectedItemProperty().addListener((ob, older, newer) -> {
                if (newer != null) {
                    if (newer instanceof ScriptTab) {
                        ScriptTab ba = (ScriptTab) newer;
                        appBar.setTitleText(ba.getScript().getProject().getProjectName());
                        if (ba instanceof Editor) {
                            for (Node n : appBar.getActionItems()) {
                                n.setDisable(false);
                            }
                        } else if (appBar.getActionItems().size() == 5) {
                            appBar.getActionItems().get(0).setDisable(true);
                            appBar.getActionItems().get(1).setDisable(true);
                            appBar.getActionItems().get(2).setDisable(false);
                            appBar.getActionItems().get(3).setDisable(true);
                            appBar.getActionItems().get(4).setDisable(false);
                        }
                    } else {
                        appBar.setTitleText("Environment");
                        for (Node n : appBar.getActionItems()) {
                            n.setDisable(true);
                        }
                    }
                } else {
                    appBar.setTitleText("Environment");
                    for (Node n : appBar.getActionItems()) {
                        n.setDisable(true);
                    }
                }
            });
        }
    }

}
