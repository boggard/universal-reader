package com.github.boggard.universalreader;

import com.github.boggard.universalreader.contenthandler.ContentHandler;
import lombok.experimental.UtilityClass;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.util.IOUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@UtilityClass
public class SpreadsheetReader {

    public static <R, T> R readRecords(FileSource fileSource, ContentHandler<R, T> contentsHandler,
                                                  ReaderConfiguration configuration)
            throws IOException, ParserConfigurationException, SAXException, OpenXML4JException {
        File tempFile = File.createTempFile("spreadsheet-temp", null);
        writeToFile(fileSource, tempFile);

        R result = ExcelReader.readRecords(tempFile, contentsHandler, configuration);

        Files.delete(tempFile.toPath());

        return result;
    }

    private static void writeToFile(FileSource fileSource, File file) throws IOException {
        try (InputStream inputStream = fileSource.getInputStream()) {
            IOUtils.copy(inputStream, file);
        }
    }
}
