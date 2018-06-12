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

package org.thorn.common.io;

import org.junit.Test;

import java.io.*;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class SerializTest {

    @Test
    public void testSerializ() throws IOException, ClassNotFoundException {
        Student s1 = new Student("sder423", 24);

        File file = new File("D:\\Git\\a.txt");
        file.createNewFile();

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(s1);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

        Student s2 = (Student) ois.readObject();

        System.out.println(s2);
    }

    @Test
    public void testSerializSign() throws IOException, ClassNotFoundException {
        File file = new File("D:\\Git\\a.txt");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

        Student s2 = (Student) ois.readObject();

        System.out.println(s2);
    }


    @Test
    public void testExternaliz() throws IOException, ClassNotFoundException {
        Teacher s1 = new Teacher("sder423", 24);

        File file = new File("D:\\Git\\b.txt");
        file.createNewFile();

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(s1);
        oos.close();

        System.out.println("---------------");

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

        Teacher s2 = (Teacher) ois.readObject();

        ois.close();

        System.out.println(s2);
    }

    @Test
    public void testProSerializ() throws IOException, ClassNotFoundException {
        Student s1 = new Student("sder423", 24);
        Teacher teacher = new Teacher("wert", 22);
        s1.setTeacher(teacher);

        Friend friend = new Friend("rrrrrr", 98);
        s1.setFriend(friend);

        File file = new File("D:\\Git\\a.txt");
        file.createNewFile();

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(s1);
        oos.close();

        System.out.println("---------------");

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

        Student s2 = (Student) ois.readObject();

        System.out.println(s2);
    }

    @Test
    public void testParentSerializ() throws IOException, ClassNotFoundException {
        LittleFriend littleFriend = new LittleFriend("qqq", 999);
        littleFriend.setHeight(2345L);

        File file = new File("D:\\Git\\a.txt");
        file.createNewFile();

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(littleFriend);
        oos.close();

        System.out.println("---------------");

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

        LittleFriend s2 = (LittleFriend) ois.readObject();

        System.out.println(s2);
    }


}
