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

package org.thorn.common.jdbc.defaults;

import org.thorn.common.jdbc.ConfigCastException;
import org.thorn.common.jdbc.JdbcClient;
import org.thorn.common.jdbc.SqlBuilder;
import org.thorn.common.jdbc.model.MappingModel;
import org.thorn.common.jdbc.model.PKGeneratePolicy;
import org.thorn.common.jdbc.model.PrimaryKey;
import org.thorn.common.jdbc.parse.ClassParser;
import org.thorn.common.jdbc.type.TypeHandler;
import org.thorn.common.jdbc.type.impl.DefaultTypeHandler;
import org.thorn.common.lang.ReflectUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * 默认的JDBCClient实现.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class DefaultJdbcClient implements JdbcClient {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private int executeUpdate(String sql, Object[] args) throws SQLException {

        PreparedStatement ps = null;
        Connection connection = null;
        try {

            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);

            int i = 1;
            for(Object arg : args) {
                DefaultTypeHandler handler = new DefaultTypeHandler(arg.getClass());
                handler.setParameter(ps, i, arg);
                i++;
            }

            int rows = ps.executeUpdate();

            return rows;
        } catch (Exception e) {
            throw new ConfigCastException(e);
        } finally {
            close(null, ps, connection);
        }
    }

    private void close(ResultSet rs, Statement st, Connection conn) {
        try {
            if(rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
        }
        try {
            if(st != null) {
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn != null) {
                conn.close();
            }
        } catch (SQLException e) {

        }
    }

    @Override
    public int insert(Object o) throws SQLException {

        PreparedStatement ps = null;
        Connection connection = null;
        try {
            MappingModel model = ClassParser.parse(o.getClass());
            String sql = SqlBuilder.insert(o.getClass(), model);

            connection = dataSource.getConnection();

            PrimaryKey autoPK = null;
            for(PrimaryKey key : model.getPrimaryKeys()) {
                if(key.getPolicy() == PKGeneratePolicy.AUTO_INCR) {
                    autoPK = key;
                    break;
                }
            }

            // 此处可以指定是否返回自动生成的主键 Statement.RETURN_GENERATED_KEYS
            if(autoPK != null) {
                ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            } else {
                ps = connection.prepareStatement(sql);
            }

            int i = 1;
            // 设置值
            for(PrimaryKey key : model.getPrimaryKeys()) {
                TypeHandler handler = model.getTypeHandlerMap().get(key.getPk());
                if(key.getPolicy() == PKGeneratePolicy.VALUE) {
                    Object value = ReflectUtils.fieldGet(o, key.getName());
                    handler.setParameter(ps, i, value);
                    i++;
                }
            }

            for(String column : model.getColumns()) {
                TypeHandler handler = model.getTypeHandlerMap().get(column);

                String name = model.getMap().get(column);
                Object value = ReflectUtils.fieldGet(o, name);
                handler.setParameter(ps, i, value);
                i++;
            }

            int rows = ps.executeUpdate();

            // 返回自增主键生成的主键值
            if(autoPK != null) {
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) {
                    TypeHandler handler = model.getTypeHandlerMap().get(autoPK.getPk());
                    Object id = handler.getResult(rs, 1);
                    ReflectUtils.fieldSet(o, autoPK.getName(), id);
                }

                rs.close();
            }

            return rows;
        } catch (Exception e) {
            throw new ConfigCastException(e);
        } finally {
            close(null, ps, connection);
        }
    }

    @Override
    public int insert(String sql, Object[] args) throws SQLException {
        return executeUpdate(sql, args);
    }

    @Override
    public int delete(Object o) throws SQLException {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            MappingModel model = ClassParser.parse(o.getClass());
            String sql = SqlBuilder.delete(o.getClass(), model);

            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);

            int i = 1;
            // 设置值
            for(PrimaryKey key : model.getPrimaryKeys()) {
                TypeHandler handler = model.getTypeHandlerMap().get(key.getPk());
                Object value = ReflectUtils.fieldGet(o, key.getName());
                handler.setParameter(ps, i, value);
                i++;
            }

            int rows = ps.executeUpdate();

            return rows;
        } catch (Exception e) {
            throw new ConfigCastException(e);
        } finally {
            close(null, ps, connection);
        }
    }

    @Override
    public int delete(String sql, Object[] args) throws SQLException {
        return executeUpdate(sql, args);
    }

    @Override
    public int update(Object o) throws SQLException {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            MappingModel model = ClassParser.parse(o.getClass());
            String sql = SqlBuilder.update(o.getClass(), model);

            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);

            int i = 1;
            // 设置值
            for(String column : model.getColumns()) {
                TypeHandler handler = model.getTypeHandlerMap().get(column);

                String name = model.getMap().get(column);
                Object value = ReflectUtils.fieldGet(o, name);
                handler.setParameter(ps, i, value);
                i++;
            }

            for(PrimaryKey key : model.getPrimaryKeys()) {
                TypeHandler handler = model.getTypeHandlerMap().get(key.getPk());
                Object value = ReflectUtils.fieldGet(o, key.getName());
                handler.setParameter(ps, i, value);
                i++;
            }

            int rows = ps.executeUpdate();

            return rows;
        } catch (Exception e) {
            throw new ConfigCastException(e);
        } finally {
            close(null, ps, connection);
        }
    }

    @Override
    public int update(String sql, Object[] args) throws SQLException {
        return executeUpdate(sql, args);
    }

    @Override
    public <T> T selectOne(Class<T> cls, TreeMap<String, Object> filter) throws SQLException {
        PreparedStatement ps = null;
        Connection connection = null;
        ResultSet rs = null;

        try {
            MappingModel model = ClassParser.parse(cls);
            String sql = SqlBuilder.select(model, filter);

            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            // 设置值
            if(filter != null && filter.size() > 0) {
                Set<String> set = filter.keySet();
                int i = 1;
                for(String s : set) {
                    Object value = filter.get(s);
                    DefaultTypeHandler handler = new DefaultTypeHandler(value.getClass());
                    handler.setParameter(ps, i, value);
                    i++;
                }
            }

            rs = ps.executeQuery();

            T t = cls.newInstance();

            int i = 0;

            while (rs.next()) {

                if(i > 1) {
                    throw new IllegalArgumentException(
                            "You want one result, but there was at least two results.");
                }

                for(PrimaryKey key : model.getPrimaryKeys()) {
                    String pk = key.getPk();
                    TypeHandler handler = model.getTypeHandlerMap().get(key.getPk());
                    Object value = handler.getResult(rs, pk);
                    if(value != null) {
                        ReflectUtils.fieldSet(t, key.getName(), value);
                    }
                }

                for(String column : model.getColumns()) {
                    TypeHandler handler = model.getTypeHandlerMap().get(column);
                    Object value = handler.getResult(rs, column);
                    String name = model.getMap().get(column);
                    if(value != null) {
                        ReflectUtils.fieldSet(t, name, value);
                    }
                }

                i++;
            }

            return t;
        } catch (Exception e) {
            throw new ConfigCastException(e);
        } finally {
            close(rs, ps, connection);
        }
    }

    @Override
    public <T> List<T> selectList(Class<T> cls, TreeMap<String, Object> filter) throws SQLException {
        PreparedStatement ps = null;
        Connection connection = null;
        ResultSet rs = null;

        try {
            MappingModel model = ClassParser.parse(cls);
            String sql = SqlBuilder.select(model, filter);

            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            // 设置值
            if(filter != null && filter.size() > 0) {
                Set<String> set = filter.keySet();
                int i = 1;
                for(String s : set) {
                    Object value = filter.get(s);
                    DefaultTypeHandler handler = new DefaultTypeHandler(value.getClass());
                    handler.setParameter(ps, i, value);
                    i++;
                }
            }

            rs = ps.executeQuery();

            List<T> list = new ArrayList<T>();

            while (rs.next()) {

                T t = cls.newInstance();

                for(PrimaryKey key : model.getPrimaryKeys()) {
                    String pk = key.getPk();
                    TypeHandler handler = model.getTypeHandlerMap().get(key.getPk());
                    Object value = handler.getResult(rs, pk);
                    if(value != null) {
                        ReflectUtils.fieldSet(t, key.getName(), value);
                    }
                }

                for(String column : model.getColumns()) {
                    TypeHandler handler = model.getTypeHandlerMap().get(column);
                    Object value = handler.getResult(rs, column);
                    String name = model.getMap().get(column);
                    if(value != null) {
                        ReflectUtils.fieldSet(t, name, value);
                    }
                }

                list.add(t);
            }

            return list;
        } catch (Exception e) {
            throw new ConfigCastException(e);
        } finally {
            close(rs, ps, connection);
        }
    }

    @Override
    public <T> T selectOne(Class<T> cls, String sql, Object[] args) throws SQLException {
        PreparedStatement ps = null;
        Connection connection = null;
        ResultSet rs = null;

        try {
            MappingModel model = ClassParser.parse(cls);

            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            // 设置值
            if(args != null && args.length > 0) {
                int i = 1;
                for(Object o : args) {
                    DefaultTypeHandler handler = new DefaultTypeHandler(o.getClass());
                    handler.setParameter(ps, i, o);
                }
            }

            rs = ps.executeQuery();

            T t = cls.newInstance();

            int i = 0;

            while (rs.next()) {

                if(i > 1) {
                    throw new IllegalArgumentException(
                            "You want one result, but there was at least two results.");
                }

                for(PrimaryKey key : model.getPrimaryKeys()) {
                    String pk = key.getPk();
                    TypeHandler handler = model.getTypeHandlerMap().get(key.getPk());
                    Object value = handler.getResult(rs, pk);
                    if(value != null) {
                        ReflectUtils.fieldSet(t, key.getName(), value);
                    }
                }

                for(String column : model.getColumns()) {
                    TypeHandler handler = model.getTypeHandlerMap().get(column);
                    Object value = handler.getResult(rs, column);
                    String name = model.getMap().get(column);
                    if(value != null) {
                        ReflectUtils.fieldSet(t, name, value);
                    }
                }

                i++;
            }

            return t;
        } catch (Exception e) {
            throw new ConfigCastException(e);
        } finally {
            close(rs, ps, connection);
        }
    }

    @Override
    public <T> List<T> selectList(Class<T> cls, String sql, Object[] args) throws SQLException {
        PreparedStatement ps = null;
        Connection connection = null;
        ResultSet rs = null;

        try {
            MappingModel model = ClassParser.parse(cls);

            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            // 设置值
            if(args != null && args.length > 0) {
                int i = 1;
                for(Object o : args) {
                    DefaultTypeHandler handler = new DefaultTypeHandler(o.getClass());
                    handler.setParameter(ps, i, o);
                }
            }

            rs = ps.executeQuery();
            List<T> list = new ArrayList<T>();

            while (rs.next()) {

                T t = cls.newInstance();

                for(PrimaryKey key : model.getPrimaryKeys()) {
                    String pk = key.getPk();
                    TypeHandler handler = model.getTypeHandlerMap().get(key.getPk());
                    Object value = handler.getResult(rs, pk);
                    if(value != null) {
                        ReflectUtils.fieldSet(t, key.getName(), value);
                    }
                }

                for(String column : model.getColumns()) {
                    TypeHandler handler = model.getTypeHandlerMap().get(column);
                    Object value = handler.getResult(rs, column);
                    String name = model.getMap().get(column);
                    if(value != null) {
                        ReflectUtils.fieldSet(t, name, value);
                    }
                }

                list.add(t);
            }

            return list;
        } catch (Exception e) {
            throw new ConfigCastException(e);
        } finally {
            close(rs, ps, connection);
        }
    }

    @Override
    public Object selectOne(String sql, Object[] args) throws SQLException {
        PreparedStatement ps = null;
        Connection connection = null;
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            // 设置值
            if(args != null && args.length > 0) {
                int i = 1;
                for(Object o : args) {
                    DefaultTypeHandler handler = new DefaultTypeHandler(o.getClass());
                    handler.setParameter(ps, i, o);
                }
            }

            rs = ps.executeQuery();

            int i = 0;
            Object value = null;
            while (rs.next()) {

                if(i > 1) {
                    throw new IllegalArgumentException(
                            "You want one result, but there was at least two results.");
                }

                value = rs.getObject(1);
                i++;
            }

            return value;
        } catch (Exception e) {
            throw new ConfigCastException(e);
        } finally {
            close(rs, ps, connection);
        }
    }

    @Override
    public Map<String, Object> selectOneForMap(String sql, Object[] args) throws SQLException {
        PreparedStatement ps = null;
        Connection connection = null;
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            // 设置值
            if(args != null && args.length > 0) {
                int i = 1;
                for(Object o : args) {
                    DefaultTypeHandler handler = new DefaultTypeHandler(o.getClass());
                    handler.setParameter(ps, i, o);
                }
            }

            rs = ps.executeQuery();

            int i = 0;
            Map<String, Object> result = new HashMap<String, Object>();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnNum = metaData.getColumnCount();
            String[] columns = new String[columnNum];
            for(int j = 0; j < columnNum; j++) {
                columns[j] = metaData.getColumnName(j+1);
            }

            while (rs.next()) {

                if(i > 1) {
                    throw new IllegalArgumentException(
                            "You want one result, but there was at least two results.");
                }

                for(String column : columns) {
                    result.put(column, rs.getObject(column));
                }

                i++;
            }

            return result;
        } catch (Exception e) {
            throw new ConfigCastException(e);
        } finally {
            close(rs, ps, connection);
        }
    }

    @Override
    public List<Map<String, Object>> selectListForMap(String sql, Object[] args) throws SQLException {
        PreparedStatement ps = null;
        Connection connection = null;
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            // 设置值
            if(args != null && args.length > 0) {
                int i = 1;
                for(Object o : args) {
                    DefaultTypeHandler handler = new DefaultTypeHandler(o.getClass());
                    handler.setParameter(ps, i, o);
                }
            }

            rs = ps.executeQuery();

            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnNum = metaData.getColumnCount();
            String[] columns = new String[columnNum];
            for(int j = 0; j < columnNum; j++) {
                columns[j] = metaData.getColumnName(j+1);
            }

            while (rs.next()) {

                Map<String, Object> map = new HashMap<String, Object>();

                for(String column : columns) {
                    map.put(column, rs.getObject(column));
                }

                result.add(map);
            }

            return result;
        } catch (Exception e) {
            throw new ConfigCastException(e);
        } finally {
            close(rs, ps, connection);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}
