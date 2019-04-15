package com.cannapaceus.services;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class OSService {

    private String sOSName;
    private Boolean bWindows;
    private Boolean bOSX;
    private String sHomeDirectoryName;
    private String sTempDirectoryName;

    /** Singleton Class Design **/
    private static OSService instance = null;

    public static OSService getInstance() {
        if(instance == null)
            instance = new OSService();
        return instance;
    }

    /**
     * Constructor
     */
    private OSService() {
        /**
         * Detecting the OS
         */
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")){
            sOSName = "Windows";
            bWindows = true;
            bOSX = false;
        }
        else if (os.contains("osx")){
            sOSName = "OSX";
            bWindows = false;
            bOSX = true;
        }

        /**
         * Desktop Directory
         */
        FileSystemView filesys = FileSystemView.getFileSystemView();
        String PATH_Desktop = filesys.getHomeDirectory().getPath();

        if (bWindows) {
            sHomeDirectoryName = PATH_Desktop.concat("\\Instructor Grading Program");
        }
        else if (bOSX) {
            sHomeDirectoryName = PATH_Desktop.concat("/Instructor Grading Program");
        }

        File homeDirectory = new File(sHomeDirectoryName);
        if (! homeDirectory.exists()){
            homeDirectory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }

        /**
         * Temp Data Directory
         */
        String property = "java.io.tmpdir";
        String PATH_TempDirectory = System.getProperty(property);

        if (bWindows) {
            sTempDirectoryName = PATH_TempDirectory.concat("\\Instructor Grading Program");
        }
        else if (bOSX) {
            sTempDirectoryName = PATH_TempDirectory.concat("/Instructor Grading Program");
        }

        File tempDirectory = new File(sTempDirectoryName);
        if (! tempDirectory.exists()){
            tempDirectory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
    }

    public String getDesktopDirectoryPath() {
        if (bWindows) {
            return sHomeDirectoryName.concat("\\");
        }
        else
            return sHomeDirectoryName.concat("/");
    }

    public String getTempDirectoryPath() {
        if (bWindows) {
            return sTempDirectoryName.concat("\\");
        }
        else
            return sTempDirectoryName.concat("/");
    }

    public Boolean delete(String sFullPath) {
        File file = new File(sFullPath);
        if (file.delete()){
            return true;
        }
        else
            return false;
    }
}
