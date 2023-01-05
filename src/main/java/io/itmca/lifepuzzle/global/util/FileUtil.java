package io.itmca.lifepuzzle.global.util;

import java.io.File;

public class FileUtil {
    static public Boolean isExistFolder(String folderPath){
        var folder = new File(folderPath);
        return folder.exists();
    }

    static public void createAllFolder(String folderPath){
        var folder = new File(folderPath);
        folder.mkdirs();
    }
}
