/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scriptfx.core;

import java.io.File;
import java.util.List;
import javafx.collections.FXCollections;

/**
 *
 * @author Aniket
 */
public class Script {

    public static final int JAVASCRIPT = 0;
    public static final int HTML = 1;
    public static final int CSS = 2;
    public static final int RESOURCE = 4;
    public static final int UNDETERMINED = -1;
    public static final int RANDOMEDITABLE = 3;
    private File file;
    private final int type;
    private String lastCode;
    private final Project project;

    public Script(File f, int flag, Project pro, String code) {
        file = f;
        if (flag == UNDETERMINED) {
            if (file.getAbsolutePath().endsWith(".js")) {
                type = 0;
            } else if (file.getAbsolutePath().endsWith(".css")) {
                type = 2;
            } else if (file.getAbsolutePath().endsWith(".html")) {
                type = 1;
            } else {
                type = 3;
            }
        } else {
            type = flag;
        }
        lastCode = code;
        project = pro;
        initScript(code);
    }

    public void reload() {
        lastCode = readCode();
    }

    public boolean canSave(String code) {
        return !code.equals(lastCode);
    }

    public Project getProject() {
        return project;
    }

    public void save(String code) {
        System.out.println("save : " + file.getAbsolutePath());
        FileUtils.write(file, FXCollections.observableArrayList(code.split("\n")));
        lastCode = code;
    }

    public String getCurrentCode() {
        return lastCode;
    }

    private void initScript(String list) {
        if (!file.exists()) {
            getFile().getParentFile().mkdirs();
            FileUtils.createFile(getFile());
            FileUtils.write(getFile(), FXCollections.observableArrayList(list.split("\n")));
        } else {
            lastCode = readCode();
        }

    }

    private String readCode() {
        StringBuilder sb = new StringBuilder();
        List<String> lines = FileUtils.readAllLines(file);
        for (String s : lines) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }
    
    void rename(String s) {
        File f;
        file.renameTo(f = new File(file.getParent(), s));
        file = f;
    }

    public File getFile() {
        return file;
    }

    public int getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Script) {
            Script pro = (Script) obj;
            if (pro.getFile().equals(getFile())) {
                return true;
            }
        }
        return false;
    }

}
