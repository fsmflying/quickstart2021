package com.fsmflying.study.quickstart2021.netty;

import org.junit.Test;

public class RunnableTest {

    @Test
    public void test01_Runnable(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };


        Thread  task = new Thread(runnable);
        task.start();
        try {
            task.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
