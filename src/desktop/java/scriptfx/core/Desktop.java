/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scriptfx.core;

import java.io.File;

/**
 *
 * @author Aniket
 */
public class Desktop extends Provider {

    @Override
    public File getFile(String s) {
        return new File(s);
    }

    @Override
    public void sendEmail(File attach) {
        new EmailPicker(null, "Export File", attach).showAndWait();
    }

}
