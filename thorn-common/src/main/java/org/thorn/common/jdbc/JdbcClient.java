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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 常用数据库操作封装类.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public interface JdbcClient {

    public int insert(Object o) throws SQLException;

    public int insert(String sql, Object[] args) throws SQLException;

    public int delete(Object o) throws SQLException;

    public int delete(String sql, Object[] args) throws SQLException;

    public int update(Object o) throws SQLException;

    public int update(String sql, Object[] args) throws SQLException;

    public <T> T selectOne(Class<T> cls, TreeMap<String, Object> filter) throws SQLException;

    public <T> List<T> selectList(Class<T> cls, TreeMap<String, Object> filter) throws SQLException;

    public <T> T selectOne(Class<T> cls, String sql, Object[] args) throws SQLException;

    public <T> List<T> selectList(Class<T> cls, String sql, Object[] args) throws SQLException;

    public Object selectOne(String sql, Object[] args) throws SQLException;

    public Map<String, Object> selectOneForMap(String sql, Object[] args) throws SQLException;

    public List<Map<String, Object>> selectListForMap(String sql, Object[] args) throws SQLException;

    public Connection getConnection() throws SQLException;
}
