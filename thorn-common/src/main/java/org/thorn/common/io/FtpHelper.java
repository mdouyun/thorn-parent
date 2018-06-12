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

import org.apache.commons.io.*;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * ftp操作相关类，主要是文件的上传及下载.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class FtpHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpHelper.class);

    /** 用户名 */
    private String userName = null;
    /** 密码 */
    private String password = null;
    /** 服务器主机地址 */
    private String ftpServer = null;
    /** 端口号 */
    private int port = 21;

    private FTPClient ftpClient;

    public FtpHelper(String userName, String password, String ftpServer) {
        this.ftpClient = new FTPClient();
        this.userName = userName;
        this.password = password;
        this.ftpServer = ftpServer;
    }

    public FtpHelper(String userName, String password, String ftpServer, int port) {
        this(userName, password, ftpServer);
        this.port = port;
    }

    public void login() throws IOException {

        ftpClient.connect(ftpServer, port);

        if(!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            ftpClient.disconnect();
        } else {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("{}:{}FTP服务器连接成功...", ftpServer, port);
            }

            ftpClient.login(userName, password);

            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("{}:{}登录FTP服务器成功...", ftpServer, port);
            }

            ftpClient.pasv();
        }
    }

    public void close() {

        if(ftpClient != null) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                LOGGER.warn("{}:{}退出FTP服务器失败", ftpServer, port);
            }
        }
    }

    public boolean upload(File file, String targetFolder) throws IOException {

        LOGGER.debug("开始上传文件, {}/{}", targetFolder, file.getName());

        FileInputStream in = null;

        try {
            in = org.apache.commons.io.FileUtils.openInputStream(file);

            ftpClient.changeWorkingDirectory("/");
            ftpClient.makeDirectory(targetFolder);
            ftpClient.changeWorkingDirectory(targetFolder);

            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);

            return ftpClient.storeFile(file.getName(), in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
