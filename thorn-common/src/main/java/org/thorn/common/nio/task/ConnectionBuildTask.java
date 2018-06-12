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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * todo.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class ConnectionBuildTask extends EventTask {

    public ConnectionBuildTask(SelectionKey key) {
        super(key);
    }

    @Override
    public void run() {
        try {
            // 有客户端连接请求时
            // 返回创建此键的通道，接受客户端建立连接的请求，并返回 SocketChannel 对象
            SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
            // 非阻塞式
            clientChannel.configureBlocking(false);
            // 注册到selector
            clientChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(8));

            LOGGER.info("收到客户端建立连接请求：{}", clientChannel.socket().getRemoteSocketAddress());
        } catch (IOException e) {
            super.closeChannel();
        } finally {
            super.releaseTask();
        }
    }
}
