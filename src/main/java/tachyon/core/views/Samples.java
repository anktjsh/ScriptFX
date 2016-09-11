/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tachyon.core.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.scene.control.TabPane;
import tachyon.core.Tachyon;
import static tachyon.core.Tachyon.LAUNCHER;

/**
 *
 * @author Aniket
 */
public class Samples extends View {

    private final TabPane pane;

    public Samples(String string) {
        super(string);
        pane = new TabPane();
        setCenter(pane);
        pane.getTabs().addAll(
                new JavaScriptTab(),
                new HTMLTab(),
                new CSSTab(),
                new AngularTab(),
                new BootstrapTab()/*,
                new JQueryTab()*/
        );
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Tachyon.MENU_LAYER)));
        appBar.setTitleText("Samples");
        appBar.getActionItems().add(MaterialDesignIcon.PLAY_ARROW.button((e) -> {
            if (pane.getSelectionModel().getSelectedItem() instanceof SamplesTab) {
                SamplesTab b = (SamplesTab) pane.getSelectionModel().getSelectedItem();
                if (b.getCodeText() != null && !b.getCodeText().isEmpty()) {
                    ((Launcher) Tachyon.getInstance().retrieveView(LAUNCHER).get()).launch(null, b.getCodeText(), Tachyon.SAMPLES);
                    Tachyon.getInstance().switchView(LAUNCHER);
                }
            }
        }));
    }

}
