/*
 * To change this license header; choose License Headers in Project Properties.
 * To change this template file; choose Tools | Templates
 * and open the template in the editor.
 */
package tachyon.core.views;

import java.io.File;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import scriptfx.core.FileUtils;
import scriptfx.core.Project;
import scriptfx.core.ProjectTree;
import scriptfx.core.Script;
import tachyon.core.Tachyon;
import static tachyon.core.Tachyon.LAUNCHER;

/**
 *
 * @author Aniket
 */
public class ScriptWriter extends BorderPane {

    private final TabPane tabPane;

    public ScriptWriter() {
        tabPane = new TabPane();
        setCenter(tabPane);
        openPreviousProjects();
        if (tabPane.getTabs().isEmpty()) {
            tabPane.getTabs().add(new Welcome());
        }
    }

    private class Welcome extends Tab {

        private final Button start;

        public Welcome() {
            super("Welcome");
            BorderPane pane = new BorderPane();
            setContent(pane);
            pane.setCenter(start = new Button("Welcome to ScriptFx!\nA Mobile Web Development IDE\nGet Started with a new Project!"));
            start.setOnAction((e) -> {
                Tachyon.drawer.setSelectedItem(Tachyon.secondaryItem);
                getTabPane().getTabs().remove(this);
            });
        }
    }

    private ScriptTab getSelectedTab() {
        if (tabPane.getSelectionModel().getSelectedItem() != null) {
            if (tabPane.getSelectionModel().getSelectedItem() instanceof ScriptTab) {
                return ((ScriptTab) tabPane.getSelectionModel().getSelectedItem());
            }
        }
        return null;
    }

    public final Project getCurrentProject() {
        if (getSelectedTab() != null) {
            return getSelectedTab().getProject();
        }
        return null;
    }

    public final void run() {
        saveAll();
        if (getCurrentProject() != null) {
            ((Launcher) Tachyon.getInstance().retrieveView(LAUNCHER).get()).launch(getCurrentProject(), null, Tachyon.HOME_VIEW);
            Tachyon.getInstance().switchView(LAUNCHER);
        }
    }

    public final void saveAll() {
        for (Tab b : tabPane.getTabs()) {
            if (b instanceof Editor) {
                Editor e = (Editor) b;
                e.save();
            }
        }
    }

    private void saveOpenTabsInformation() {
        File p = FileUtils.newFile("storage");
        if (!p.exists()) {
            FileUtils.createDirectories(p);
        }
        File f = FileUtils.newFile("storage" + File.separator + "open03.txt");
        ArrayList<String> al = new ArrayList<>();
        for (Tab b : tabPane.getTabs()) {
            if (b instanceof ScriptTab) {
                ScriptTab sb = (ScriptTab) b;
                al.add(sb.getScript().getFile().getAbsolutePath());
            }
        }
        FileUtils.write(f, al);
    }

    public void saveOpenProjectsInformation() {
        saveOpenTabsInformation();
    }

    public final void openPreviousProjects() {
        File f = FileUtils.newFile("Projects");
        if (f.listFiles() != null) {
            for (File fi : f.listFiles()) {
                Project p = Project.unserialize(fi);
                if (p != null) {
                    ProjectTree.getTree().addProject(p);
                }
            }
        }
        openPreviousTabs();
    }

    private void openPreviousTabs() {
        File f = FileUtils.newFile("storage" + File.separator + "open03.txt");
        ArrayList<String> al = new ArrayList<>();
        al.addAll(FileUtils.readAllLines(f));
        ArrayList<Script> asc = new ArrayList<>();
        for (Project p : ProjectTree.getTree().getProjects()) {
            for (Script sc : p.getScripts()) {
                for (int x = al.size() - 1; x >= 0; x--) {
                    if (sc.getFile().getAbsolutePath().equals(al.get(x))) {
                        asc.add(sc);
                        al.remove(x);
                    }
                }
            }
        }
        for (Script sti : asc) {
            if (sti.getType() < 4) {
                Editor ed;
                tabPane.getTabs().add(ed = new Editor(sti, sti.getProject()));
                tabPane.getSelectionModel().select(ed);
            } else {
                Viewer ed;
                tabPane.getTabs().add(ed = new Viewer(sti, sti.getProject()));
                tabPane.getSelectionModel().select(ed);
                //different file formats
            }
        }

    }

    public final void undo() {
        ScriptTab select = getSelectedTab();
        if (select instanceof Editor) {
            Editor ed = (Editor) select;
            ed.undo();
        }
    }

    public final void redo() {
        ScriptTab select = getSelectedTab();
        if (select instanceof Editor) {
            Editor ed = (Editor) select;
            ed.redo();
        }
    }

    public void replace() {
        ScriptTab select = getSelectedTab();
        if (select instanceof Editor) {
            Editor ed = (Editor) select;
            ed.replace();
        }
    }
}
