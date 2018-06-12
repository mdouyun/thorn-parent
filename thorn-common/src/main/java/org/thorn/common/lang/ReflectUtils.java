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

package org.thorn.common.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射操作工具类.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class ReflectUtils {

    /**
     * 通过反射方法设置属性值
     *
     * @param o  需要设置属性值的对象
     * @param property  需要设置的属性名称
     * @param value  需要设置的属性值，需要与对象的属性类型一致
     * @throws NoSuchFieldException  属性名不存在时抛出异常
     * @throws IllegalAccessException  属性类型匹配错误时抛出异常
     */
    public static void fieldSet(Object o, String property, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Class cls = o.getClass();

        Field field = cls.getDeclaredField(property);
        field.setAccessible(true);

        field.set(o, value);
    }

    /**
     * 通过反射方法获取属性值
     *
     * @param o   需要设置属性值的对象
     * @param property  需要设置的属性名称
     * @return  属性的值
     * @throws NoSuchFieldException  属性名不存在时抛出异常
     * @throws IllegalAccessException
     */
    public static Object fieldGet(Object o, String property)
            throws NoSuchFieldException, IllegalAccessException {
        Class cls = o.getClass();

        Field field = cls.getDeclaredField(property);
        field.setAccessible(true);
        return field.get(o);
    }

}
