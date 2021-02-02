package com.fsmflying.study.quickstart2021;

import java.nio.ByteBuffer;

public class BufferUtils {
    /**
     * 生成一引示例ByteBuffer,含有0-9,a-z
     * @return
     */
    public static ByteBuffer getExampleByteBuffer(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        for (int i = 0; i < 10; i++) {
            byteBuffer.put((byte)('0'+i));
        }
        for (int i = 0; i < 26; i++) {
            byteBuffer.put((byte)('a'+i));
        }
        return byteBuffer;
    }

    public static void printByteBuffer(ByteBuffer byteBuffer){
        while(byteBuffer.hasRemaining()){
            System.out.print((char)byteBuffer.get());
        }
        System.out.println();
    }

    public static void printByteBuffer(ByteBuffer byteBuffer,int length){
        int i = 0;
        while(byteBuffer.hasRemaining() && (i<length)){
            System.out.print((char)byteBuffer.get());
            i++;
        }
        System.out.println();
    }

    public static void printByteBufferStatus(ByteBuffer byteBuffer){
        System.out.println("****byteBuffer**Status**Start*******************");
        System.out.println("position:"+byteBuffer.position());
        System.out.println("limit:"+byteBuffer.limit());
        System.out.println("capacity:"+byteBuffer.capacity());
        System.out.println("****byteBuffer**Status**End*********************");
    }
}
