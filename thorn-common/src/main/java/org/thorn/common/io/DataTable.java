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

import java.util.ArrayList;
import java.util.List;

/**
 * 数据表格模型.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class DataTable {

    private String[] header;

    private List<String[]> rows = new ArrayList<String[]>();

    private float[] colWidth;

    public void addRow(String[] row) {
        this.rows.add(row);
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    public void setColWidth(float[] colWidth) {
        this.colWidth = colWidth;
    }

    public String[] getHeader() {
        return header;
    }

    public float[] getColWidth() {
        return colWidth;
    }

    public List<String[]> getRows() {
        return rows;
    }

    public void setRows(List<String[]> rows) {
        this.rows = rows;
    }
}
