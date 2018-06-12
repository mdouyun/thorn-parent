package org.thorn.common.security;

import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES算法加解密类.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class AES {

    private static final Logger LOGGER = LoggerFactory.getLogger(AES.class);

    /**
     * 默认加密模式：ECB（电子密码本）
     */
    private static final String DEFAULT_MODE = "ECB";

    /**
     * 默认对称加密填充算法
     */
    private static final String DEFAULT_SYM_PADDING = "PKCS5Padding";

    private static final String ALGORITHM = String.format("AES/%s/%s", DEFAULT_MODE, DEFAULT_SYM_PADDING);

    /**
     * AES算法对字节数组进行加密，使用模式为ECB，padding方式为PKCS5Padding。
     *
     * @param data
     * @param key
     * @return
     * @throws GeneralSecurityException
     */
    public static byte[] encrypt(byte[] data, String key) throws GeneralSecurityException {
        SecretKey secretKey = generateKey(key);
        return cipher(data, secretKey, Cipher.ENCRYPT_MODE);
    }

    /**
     * AES算法对字节数组进行解密，使用模式为ECB，padding方式为PKCS5Padding。
     *
     * @param data
     * @param key
     * @return
     * @throws GeneralSecurityException
     */
    public static byte[] decrypt(byte[] data, String key) throws GeneralSecurityException {
        SecretKey secretKey = generateKey(key);
        return cipher(data, secretKey, Cipher.DECRYPT_MODE);
    }


    /**
     * AES算法对字符串加密，使用模式为ECB，padding方式为PKCS5Padding。<br/>
     * 对加密的字节数组做Base64编码。
     *
     * @param data  需要加密的字符串
     * @param key  密钥
     * @return  加密后的Base64字符串
     * @throws GeneralSecurityException
     */
    public static String encrypt(String data, String key) throws GeneralSecurityException {
        // 明文作base64
        byte[] bytes = StringUtils.getBytesUtf8(data);

        SecretKey secretKey = generateKey(key);
        byte[] encryptBytes = cipher(bytes, secretKey, Cipher.ENCRYPT_MODE);

        return org.apache.commons.codec.binary.Base64.encodeBase64String(encryptBytes);
    }

    /**
     * AES算法对字符串解密，使用模式为ECB，padding方式为PKCS5Padding。<br/>
     * 需要解密的字符串必须是Base64编码后的字符串。
     *
     * @param data  需要解密的Base64字符串
     * @param key  密钥
     * @return
     * @throws GeneralSecurityException
     */
    public static String decrypt(String data, String key) throws GeneralSecurityException {
        byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(data);

        SecretKey secretKey = generateKey(key);

        byte[] decryptBytes = cipher(bytes, secretKey, Cipher.DECRYPT_MODE);

        return StringUtils.newStringUtf8(decryptBytes);
    }

    /**
     * 通用加解密方式。
     *
     * @param data  原文
     * @param key  密钥
     * @param opMode  模式（ENCODE_MODE|DECRYPT_MODE）
     * @return  加密/解密结果。字节数组形式
     * @throws GeneralSecurityException  加解密出现错误时抛出
     * @throws IllegalArgumentException  参数不合法时抛出
     */
    private static byte[] cipher(byte[] data, Key key, int opMode)
            throws GeneralSecurityException, IllegalArgumentException {
        long start = System.nanoTime();

        Cipher c1 = Cipher.getInstance(ALGORITHM);
        c1.init(opMode, key);
        byte[] result = c1.doFinal(data);

        long used = System.nanoTime() - start;
        if (LOGGER.isDebugEnabled()) {//
            LOGGER.debug("#cipher {} {} used {} nano.",
                    opMode == Cipher.ENCRYPT_MODE ? "encode" : "decode", ALGORITHM, used);
        }

        return result;
    }


    /**
     * AES算法128位密钥生成方法
     *
     * @param key  原始字符串key
     * @return  满足AES算法的key
     * @throws NoSuchAlgorithmException  原始密钥长度不符合要求
     */
    private static SecretKey generateKey(String key) throws NoSuchAlgorithmException {
        byte[] bytes = StringUtils.getBytesUtf8(key);

        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128, new SecureRandom(bytes));

        return generator.generateKey();
    }

}
