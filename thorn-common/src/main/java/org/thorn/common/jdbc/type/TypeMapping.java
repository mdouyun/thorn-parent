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

package org.thorn.common.jdbc.type;

import org.thorn.common.jdbc.type.impl.StringTypeHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class TypeMapping {

    private static final Map<Class, TypeHandler> TYPE_HANDLER_MAP = new HashMap<Class, TypeHandler>();

    static {
        TYPE_HANDLER_MAP.put(String.class, new StringTypeHandler());
    }

    public static TypeHandler getDefaultTypeHandler(Class cls) {
        return TYPE_HANDLER_MAP.get(cls);
    }


}
