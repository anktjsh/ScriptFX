/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scriptfx.core;

/**
 *
 * @author Aniket
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 *
 * @author Spartacus Rex
 */
public class Jar {

    static final int BUFFER_SIZE = 10240;
    static byte mBuffer[] = new byte[BUFFER_SIZE];
    static boolean mVerbose = false;

    public static void main(String[] zArgs) {
        if (zArgs == null || zArgs.length < 2) {
            System.out.println("Usage : jar [-v] JARFILE FOLDER SOURCE");
        } else if (zArgs.length == 2) {
            //Its just the File and Folder
            String archive = zArgs[0];
            String folder = zArgs[1];
            String source = zArgs[2];

            createJarArchive(source, new File(archive), new File(folder));
        } else {
            //Its just the File and Folder
            mVerbose = true;
            String archive = zArgs[1];
            String folder = zArgs[2];
            String source = zArgs[2];

            System.out.println("JAR folder : " + folder + " > " + archive);
            createJarArchive(source, new File(archive), new File(folder));
        }
    }

    protected static void createJarArchive(String SOURCE, File archiveFile, File zTobeJared) {
        try {
            try ( // Open archive file
                    FileOutputStream stream = new FileOutputStream(archiveFile)) {
                Manifest manifest = new Manifest();
                manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
                manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, "scriptfx.resources.Launcher");

                //Add the files..
                //addFile(zTobeJared, out);
                try ( //Create the jar file
                        JarOutputStream out = new JarOutputStream(stream, manifest)) {
                    //Add the files..
                    //addFile(zTobeJared, out);
                    add(SOURCE, zTobeJared, out);
                }
            }
            System.out.println("Adding completed OK");

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error: " + ex.getMessage());
        }
    }

//    protected static void addFile(File zFile, JarOutputStream zOut) throws IOException {
//        //Check..
//        if (!zFile.exists() || !zFile.canRead()) {
//            return;
//        }
//
//        if (mVerbose) {
//            System.out.println("Adding file : " + zFile.getPath());
//        }
//
//        //Add it..
//        if (zFile.isDirectory()) {
//            //Cycle through
//            File[] files = zFile.listFiles();
//            for (File ff : files) {
//                addFile(ff, zOut);
//            }
//
//        } else {
//            // Add archive entry
//            JarEntry jarAdd = new JarEntry(zFile.getName());
//            jarAdd.setTime(zFile.lastModified());
//            zOut.putNextEntry(jarAdd);
//            try ( // Write file to archive
//                    FileInputStream in = new FileInputStream(zFile)) {
//                while (true) {
//                    int nRead = in.read(mBuffer, 0, mBuffer.length);
//                    if (nRead <= 0) {
//                        break;
//                    }
//
//                    zOut.write(mBuffer, 0, nRead);
//                }
//            }
//        }
//    }

    private static void add(String SOURCE, File source, JarOutputStream target) throws IOException {
        if (mVerbose) {
            System.out.println("Adding file : " + source.getPath());
        }

        BufferedInputStream in = null;
        try {
            if (source.isDirectory()) {
                String name = source.getPath();
                if (!name.isEmpty()) {
                    if (!name.endsWith("/")) {
                        name += "/";
                    }
                    //Add the Entry
                    JarEntry entry = new JarEntry(name.replace(SOURCE, ""));
                    entry.setTime(source.lastModified());
                    target.putNextEntry(entry);
                    target.closeEntry();
                }

                for (File nestedFile : source.listFiles()) {
                    add(SOURCE, nestedFile, target);
                }

                return;
            }
            JarEntry entry = new JarEntry(source.getPath().replace(SOURCE, ""));
            entry.setTime(source.lastModified());
            target.putNextEntry(entry);
            in = new BufferedInputStream(new FileInputStream(source));
            byte[] buffer = new byte[1024];
            while (true) {
                int count = in.read(buffer);
                if (count == -1) {
                    break;
                }
                target.write(buffer, 0, count);
            }
            target.closeEntry();

        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

}
