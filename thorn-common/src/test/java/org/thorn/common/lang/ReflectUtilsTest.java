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

import org.junit.Test;
import org.thorn.common.jdbc.Student;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class ReflectUtilsTest {

    private Student student = new Student();

    @Test
    public void testBeanSet() throws Exception {
        ReflectUtils.fieldSet(student, "id", "123455");
        ReflectUtils.fieldSet(student, "age", 12);

        System.out.println(student.getId() + " " + student.getAge());
    }

    @Test
    public void testBeanGet() throws Exception {
        testBeanSet();

        System.out.println(ReflectUtils.fieldGet(student, "id"));
        System.out.println(ReflectUtils.fieldGet(student, "age"));
    }
}
