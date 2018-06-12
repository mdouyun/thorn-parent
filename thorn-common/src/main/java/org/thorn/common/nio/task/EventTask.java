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
import org.thorn.common.nio.TaskKeyCache;

import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * todo.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public abstract class EventTask implements Runnable {

    protected static final Logger LOGGER = LoggerFactory.getLogger(EventTask.class);

    protected SelectionKey key;

    protected String cacheKey;

    protected EventTask(SelectionKey key) {
        this.key = key;

        Channel channel = key.channel();
        if(channel != null) {
            cacheKey = String.valueOf(channel.hashCode());
        }
    }

    void closeChannel() {
        SelectableChannel channel = key.channel();

        try {
            if(channel instanceof SocketChannel) {
                ((SocketChannel) channel).socket().close();
            }
            channel.close();
        } catch (IOException e) {

        }
        key.cancel();
    }

    void releaseTask() {
        if(cacheKey != null) {
            TaskKeyCache.clear(this.cacheKey);
        }
    }

}
