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

package org.thorn.common.nio;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.thorn.common.lang.ByteUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class ABCTest {

    @Test
    public void testByte() {

        long a = Long.MIN_VALUE;

        byte[] b = ByteUtils.longToByte(a);

        System.out.println(ByteUtils.byteToLong(b));

        int c = Integer.MAX_VALUE;

        byte[] d = ByteUtils.intToByte(c);

        System.out.println(ByteUtils.byteToInt(d));
    }

    @Test
    public void testString() throws UnsupportedEncodingException {

        String a = "卧室照片呢";

        byte[] b = a.getBytes(Charset.forName("UTF-8"));

        System.out.println(b.length);


        byte[] c = new byte[30];

        System.arraycopy(b, 0, c, 0, b.length);

        String d = new String(c, "UTF-8");

        System.out.println(d + "-------------------");
    }


    @Test
    public void testSet() {

        Set<String> set = new HashSet<String>();
        set.add("wer");
        set.add("45iou");
        set.add("8907tyyu");

        Iterator<String> it = set.iterator();

        while (it.hasNext()) {
            it.next();
            it.remove();
        }

        System.out.println(set.toString());
    }

}
