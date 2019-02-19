package com.github.universalreader;

import com.github.universalreader.contenthandler.ContentHandler;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;

import static com.github.universalreader.util.ReaderUtil.readRecord;

@UtilityClass
public class XLSReader {

    private static final DataFormatter DATA_FORMATTER = new DataFormatter();

    public static <R> R readRecords(FileSource inputStreamSource, ContentHandler<R> contentsHandler,
                                                        ReaderConfiguration configuration)
            throws IOException {
        try (InputStream inputStream = inputStreamSource.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            workbook.sheetIterator().forEachRemaining(sheet -> {
                for (int i = configuration.getStartLineIndex(); i < sheet.getLastRowNum() + 1; i++) {
                    Row row = sheet.getRow(i);

                    if (row != null) {
                        contentsHandler.startRecord();

                        row.cellIterator().forEachRemaining(cell -> {
                            String value;

                            if (cell.getCellType() == CellType.NUMERIC) {
                                CellStyle cellStyle = cell.getCellStyle();
                                value = DATA_FORMATTER.formatRawCellContents(cell.getNumericCellValue(),
                                        cellStyle.getDataFormat(), cellStyle.getDataFormatString());
                            } else {
                                value = cell.getStringCellValue();
                            }

                            contentsHandler.startField(cell.getColumnIndex(), value);
                        });

                        readRecord(contentsHandler);
                    }
                }
            });

            contentsHandler.endFile();

            return contentsHandler.getResult();
        }
    }
}
