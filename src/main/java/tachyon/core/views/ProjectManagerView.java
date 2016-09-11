package tachyon.core.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import static com.gluonhq.charm.glisten.application.MobileApplication.HOME_VIEW;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import scriptfx.core.FileUtils;
import scriptfx.core.GlistenInputDialog;
import scriptfx.core.Jar;
import scriptfx.core.Project;
import scriptfx.core.ProjectTree;
import scriptfx.core.Script;
import scriptfx.core.Service;
import scriptfx.core.ZipUtils;
import tachyon.core.Tachyon;

public class ProjectManagerView extends View {

    TreeView<String> manager;
    private final FloatingActionButton add;
    private final HBox options;
    private final Button open, newF, delete, export, rename;
    private final TabPane tabPane;

    public ProjectManagerView(String name) {
        super(name);
        tabPane = (TabPane) ((ScriptWriter) Tachyon.getInstance().retrieveView(HOME_VIEW).get().getCenter()).getCenter();
        getStylesheets().add(ProjectManagerView.class.getResource("filemanager.css").toExternalForm());
        setTop(options = new HBox(2));
        options.setPadding(new Insets(5));
        options.setAlignment(Pos.CENTER);
        options.getChildren().addAll(open = new Button("Open"), newF = new Button("New File"),
                delete = new Button("Delete"), rename = new Button("Rename"), export = new Button("Export"));
        setCenter(manager = new TreeView<>());
        manager.setRoot(new TreeItem<>("Projects"));
        manager.getRoot().setExpanded(true);
        for (Project added : ProjectTree.getTree().getProjects()) {
            ProjectTreeItem item;
            manager.getRoot().getChildren().add(item = new ProjectTreeItem(added));
            item.setExpanded(true);
            added.addListener((new Project.ProjectListener() {

                @Override
                public void fileAdded(Project pro, Script add) {
                    if (!scriptContains(item.getChildren(), add)) {
                        addScriptTreeItem(item, new ScriptTreeItem(add));
                    }
                }

                @Override
                public void fileRemoved(Project pro, Script scr) {
                    findScriptTreeItem(item, scr);
                    for (int x = tabPane.getTabs().size() - 1; x >= 0; x--) {
                        if (tabPane.getTabs().get(x) instanceof Editor) {
                            Editor ed = (Editor) tabPane.getTabs().get(x);
                            if (ed.getScript().equals(scr)) {
                                ed.close();
                            }
                        }
                    }
                }

                @Override
                public void projectNamed(Project pro, String previous, String name) {
                    for (TreeItem<String> sti : manager.getRoot().getChildren()) {
                        if (sti instanceof ProjectTreeItem) {
                            ProjectTreeItem pti = (ProjectTreeItem) sti;
                            if (pti.getProject().getRootDirectory().getName().equals(name)) {
                                pti.setValue(name);
                                break;
                            }
                        }
                    }
                }

                @Override
                public void scriptRenamed(Script scr, String previous, String name) {
                    renameScriptTreeItem(item, scr, name);
                }
            }));
            for (Script s : added.getScripts()) {
                addScriptTreeItem(item, new ScriptTreeItem(s));
            }
        }
        ProjectTree.getTree().addListener(new ProjectTree.ProjectTreeListener() {

            @Override
            public void projectAdded(Project added) {
                if (!projectContains(manager.getRoot().getChildren(), added)) {
                    ProjectTreeItem item;
                    manager.getRoot().getChildren().add(item = new ProjectTreeItem(added));
                    item.setExpanded(true);
                    added.addListener((new Project.ProjectListener() {

                        @Override
                        public void fileAdded(Project pro, Script add) {
                            if (!scriptContains(item.getChildren(), add)) {
                                addScriptTreeItem(item, new ScriptTreeItem(add));
                            }
                        }

                        @Override
                        public void fileRemoved(Project pro, Script scr) {
                            findScriptTreeItem(item, scr);
                            for (int x = tabPane.getTabs().size() - 1; x >= 0; x--) {
                                if (tabPane.getTabs().get(x) instanceof Editor) {
                                    Editor ed = (Editor) tabPane.getTabs().get(x);
                                    if (ed.getScript().equals(scr)) {
                                        ed.close();
                                    }
                                }
                            }
                        }

                        @Override
                        public void projectNamed(Project pro, String previous, String name) {
                            for (TreeItem<String> sti : manager.getRoot().getChildren()) {
                                if (sti instanceof ProjectTreeItem) {
                                    ProjectTreeItem pti = (ProjectTreeItem) sti;
                                    if (pti.getProject().getRootDirectory().getName().equals(previous)) {
                                        pti.setValue(name);
                                        break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void scriptRenamed(Script scr, String previous, String name) {
                            renameScriptTreeItem(item, scr, name);
                        }
                    }));
                    for (Script s : added.getScripts()) {
                        addScriptTreeItem(item, new ScriptTreeItem(s));
                    }
                }
            }

            @Override
            public void projectRemoved(Project pro) {
                for (TreeItem<String> tre : manager.getRoot().getChildren()) {
                    if (tre instanceof ProjectTreeItem) {
                        ProjectTreeItem pri = (ProjectTreeItem) tre;
                        if (pri.getProject().equals(pro)) {
                            if (pri.getParent() != null) {
                                pri.getParent().getChildren().remove(pri);
                                break;
                            }
                        }
                    }
                }

                for (int x = tabPane.getTabs().size() - 1; x >= 0; x--) {
                    Tab b = tabPane.getTabs().get(x);
                    if (b instanceof Editor) {
                        Editor ea = (Editor) b;
                        if (ea.getScript().getProject().equals(pro)) {
                            ea.close();
                        }
                    }
                }
            }
        });
        open.setDisable(true);
        newF.setDisable(true);
        rename.setDisable(true);
        delete.setDisable(true);
        export.setDisable(true);
        manager.getSelectionModel().selectedItemProperty().addListener((ob, older, newer) -> {
            if (newer instanceof ScriptTreeItem) {
                open.setDisable(false);
                newF.setDisable(false);
                rename.setDisable(false);
                delete.setDisable(false);
                export.setDisable(true);
            } else if (newer instanceof DirectoryTreeItem) {
                open.setDisable(false);
                newF.setDisable(false);
                rename.setDisable(true);
                delete.setDisable(true);
                export.setDisable(true);
            } else if (newer instanceof ProjectTreeItem) {
                open.setDisable(true);
                newF.setDisable(false);
                rename.setDisable(false);
                delete.setDisable(false);
                export.setDisable(false);
            } else {
                open.setDisable(true);
                newF.setDisable(true);
                rename.setDisable(true);
                delete.setDisable(true);
                export.setDisable(true);
            }
        });
        open.setOnAction((e) -> {
            boolean contains = false;
            ScriptTab tab = null;
            for (Tab b : tabPane.getTabs()) {
                if (b instanceof ScriptTab) {
                    if (manager.getSelectionModel().getSelectedItem() instanceof ScriptTreeItem) {
                        if (((ScriptTab) b).getScript().equals(((ScriptTreeItem) (manager.getSelectionModel().getSelectedItem())).getScript())) {
                            contains = true;
                            tab = (ScriptTab) b;
                            break;
                        }
                    }

                }
            }
            Script scr = ((ScriptTreeItem) (manager.getSelectionModel().getSelectedItem())).getScript();
            if (scr.getType() < 4) {
                if (!contains) {
                    Editor ed;
                    tabPane.getTabs().add(ed = new Editor(scr, scr.getProject()));
                    tabPane.getSelectionModel().select(ed);
                    Tachyon.drawer.setSelectedItem(Tachyon.primaryItem);
                } else {
                    tabPane.getSelectionModel().select(tab);
                    Tachyon.drawer.setSelectedItem(Tachyon.primaryItem);
                }
            } else if (!contains) {
                Viewer ed;
                tabPane.getTabs().add(ed = new Viewer(scr, scr.getProject()));
                tabPane.getSelectionModel().select(ed);
                Tachyon.drawer.setSelectedItem(Tachyon.primaryItem);
            } else {
                tabPane.getSelectionModel().select(tab);
                Tachyon.drawer.setSelectedItem(Tachyon.primaryItem);
            }
        });
        newF.setOnAction((e) -> {
            newFile();
        });
        rename.setOnAction((e) -> {
            if (manager.getSelectionModel().getSelectedItem() != null) {
                if (manager.getSelectionModel().getSelectedItem() instanceof ScriptTreeItem) {
                    Script scr = ((ScriptTreeItem) manager.getSelectionModel().getSelectedItem()).getScript();
                    if (!scr.getFile().getName().equals("index.html")) {
                        String filename = scr.getFile().getName().substring(0, scr.getFile().getName().lastIndexOf('.'));
                        String extension = scr.getFile().getName().substring(scr.getFile().getName().lastIndexOf('.'));
                        GlistenInputDialog gid = new GlistenInputDialog(filename);
                        gid.setTitle("Rename File");
                        gid.setExtension(extension);
                        Optional<String> show = gid.showAndWait();
                        if (show.isPresent()) {
                            File fa = new File(scr.getFile().getParent(), show.get() + extension);
                            if (!fa.exists()) {
                                scr.getProject().renameScript(scr, fa.getName());
                            } else {
                                Alert al = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
                                al.setContentText("Project Exists!");
                                al.showAndWait();
                            }
                        }
                    } else if (!(manager.getSelectionModel().getSelectedItem().getParent() instanceof DirectoryTreeItem)) {
                        String filename = scr.getFile().getName().substring(0, scr.getFile().getName().lastIndexOf('.'));
                        String extension = scr.getFile().getName().substring(scr.getFile().getName().lastIndexOf('.'));
                        GlistenInputDialog gid = new GlistenInputDialog(filename);
                        gid.setTitle("Rename File");
                        gid.setExtension(extension);
                        Optional<String> show = gid.showAndWait();
                        if (show.isPresent()) {
                            File fa = new File(scr.getFile().getParent(), show.get() + extension);
                            if (!fa.exists()) {
                                scr.getProject().renameScript(scr, fa.getName());
                            } else {
                                Alert al = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
                                al.setContentText("Project Exists!");
                                al.showAndWait();
                            }
                        }
                    } else {
                        //FIX
                    }
                } else if (manager.getSelectionModel().getSelectedItem() instanceof ProjectTreeItem) {
                    Project pro = ((ProjectTreeItem) manager.getSelectionModel().getSelectedItem()).getProject();
                    GlistenInputDialog gid = new GlistenInputDialog(pro.getProjectName());
                    gid.setTitle("Rename Project");
                    Optional<String> show = gid.showAndWait();
                    if (show.isPresent()) {
                        File fa = new File(pro.getRootDirectory().getParent(), show.get());
                        if (!fa.exists()) {
                            pro.rename(show.get());
                        } else {
                            Alert al = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
                            al.setContentText("Project Exists!");
                            al.showAndWait();
                        }
                    }
                }
            }
        });
        delete.setOnAction((e) -> {
            Alert al = new Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION, "Would you like to delete this item?");
            Optional<ButtonType> op = al.showAndWait();
            if (op.isPresent()) {
                if (op.get() == ButtonType.OK) {
                    if (manager.getSelectionModel().getSelectedItem() instanceof DirectoryTreeItem) {
                        DirectoryTreeItem dti = (DirectoryTreeItem) manager.getSelectionModel().getSelectedItem();
                        //deepDelete(dti.getPath());
                    } else if (manager.getSelectionModel().getSelectedItem() instanceof ProjectTreeItem) {
                        ProjectTreeItem pti = ((ProjectTreeItem) manager.getSelectionModel().getSelectedItem());
                        closeProject(pti.getProject());
                        pti.getProject().delete();
                    } else if (manager.getSelectionModel().getSelectedItem() instanceof ScriptTreeItem) {
                        ScriptTreeItem sti = ((ScriptTreeItem) manager.getSelectionModel().getSelectedItem());
                        if (sti.getScript().getFile().getName().equals("index.html")) {
                            Alert ala = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "Cannot delete the entrypoint html file!");
                            ala.showAndWait();
                        } else {
                            sti.getScript().getFile().delete();
                            sti.getScript().getProject().removeScript(sti.getScript());
                        }
                    }
                }
            }
        });
        export.setOnAction((e) -> {
            if (manager.getSelectionModel().getSelectedItem() instanceof ProjectTreeItem) {
                ProjectTreeItem pti = (ProjectTreeItem) manager.getSelectionModel().getSelectedItem();
                Project cur = pti.getProject();
                File dist = new File(cur.getRootDirectory().getAbsolutePath() + File.separator + "dist");
                dist.mkdirs();
                GlistenChoiceDialog cd = new GlistenChoiceDialog();
                cd.setItems(FXCollections.observableArrayList("Zip File", "Jar File"));
                cd.setSelectedValue("Zip File");
                cd.setTitle("Export Type");
                Optional<String> op = cd.showAndWait();
                if (op.isPresent()) {
                    if (op.get().startsWith("Zip")) {
                        String out = dist.getAbsolutePath()
                                + File.separator + cur.getProjectName() + ".zip";
                        ZipUtils appZip = new ZipUtils();
                        String source = cur.getSource().getAbsolutePath();
                        appZip.generateFileList(new File(source), source);
                        appZip.zipIt(source, out);
                        Service.get().sendEmail(new File(out));
                    } else {
                        Jar.main(new String[]{"-v", dist.getAbsolutePath()
                            + File.separator + cur.getProjectName() + ".jar", cur.getSource().getPath(), cur.getSource().getParentFile().getPath()});
                        Service.get().sendEmail(new File(dist.getAbsolutePath()
                                + File.separator + cur.getProjectName() + ".jar"));
                    }
                }
            }
        });
        setShowTransitionFactory(BounceInRightTransition::new);

        getLayers().add(add = new FloatingActionButton());
        add.setOnAction((e) -> {
            newProject();
        });
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Tachyon.MENU_LAYER)));
        appBar.setTitleText("Project Manager");
    }

    private void closeProject(Project pro) {
        ProjectTree.getTree().removeProject(pro);
    }

    public final void newProject() {
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
                for (Script s : scripts) {
                    if (s.getType() < 4) {
                        Editor ed;
                        tabPane.getTabs().add(ed = new Editor(s, s.getProject()));
                        tabPane.getSelectionModel().select(ed);
                    } else {
                        Viewer ed;
                        tabPane.getTabs().add(ed = new Viewer(s, s.getProject()));
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
    }

    private void newFile(Project pro, String temp) {
        GlistenChoiceDialog choice = new GlistenChoiceDialog();
        choice.setTitle("File Type");
        choice.setItems(FXCollections.observableArrayList("HTML File", "CSS File", "Javascript File", "Resource File"));
        Optional<String> ch = choice.showAndWait();
        if (ch.isPresent()) {
            int type = getType(ch.get());
            if (type < 3) {
                GlistenInputDialog dialog = new GlistenInputDialog(temp);
                String extension = getExtension(type);
                dialog.setTitle("File Name");
                dialog.setExtension(extension);
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    File f = new File(pro.getSource() + File.separator + result.get() + extension);
                    boolean contains = false;
                    for (Script scr : pro.getScripts()) {
                        if (scr.getFile().equals(f)) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        Script sc = new Script(f, type, pro, getCode(type));
                        pro.addScript(sc);
                    } else {
                        Alert al = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "File with this name already exists!");
                        al.showAndWait();
                    }
                }
            } else {
                GlistenInputDialog dialog = new GlistenInputDialog("");
                dialog.setTitle("Enter Resource URL");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    Pair<String, List<String>> combo = getContentType(result.get());
                    if (combo.getKey() != null && !combo.getKey().isEmpty()) {
                        String filename = getFileName(combo.getValue());
                        File f = automaticDownload(result.get(), combo.getKey(), filename, pro);
                        if (f != null) {
                            Download d = download(result.get(), f);
                            if (d != null) {
                                d.setOnSucceeded((e) -> {
                                    pro.addScript(new Script(f, Script.RESOURCE, pro, ""));
                                    Alert al = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION, result.get() + " Downloaded successfully!");
                                    al.showAndWait();
                                });
                                d.setOnFailed((e) -> {
                                    Alert al = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "File failed to download!");
                                    al.showAndWait();
                                });
                                (new Thread(d)).start();
                            } else {
                                Alert al = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "File failed to download!");
                                al.showAndWait();
                            }
                        } else {
                            Alert al = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "File failed to download!");
                            al.showAndWait();
                        }
                    } else {
                        Alert al = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "File failed to download!");
                        al.showAndWait();
                    }
                }
            }
        }
    }

    private Download download(String url, File file) {
        if (file != null) {
            Download d = new Download(url, file);
            return d;
        }
        return null;
    }

    public File automaticDownload(String url, String contentType, String name, Project pro) {
        try {
            String filename, extension;
            if (name == null) {
                filename = url.substring(url.lastIndexOf('/') + 1);
            } else {
                filename = name;
            }
            extension = filename.substring(filename.lastIndexOf('.') + 1);
            String sa = pro.getSource().getAbsolutePath() + File.separator + filename.substring(0, filename.lastIndexOf('.'));
            File f = new File(sa + "." + extension);
            int x = 1;
            while (f.exists()) {
                f = new File(sa + " (" + x + ")" + "." + extension);
                x++;
            }
            return f;
        } catch (Exception e) {
            return null;
        }
    }

    private String getFileName(List<String> disposition) {
        try {
            if (disposition != null) {
                String one = disposition.get(0);
                if (one.contains("filename*=UTF-8''")) {
                    String temp = one.substring(one.indexOf("filename*=UTF-8''") + "filename*=UTF-8''".length());
                    return temp;
                } else if (one.contains("filename=\"")) {
                    String temp = one.substring(one.indexOf("filename=\"") + "filename=\"".length());
                    return temp.substring(0, temp.indexOf('"'));
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    private Pair<String, List<String>> getContentType(String url) {
        try {
            URL urlas = new URL(url);
            URLConnection conn = urlas.openConnection();
            return new Pair<>(conn.getHeaderFields().get("Content-Type").get(0), conn.getHeaderFields().get("Content-Disposition"));
        } catch (Exception e) {
        }
        return new Pair<>("", new ArrayList<>());
    }

    public final void newFile() {
        if (manager.getSelectionModel().getSelectedItem() != null) {
            if (manager.getSelectionModel().getSelectedItem() instanceof DirectoryTreeItem) {
                Project pro = ((DirectoryTreeItem) manager.getSelectionModel().getSelectedItem()).getProject();
                newFile(pro, (((DirectoryTreeItem) manager.getSelectionModel().getSelectedItem()).getPath().getAbsolutePath())
                        .replace(((DirectoryTreeItem) manager.getSelectionModel().getSelectedItem()).getProject().getSource().getAbsolutePath() + File.separator, "") + File.separator);
            } else if (manager.getSelectionModel().getSelectedItem() instanceof ProjectTreeItem) {
                Project pro = ((ProjectTreeItem) manager.getSelectionModel().getSelectedItem()).getProject();
                newFile(pro, "");
            } else if (manager.getSelectionModel().getSelectedItem() instanceof ScriptTreeItem) {
                Project pro = ((ScriptTreeItem) manager.getSelectionModel().getSelectedItem()).getScript().getProject();
                newFile(pro, "");
            }
        }
    }

    private String getCode(int type) {
        StringBuilder sb = new StringBuilder();
        if (type == Script.JAVASCRIPT) {
            Scanner in = new Scanner(getClass().getResourceAsStream("test.js"));
            while (in.hasNextLine()) {
                sb.append(in.nextLine()).append("\n");
            }
        } else if (type == Script.HTML) {
            Scanner in = new Scanner(getClass().getResourceAsStream("index.html"));
            while (in.hasNextLine()) {
                sb.append(in.nextLine()).append("\n");
            }
        }

        return sb.toString();
    }

    private String getExtension(int type) {
        switch (type) {
            case 1:
                return ".html";
            case 0:
                return ".js";
            case 2:
                return ".css";
        }
        return "";
    }

    private int getType(String s) {
        if (s.equals("HTML File")) {
            return 1;
        }
        if (s.equals("Javascript File")) {
            return 0;
        }
        if (s.equals("CSS File")) {
            return 2;
        }
        return 3;
    }

    private void addScriptTreeItem(ProjectTreeItem pti, ScriptTreeItem sti) {
        String one, two;
        one = pti.getProject().getRootDirectory().getAbsolutePath() + File.separator + "src" + File.separator;
        two = sti.getScript().getFile().getAbsolutePath();
        String left = two.substring(one.length());
        if (left.contains(File.separator)) {
            DirectoryTreeItem last = null;
            while (left.contains(File.separator)) {
                DirectoryTreeItem dti = new DirectoryTreeItem(pti.getProject(), new File(one + File.separator + left.substring(0, left.indexOf(File.separator) + 1)));
                left = left.substring(left.indexOf(File.separator) + 1);
                if (pti.getChildren().contains(dti)) {
                    DirectoryTreeItem five = getDirectory(pti.getChildren(), dti);
                    if (five != null) {
                        dti = five;
                    }
                } else {
                    pti.getChildren().add(dti);
                }
                last = dti;
            }
            if (last != null) {
                last.getChildren().add(sti);
            }
        } else {
            pti.getChildren().add(sti);
        }
    }

    private DirectoryTreeItem getDirectory(List<TreeItem<String>> li, DirectoryTreeItem si) {
        for (TreeItem<String> ti : li) {
            if (ti instanceof DirectoryTreeItem) {
                DirectoryTreeItem dti = (DirectoryTreeItem) ti;
                if (dti.equals(si)) {
                    return dti;
                }
            }
        }
        return null;
    }

    private void findScriptTreeItem(ProjectTreeItem pro, Script scr) {
        for (int x = pro.getChildren().size() - 1; x >= 0; x--) {
            TreeItem<String> al = pro.getChildren().get(x);
            if (al instanceof ScriptTreeItem) {
                ScriptTreeItem sti = (ScriptTreeItem) al;
                if (sti.getScript().equals(scr)) {
                    pro.getChildren().remove(sti);
                }
            } else if (al instanceof DirectoryTreeItem) {
                DirectoryTreeItem dir = (DirectoryTreeItem) al;
                findScriptTreeItem(dir, scr);
            }
        }
    }

    private void renameScriptTreeItem(ProjectTreeItem pro, Script scr, String newer) {
        for (int x = pro.getChildren().size() - 1; x >= 0; x--) {
            TreeItem<String> al = pro.getChildren().get(x);
            if (al instanceof ScriptTreeItem) {
                ScriptTreeItem sti = (ScriptTreeItem) al;
                if (sti.getScript().equals(scr)) {
                    sti.setValue(newer);
                }
            } else if (al instanceof DirectoryTreeItem) {
                DirectoryTreeItem dir = (DirectoryTreeItem) al;
                findScriptTreeItem(dir, scr);
            }
        }
    }

    private boolean projectContains(ObservableList<TreeItem<String>> tre, Project pr) {
        for (TreeItem<String> tr : tre) {
            if (tr instanceof ProjectTreeItem) {
                ProjectTreeItem sc = (ProjectTreeItem) tr;
                return (sc.getProject().equals(pr));
            }
        }
        return false;
    }

    private boolean scriptContains(ObservableList<TreeItem<String>> pti, Script pro) {
        for (TreeItem<String> tr : pti) {
            if (tr instanceof ScriptTreeItem) {
                ScriptTreeItem sc = (ScriptTreeItem) tr;
                return (sc.getScript().equals(pro));
            }
        }
        return false;
    }

}
