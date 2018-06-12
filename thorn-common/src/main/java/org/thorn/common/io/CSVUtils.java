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

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Csv文件操作类.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class CSVUtils {

    private final static char DEFAULT_SEPARATOR = ',';

    public static void generate(DataTable table, OutputStream outputStream, char separator)
            throws IOException {

        CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream), separator,
                CSVWriter.NO_QUOTE_CHARACTER, "\r\n");

        if(table.getHeader() != null) {
            writer.writeNext(table.getHeader());
        }
        writer.writeAll(table.getRows());
        writer.close();
    }

    public static void generate(DataTable table, OutputStream outputStream) throws IOException {
        generate(table, outputStream, DEFAULT_SEPARATOR);
    }

    public static void generate(DataTable table, File csv) throws IOException {

        File dir = csv.getParentFile();
        if(!dir.exists()) {
            dir.mkdirs();
        }

        if(!csv.exists()) {
            csv.createNewFile();
        }

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(csv);
            generate(table, outputStream, DEFAULT_SEPARATOR);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

}
