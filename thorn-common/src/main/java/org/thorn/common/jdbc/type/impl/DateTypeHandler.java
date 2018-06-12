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

import org.thorn.common.jdbc.type.BaseTypeHandler;
import org.thorn.common.jdbc.type.JdbcType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class DateTypeHandler extends BaseTypeHandler<Date> {
    @Override
    public int getJdbcType() {
        return JdbcType.TIMESTAMP.TYPE_CODE;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date parameter) throws SQLException {
        Timestamp timestamp = new Timestamp(parameter.getTime());
        ps.setTimestamp(i, timestamp);
    }

    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);

        if(timestamp != null) {
            return new Date(timestamp.getTime());
        }

        return null;
    }

    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnIndex);

        if(timestamp != null) {
            return new Date(timestamp.getTime());
        }

        return null;
    }
}
