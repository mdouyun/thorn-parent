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
import org.thorn.common.nio.Filter.Filter;
import org.thorn.common.nio.Handler.RequestHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class RequestDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestDispatcher.class);

    private List<Filter> filters;

    private Map<String, RequestHandler> handlerMap;

    public RequestDispatcher() {
        this.filters = new ArrayList<Filter>();
        this.handlerMap = new HashMap<String, RequestHandler>();
    }

    public RequestDispatcher(List<Filter> filters, Map<String, RequestHandler> handlerMap) {
        this.filters = filters;
        this.handlerMap = handlerMap;
    }

    public void addFilter(Filter filter) {
        this.filters.add(filter);
    }

    public void addHandlerMap(String key, RequestHandler handler) {
        this.handlerMap.put(key, handler);
    }

    public SocketResponse doHandle(SocketRequest request) {
        SocketResponse response = new SocketResponse();

        try {
            for (Filter filter : filters) {
                filter.doFilter(request, response);
            }

            RequestHandler handler = handlerMap.get(request.getContext());
            if(handler != null) {
                handler.doHandle(request, response);
            }
        } catch (IOException e) {
            // todo 异常处理逻辑需要细化，过滤器异常，未找到对应的handler异常，handler处理异常
            LOGGER.error("处理异常", e);
        }

        return response;
    }

}
