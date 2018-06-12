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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * socket服务器端，接口socket请求.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class TCPSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TCPSocketServer.class);

    private TCPEventHandler eventHandler;

    public TCPSocketServer() {
    }

    public TCPSocketServer(TCPEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void setEventHandler(TCPEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void start() throws IOException {

        TCPContextConfig contextConfig = TCPContextConfig.getInstance();

        // 创建选择器
        Selector selector = Selector.open();
        // 打开监听信道
        ServerSocketChannel listenerChannel = ServerSocketChannel.open();
        // 与本地端口绑定
        listenerChannel.socket().bind(new InetSocketAddress(contextConfig.getPort()));
        // 设置为非阻塞模式
        listenerChannel.configureBlocking(false);
        // 将选择器绑定到监听信道,只有非阻塞信道才可以注册选择器.并在注册过程中指出该信道可以进行Accept操作
        listenerChannel.register(selector, SelectionKey.OP_ACCEPT);

        LOGGER.info("TCPSocketServer start success, port[{}]", contextConfig.getPort());

        while (true) {

            if(selector.select(contextConfig.getSelectTimeOut()) == 0) {
                continue;
            }

            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey key = selectionKeyIterator.next();

                try {
                    this.eventHandler.handle(key);
                } finally {
                    selectionKeyIterator.remove();
                }
            }
        }
    }


}
