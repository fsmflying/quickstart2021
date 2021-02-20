package com.fsmflying.study.quickstart2021.netty;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for Chapter10 in Netty实战
 */
public class Chapter10Test
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void test01_ByteToMessageDecoder(){
        ByteToMessageDecoder decoder = null;
    }

    @Test
    public void test02_MessageToByteEncoder(){
        MessageToByteEncoder encoder = null;
    }

    @Test
    public void test03_MessageToMessageDecoder(){
        MessageToMessageDecoder decoder = null;
    }

    @Test
    public void test04_MessageToMessageEncoder(){
        MessageToMessageEncoder encoder = null;
    }

    @Test
    public void test05_MessageToMessageEncoder(){
        //WebSocketConvertHandler webSocketConvertHandler = null;
    }
}
