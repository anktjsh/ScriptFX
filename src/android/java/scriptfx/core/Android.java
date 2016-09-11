/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scriptfx.core;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import java.io.File;
import javafxports.android.FXActivity;

/**
 *
 * @author Aniket
 */
public class Android extends Provider {

    @Override
    public File getFile(String s) {
        return new File(s);
    }

    @Override
    public void sendEmail(File attach) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("application/zip");
        File attachment = new File(Environment.getExternalStorageDirectory(), attach.getName());
        FileUtils.copy(attach, attachment);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Export Source Files");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Zip File : " + attachment.getName());
        if (!attachment.exists() || !attachment.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(attachment);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        FXActivity.getInstance().startActivity(Intent.createChooser(emailIntent, "Choose a way to export this file"));
    }

}
