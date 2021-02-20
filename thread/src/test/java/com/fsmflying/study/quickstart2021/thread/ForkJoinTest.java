package com.fsmflying.study.quickstart2021.thread;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ForkJoinTest {

    private volatile int counter = 0;
    private AtomicInteger atomicCounter = new AtomicInteger(0);

    /**
     * 使用join合并一组线程执行结果
     */
    @Test
    public void test01_join() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    counter++;
                    atomicCounter.incrementAndGet();
                }
            }
        };
        int numThread = 100;
        List<Thread> threadList = new ArrayList<>(numThread);
        for (int i = 0; i < numThread; i++) {
            threadList.add(new Thread(runnable));
        }
        for (Thread t : threadList) {
            t.start();
        }
        try {
            for (Thread t : threadList) {
                t.join();
            }
        } catch (
                InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("counter:" + counter);
        System.out.println("atomicCounter:" + atomicCounter.get());

    }

    @Test
    public void test02_join() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < 10000; j++) {
                    counter++;
                }
                return;
            });
        }
    }
}
