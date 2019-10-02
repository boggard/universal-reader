package com.github.boggard.universalreader;

import com.github.boggard.universalreader.contenthandler.ContentHandler;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Slf4j
@UtilityClass
public class UniversalReader {

    public static <R> R processRecords(FileSource fileSource, ContentHandler<R> contentsHandler,
                                       ReaderConfiguration configuration) {
        try {
            log.debug("Parsing file: " + fileSource.getFileName());

            String fileExt = getFileExtension(fileSource.getFileName()).toLowerCase();

            log.debug("File extension  " + fileExt);

            R result;
            switch (fileExt) {
                case "txt":
                    result = TXTReader.readRecords(fileSource, contentsHandler, configuration);
                    break;
                case "csv":
                    result = CSVReader.readRecords(fileSource, contentsHandler, configuration);
                    break;
                case "xls":
                case "xlsx":
                    result = SpreadsheetReader.readRecords(fileSource, contentsHandler, configuration);
                    break;
                default:
                    throw new IllegalStateException("Incorrect file format");
            }

            return result;

        } catch (SAXException | IOException | ParserConfigurationException | OpenXML4JException ex) {
            throw new IllegalStateException("An error occurred with message: " + ex.getMessage(), ex);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            throw new IllegalStateException("File extension can't be determine");
        }
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
