/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scriptfx.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tachyon.core.views.ProjectManagerView;

/**
 *
 * @author Aniket
 */
public class Project {

    private File rootDirectory;
    private String projectName;
    private File source;
    private File config;
    private final ArrayList<Script> allScripts;
    private final ObservableList<ProjectListener> listeners;

    public Project(File src, boolean isNew) {
        rootDirectory = src;
        if (!rootDirectory.exists()) {
            rootDirectory.mkdirs();
        }
        projectName = rootDirectory.getName();
        source = new File(rootDirectory.getAbsolutePath() + File.separator + "src");
        if (!source.exists()) {
            FileUtils.createDirectories(source);
        }

        allScripts = new ArrayList<>();
        listeners = FXCollections.observableArrayList();
        if (isNew) {
            initializeScript();
        } else {
            addScriptsToList();
        }
        config = new File(rootDirectory.getAbsolutePath() + File.separator + "settings.config");
        if (!config.exists()) {
            saveConfig();
        } else {
            readConfig();
        }
    }

    public void close() {
        saveConfig();
    }

    private String iconPath;
    private String applicationName;
    private String windowWidth;
    private String windowHeight;

    private void readConfig() {
        try {
            List<String> al = FileUtils.readAllLines(config);
            setIconPath(al.remove(0));
            setApplicationName(al.remove(0));
            setWindowWidth(al.remove(0));
            setWindowHeight(al.remove(0));
        } catch (Exception e) {
        }
    }

    public void saveConfig() {
        FileUtils.write(config, FXCollections.observableArrayList(getIconPath() == null ? "" : getIconPath(),
                getApplicationName() == null ? "" : getApplicationName(),
                getWindowWidth() == null ? "" : getWindowWidth(),
                getWindowHeight() == null ? "" : getWindowHeight()));
    }

    public File getConfigFile() {
        return config;
    }

