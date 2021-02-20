package com.fsmflying.study.quickstart2021;

import java.io.FileNotFoundException;

public class PathUtils {

    /**
     * 获取相对资源文件完整路径
     *  如果在src/main/java目录下调用，则在src/main/resources/下查找
     *  如果在src/test/java目录下调用，则在src/test/resources/下查找
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
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

    /**
     * 从src/test/resources/目录下获取资源文件完整路径
     *
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
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

    /**
     * 从src/main/resources/目录下获取资源文件完整路径
     * @param fileName
     * @return
     */
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
