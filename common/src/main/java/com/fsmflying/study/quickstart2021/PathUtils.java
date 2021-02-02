package com.fsmflying.study.quickstart2021;

import java.io.FileNotFoundException;

public class PathUtils {

    public static String getResourcePath(String fileName) throws FileNotFoundException{
        if(null == fileName)  return null;
        String path = ClassLoader.getSystemResource(".").getPath();
        if (path.endsWith("target/classes/")) {
            path = path.replace("target/classes/", "src/main/resources/");
        } else if (path.endsWith("target/test-classes/")) {
            path = path.replace("target/test-classes/", "src/main/resources/");
        }
        return path + fileName;
    }

    public static String getTestResourcePath(String fileName) {
        if(null == fileName)  return null;
        String path = ClassLoader.getSystemResource(".").getPath();
        if (path.endsWith("target/classes/")) {
            path = path.replace("target/classes/", "src/test/resources/");
        } else if (path.endsWith("target/test-classes/")) {
            path = path.replace("target/test-classes/", "src/test/resources/");
        }
        return path + fileName;
    }

    public static String getRelativeResourcePath(String fileName) {
        if(null == fileName)  return null;
        String path = ClassLoader.getSystemResource(".").getPath();
        if (path.endsWith("target/classes/")) {
            path = path.replace("target/classes/", "src/main/resources/");
        } else if (path.endsWith("target/test-classes/")) {
            path = path.replace("target/test-classes/", "src/test/resources/");
        }
        return path + fileName;
    }
}
