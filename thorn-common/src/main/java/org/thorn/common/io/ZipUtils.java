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


import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.*;

/**
 * zip压缩、解压缩文件及文件夹.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class ZipUtils {

    /**
     * 压缩文件及文件夹。
     *
     * @param zipPath  生成的压缩文件路径
     * @param files  需要压缩的文件及文件夹列表
     * @throws IOException
     * @throws ArchiveException
     */
    public static void zip(String zipPath, File[] files) throws IOException, ArchiveException {
        zip(new File(zipPath), files);
    }

    /**
     * 压缩文件及文件夹。
     *
     * @param zipFile  生成的压缩文件
     * @param files  需要压缩的文件及文件夹列表
     * @throws IOException
     * @throws ArchiveException
     */
    public static void zip(File zipFile, File[] files) throws IOException, ArchiveException {

        if(!zipFile.exists()) {
            zipFile.createNewFile();
        }

        ZipArchiveOutputStream zipOut = null;
        try {
            zipOut = new ZipArchiveOutputStream(zipFile);

            for(File file : files) {
                addZipFile(zipOut, file, "");
            }


        } finally {
            try {
                if(zipOut != null) {
                    zipOut.flush();
                    zipOut.finish();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 通过递归的方式将文件添加到压缩文件中。
     *
     * @param zipOut  压缩文件的输出流
     * @param file  需要压缩的文件
     * @param pname  压缩文件的父文件夹路径，无父文件夹为""
     * @throws IOException
     */
    private static void addZipFile(ZipArchiveOutputStream zipOut, File file, String pname) throws IOException {

        if(file.isDirectory()) {
            pname = pname + file.getName() + File.separator;

            File[] files = file.listFiles();

            for(File child : files) {
                addZipFile(zipOut, child, pname);
            }
        } else {

            String name = pname + file.getName();

            ZipArchiveEntry zipEntry = new ZipArchiveEntry(file, name);
            zipOut.putArchiveEntry(zipEntry);

            InputStream is = new FileInputStream(file);
            try {
                byte[] b = new byte[1024 * 5];
                int i = -1;
                while ((i = is.read(b)) != -1) {
                    zipOut.write(b, 0, i);
                }
            } finally {
                is.close();
                zipOut.closeArchiveEntry();
            }
        }
    }

    /**
     * 将压缩文件解压缩到文件夹中。
     *
     * @param zipFile  需要解压缩的压缩文件
     * @param unzipDir  解压缩的文件夹
     * @throws IOException
     */
    public static void unzip(File zipFile, File unzipDir) throws IOException {

        if(!unzipDir.exists() || !unzipDir.isDirectory()) {
            unzipDir.mkdirs();
        }

        FileInputStream inputStream = null;
        ZipArchiveInputStream zipInput = null;
        try {
            inputStream = new FileInputStream(zipFile);
            zipInput = new ZipArchiveInputStream(inputStream);

            ArchiveEntry archiveEntry = null;

            while((archiveEntry = zipInput.getNextEntry()) != null) {
                //获取文件名
                String entryFileName = archiveEntry.getName();

                FileOutputStream outputStream = null;
                try {
                    //把解压出来的文件写到指定路径
                    File entryFile = new File(unzipDir, entryFileName);

                    File dir = entryFile.getParentFile();

                    if(!dir.exists()) {
                        dir.mkdirs();
                    }

                    entryFile.createNewFile();

                    outputStream = new FileOutputStream(entryFile);

                    byte[] b = new byte[1024 * 5];
                    int i = -1;
                    while ((i = zipInput.read(b)) != -1) {
                        outputStream.write(b, 0, i);
                    }

                } finally {
                    if (outputStream != null) {
                        outputStream.flush();
                        outputStream.close();
                    }
                }
            }
        } finally {

            if(inputStream != null) {
                inputStream.close();
            }

            if(zipInput != null) {
                zipInput.close();
            }
        }

    }

    /**
     * 将压缩文件解压缩到文件夹中。
     *
     * @param zipFile  需要解压缩的压缩文件
     * @param unzipDir  解压缩的文件夹路径
     * @throws IOException
     */
    public static void unzip(File zipFile, String unzipDir) throws IOException {
        File file = new File(unzipDir);
        unzip(zipFile, file);
    }


    public static void main(String[] args) {
        File zip = new File("D:\\Document\\代收代付\\xin.zip");

        File[] files = new File[] {
            new File("D:\\Document\\代收代付\\银商接口"),
            new File("D:\\Document\\代收代付\\PaymentDeploy.vsd"),
            new File("D:\\Document\\代收代付\\收银台项目文档.zip")
        };

        try {
//            zip(zip, files);

            unzip(zip, new File("D:\\Document\\代收代付\\xin111"));
        } catch (IOException e) {
            e.printStackTrace();
//        } catch (ArchiveException e) {
//            e.printStackTrace();
        }
    }

}
