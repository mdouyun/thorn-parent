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

/**
 * ORM的配置转换异常，此异常都是由于注解的配置映射问题引起的.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class ConfigCastException extends RuntimeException {

    public ConfigCastException() {
    }

    public ConfigCastException(String message) {
        super(message);
    }

    public ConfigCastException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigCastException(Throwable cause) {
        super(cause);
    }
}
