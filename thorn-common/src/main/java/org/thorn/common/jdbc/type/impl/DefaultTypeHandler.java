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

package org.thorn.common.jdbc.type.impl;

import org.thorn.common.jdbc.type.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class DefaultTypeHandler implements TypeHandler {

    private static final Map<Class, TypeHandler> TYPE_HANDLER_MAP = new HashMap<Class, TypeHandler>();

    private static final Map<String, Class> PRIMITIVE_MAP = new HashMap<String, Class>();

    private static final TypeHandler DEFAULT_TYPE_HANDLER = new StringTypeHandler();

    static {
        TYPE_HANDLER_MAP.put(String.class, DEFAULT_TYPE_HANDLER);
        TYPE_HANDLER_MAP.put(Boolean.class, new BooleanTypeHandler());
        TYPE_HANDLER_MAP.put(Integer.class, new IntegerTypeHandler());
        TYPE_HANDLER_MAP.put(Double.class, new DoubleTypeHandler());
        TYPE_HANDLER_MAP.put(Float.class, new FloatTypeHandler());
        TYPE_HANDLER_MAP.put(Long.class, new LongTypeHandler());
        TYPE_HANDLER_MAP.put(Character.class, new CharacterTypeHandler());
        TYPE_HANDLER_MAP.put(Date.class, new DateTypeHandler());

        PRIMITIVE_MAP.put("int", Integer.class);
        PRIMITIVE_MAP.put("boolean", Boolean.class);
        PRIMITIVE_MAP.put("byte", Byte.class);
        PRIMITIVE_MAP.put("short", Short.class);
        PRIMITIVE_MAP.put("long", Long.class);
        PRIMITIVE_MAP.put("float", Float.class);
        PRIMITIVE_MAP.put("double", Double.class);
        PRIMITIVE_MAP.put("char", Character.class);
    }

    private Class cls;

    public DefaultTypeHandler(Class cls) {
        if(cls.isPrimitive()) {
            this.cls = PRIMITIVE_MAP.get(cls.getName());
        } else {
            this.cls = cls;
        }
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, Object parameter) throws SQLException {
        getTypeHandler().setParameter(ps, i, parameter);
    }

    @Override
    public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getTypeHandler().getResult(rs, columnIndex);
    }

    @Override
    public Object getResult(ResultSet rs, String columnName) throws SQLException {
        return getTypeHandler().getResult(rs, columnName);
    }


    private TypeHandler getTypeHandler() {
        TypeHandler handler = TYPE_HANDLER_MAP.get(cls);
        if(handler == null) {
            handler = DEFAULT_TYPE_HANDLER;
        }

        return handler;
    }
}
