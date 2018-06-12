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

package org.thorn.common.io;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class NIOTest {

    static byte[] request;

    static byte[] request1;

    static byte[] request2;

    static {
        StringBuffer temp = new StringBuffer();
        temp.append("GET http://reqp.cbpmgt.com HTTP/1.1\r\n");
        temp.append("Host: 127.0.0.1:8080\r\n");
        temp.append("Connection: keep-alive\r\n");
        temp.append("Cache-Control: max-age=0\r\n");
        temp.append("User-Agent: Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.47 Safari/536.11\r\n");
        temp.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n");
        temp.append("Accept-Encoding: gzip,deflate,sdch\r\n");
        temp.append("Accept-Language: zh-CN,zh;q=0.8\r\n");
        temp.append("Accept-Charset: GBK,utf-8;q=0.7,*;q=0.3\r\n");
        temp.append("\r\n");
        request = temp.toString().getBytes();

        request1 = temp.toString().replaceAll("reqp.cbpmgt.com", "www.baidu.com").getBytes();
        request2 = temp.toString().replaceAll("reqp.cbpmgt.com", "www.google.com").getBytes();
    }


    @Test
    public void testNio() throws Exception {


        Selector selector = Selector.open();

        SocketChannel socketChannel1 = SocketChannel.open();
        socketChannel1.configureBlocking(false);

        socketChannel1.register(selector,
                SelectionKey.OP_CONNECT, "sc1");

        SocketChannel socketChannel2 = SocketChannel.open();
        socketChannel2.configureBlocking(false);
        socketChannel2.register(selector,
                SelectionKey.OP_READ, "sc2");


        socketChannel1.connect(new InetSocketAddress("www.baidu.com", 80));
//        socketChannel1.write(ByteBuffer.wrap(request1));
        socketChannel2.connect(new InetSocketAddress("www.google.com", 80));
//        socketChannel2.write(ByteBuffer.wrap(request2));

        while(true) {
            int readyChannels = selector.select();
            if (readyChannels == 0) {
                continue;
            }
            Set selectedKeys = selector.selectedKeys();
            Iterator keyIterator = selectedKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = (SelectionKey) keyIterator.next();

                if (key.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel.
                    Channel channel = key.channel();
                    System.out.println(key.attachment() + " accept");


                } else if (key.isConnectable()) {
                    // a connection was established with a remote server.

                    System.out.println(key.attachment() + "   connect");

                    socketChannel1.register(selector,
                            SelectionKey.OP_READ, "sc1");


                } else if (key.isReadable()) {
                    // a channel is ready for reading

                    System.out.println(key.attachment() + " read");
                } else if (key.isWritable()) {
                    // a channel is ready for writing

                    System.out.println(key.attachment() + " write");
                }

                keyIterator.remove();
            }
        }
    }

    @Test
    public void testNio1() throws Exception {
        SocketChannel socketChannel1 = SocketChannel.open();
        boolean res = socketChannel1.connect(new InetSocketAddress("reqp.cbpmgt.com", 80));

        System.out.println("连接结果：" + res);

        socketChannel1.write(ByteBuffer.wrap(request));

        ByteBuffer buf = ByteBuffer.allocate(1024);

        int n = socketChannel1.write(buf);
        while(n != -1) {
            buf.flip();

            while(buf.hasRemaining()){
                System.out.print((char) buf.get()); // read 1 byte at a time
            }

            buf.clear(); //make buffer ready for writing
            n = socketChannel1.read(buf);
        }

        socketChannel1.finishConnect();
        socketChannel1.close();

    }
}
