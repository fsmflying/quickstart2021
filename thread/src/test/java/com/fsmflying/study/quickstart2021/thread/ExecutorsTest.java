package com.fsmflying.study.quickstart2021.thread;

import org.junit.Test;

import java.util.concurrent.*;

public class ExecutorsTest {

    /**
     * 计数器
     */
    private int counter = 0;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                //counter++;
                System.out.println(counter++);
            }
        }
    };

    private Callable<Integer> callable = new Callable<Integer>() {


        @Override
        public Integer call() throws Exception {
            for (int i = 0; i < 10000; i++) {
                counter++;
            }
            return counter;
        }
    };

    /**
     * 创建线程：方式1
     */
    @Test
    public void test01_newThread() {
        //创建一个其线程池具有 10 个线程的ScheduledExecutorService
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        //创建一个 Runnable,以供调度稍后执行
        ScheduledFuture<?> f = executor.schedule(runnable, 3, TimeUnit.SECONDS);//调度任务在从现在开始的60秒之后执行
        //一旦调度任务执行完成,就关闭ScheduledExecutorService 以释放资源
        try {
            executor.awaitTermination(5,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();


    }

    /**
     * 创建线程：方式2
     */
    @Test
    public void test02_newThread() {


    }

    /**
     *
     */
    @Test
    public void test03_Executor() {

    }


}
