package com.fsmflying.study.quickstart2021.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Common {
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 向ctx中写数据，并决定是否打印到屏幕
     *
     * @param ctx
     * @param printInConsole
     * @param serverPrompt
     * @param addTime
     * @param params
     */
    public static void writeAndFlush(ChannelHandlerContext ctx, boolean printInConsole,
                                     String serverPrompt, boolean addTime, String... params) {
        //将接收到的消息写给发送者,而不冲刷出站消息
        StringBuffer stringBuffer = new StringBuffer(1024);

        if (addTime) {
            String strForTime = formatter.format(ZonedDateTime.now());
            stringBuffer.append("[" + strForTime + "]");
        }
        if (null != serverPrompt && !serverPrompt.isEmpty() && !serverPrompt.trim().isEmpty()) {
            stringBuffer.append("[" + serverPrompt + "]");
        }
        stringBuffer.append(":");
        if (params.length > 0) {
            for (String s : params) {
                stringBuffer.append(s);
            }
        }

        ctx.writeAndFlush(Unpooled.copiedBuffer(stringBuffer.toString().getBytes(Charset.forName("UTF-8"))));
        if (printInConsole) {
            System.out.println(stringBuffer.toString());
        }
    }

    /**
     * 将读取的数据写回，并决定是否在屏幕上打印出来
     *
     * @param ctx
     * @param printInConsole
     * @param serverPrompt
     * @param addTime
     * @param in
     * @return
     */
    public static String writeAndFlush(ChannelHandlerContext ctx, boolean printInConsole,
                                       String serverPrompt, boolean addTime, ByteBuf in) {
        String message = readByteBuf(in);
        writeAndFlush(ctx, printInConsole, serverPrompt, addTime, message);
        return message;
    }

    public static String readByteBuf(ByteBuf buf) {
        //return buf.toString(Charset.forName("UTF-8"));
        return buf.toString(CharsetUtil.UTF_8);
    }

    /**
     * 打印读索引(readerIndex),
     * 写索引(writerIndex)，
     * 容量(capacity),
     * 引用计算器(refCnt)
     *
     * @param buf
     */
    public static void printByteBufStatus(ByteBuf buf) {

        System.out.println("<<<<printByteBufIndex<<<<START<<<<<<<<<<<<<<<<");
        if (buf != null) {
            System.out.println("isReadable:" + buf.isReadable());
            System.out.println("readableBytes:" + buf.readableBytes());
            System.out.println("readerIndex:" + buf.readerIndex());
            System.out.println("writerIndex:" + buf.writerIndex());
            System.out.println("capacity:" + buf.capacity());
            System.out.println("refCnt:" + buf.refCnt());
            System.out.println("hasMemoryAddress:" + buf.hasMemoryAddress());
            if (buf.hasMemoryAddress()) {
                System.out.println("memoryAddress:" + buf.memoryAddress());
            }

        }

        System.out.println(">>>>printByteBufIndex>>>>END>>>>>>>>>>>>>>>>>>>");
    }

    /**
     * 在不改变读写索引的情况下，打印ByteBuf所有可读内容，从第0位开始读取
     * 使用readableBytes判定读取长度
     *
     * @param buf
     */
    public static void printByteBufContent(ByteBuf buf) {
        System.out.println("<<<<printByteBufContent<<<<START<<<<<<<<<<<<<<<<<");
        for (int i = 0; i < buf.readableBytes(); i++) {
            System.out.print(String.format("%c", buf.getByte(i)));
        }
        System.out.println();
        System.out.println(">>>>printByteBufContent>>>>END>>>>>>>>>>>>>>>>>>>");
    }

    /**
     * 在不改变读写索引的情况下，打印ByteBuf所有可读内容，从第0位开始读取
     * 使用readableBytes判定读取长度
     *
     * @param buf
     */
    public static void printByteBufContentWithHex(ByteBuf buf) {
        System.out.println("<<<<printByteBufContent<<<<START<<<<<<<<<<<<<<<<<");
        for (int i = 0; i < buf.readableBytes(); i++) {
            System.out.print(Integer.toHexString(buf.getByte(i)) + " ");
        }
        System.out.println();
        System.out.println(">>>>printByteBufContent>>>>END>>>>>>>>>>>>>>>>>>>");
    }


    /**
     * 在不改变读写索引的情况下，打印ByteBuf所有可读内容，从第0位开始读取
     * 使用isReadable判定是否还有字节可读
     *
     * @param buf
     */
    public static void readAndPrintByteOfByteBufContent2(ByteBuf buf) {
        while (buf.isReadable()) {
            System.out.print(Integer.toHexString(buf.readByte()));
            System.out.print(' ');
        }
        System.out.println();
    }


    /**
     * 在不改变读写索引的情况下，打印ByteBuf所有可读内容，从第0位开始读取
     * 使用isReadable判定是否还有字节可读
     *
     * @param buf
     */
    public static void readAndPrintCharOfByteBufContent2(ByteBuf buf) {
        while (buf.isReadable()) {
            System.out.print((char) buf.readByte());
            System.out.print(' ');
        }
        System.out.println();
    }

    /**
     * 读取并打印ByteBuf的部分或全部内容
     *
     * @param buf    ByteBuf实例
     * @param offset 读取开始偏移位置
     * @param length 读取长度
     */
    public static void readAndPrintCharOfByteBuf(ByteBuf buf, int offset, int length) {
        if (buf == null) return;
        else if (offset >= buf.readableBytes()) return;
        int start = offset, end = Integer.min(offset + length - 1, buf.readableBytes() - 1);
        //buf.readerIndex(offset);
        for (int i = start; i <= end; i++) {
            System.out.print(String.format("%c", buf.readByte()));
        }
        System.out.println();
    }

    /**
     * 读取并打印ByteBuf的部分或全部内容
     *
     * @param buf    ByteBuf实例
     * @param length 读取长度
     */
    public static void readAndPrintCharOfByteBuf(ByteBuf buf, int length) {
        readAndPrintCharOfByteBuf(buf, 0, length);
    }


    /**
     * 添加大写字母[A-Z]到ByteBuf
     *
     * @param buf 待添加到的ByteBuf
     */
    public static void addUpperCaseTo(ByteBuf buf) {
        if (buf != null) {
            for (int i = 0; i < 26; i++) {
                buf.writeByte(0x41 + i);
            }
        }

    }

    /**
     * 添加小写字母[a-z]到ByteBuf
     *
     * @param buf
     */
    public static void addLowerCaseTo(ByteBuf buf) {
        if (buf != null) {
            for (int i = 0; i < 26; i++) {
                buf.writeByte(0x61 + i);
            }
        }
    }

    /**
     * 添加数字[0-9]到ByteBuf
     *
     * @param buf
     */
    public static void addNumberTo(ByteBuf buf) {
        if (buf != null) {
            for (int i = 0; i < 10; i++) {
                buf.writeByte(0x30 + i);
            }
        }
    }


    /**
     * 添加数字[0-9]到ByteBuf
     *
     * @param buf
     */
    public static void addByteTo(ByteBuf buf, int from, int to) {
        if (buf != null) {
            for (int i = from; i <= to; i++) {
                buf.writeByte(i);
            }
        }
    }


    /**
     * 构造一个ByteBuf示例,包含(注意其中包含换行符)
     * 0123456789
     * ABCDEFGHIJKLMNOPQRSTUVWXYZ
     * abcdefghijklmnopqrstuvwxyz
     *
     * @return
     */
    public static ByteBuf getExampleByteBuf() {
        return getExampleByteBuf((byte) '\n', true);
    }

    /**
     * 构造一个ByteBuf示例,包含(注意其中包含换行符)
     * 0123456789
     * ABCDEFGHIJKLMNOPQRSTUVWXYZ
     * abcdefghijklmnopqrstuvwxyz
     *
     * @return
     */
    public static ByteBuf getExampleByteBufWithoutLine() {
        ByteBuf buf = Unpooled.buffer();
        addNumberTo(buf);
        addUpperCaseTo(buf);
        addLowerCaseTo(buf);
        return buf;
    }

    /**
     * 构造一个ByteBuf示例,包含
     *
     * @param delimiter 分割符
     * @param addInLast 是否在构造的ByteBuf最后添加分割符
     * @return
     */
    public static ByteBuf getExampleByteBuf(Character delimiter, boolean addInLast) {
        return getExampleByteBuf((byte) delimiter.charValue(), addInLast);
    }


    /**
     * 构造一个ByteBuf示例,包含"[0-9][delimiter]A-Z[delimiter]a-z"
     *
     * @param delimiter 分割符
     * @param addInLast 是否在构造的ByteBuf最后添加分割符
     * @return
     */
    public static ByteBuf getExampleByteBuf(String delimiter, boolean addInLast) {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf delimiterBuf = Unpooled.copiedBuffer(delimiter.getBytes(CharsetUtil.ISO_8859_1));
        addNumberTo(buf);
        buf.writeBytes(delimiterBuf);
        addUpperCaseTo(buf);
        buf.writeBytes(delimiterBuf);
        addLowerCaseTo(buf);
        if (addInLast) {
            buf.writeBytes(delimiterBuf);
        }
        return buf;
    }

    /**
     * 构造一个ByteBuf示例
     *
     * @param delimiter 分割符
     * @param addInLast 是否在构造的ByteBuf最后添加分割符
     * @return a example of ByteBuf
     */
    public static ByteBuf getExampleByteBuf(byte delimiter, boolean addInLast) {
        ByteBuf buf = Unpooled.buffer();
        addNumberTo(buf);
        buf.writeByte(delimiter);
        addUpperCaseTo(buf);
        buf.writeByte(delimiter);
        addLowerCaseTo(buf);
        if (addInLast) {
            buf.writeByte(delimiter);
        }
        return buf;
    }

    /**
     * 获取一个指定长度的ByteBuf
     *
     * @param length 长度
     * @return
     */
    public static ByteBuf getExampleByteBuf(int length) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeLong(length);
        ByteBuf exampleBuf = getExampleByteBufWithoutLine();
        int exampleLength = exampleBuf.readableBytes();
        int writeLength = 0;
        while ((length >= exampleLength) && (writeLength + exampleLength <= length)) {
            buf.writeBytes(exampleBuf.copy());
            writeLength += exampleLength;
        }
        buf.writeBytes(exampleBuf.slice(0, length - writeLength));
        return buf;
    }


}
