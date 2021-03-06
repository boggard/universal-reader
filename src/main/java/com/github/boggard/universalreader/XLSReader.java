package com.github.boggard.universalreader;

import com.github.boggard.universalreader.contenthandler.ContentHandler;
import com.github.boggard.universalreader.util.ReaderUtil;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;

@UtilityClass
public class XLSReader {

    private static final DataFormatter DATA_FORMATTER = new DataFormatter();

    public static <R> R readRecords(File sourceFile, ContentHandler<R> contentsHandler,
                                    ReaderConfiguration configuration)
            throws IOException {
        try (Workbook workbook = WorkbookFactory.create(sourceFile)) {
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

                        ReaderUtil.readRecord(contentsHandler);
                    }
                }
            });

            contentsHandler.endFile();

            return contentsHandler.getResult();
        }
    }
}
