package com.cannapaceus.services;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class trial {

    public static void main(String[] args) {
        FileSystemView filesys = FileSystemView.getFileSystemView();
        String PATH_Desktop = filesys.getHomeDirectory().getPath();
        String homeDirectoryName = PATH_Desktop.concat("\\Instructor Grading Program");

        System.out.println(homeDirectoryName);

        String property = "java.io.tmpdir";
        String PATH_TempDirectory = System.getProperty(property);

        System.out.println(PATH_TempDirectory);
    }
}
