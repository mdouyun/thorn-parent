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

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class CharacterTypeHandler extends BaseTypeHandler<Character> {

    @Override
    public int getJdbcType() {
        return JdbcType.VARCHAR.TYPE_CODE;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Character parameter) throws SQLException {
        ps.setString(i, String.valueOf(parameter));
    }

    @Override
    public Character getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);

        if(result != null) {
            return result.charAt(0);
        }

        return null;
    }

    @Override
    public Character getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);

        if(result != null) {
            return result.charAt(0);
        }

        return null;
    }
}
