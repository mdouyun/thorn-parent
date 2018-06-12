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

package org.thorn.common.nio.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thorn.common.lang.ByteUtils;
import org.thorn.common.nio.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * 收到数据进行处理的线程.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class RequestHandleTask extends EventTask {

    private final static CharsetDecoder CHARSET_DECODER = Charset.forName("UTF-8").newDecoder();

    private final static CharsetEncoder CHARSET_ENCODER = Charset.forName("UTF-8").newEncoder();

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandleTask.class);

    public RequestHandleTask(SelectionKey key) {
        super(key);
    }

    @Override
    public void run() {

        // 获得与客户端通信的信道
        SocketChannel clientChannel = (SocketChannel) key.channel();
        // 得到并清空缓冲区，该缓冲区为消息头，记录消息体的大小
        ByteBuffer headerBuffer = (ByteBuffer) key.attachment();
        headerBuffer.clear();

        try {
            // 从通道中将头文件数据读到缓冲区
            long bytesRead = clientChannel.read(headerBuffer);

            LOGGER.info("Receive the client [{}] sends data, header length:[{}]",
                    clientChannel.socket().getRemoteSocketAddress(), bytesRead);

            if(bytesRead == -1) {
                closeChannel();
                LOGGER.info("The client [{}] disconnected", clientChannel.socket().getRemoteSocketAddress());
            } else if(bytesRead > 0) {
                headerBuffer.flip();

                byte[] clen = new byte[4];
                byte[] dlen = new byte[4];
                System.arraycopy(headerBuffer.array(), 0, clen, 0, 4);
                System.arraycopy(headerBuffer.array(), 4, dlen, 0, 4);

                int contextLength = ByteUtils.byteToInt(clen);
                int dataLength = ByteUtils.byteToInt(dlen);
                LOGGER.info("Context length:[{}] and data length:[{}]", contextLength, dataLength);

                // todo 需要校验包的大小是否正确
                ByteBuffer contextBuffer = ByteBuffer.allocate(contextLength);
                clientChannel.read(contextBuffer);
                contextBuffer.flip();
                String context = CHARSET_DECODER.decode(contextBuffer).toString();
                contextBuffer.clear();

                ByteBuffer dataBuffer = ByteBuffer.allocate(dataLength);
                clientChannel.read(dataBuffer);
                dataBuffer.flip();
                byte[] data = dataBuffer.array();
                dataBuffer.clear();

                TCPContextConfig contextConfig = TCPContextConfig.getInstance();
                RequestDispatcher dispatcher = contextConfig.getDispatcher();

                SocketRequest request = new SocketRequest(context, data);
                // 根据context找到对应的处理类处理
                SocketResponse response = dispatcher.doHandle(request);

                // 写返回
                ByteBuffer retContextBuffer = CHARSET_ENCODER.encode(
                        CharBuffer.wrap(response.getContext().toCharArray()));
                ByteBuffer retContextLengthBuffer = ByteBuffer.wrap(
                        ByteUtils.intToByte(retContextBuffer.remaining()));

                ByteBuffer retDataLengthBuffer = ByteBuffer.wrap(ByteUtils.intToByte(response.getData().length));
                ByteBuffer retDateBuffer = ByteBuffer.wrap(response.getData());

                clientChannel.write(new ByteBuffer[] {
                        retContextLengthBuffer, retDataLengthBuffer, retContextBuffer, retDateBuffer
                });

                retContextLengthBuffer.clear();
                retDataLengthBuffer.clear();
                retContextBuffer.clear();
                retDateBuffer.clear();

                // 设置为下一次读取或是写入做准备
//                key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            }
        } catch (IOException e) {
            closeChannel();
            LOGGER.error("The client [{}] disconnected abnormal", clientChannel.socket().getRemoteSocketAddress());
        } finally {
            super.releaseTask();
        }
    }
}
