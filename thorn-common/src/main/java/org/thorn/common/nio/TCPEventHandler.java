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

import org.thorn.common.nio.task.ConnectionBuildTask;
import org.thorn.common.nio.task.RequestHandleTask;

import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutorService;

/**
 * socket连接的事件处理器.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class TCPEventHandler {

    public ExecutorService threadPool;

    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public TCPEventHandler() {
    }

    public TCPEventHandler(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public void handle(SelectionKey key) {
        Channel channel = key.channel();

        if (key.isValid() && key.isAcceptable() && channel != null) {
            if(TaskKeyCache.setChannelHandle(String.valueOf(channel.hashCode()))) {
                threadPool.execute(new ConnectionBuildTask(key));
            }
        }

        if (key.isValid() && key.isReadable() && channel != null) {
            // 判断是否有数据发送过来
            // 从客户端读取数据
            if(TaskKeyCache.setChannelHandle(String.valueOf(channel.hashCode()))) {
                threadPool.execute(new RequestHandleTask(key));
            }
        }

        if (key.isValid() && key.isWritable()) {
            // 判断是否有效及可以发送给客户端
            // 客户端可写时

        }
    }
}
