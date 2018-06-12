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

package org.thorn.common.jdbc;

import org.apache.commons.lang3.StringUtils;
import org.thorn.common.jdbc.model.*;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SQL语句构建器.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class SqlBuilder {

    /**
     * 缓存的SQL语句，包含insert、update、delete。key值为{@link Class#getName()} + "-insert|update|delete"
     */
    private static final Map<String, String> SQL_CACHE_MAP = new ConcurrentHashMap<String, String>();

    private static final String INSERT_KEY_SUFFIX = "-insert";

    private static final String UPDATE_KEY_SUFFIX = "-update";

    private static final String DELETE_KEY_SUFFIX = "-delete";

    private static String cacheSql(String key, String sql) {
        String cacheSql = SQL_CACHE_MAP.get(key);

        if(cacheSql == null) {
            SQL_CACHE_MAP.put(key, sql);
            cacheSql = sql;
        }

        return cacheSql;
    }

    public static String insert(Class cls, MappingModel model) {

        String cacheKey = StringUtils.join(cls.getName(), INSERT_KEY_SUFFIX);
        String cacheSql = SQL_CACHE_MAP.get(cacheKey);

        if(cacheSql != null) {
            return cacheSql;
        }

        StringBuilder columnSql = new StringBuilder("insert into ");
        columnSql.append(model.getTableName()).append("(");

        StringBuilder valueSql = new StringBuilder("values(");

        PrimaryKey[] keys = model.getPrimaryKeys();
        if(keys != null && keys.length > 0) {
            for(PrimaryKey key : keys) {
                if(key.getPolicy() == PKGeneratePolicy.VALUE) {
                    columnSql.append(key.getPk()).append(",");
                    valueSql.append(" ?,");
                } else if(key.getPolicy() == PKGeneratePolicy.SQL) {
                    columnSql.append(key.getPk()).append(",");
                    valueSql.append(key.getSql()).append(",");
                }
            }
        }

        String[] columns = model.getColumns();
        if(columns != null && columns.length > 0) {
            for(String column : columns) {
                columnSql.append(column).append(",");
                valueSql.append(" ?,");
            }
        }

        if((keys == null || keys.length == 0)
                && (columns == null || columns.length == 0)) {
            throw new IllegalArgumentException(model.getTableName() + " no columns!");
        }

        columnSql = columnSql.deleteCharAt(columnSql.length() - 1);
        valueSql = valueSql.deleteCharAt(valueSql.length() - 1);

        valueSql.append(") ");
        columnSql.append(") ").append(valueSql);

        cacheSql = cacheSql(cacheKey, columnSql.toString());

        return cacheSql;
    }

    public static String update(Class cls, MappingModel model) {

        String cacheKey = StringUtils.join(cls.getName(), UPDATE_KEY_SUFFIX);
        String cacheSql = SQL_CACHE_MAP.get(cacheKey);

        if(cacheSql != null) {
            return cacheSql;
        }

        StringBuilder sql = new StringBuilder("update ");
        sql.append(model.getTableName()).append(" set ");

        PrimaryKey[] keys = model.getPrimaryKeys();
        String[] columns = model.getColumns();
        if((keys == null || keys.length == 0)
                && (columns == null || columns.length == 0)) {
            throw new IllegalArgumentException(model.getTableName() + " no columns!");
        }

        for(String column : columns) {
            sql.append(column).append(" = ?,");
        }

        sql = sql.deleteCharAt(sql.length() - 1);
        sql.append(" where");

        for(PrimaryKey key : keys) {
            sql.append(" ").append(key.getPk()).append(" = ? and");
        }
        sql = sql.delete(sql.length() - 3, sql.length());

        cacheSql = cacheSql(cacheKey, sql.toString());

        return cacheSql;
    }

    public static String delete(Class cls, MappingModel model) {

        String cacheKey = StringUtils.join(cls.getName(), DELETE_KEY_SUFFIX);
        String cacheSql = SQL_CACHE_MAP.get(cacheKey);

        if(cacheSql != null) {
            return cacheSql;
        }

        StringBuilder sql = new StringBuilder("delete from  ");
        sql.append(model.getTableName()).append(" where ");

        PrimaryKey[] keys = model.getPrimaryKeys();
        if(keys == null || keys.length == 0) {
            throw new IllegalArgumentException(model.getTableName() + " no columns!");
        }

        for(PrimaryKey key : keys) {
            sql.append(" ").append(key.getPk()).append(" = ? and");
        }
        sql = sql.delete(sql.length() - 3, sql.length());

        cacheSql = cacheSql(cacheKey, sql.toString());

        return cacheSql;
    }

    public static String select(MappingModel model, TreeMap<String, Object> filter) {
        StringBuilder sql = new StringBuilder("select ");

        PrimaryKey[] keys = model.getPrimaryKeys();
        String[] columns = model.getColumns();
        if((keys == null || keys.length == 0)
                && (columns == null || columns.length == 0)) {
            throw new IllegalArgumentException(model.getTableName() + " no columns!");
        }

        for(PrimaryKey key : keys) {
            sql.append(key.getPk()).append(",");
        }

        for(String column : columns) {
            sql.append(column).append(",");
        }
        sql = sql.deleteCharAt(sql.length() - 1);
        sql.append(" from ").append(model.getTableName());

        if(filter != null && filter.size() > 0) {
            sql.append(" where");
            Set<String> set = filter.keySet();
            for(String s : set) {
                sql.append(" ").append(s).append(" = ? and");
            }
            sql = sql.delete(sql.length() - 3, sql.length());
        }


        return sql.toString();
    }
}
