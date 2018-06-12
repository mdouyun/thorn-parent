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

package org.thorn.common.jdbc.model;

import org.thorn.common.jdbc.type.TypeHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库-对象映射数据模型.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class MappingModel {

    private String tableName;

    private PrimaryKey[] primaryKeys;

    private String[] columns;

    /**
     * 数据库字段名与对象属性之间的映射表
     */
    private Map<String, String> map = new HashMap<String, String>();

    /**
     * 数据库字段名与类型转换器之间的映射表
     */
    private Map<String, TypeHandler> typeHandlerMap = new HashMap<String, TypeHandler>();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public PrimaryKey[] getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(PrimaryKey[] primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Map<String, TypeHandler> getTypeHandlerMap() {
        return typeHandlerMap;
    }

    public void setTypeHandlerMap(Map<String, TypeHandler> typeHandlerMap) {
        this.typeHandlerMap = typeHandlerMap;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MappingModel{");
        sb.append("tableName='").append(tableName).append('\'');
        sb.append(", primaryKeys=").append(Arrays.toString(primaryKeys));
        sb.append(", columns=").append(Arrays.toString(columns));
        sb.append(", map=").append(map);
        sb.append(", typeHandlerMap=").append(typeHandlerMap);
        sb.append('}');
        return sb.toString();
    }
}
