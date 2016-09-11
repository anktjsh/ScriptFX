/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tachyon.core.views;

import javafx.scene.control.TreeItem;
import scriptfx.core.Script;

/**
 *
 * @author Aniket
 */
public class ScriptTreeItem extends TreeItem<String> {

    private final Script script;

    public ScriptTreeItem(Script pro) {
        setValue(pro.getFile().getName());
        script = pro;
    }

    public Script getScript() {
        return script;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ScriptTreeItem) {
            ScriptTreeItem pt = (ScriptTreeItem) obj;
            return pt.getScript().equals(getScript());
        }
        return false;
    }

}
