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

import org.junit.Test;
import org.thorn.common.nio.Handler.ParrotHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class TCPSocketServerTest {

    @Test
    public void testStart() throws Exception {
        RequestDispatcher dispatcher = new RequestDispatcher();
        dispatcher.addHandlerMap("parrot", new ParrotHandler());

        TCPContextConfig.init(12345, 3000L, dispatcher);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        TCPEventHandler eventHandler = new TCPEventHandler(executorService);

        TCPSocketServer tcpSocketServer = new TCPSocketServer(eventHandler);
        tcpSocketServer.start();
    }
}
