package com.cannapaceus.services;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class OSService {
    private String sOSName;

    /** Singleton Class Design **/
    private static OSService instance = null;

    public OSService getInstance() {
        if(instance == null) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")){
                sOSName = "Windows";
            }
            else if (os.contains("osx")){
                sOSName = "OSX";
            }
        }

        return instance;
    }

    private void directoriesServices() {
        FileSystemView filesys = FileSystemView.getFileSystemView();
        String PATH_Desktop = filesys.getHomeDirectory().getPath();
        String homeDirectoryName = PATH_Desktop.concat("\\Instructor Grading Program");

        File homeDirectory = new File(homeDirectoryName);
        if (! homeDirectory.exists()){
            homeDirectory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }

        String property = "java.io.tmpdir";
        String PATH_TempDirectory = System.getProperty(property);
        String tempDirectoryName = PATH_Desktop.concat("\\Instructor Grading Program");

        File tempDirectory = new File(homeDirectoryName);
        if (! homeDirectory.exists()){
            homeDirectory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
    }
}