    public ArrayList<Script> getScripts() {
        return allScripts;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getApplicationName() {
        if (applicationName == null || applicationName.isEmpty()) {
            return rootDirectory.getName();
        }
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(String windowWidth) {
        this.windowWidth = windowWidth;
    }

    public String getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(String windowHeight) {
        this.windowHeight = windowHeight;
    }

    private void initializeScript() {
        initializeFiles();
    }

    private void addScriptsToList() {
        for (File f : source.listFiles()) {
            addScriptsToList(f);
        }
    }

    private void addScriptsToList(File f) {
        if (!f.isHidden()) {
            if (f.isFile()) {
                if (f.getName().endsWith(".html")) {
                    addScript(new Script(f, 1, this, ""));
                } else if (f.getName().endsWith(".css")) {
                    addScript(new Script(f, 2, this, ""));
                } else if (f.getName().endsWith(".js")) {
                    addScript(new Script(f, 0, this, ""));
                } else {
                    String mime = FileUtils.probeContentType(f);
                    if (mime != null) {
                        if (mime.startsWith("text/")) {
                            addScript(new Script(f, Script.RANDOMEDITABLE, this, ""));
                        } else if (mime.startsWith("image/")) {
                            addScript(new Script(f, Script.RESOURCE, this, ""));
                        } else {
                            addScript(new Script(f, Script.RESOURCE, this, ""));
                        }
                    } else {
                        addScript(new Script(f, Script.RESOURCE, this, ""));
                    }

                }
            } else {
                for (File fa : f.listFiles()) {
                    addScriptsToList(fa);
                }
            }
        }
    }

    public void addListener(ProjectListener al) {
        listeners.add(al);
    }

    public void addScript(Script scr) {
        allScripts.add(scr);
        for (ProjectListener pl : listeners) {
            pl.fileAdded(this, scr);
        }
    }

    public void removeScript(Script scr) {
        allScripts.remove(scr);
        for (ProjectListener pl : listeners) {
            pl.fileRemoved(this, scr);
        }
    }

    private void initializeFiles() {
        Script htm = new Script(new File(source.getAbsolutePath() + File.separator + "index.html"), Script.HTML, this, getHtmlLines());
        addScript(htm);
        Script js = new Script(new File(source.getAbsolutePath() + File.separator + "test.js"), Script.JAVASCRIPT, this, getJsLines());
        addScript(js);
    }

    public void delete() {
        deepDelete(rootDirectory);
    }

    private String getJsLines() {
        StringBuilder sb = new StringBuilder();
        Scanner in = new Scanner(ProjectManagerView.class.getResourceAsStream("test.js"));
        while (in.hasNextLine()) {
            sb.append(in.nextLine()).append("\n");
        }
        return sb.toString();
    }

    private String getHtmlLines() {
        StringBuilder sb = new StringBuilder();
        Scanner in = new Scanner(ProjectManagerView.class.getResourceAsStream("index.html"));
        while (in.hasNextLine()) {
            sb.append(in.nextLine()).append("\n");
        }
        return sb.toString();
    }

    public String serialize() {
        return "Project : " + getRootDirectory().getAbsolutePath();
    }

    public static Project unserialize(String s) {
        try {
            String[] split = s.split(" : ");
            return unserialize(new File(split[1]));
        } catch (Exception e) {
            return null;
        }
    }

    public static Project unserialize(File f) {
        if (FileUtils.exists(f)) {
            try {
                return new Project(f, false);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static long randomNum() {
        return Math.abs(new Random().nextLong());
    }

    public String getHtmlUri() {
        for (int x = rootDirectory.listFiles().length - 1; x >= 0; x--) {
            if (rootDirectory.listFiles()[x].getName().startsWith("launcher")) {
                deepDelete(rootDirectory.listFiles()[x]);
            }
        }
        File launcher = new File(rootDirectory.getAbsolutePath() + File.separator + "launcher" + randomNum());
        if (!launcher.exists()) {
            launcher.mkdirs();
        }
//        copy(source, launcher, "");
        for (File f : source.listFiles()) {
            if (f.isDirectory()) {
                copy(f, launcher, "");
            } else {
                try {
                    FileUtils.copy(new FileInputStream(f), new FileOutputStream(launcher.getAbsolutePath() + File.separator + f.getName()));
                } catch (IOException ex) {
                    Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return new File(launcher.getAbsolutePath() + File.separator + "index.html").toURI().toString();
    }

    public static void copy(File src, File dest, String temp) {
        if (src.isDirectory()) {
            for (File f : src.listFiles()) {
                copy(f, dest, temp += File.separator + src.getName());
            }
        } else {
            try {
                File out = new File(dest.getAbsolutePath() + File.separator + temp + File.separator + src.getName());
                if (!out.getParentFile().exists()) {
                    out.getParentFile().mkdirs();
                }
                FileUtils.copy(new FileInputStream(src), new FileOutputStream(out));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deepDelete(File fe) {
        if (!FileUtils.exists(fe)) {
            return;
        }
        if (fe.isDirectory()) {
            if (fe.listFiles() != null) {
                for (File f : fe.listFiles()) {
                    deepDelete(f);
                }
            }
            fe.delete();
        } else {
            fe.delete();
        }
    }

    public File getRootDirectory() {
        return rootDirectory;
    }

    public String getProjectName() {
        return projectName;
    }

    public File getSource() {
        return source;
    }

    public interface ProjectListener {

        public void fileAdded(Project pro, Script add);

        public void fileRemoved(Project pro, Script scr);

        public void projectNamed(Project pro, String previous, String name);

        public void scriptRenamed(Script scr, String previous, String name);
    }

    public void rename(String s) {
        String prev = getRootDirectory().getName();
        File f;
        getRootDirectory().renameTo(f = new File(getRootDirectory().getParent(), s));
        rootDirectory = f;
        projectName = getRootDirectory().getName();
        source = (new File(rootDirectory.getAbsolutePath() + File.separator + "src"));
        config = (new File(rootDirectory.getAbsolutePath() + File.separator + "settings.config"));
        for (ProjectListener pl : listeners) {
            pl.projectNamed(this, prev, s);
        }
    }

    public void renameScript(Script s, String na) {
        for (Script sc : getScripts()) {
            if (sc.equals(s)) {
                String prev = sc.getFile().getName();
                sc.rename(na);
                for (ProjectListener pl : listeners) {
                    pl.scriptRenamed(sc, prev, na);
                }
                break;
            }
        }
    }

    public static Project loadProject(File pro, boolean isNew) {
        File config = new File(pro.getAbsolutePath() + File.separator + "settings.config");
        if (config.exists()) {
            return new Project(pro, isNew);
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Project) {
            Project pro = (Project) obj;
            if (pro.getRootDirectory().equals(getRootDirectory())) {
                return true;
            }
        }
        return false;
    }
}
