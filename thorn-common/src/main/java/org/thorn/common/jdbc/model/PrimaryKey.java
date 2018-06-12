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

/**
 * 主键模型.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class PrimaryKey {

    /**
     * 数据库主键名称
     */
    private String pk;

    /**
     * 对象属性名
     */
    private String name;

    private PKGeneratePolicy policy;

    private String sql;

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PKGeneratePolicy getPolicy() {
        return policy;
    }

    public void setPolicy(PKGeneratePolicy policy) {
        this.policy = policy;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
