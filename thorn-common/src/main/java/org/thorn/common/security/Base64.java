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

package org.thorn.common.security;

import org.apache.commons.codec.binary.StringUtils;

/**
 * Base64编码算法，实际实现参考：{@link org.apache.commons.codec.binary.Base64}.
 *
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class Base64 {

    /**
     * 对字符串作Base64编码，对编码内容不作分块。
     *
     * @param data  需要编码的数据
     * @return  编码后的数据
     */
    public static String encode(String data) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(
                StringUtils.getBytesUtf8(data));
    }

    /**
     * 对字符串作Base64编码，对编码内容作分块，分块分隔符为‘\r\n’。
     *
     * @param data  需要编码的数据
     * @return  编码后的数据
     */
    public static String encodeByChunk(String data) {
        byte[] bytes = org.apache.commons.codec.binary.Base64.encodeBase64(
                StringUtils.getBytesUtf8(data), true);

        return StringUtils.newStringUtf8(bytes);
    }

    /**
     * 对字符串作Base64编码，编码内容可作分块，分块分隔符为‘\r\n’。
     *
     * @param data  需要编码的数据
     * @param isChunked  是否对内容分块
     * @param isUrlSafe  如果{@code true}，将 - 和 _ 使用 + 和 / 替换
     * @return  编码后的数据
     */
    public static String encode(String data, boolean isChunked, boolean isUrlSafe) {
        byte[] bytes =  org.apache.commons.codec.binary.Base64.encodeBase64(
                StringUtils.getBytesUtf8(data), isChunked, isUrlSafe);

        return StringUtils.newStringUtf8(bytes);
    }

    /**
     * 对Base64字符串作反编码，得到明文。
     *
     * @param data  Base64字符串
     * @return  明文字符串
     */
    public static String decode(String data) {
        byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(data);

        return StringUtils.newStringUtf8(bytes);
    }

}
