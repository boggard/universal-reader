package com.github.boggard.universalreader;

import com.github.boggard.universalreader.contenthandler.ContentHandler;
import com.github.boggard.universalreader.contenthandler.RecordHandler;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@UtilityClass
public class ExcelReader {

    private static final DataFormatter DATA_FORMATTER = new DataFormatter();

    public static <R, T> R readRecords(File sourceFile, ContentHandler<R, T> contentsHandler,
                                       ReaderConfiguration configuration)
            throws IOException {
        RecordHandler<T> recordHandler = contentsHandler.recordHandler();
        try (Workbook workbook = WorkbookFactory.create(sourceFile)) {
            Stream<T> resultStream = StreamSupport.stream(workbook.spliterator(), configuration.isParallelProcessing())
                    .flatMap(sheet -> createRowStream(configuration, sheet))
                    .map(row -> handleRow(recordHandler, row));

            contentsHandler.endFile();

            return contentsHandler.getResult(resultStream);
        }
    }

    private static Stream<Row> createRowStream(ReaderConfiguration configuration, Sheet sheet) {
        return StreamSupport.stream(sheet.spliterator(), configuration.isParallelProcessing())
                .skip(configuration.getStartLineIndex())
                .filter(row -> row != null && row.getRowNum() < sheet.getLastRowNum() + 1);
    }

    private static <T> T handleRow(RecordHandler<T> recordHandler, Row row) {
        T rec = recordHandler.startRecord();
        row.cellIterator().forEachRemaining(cell -> {
                    String value;
                    if (cell.getCellType() == CellType.NUMERIC) {
                        CellStyle cellStyle = cell.getCellStyle();
                        value = DATA_FORMATTER.formatRawCellContents(cell.getNumericCellValue(),
                                cellStyle.getDataFormat(), cellStyle.getDataFormatString());
                    } else {
                        value = cell.getStringCellValue();
                    }

                    recordHandler.startField(rec, cell.getColumnIndex(), value);
                }
        );
        return recordHandler.endRecord(rec);
    }
}
