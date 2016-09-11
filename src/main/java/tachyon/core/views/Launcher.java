/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tachyon.core.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import scriptfx.core.LaunchWindow;
import scriptfx.core.Project;
import tachyon.core.Tachyon;

/**
 *
 * @author Aniket
 */
public class Launcher extends View {

    private LaunchWindow window;
    private Project ject;
    private String string;

    public Launcher(String string) {
        super(string);
    }

    public void launch(Project pro, String code, String strin) {
        ject = pro;
        window = new LaunchWindow(pro, code);
        setCenter(window);
        string = strin;
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        if (ject != null) {
            appBar.setTitleText(ject.getProjectName());
        }
        window.titleProperty().addListener((ob, older, newer) -> {
            appBar.setTitleText(newer);
        });
        appBar.getActionItems().add(MaterialDesignIcon.CLOSE.button((e) -> {
            window.getEngine().load("");
            Tachyon.getInstance().switchView(string);
        }));
    }

}
