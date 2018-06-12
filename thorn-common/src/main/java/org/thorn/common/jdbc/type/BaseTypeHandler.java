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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public abstract class BaseTypeHandler<T> implements TypeHandler<T> {

    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter) throws SQLException {

        if(parameter == null) {
            ps.setNull(i, getJdbcType());
        } else {
            setNonNullParameter(ps, i, parameter);
        }
    }

    @Override
    public T getResult(ResultSet rs, int columnIndex) throws SQLException {
        T result = getNullableResult(rs, columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public T getResult(ResultSet rs, String columnName) throws SQLException {
        T result = getNullableResult(rs, columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    public abstract int getJdbcType();

    public abstract void setNonNullParameter(PreparedStatement ps, int i, T parameter) throws SQLException;

    public abstract T getNullableResult(ResultSet rs, String columnName) throws SQLException;

    public abstract T getNullableResult(ResultSet rs, int columnIndex) throws SQLException;
}
