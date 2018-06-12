/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.thorn.common.nio;

import org.thorn.common.lang.ByteUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * todo.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class TCPSocketClient {

    public static void main(String[] args) throws IOException {
        // 打开监听信道并设置为非阻塞模式
        final SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 12345));
        socketChannel.configureBlocking(false);

        // 打开并注册选择器到信道
        final Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    while (selector.select() > 0) {//select()方法只能使用一次，用了之后就会自动删除,每个连接到服务器的选择器都是独立的
                        // 遍历每个有可用IO操作Channel对应的SelectionKey
                        for (SelectionKey sk : selector.selectedKeys()) {
                            // 如果该SelectionKey对应的Channel中有可读的数据
                            if (sk.isValid() && sk.isReadable()) {
                                // 使用NIO读取Channel中的数据
                                SocketChannel sc = (SocketChannel) sk.channel();//获取通道信息
                                ByteBuffer buffer = ByteBuffer.allocate(8);//分配缓冲区大小
                                sc.read(buffer);//读取通道里面的数据放在缓冲区内
                                buffer.flip();// 调用此方法为一系列通道写入或相对获取 操作做好准备

                                byte[] clen = new byte[4];
                                byte[] dlen = new byte[4];
                                System.arraycopy(buffer.array(), 0, clen, 0, 4);
                                System.arraycopy(buffer.array(), 4, dlen, 0, 4);

                                buffer.clear();

                                int contextLength = ByteUtils.byteToInt(clen);
                                int dataLength = ByteUtils.byteToInt(dlen);
                                System.out.println("context长度：" + contextLength + " 数据长度：" + dataLength);

                                ByteBuffer contextBuffer = ByteBuffer.allocate(contextLength);
                                sc.read(contextBuffer);
                                contextBuffer.flip();
                                String context = Charset.forName("UTF-8").newDecoder().decode(contextBuffer).toString();
                                contextBuffer.clear();

                                ByteBuffer dataBuffer = ByteBuffer.allocate(dataLength);
                                sc.read(dataBuffer);
                                dataBuffer.flip();
                                String data = Charset.forName("UTF-8").newDecoder().decode(dataBuffer).toString();
                                dataBuffer.clear();

                                // 控制台打印出来
                                System.out.println("接收到来自服务器"
                                        + sc.socket().getRemoteSocketAddress() + "的信息:"
                                        + context + " | " + data);
                                // 为下一次读取作准备
                                sk.interestOps(SelectionKey.OP_READ);
                            }
                            // 删除正在处理的SelectionKey
                            selector.selectedKeys().remove(sk);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();

        try {
            send("你好!Nio!醉里挑灯看剑,梦回吹角连营醉里挑灯看剑,梦回吹角连营醉里挑灯看剑,梦回吹角连营醉里挑灯看剑,梦回吹角连营醉里挑灯看剑,梦回吹角连营醉里挑灯看剑,梦回吹角连营醉里挑灯看剑,梦回吹角连营", socketChannel);

            while (true) {
                Scanner scan = new Scanner(System.in);//键盘输入数据
                String string = scan.next();

                final String rs = string;

                Runnable runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TCPSocketClient.send(rs, socketChannel);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                new Thread(runnable1).start();
                new Thread(runnable1).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void send(String message, SocketChannel socketChannel) throws IOException {

        ByteBuffer retContextBuffer = ByteBuffer.wrap("parrot".getBytes("UTF-8"));
        ByteBuffer retContextLengthBuffer = ByteBuffer.wrap(
                ByteUtils.intToByte(retContextBuffer.remaining()));

        ByteBuffer retDateBuffer = ByteBuffer.wrap(message.getBytes("UTF-8"));
        ByteBuffer retDataLengthBuffer = ByteBuffer.wrap(ByteUtils.intToByte(retDateBuffer.remaining()));


        socketChannel.write(new ByteBuffer[] {
                retContextLengthBuffer, retDataLengthBuffer, retContextBuffer, retDateBuffer
        });

        retContextLengthBuffer.clear();
        retDataLengthBuffer.clear();
        retContextBuffer.clear();
        retDateBuffer.clear();
    }

}
