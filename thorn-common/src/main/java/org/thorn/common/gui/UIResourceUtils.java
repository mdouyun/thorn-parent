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

package org.thorn.common.gui;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

/**
 * GUI资源使用上的辅助类.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class UIResourceUtils {

    /**
     * 设置全局字体。
     *
     * @param fnt  需要设置的字体
     */
    public static void setGlobalFont(Font fnt) {
        FontUIResource fontRes = new FontUIResource(fnt);

        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }

    /**
     * 获取图片资源。
     *
     * @param url  图片对应于classpath的地址
     * @return
     */
    public static ImageIcon getImageIcon(String url) {
        return new ImageIcon(UIResourceUtils.class.getResource(url));
    }

    /**
     * 生成矩形体，位于父容器的中间。
     *
     * @param parent  父矩形体
     * @param width  宽
     * @param height  长
     * @return
     */
    public static Rectangle centerRectangle(Rectangle parent, int width, int height) {
        double px = parent.getX();
        double py = parent.getY();
        double pWidth = parent.getWidth();
        double pHeight = parent.getHeight();

        Rectangle childRec = new Rectangle();

        int cx = (int) (px + (pWidth - width) / 2);
        int cy = (int) (py + (pHeight - height) / 2);

        childRec.setBounds(cx, cy, width, height);
        return childRec;
    }

}
