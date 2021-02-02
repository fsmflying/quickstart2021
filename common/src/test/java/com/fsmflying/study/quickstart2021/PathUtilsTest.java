package com.fsmflying.study.quickstart2021;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class PathUtilsTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void test01() {
        try {
            System.out.println(PathUtils.getResourcePath("csdn.10.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(PathUtils.getTestResourcePath("csdn.10.txt"));
        System.out.println(PathUtils.getRelativeResourcePath("csdn.10.txt"));
    }
}
