package com.framework.utils;

import java.io.File;

public class FileUtils {

    public static String loadFile(String fileLocation){
        try {

            return new String(FileUtils.class.getClassLoader().
                    getResourceAsStream("testData/"+fileLocation).
                    readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
