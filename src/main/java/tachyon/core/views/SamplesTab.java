/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tachyon.core.views;

import static com.gluonhq.charm.glisten.application.MobileApplication.HOME_VIEW;
import com.gluonhq.charm.glisten.control.Alert;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import scriptfx.core.FileUtils;
import scriptfx.core.GlistenInputDialog;
import scriptfx.core.Project;
import scriptfx.core.ProjectTree;
import scriptfx.core.Script;
import tachyon.core.Tachyon;

/**
 *
 * @author Aniket
 */
public class SamplesTab extends Tab {

    private final ListView<String> view;
    private final VBox box;
    private final TextArea area;
    private final TreeMap<String, String> map;
    private final HBox hbo;
    private final Button addEx, addNe;
    private final TabPane tabPane;

    public SamplesTab(String s) {
        super(s);
        setClosable(false);
        map = new TreeMap<>();
        box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        view = new ListView<>();
        view.setMaxHeight(200);
        area = new TextArea();
        area.setEditable(false);
        tabPane = (TabPane) ((ScriptWriter) Tachyon.getInstance().retrieveView(HOME_VIEW).get().getCenter()).getCenter();
        if (Settings.fontSize.get() != null) {
            area.setStyle("-fx-font-size:" + Settings.fontSize.get().getSize() + ";");
        }
        Settings.fontSize.addListener((ob, older, newer) -> {
            if (newer != null) {
                area.setStyle("-fx-font-size:" + newer.getSize() + ";");
            }
        });
        box.getChildren().addAll(view,
                hbo = new HBox(5, addEx = new Button("Add to Existing Project"),
                        addNe = new Button("Add to New Project")),
                area);
        hbo.setAlignment(Pos.CENTER);
        area.textProperty().addListener((ob, older, newer) -> {
            if (newer != null) {
                if (newer.isEmpty()) {
                    addEx.setDisable(true);
                    addNe.setDisable(true);
                } else {
                    addEx.setDisable(false);
                    addNe.setDisable(false);
                }
            } else {
                addEx.setDisable(true);
                addNe.setDisable(true);
            }
        });
        view.getSelectionModel().selectedItemProperty().addListener((ob, older, newer) -> {
            if (newer != null) {
                showExample(newer);
            }
        });
        addEx.setOnAction((e) -> {
            GlistenChoiceDialog gcd = new GlistenChoiceDialog();
            gcd.setTitle("Select a Project");
            ArrayList<String> al = new ArrayList<>();
            for (Project p : ProjectTree.getTree().getProjects()) {
                al.add(p.getProjectName());
            }
            gcd.setItems(al);
            Optional<String> op = gcd.showAndWait();
            if (op.isPresent()) {
                if (!op.get().isEmpty()) {
                    GlistenInputDialog gid = new GlistenInputDialog("");
                    gid.setTitle("Enter File Name");
                    Optional<String> show = gid.showAndWait();
                    if (show.isPresent()) {
                        if (!show.get().isEmpty()) {
                            Project p = getProject(op.get());
                            if (p != null) {
                                File f = new File(p.getSource().getAbsolutePath()
                                        + File.separator + show.get() + ".html");
                                if (!f.exists()) {
                                    Script sc = new Script(f, Script.HTML, p, area.getText());
                                    p.addScript(sc);
                                    if (sc.getType() < 4) {
                                        Editor ed;
                                        tabPane.getTabs().add(ed = new Editor(sc, sc.getProject()));
                                        tabPane.getSelectionModel().select(ed);
                                    } else {
                                        Viewer ed;
                                        tabPane.getTabs().add(ed = new Viewer(sc, sc.getProject()));
                                        tabPane.getSelectionModel().select(ed);
                                    }
                                    Tachyon.drawer.setSelectedItem(Tachyon.primaryItem);
                                } else {
                                    Alert ala = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "File with this name already exists!");
                                    ala.showAndWait();
                                }
                            }
                        }
                    }
                }
            }
        });
        addNe.setOnAction((e) -> {
            GlistenInputDialog dialog = new GlistenInputDialog("");
            dialog.setTitle("Project Name");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().trim().isEmpty()) {
                boolean contains = false;
                for (Project p : ProjectTree.getTree().getProjects()) {
                    if (p.getProjectName().equals(result.get().trim())) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    Project pro = new Project(FileUtils.newFile("Projects" + File.separator + result.get().trim()), true);
                    ArrayList<Script> scripts = pro.getScripts();
                    scripts.get(0).save(area.getText());
                    scripts.get(1).getFile().delete();
                    pro.removeScript(scripts.get(1));
                    for (Script sc : scripts) {
                        if (sc.getType() < 4) {
                            Editor ed;
                            tabPane.getTabs().add(ed = new Editor(sc, sc.getProject()));
                            tabPane.getSelectionModel().select(ed);
                        } else {
                            Viewer ed;
                            tabPane.getTabs().add(ed = new Viewer(sc, sc.getProject()));
                            tabPane.getSelectionModel().select(ed);
                        }
                    }
                    ProjectTree.getTree().addProject(pro);
                    Tachyon.drawer.setSelectedItem(Tachyon.primaryItem);
                } else {
                    Alert al = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "Project with this name already exists!");
                    al.showAndWait();
                }
            }
        });
        setContent((box));
    }

    private Project getProject(String st) {
        for (Project p : ProjectTree.getTree().getProjects()) {
            if (p.getProjectName().equals(st)) {
                return p;
            }
        }
        return null;
    }

    public void showExample(String ident) {
        area.setText(map.get(ident));
    }

    public String getCodeText() {
        return area.getText();
    }

    public void add(String s, String code) {
        view.getItems().add(s);
        map.put(s, code);
    }

}
