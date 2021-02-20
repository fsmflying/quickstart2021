package com.fsmflying.study.quickstart2021.thread;

import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class ThreadTest {

    /**
     * 计数器
     */
    private int counter = 0;

    private Runnable runnable =  new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                counter++;
            }
        }
    };

    /**
     * 创建线程：方式1
     */
    @Test
    public void test01_newThread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                //super.run();
                for (int i = 0; i < 10000; i++) {
                    counter++;
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {


        }
        System.out.println(counter);

    }

    /**
     * 创建线程：方式2
     */
    @Test
    public void test02_newThread() {

        Thread thread = new Thread(runnable);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("counter:" + counter);
    }

    /**
     *
     */
    @Test
    public void test03_Executor(){
        Executor executor = new Executor(){
            @Override
            public void execute(Runnable command) {

            }
        };
    }

    @Test
    public void test04_ExecutorService(){
        ExecutorService executorService = null;
    }
}
