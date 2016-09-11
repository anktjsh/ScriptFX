/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tachyon.core.views;

import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import scriptfx.core.FileUtils;
import scriptfx.core.Project;
import scriptfx.core.Script;

/**
 *
 * @author Aniket
 */
public class Viewer extends ScriptTab {

    private ImageView view;

    public Viewer(Script sc, Project pro) {
        super(sc, pro);
        String mime = FileUtils.probeContentType(sc.getFile());
        if (mime != null) {
            if (mime.startsWith("image/")) {
                view = new ImageView(getImage(sc.getFile()));
                setContent(view);
            }
        }
    }

    private Image getImage(File f) {
        try {
            return new Image(f.toURI().toString());
        } catch (Exception e) {
            return null;
        }
    }

}
