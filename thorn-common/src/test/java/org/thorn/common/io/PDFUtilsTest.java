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

import com.itextpdf.text.pdf.PdfPTable;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class PDFUtilsTest {

    @Test
    public void testGenerateTable() throws Exception {

        float[] col = new float[]{
            10.0f, 5.0f, 15.0f
        };

        String[] header = {
          "列1", "列2", "列3"
        };

        List<String[]> content = new ArrayList<String[]>();

        String[] a1 = {
          "中文11212", "陈云678", "cccccccccccc"
        };
        content.add(a1);

        DataTable dataTable = new DataTable();
        dataTable.setHeader(header);
        dataTable.setColWidth(col);
        dataTable.setRows(content);

        PdfPTable pTable = PDFUtils.getTable(dataTable, PDFUtils.getGBBaseFont());

        File file = new File("D:\\DOC_SVN\\aaa.pdf");
        if(!file.exists()) {
            file.createNewFile();
        }

        PDFUtils.generateTable("测试", pTable, PDFUtils.getGBBaseFont(), new FileOutputStream(file));
    }
}
