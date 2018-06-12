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

import org.apache.commons.lang3.StringUtils;
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
public class BooleanTypeHandler extends BaseTypeHandler<Boolean> {

    private static final String TRUE = "Y";

    private static final String FALSE = "N";

    @Override
    public int getJdbcType() {
        return JdbcType.CHAR.TYPE_CODE;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter) throws SQLException {
        if(parameter) {
            ps.setString(i, TRUE);
        } else {
            ps.setString(i, FALSE);
        }
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);

        if(StringUtils.equalsIgnoreCase(result, TRUE)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);

        if(StringUtils.equalsIgnoreCase(result, TRUE)) {
            return true;
        } else {
            return false;
        }
    }
}
