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

import org.thorn.common.jdbc.model.*;
import org.thorn.common.jdbc.parse.ClassParser;

import java.lang.reflect.Field;
import java.util.TreeMap;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
@Table("T_STUDENT")
public class Student {

    @Primary(value = "ID", policy = PKGeneratePolicy.VALUE)
    private String id;

    @Column(value = "NAME")
    private String name;

    @Column(value = "AGE")
    private int age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {

        MappingModel model = ClassParser.parse(Student.class);

        System.out.println(SqlBuilder.insert(Student.class, model));

        System.out.println(SqlBuilder.update(Student.class, model));

        System.out.println(SqlBuilder.delete(Student.class, model));

        TreeMap<String, Object> map = new TreeMap<String, Object>();
        map.put("aaa", 23);
        map.put("wewe", 23);
        map.put("wwe", 23);
        map.put("ghju", 23);
        System.out.println(SqlBuilder.select(model, map));

    }

}
