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

import java.nio.channels.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class TaskKeyCache {

    private static final Map<String, Boolean> CACHE = new ConcurrentHashMap<String, Boolean>(2000);

    private static final Lock LOCK = new ReentrantLock();

    public static boolean setChannelHandle(String key) {
        LOCK.lock();

        try {
            Boolean value = CACHE.get(key);

            if(value != null && value) {
                return false;
            }
            value = true;
            CACHE.put(key, value);
            return true;
        } finally {
            LOCK.unlock();
        }
    }

    public static void clear(String key) {
        LOCK.lock();

        try {
            CACHE.put(key, false);
        } finally {
            LOCK.unlock();
        }
    }

}
