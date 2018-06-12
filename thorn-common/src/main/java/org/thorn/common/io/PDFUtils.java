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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * PDF文档相关操作类.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class PDFUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFUtils.class);

    public static BaseFont getGBBaseFont() throws IOException, DocumentException {
        // 设置中文字体
        BaseFont bfChinese = BaseFont.createFont("STSong-Light",
                "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

        return bfChinese;
    }


    public static void generateTable(String title, PdfPTable table, BaseFont font, OutputStream os)
            throws DocumentException, IOException {
        Document document = new Document();
        // 设置页面大小
        document.setPageSize(PageSize.A4);

        try {
            PdfWriter.getInstance(document, os);
            document.open();

            // 设置字体大小
            Font titleFont = new Font(font, 13, Font.BOLD);
            Paragraph titlePa = new Paragraph(title, titleFont);
            titlePa.setAlignment(Element.ALIGN_CENTER);

            document.add(titlePa);

            document.add(table);
        } finally {
            document.close();
        }
    }

    public static PdfPTable getTable(DataTable dataTable, BaseFont font) {
        // 建立一个pdf表格
        PdfPTable table = new PdfPTable(dataTable.getColWidth());
        // 设置表格上面空白宽度
        table.setSpacingBefore(20f);
        // 设置表格的宽度
        table.setTotalWidth(535);
        // 设置表格的宽度固定
        table.setLockedWidth(true);
        // 设置表格默认为边框1
        table.getDefaultCell().setBorder(1);

        Font headerFont = new Font(font, 10, Font.BOLD);
        for (String header : dataTable.getHeader()) {
            table.addCell(getHeaderCell(header, headerFont));
        }
        table.completeRow();

        Font contentFont = new Font(font, 8, Font.BOLD);
        for(String[] rows : dataTable.getRows()) {
            for(String row : rows) {
                PdfPCell cell = new PdfPCell(new Paragraph(row, contentFont));
                table.addCell(cell);
            }
            table.completeRow();
        }

        return table;
    }

    private static PdfPCell getHeaderCell(String header, Font headerFont) {
        PdfPCell cell = new PdfPCell(new Paragraph(header, headerFont));
        // 设置内容水平居中显示
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		cell.setFixedHeight(20);

        return cell;
    }

}
