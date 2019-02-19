package com.github.universalreader;

import com.github.universalreader.contenthandler.ContentCollector;
import com.github.universalreader.contenthandler.ContentHandler;
import com.github.universalreader.contenthandler.ContentProcessor;
import com.github.universalreader.readerresult.CollectResult;
import com.github.universalreader.readerresult.ProcessResult;
import com.github.universalreader.readerresult.ReaderResult;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Slf4j
@UtilityClass
public class UniversalReader {

    public static <T, E> CollectResult<T, E> collectRecords(FileSource fileSource, ContentCollector<T, E> contentsHandler,
                                                            ReaderConfiguration configuration)
            throws IOException, OpenXML4JException, SAXException, ParserConfigurationException {
        return (CollectResult<T, E>) readRecords(fileSource, contentsHandler, configuration);
    }

    public static <T, E> ProcessResult<E> processRecords(FileSource fileSource, ContentProcessor<E> contentsHandler,
                                                         ReaderConfiguration configuration)
            throws IOException, OpenXML4JException, SAXException, ParserConfigurationException {
        return (ProcessResult<E>) readRecords(fileSource, contentsHandler, configuration);
    }

    private <E> ReaderResult<E> readRecords(FileSource fileSource, ContentHandler<E> contentsHandler,
                                            ReaderConfiguration configuration) throws IOException,
            OpenXML4JException, SAXException, ParserConfigurationException {
        log.debug("Parsing file: " + fileSource.getFileName());

        String fileExt = getFileExtension(fileSource.getFileName());

        log.debug("File extension  " + fileExt);

        ReaderResult<E> result;
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
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            throw new IllegalStateException("File extension can't be determine");
        }
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
