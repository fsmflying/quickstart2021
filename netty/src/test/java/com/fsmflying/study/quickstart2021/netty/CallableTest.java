package com.fsmflying.study.quickstart2021.netty;

import org.junit.Test;

import java.util.concurrent.Callable;

public class CallableTest {

    @Test
    public void test01_Callable(){
        Callable<String> callable = new Callable<String>(){

            @Override
            public String call() throws Exception {
                return null;
            }
        };

        Thread thread = new Thread();
    }
}
