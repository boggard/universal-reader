package com.github.boggard.universalreader;

import com.github.boggard.universalreader.contenthandler.ContentHandler;
import com.github.boggard.universalreader.util.ReaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class XLSXReader {

    public static <R> R readRecords(File sourceFile, ContentHandler<R> contentsHandler,
                                    ReaderConfiguration configuration)
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        try (OPCPackage pkg = OPCPackage.open(sourceFile)) {
            XSSFReader reader = new XSSFReader(pkg);

            SheetContentsHandlerImpl<R> sheetContentsHandler = new SheetContentsHandlerImpl<>(contentsHandler, configuration);
            XMLReader parser = fetchSheetParser(reader.getStylesTable(), reader.getSharedStringsTable(),
                    sheetContentsHandler);

            Iterator<InputStream> sheetsData = reader.getSheetsData();
            sheetsData.forEachRemaining(sheet -> processSheet(parser, sheet));

            contentsHandler.endFile();

            return contentsHandler.getResult();
        }
    }

    private XMLReader fetchSheetParser(Styles styles, SharedStrings sharedStrings, SheetContentsHandler contentsHandler)
            throws SAXException, ParserConfigurationException {
        XMLReader parser = SAXHelper.newXMLReader();
        XSSFSheetXMLHandler handler = new XSSFSheetXMLHandler(styles, sharedStrings, contentsHandler, true);
        parser.setContentHandler(handler);
        return parser;
    }

    private void processSheet(XMLReader parser, InputStream sheet) {
        try {
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        } catch (SAXException | IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @RequiredArgsConstructor
    private static class SheetContentsHandlerImpl<R> implements SheetContentsHandler {

        private static final Pattern CELL_REFERENCE_PATTERN = Pattern.compile("[A-Z]+");
        private final ContentHandler<R> contentsHandler;
        private final ReaderConfiguration configuration;

        private int currentRowNum = -1;

        @Override
        public void startRow(int rowNum) {
            currentRowNum = rowNum;

            if (isContentShouldBeHandled()) {
                contentsHandler.startRecord();
            }
        }

        @Override
        public void endRow(int rowNum) {
            if (isContentShouldBeHandled()) {
                ReaderUtil.readRecord(contentsHandler);
            }
        }

        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment comment) {
            if (isContentShouldBeHandled()) {
                int index = cellReferenceToIndex(cellReference);
                contentsHandler.startField(index, formattedValue);
            }
        }

        private static int cellReferenceToIndex(String cellReference) {
            Matcher matcher = CELL_REFERENCE_PATTERN.matcher(cellReference);

            if (matcher.find()) {
                String cellLetter = matcher.group();
                return findPosition(cellLetter.charAt(0)) * cellLetter.length();
            } else {
                return -1;
            }
        }

        private static int findPosition(char inputLetter) {
            return Character.toLowerCase(inputLetter) - 'a';
        }

        private boolean isContentShouldBeHandled() {
            return currentRowNum >= configuration.getStartLineIndex();
        }
    }
}
