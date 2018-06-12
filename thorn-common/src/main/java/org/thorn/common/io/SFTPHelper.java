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

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * sftp操作相关类，主要是文件的上传及下载.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class SFTPHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SFTPHelper.class);

    private ChannelSftp sftp;
    /** 用户名 */
    private String userName = null;
    /** 密码 */
    private String password = null;
    /** 服务器主机地址 */
    private String ftpServer = null;
    /** 端口号 */
    private int port = 22;

    /**
     * 初始化构造方法，默认端口号为22。
     *
     * @param userName  用户名
     * @param password  密码
     * @param ftpServer  服务器地址
     */
    public SFTPHelper(String userName, String password, String ftpServer) {
        this.userName = userName;
        this.password = password;
        this.ftpServer = ftpServer;
    }

    /**
     * 初始化构造方法。
     *
     * @param userName  用户名
     * @param password  密码
     * @param ftpServer  服务器地址
     * @param port  端口号
     */
    public SFTPHelper(String userName, String password, String ftpServer, int port) {
        this(userName, password, ftpServer);
        this.port = port;
    }

    /**
     * 登陆到sftp服务器，上传或者下载之前都必须首先登陆。
     *
     * @throws JSchException  登陆过程中出现异常
     */
    public void login() throws JSchException {
        JSch jsch = new JSch();

        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        jsch.getSession(this.userName, this.ftpServer, this.port);
        Session sshSession = jsch.getSession(this.userName, this.ftpServer, this.port);
        sshSession.setPassword(this.password);
        sshSession.setConfig(sshConfig);

        sshSession.connect();

        sftp = (ChannelSftp) sshSession.openChannel("sftp");
        sftp.connect();

        LOGGER.info("{}:{} connect success", this.ftpServer, this.port);
    }

    /**
     * 关闭连接。
     */
    public void close() {
        if (sftp != null && sftp.isConnected()) {
            sftp.disconnect();
        }

        LOGGER.info("{}:{} disconnected", this.ftpServer, this.port);
    }

    /**
     * 上传文件。
     *
     * @param file  需要上传的文件
     * @param targetFolder  文件在服务器上存放的路径
     * @throws IOException
     * @throws SftpException
     */
    public void upload(File file, String targetFolder) throws IOException, SftpException {
        LOGGER.debug("{}/{} upload file start", targetFolder, file.getName());

        FileInputStream stream = null;

        try {
            // 进入ftp服务器的文件目录
            if (targetFolder.length() != 0) {
                sftp.cd(targetFolder);
                LOGGER.debug("cd the sftp server folder:{}", targetFolder);
            }

            stream = new FileInputStream(file);
            sftp.put(stream, file.getName());

        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    /**
     * 文件下载。
     *
     * @param fileName  服务器上需要下载的文件名
     * @param targetFolder  服务器上文件的存放目录
     * @param file  客户端下载下来的文件
     * @throws SftpException
     * @throws IOException
     */
    public void download(String fileName, String targetFolder, File file) throws SftpException, IOException {
        InputStream is = null;
        FileOutputStream out = null;

        try {
            // 进入ftp服务器的文件目录
            if (targetFolder.length() != 0) {
                sftp.cd(targetFolder);
                LOGGER.debug("cd the sftp server folder:{}", targetFolder);
            }

            is = sftp.get(fileName);
            out = new FileOutputStream(file);
            byte[] bytes = new byte[10240];
            int c;
            while ((c = is.read(bytes)) != -1) {
                out.write(bytes, 0, c);
            }

        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(out);
        }
    }

}
