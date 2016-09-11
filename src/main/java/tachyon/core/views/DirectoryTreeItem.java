/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tachyon.core.views;

import java.io.File;
import scriptfx.core.Project;

/**
 *
 * @author Aniket
 */
public class DirectoryTreeItem extends ProjectTreeItem {

    private final File path;

    public DirectoryTreeItem(Project pro, File dir) {
        super(pro);
        path = dir;
        setValue(dir.getName());
    }

    public File getPath() {
        return path;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DirectoryTreeItem) {
            DirectoryTreeItem dti = (DirectoryTreeItem) obj;
            if (dti.getPath().equals(getPath())) {
                return true;
            }
        }
        return false;
    }

}
