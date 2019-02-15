package com.github.universalreader;

import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static com.github.universalreader.util.ReaderUtil.readRecord;

@UtilityClass
public class XLSReader {

    public static <T, E> ReaderResult<T, E> readRecords(FileSource inputStreamSource, ContentHandler<T, E> contentsHandler,
                                          ReaderConfiguration configuration)
            throws IOException {
        try (InputStream inputStream = inputStreamSource.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            List<T> records = new LinkedList<>();
            List<E> errorRecords = new LinkedList<>();

            workbook.sheetIterator().forEachRemaining(sheet -> {
                for (int i = configuration.getStartLineIndex(); i < sheet.getLastRowNum() + 1; i++) {
                    Row row = sheet.getRow(i);

                    if (row != null) {
                        contentsHandler.startRecord();

                        row.cellIterator().forEachRemaining(cell -> {
                            cell.setCellType(CellType.STRING);
                            contentsHandler.startField(cell.getColumnIndex(), cell.getStringCellValue());
                        });

                        readRecord(contentsHandler, records::add, errorRecords::add);
                    }
                }
            });

            return new ReaderResult<>(records, errorRecords);
        }
    }
}
