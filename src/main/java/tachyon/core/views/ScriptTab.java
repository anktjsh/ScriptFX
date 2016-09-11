/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tachyon.core.views;

import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import scriptfx.core.Project;
import scriptfx.core.Script;

/**
 *
 * @author Aniket
 */
public class ScriptTab extends Tab {

    private final BorderPane content;
    private final Project project;
    private final Script script;

    public ScriptTab(Script sc, Project pro) {
        super(sc.getFile().getName());
        project = pro;
        content = new BorderPane();
        setContent(content);
        script = sc;
    }

    public BorderPane getCenter() {
        return content;
    }

    public Script getScript() {
        return script;
    }

    public Project getProject() {
        return project;
    }
}
