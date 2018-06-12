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

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * 文件操作类，提供文件的读、写.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class FileUtils {

    /**
     * 默认的字符串编码方式，UTF-8
     */
    private static final String DEFAULT_CHART_SET = "UTF-8";

    /**
     * 将字符串写入文件，若文件不存在则会创建该文件包括文件所属的目录。
     *
     * @param content  待写入的字符串
     * @param file  写入的文件
     * @param append  是否在文件末尾追加
     * @throws IOException IO异常时抛出
     */
    public static void writeStringToFile(String content, File file, boolean append) throws IOException {
        writeByteToFile(StringUtils.getBytesUtf8(content), file, append);
    }

    /**
     * 将字符串写入文件，若文件不存在则会创建该文件包括文件所属的目录。
     *
     * @param content  待写入的字符串
     * @param filePath  写入文件的路径
     * @param append  是否在文件末尾追加
     * @throws IOException IO异常时抛出
     */
    public static void writeStringToFile(String content, String filePath, boolean append) throws IOException {
        writeByteToFile(StringUtils.getBytesUtf8(content), new File(filePath), append);
    }

    /**
     * 将字节数组写入文件，若文件不存在则会创建该文件包括文件所属的目录。
     *
     * @param content  待写入的字节数组
     * @param file  写入的文件
     * @param append  是否在文件末尾追加
     * @throws IOException IO异常时抛出
     */
    public static void writeByteToFile(byte[] content, File file, boolean append) throws IOException {
        File dir = file.getParentFile();

        if(!dir.exists()) {
            dir.mkdirs();
        }

        if(!file.exists()) {
            file.createNewFile();
        }

        OutputStream output = null;
        try {
            output = new FileOutputStream(file, append);
            output.write(content);
            output.flush();

        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    /**
     * 将字节数组写入文件，若文件不存在则会创建该文件包括文件所属的目录。
     *
     * @param content  待写入的字节数组
     * @param filePath  写入文件的路径
     * @param append  是否在文件末尾追加
     * @throws IOException IO异常时抛出
     */
    public static void writeByteToFile(byte[] content, String filePath, boolean append) throws IOException {
        writeByteToFile(content, new File(filePath), append);
    }


    /**
     * 指定文件的路径，读取文件的全部内容，转换成字符串返回。
     *
     * @param filePath  读取文件的路径
     * @return  文件内容的字符串形式
     * @throws IOException  IO类的异常
     * @see org.apache.commons.io.FileUtils#openInputStream(java.io.File)
     */
    public static String readFileToString(String filePath) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToString(
                new File(filePath), DEFAULT_CHART_SET);
    }

    /**
     * 读取指定文件的全部内容，转换成字符串返回。
     *
     * @param file  读取的文件
     * @return  文件内容的字符串形式
     * @throws IOException  IO类的异常
     * @see org.apache.commons.io.FileUtils#openInputStream(java.io.File)
     */
    public static String readFileToString(File file) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToString(file, DEFAULT_CHART_SET);
    }

    /**
     * 指定文件的路径，读取文件的全部内容。
     *
     * @param filePath  读取文件的路径
     * @return  文件内容的字节数组
     * @throws IOException  IO类的异常
     * @see org.apache.commons.io.FileUtils#openInputStream(java.io.File)
     */
    public static byte[] readFileToByte(String filePath) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToByteArray(new File(filePath));
    }

    /**
     * 读取指定文件的全部内容。
     *
     * @param file  读取的文件
     * @return  文件内容的字节数组
     * @throws IOException  IO类的异常
     * @see org.apache.commons.io.FileUtils#openInputStream(java.io.File)
     */
    public static byte[] readFileToByte(File file) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToByteArray(file);
    }

    /**
     * 删除文件或文件夹，级联删除文件夹下的子文件夹。
     *
     * @param folder  需要删除的文件夹
     */
    public static void deleteDir(File folder) {
        File[] files = folder.listFiles();

        for(File file : files) {
            if(file.isFile()) {
                file.delete();
            } else {
                deleteDir(file);
            }
        }

        folder.delete();
    }


}
