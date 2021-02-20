package com.fsmflying.study.quickstart2021.netty;

import com.fsmflying.study.quickstart2021.PathUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureTest {


    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * 统计文件中字符的数量
     * @param fileFullName
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private Map<Character, Integer> calculate(String fileFullName)
            throws IOException, ExecutionException, InterruptedException {
        final RandomAccessFile file = new RandomAccessFile(fileFullName, "r");
        String line = null;
        List<List<String>> list = new ArrayList<>(10);
        int num = 0;
        List<String> sublist = new ArrayList<>(10);
        //拆分文件成字符串列表，每10行一个列表
        while (null != (line = file.readLine())) {
            if ((num % 10) == 0 && (num > 0)) {
                list.add(sublist);
                sublist = new ArrayList<>(10);
            }
            sublist.add(line);
            num++;
        }
        file.close();
        list.add(sublist);
        //调用线程池进行统计
        List<Future<Map<Character, Integer>>> futureList = new ArrayList<>();
        for (List<String> e : list) {
            futureList.add(executorService.submit(() -> {
                Map<Character, Integer> map = new HashMap<>();
                for (String s : e) {
                    for (int i = 0; i < s.length(); i++) {
                        if (map.containsKey(s.charAt(i))) {
                            map.put(s.charAt(i), map.get(s.charAt(i)) + 1);
                        } else {
                            map.put(s.charAt(i), 1);
                        }
                    }
                }
                return map;
            }));
        }
        //检查任务是否完成
        boolean finish = false;
        while (!finish) {
            finish = true;
            for (Future<Map<Character, Integer>> f : futureList) {
                if (!f.isDone()) {
                    finish = false;
                }
            }
            if (!finish) {
                Thread.sleep(3000);
            }
        }
        //分别执行
        Map<Character, Integer> resultMap = new HashMap<>();
        for (Future<Map<Character, Integer>> f : futureList) {
            for (Map.Entry<Character, Integer> m : f.get().entrySet()) {
                if (resultMap.containsKey(m.getKey())) {
                    resultMap.put(m.getKey(), resultMap.get(m.getKey()) + m.getValue());
                } else {
                    resultMap.put(m.getKey(), m.getValue());
                }
            }
        }
        return resultMap;
    }

    @Test
    public void test01_Future() throws IOException, ExecutionException, InterruptedException {
        String fileFulllName = PathUtils.getResourcePath("csdn.24000.txt");
        Map<Character, Integer> f = calculate(fileFulllName);
        for (Map.Entry<Character, Integer> e : f.entrySet()) {
            System.out.println(String.format("%c:%d", e.getKey(), e.getValue()));
        }
    }

    @Test
    public void test02_Future(){

    }
}
