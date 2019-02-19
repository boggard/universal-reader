package com.github.universalreader;

import com.github.universalreader.contenthandler.ContentHandler;
import com.github.universalreader.readerresult.ReaderResult;
import com.github.universalreader.util.LocalFileSource;
import lombok.experimental.UtilityClass;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
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

    public static <E> ReaderResult<E> readRecords(FileSource fileSource, ContentHandler<E> contentsHandler,
                                                  ReaderConfiguration configuration)
            throws IOException, ParserConfigurationException, SAXException, OpenXML4JException {
        File tempFile = File.createTempFile("spreadsheet-temp", null);
        writeToFile(fileSource, tempFile);

        FileSource tempFileSource = new LocalFileSource(tempFile);

        ReaderResult<E> result;

        try {
            result = XLSXReader.readRecords(tempFileSource, contentsHandler, configuration);
        } catch (OLE2NotOfficeXmlFileException e) {
            result = XLSReader.readRecords(tempFileSource, contentsHandler, configuration);
        }

        Files.delete(tempFile.toPath());

        return result;
    }

    private static void writeToFile(FileSource fileSource, File file) throws IOException {
        try (InputStream inputStream = fileSource.getInputStream()) {
            IOUtils.copy(inputStream, file);
        }
    }
}
