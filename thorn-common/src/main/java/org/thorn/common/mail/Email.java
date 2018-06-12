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

package org.thorn.common.mail;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * 电子邮件数据实体.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class Email implements Serializable {

    /** 邮件主题 */
    private String subject;

    /** 邮件内容 */
    private String content;

    /** 接收人 */
    private List<Contacts> receivers = null;

    /** 抄送人 */
    private List<Contacts> copiers = null;

    /** 附件地址 */
    private List<File> attAddress = null;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Contacts> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Contacts> receivers) {
        this.receivers = receivers;
    }

    public List<Contacts> getCopiers() {
        return copiers;
    }

    public void setCopiers(List<Contacts> copiers) {
        this.copiers = copiers;
    }

    public List<File> getAttAddress() {
        return attAddress;
    }

    public void setAttAddress(List<File> attAddress) {
        this.attAddress = attAddress;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Email{");
        sb.append("subject='").append(subject).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", receivers=").append(receivers == null ? null : receivers.size());
        sb.append(", copiers=").append(copiers == null ? null : copiers.size());
        sb.append(", attAddress=").append(attAddress == null ? null : attAddress.size());
        sb.append('}');
        return sb.toString();
    }

}
