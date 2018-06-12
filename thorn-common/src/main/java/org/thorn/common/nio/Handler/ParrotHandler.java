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

package org.thorn.common.nio.Handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thorn.common.nio.SocketRequest;
import org.thorn.common.nio.SocketResponse;

import java.io.IOException;

/**
 * todo.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class ParrotHandler implements RequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParrotHandler.class);

    @Override
    public void doHandle(SocketRequest request, SocketResponse response) throws IOException {

        String msg = new String(request.getData(), "UTF-8");

        LOGGER.info("The received message is {}", msg);

        response.setContext(request.getContext());

        msg = "我收到了你的消息 [" + msg + "]";

        response.setData(msg.getBytes("UTF-8"));
    }
}
