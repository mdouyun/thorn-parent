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

/**
 * 配置类，采用单例模式实现，使用之前必须初始化，仅可初始化一次.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class TCPContextConfig {

    private static TCPContextConfig contextConfig = null;

    private TCPContextConfig() {

    }

    public static void init(int port, long selectTimeOut, RequestDispatcher dispatcher) {

        if(contextConfig == null) {
            contextConfig = new TCPContextConfig();
            contextConfig.port = port;
            contextConfig.selectTimeOut = selectTimeOut;
            contextConfig.dispatcher = dispatcher;
        } else {
            throw new UnsupportedOperationException("ContextConfig has been initialized");
        }

    }

    public static TCPContextConfig getInstance() {

        if(contextConfig == null) {
            throw new IllegalStateException("Uninitialized contextConfig");
        }

        return contextConfig;
    }

    private int port;

    private long selectTimeOut;

    private RequestDispatcher dispatcher;

    public RequestDispatcher getDispatcher() {
        return dispatcher;
    }

    public long getSelectTimeOut() {
        return selectTimeOut;
    }

    public int getPort() {
        return port;
    }
}
