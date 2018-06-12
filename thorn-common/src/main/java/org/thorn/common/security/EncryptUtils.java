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

import org.apache.commons.codec.binary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * 加解密算法类.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class EncryptUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtils.class);

    /**
     * 加密模式
     */
    public static final int ENCRYPT_MODE = Cipher.ENCRYPT_MODE;

    /**
     * 解密模式
     */
    public static final int DECRYPT_MODE = Cipher.DECRYPT_MODE;

    /**
     * 默认加密模式：ECB（电子密码本）
     */
    private static final String DEFAULT_MODE = "ECB";

    /**
     * 默认对称加密填充算法
     */
    private static final String DEFAULT_SYM_PADDING = "PKCS5Padding";

    /**
     * 默认非对称加密填充算法
     */
    private static final String DEFAULT_ASYM_PADDING = "PKCS1Padding";

    /**
     * 通用加解密方式。
     *
     * @param data  原文
     * @param key  密钥
     * @param opmode  模式（ENCODE_MODE|DECRYPT_MODE）
     * @param alg  算法：算法/模式/填充算法，如DES/ECB/PKCS5Padding
     * @return  加密/解密结果。字节数组形式
     * @throws GeneralSecurityException  加解密出现错误时抛出
     * @throws IllegalArgumentException  参数不合法时抛出
     */
    private static byte[] cipher(byte[] data, Key key, int opmode, String alg)
            throws GeneralSecurityException, IllegalArgumentException {
        long start = System.nanoTime();
        Cipher c1 = Cipher.getInstance(alg);
        c1.init(opmode, key);
        byte[] result = c1.doFinal(data);
        long used = System.nanoTime() - start;

        if (LOGGER.isDebugEnabled()) {//
            LOGGER.debug("#cipher {} {} used {} nano.",
                    opmode == ENCRYPT_MODE ? "encode" : "decode", alg, used);
        }

        return result;
    }

    /**
     * DES算法对字符串加密，使用模式为ECB，padding方式为PKCS5Padding。<br/>
     * 对加密的字节数组做Base64编码。
     *
     * @param data  需要加密的字符串
     * @param key  密钥，长度不能小于8位，自动取前8位为密钥
     * @return  加密后的Base64字符串
     * @throws GeneralSecurityException
     */
    public static String desEncrypt(String data, String key) throws GeneralSecurityException {
        String alg = String.format("DES/%s/%s", DEFAULT_MODE, DEFAULT_SYM_PADDING);
        // 明文作base64
        byte[] bytes = StringUtils.getBytesUtf8(data);

        byte[] desKey = new DESKeySpec(StringUtils.getBytesUtf8(key)).getKey();

        SecretKey secretKey = new SecretKeySpec(desKey, "DES");
        byte[] encryptBytes = cipher(bytes, secretKey, ENCRYPT_MODE, alg);

        return org.apache.commons.codec.binary.Base64.encodeBase64String(encryptBytes);
    }

    /**
     * DES算法对字符串解密，使用模式为ECB，padding方式为PKCS5Padding。<br/>
     * 需要解密的字符串必须是Base64编码后的字符串。
     *
     * @param data  需要解密的Base64字符串
     * @param key  密钥，长度不能小于8位，自动取前8位为密钥
     * @return
     * @throws GeneralSecurityException
     */
    public static String desDecrypt(String data, String key) throws GeneralSecurityException {
        String alg = String.format("DES/%s/%s", DEFAULT_MODE, DEFAULT_SYM_PADDING);
        byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(data);

        byte[] desKey = new DESKeySpec(StringUtils.getBytesUtf8(key)).getKey();
        SecretKey secretKey = new SecretKeySpec(desKey, "DES");

        byte[] decryptBytes = cipher(bytes, secretKey, DECRYPT_MODE, alg);

        return StringUtils.newStringUtf8(decryptBytes);
    }

    /**
     * 3DES算法对字符串加密，使用模式为ECB，padding方式为PKCS5Padding。<br/>
     * 对加密的字节数组做Base64编码。
     *
     * @param data  需要加密的字符串
     * @param key  密钥，长度不能小于24位，自动取前24位为密钥
     * @return  加密后的Base64字符串
     * @throws GeneralSecurityException
     */
    public static String desedeEncrypt(String data, String key) throws GeneralSecurityException {
        String alg = String.format("DESede/%s/%s", DEFAULT_MODE, DEFAULT_SYM_PADDING);
        // 明文作base64
        byte[] bytes = StringUtils.getBytesUtf8(data);

        byte[] desKey = new DESedeKeySpec(StringUtils.getBytesUtf8(key)).getKey();

        SecretKey secretKey = new SecretKeySpec(desKey, "DESede");
        byte[] encryptBytes = cipher(bytes, secretKey, ENCRYPT_MODE, alg);

        return org.apache.commons.codec.binary.Base64.encodeBase64String(encryptBytes);
    }

    /**
     * 3DES算法对字符串解密，使用模式为ECB，padding方式为PKCS5Padding。<br/>
     * 需要解密的字符串必须是Base64编码后的字符串。
     *
     * @param data  需要解密的Base64字符串
     * @param key  密钥，长度不能小于24位，自动取前24位为密钥
     * @return
     * @throws GeneralSecurityException
     */
    public static String desedeDecrypt(String data, String key) throws GeneralSecurityException {
        String alg = String.format("DESede/%s/%s", DEFAULT_MODE, DEFAULT_SYM_PADDING);
        byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(data);

        byte[] desKey = new DESedeKeySpec(StringUtils.getBytesUtf8(key)).getKey();
        SecretKey secretKey = new SecretKeySpec(desKey, "DESede");

        byte[] decryptBytes = cipher(bytes, secretKey, DECRYPT_MODE, alg);

        return StringUtils.newStringUtf8(decryptBytes);
    }

    /**
     * 3DES算法对字符串加密，使用模式为ECB，padding方式为PKCS5Padding。<br/>
     * 对加密的字节数组做Base64编码。
     *
     * @param data  需要加密的字符串
     * @param key  密钥
     * @return  加密后的Base64字符串
     * @throws GeneralSecurityException
     */
    public static String aesEncrypt(String data, String key) throws GeneralSecurityException {
        String alg = String.format("AES/%s/%s", DEFAULT_MODE, DEFAULT_SYM_PADDING);
        // 明文作base64
        byte[] bytes = StringUtils.getBytesUtf8(data);

        SecretKey secretKey = getAesKey(key);
        byte[] encryptBytes = cipher(bytes, secretKey, ENCRYPT_MODE, alg);

        return org.apache.commons.codec.binary.Base64.encodeBase64String(encryptBytes);
    }

    /**
     * 3DES算法对字符串解密，使用模式为ECB，padding方式为PKCS5Padding。<br/>
     * 需要解密的字符串必须是Base64编码后的字符串。
     *
     * @param data  需要解密的Base64字符串
     * @param key  密钥
     * @return
     * @throws GeneralSecurityException
     */
    public static String aesDecrypt(String data, String key) throws GeneralSecurityException {
        String alg = String.format("AES/%s/%s", DEFAULT_MODE, DEFAULT_SYM_PADDING);
        byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(data);

        SecretKey secretKey = getAesKey(key);

        byte[] decryptBytes = cipher(bytes, secretKey, DECRYPT_MODE, alg);

        return StringUtils.newStringUtf8(decryptBytes);
    }

    /**
     * AES算法128位密钥生成方法
     *
     * @param key  原始字符串key
     * @return  满足AES算法的key
     * @throws NoSuchAlgorithmException  原始密钥长度不符合要求
     */
    private static SecretKey getAesKey(String key) throws NoSuchAlgorithmException {
        byte[] bytes = StringUtils.getBytesUtf8(key);

        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128, new SecureRandom(bytes));

        return generator.generateKey();
    }

    public static void main(String[] args) throws GeneralSecurityException {
        String data = "我是陈云1223dfh212-0";
        String key = "aaaaaaaaaabbbbbbaaaaaaaaaabbbbbb11qqq111111111111";

        String a = aesEncrypt(data, key);

        System.out.println(a);
        System.out.println(aesDecrypt(a, key));
    }

}
