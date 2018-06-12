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

/**
 * 字节数组转换相关的工具类.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class ByteUtils {

    public static final long byteToLong(byte[] buf) {

        byte[] bytes;

        if(buf.length < 8) {
            bytes = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0 };
            System.arraycopy(buf, 0, bytes, 8 - buf.length, buf.length);
        } else {
            bytes = buf;
        }

        long toDecode = 0; // 网络序
        for (int i = 0; i < 8; i++) {
            toDecode |= (long) (0xff & buf[i]) << (7 - i) * 8;
        }
        return toDecode;
    }

    public static final byte[] longToByte(long l) {
        byte[] bytes = new byte[8];

        for (int i = 0; i < 8; i++) {// 右移，取某一字节
            bytes[i] = (byte) (l >>> (7 - i) * 8 & 0xff);
        }

        return bytes;
    }


    public static final int byteToInt(byte[] buf) {

        byte[] bytes;

        if(buf.length < 4) {
            bytes = new byte[]{ 0, 0, 0, 0 };
            System.arraycopy(buf, 0, bytes, 4 - buf.length, buf.length);
        } else {
            bytes = buf;
        }

        int toDecode = 0; // 网络序
        for (int i = 0; i < 4; i++) {
            toDecode |= (int) (0xff & buf[i]) << (3 - i) * 8;
        }
        return toDecode;
    }

    public static final byte[] intToByte(int l) {
        byte[] bytes = new byte[4];

        for (int i = 0; i < 4; i++) {// 右移，取某一字节
            bytes[i] = (byte) (l >>> (3 - i) * 8 & 0xff);
        }

        return bytes;
    }

}
