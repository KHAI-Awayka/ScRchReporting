package ua.khai.yarovyi.reader.impl;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.khai.yarovyi.reader.DocumentReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Provides document reader implementation for XWPFDocument objects
 */
public class XwpfDocumentReader implements DocumentReader<XWPFDocument> {

    private static final Logger LOGGER = LoggerFactory.getLogger(XwpfDocumentReader.class);

    /**
     * Open FileInputStream for each document. In case if file cannot be opened, it will be skipped
     *
     * @param pathList list of paths to files that are needed to be processed
     * @return List of XWPFDocument with opened streams on real file
     */
    @Override
    public List<XWPFDocument> readFiles(List<String> pathList) {
        List<XWPFDocument> documents = new ArrayList<>();
        List<File> files = pathList.stream().map(File::new).collect(Collectors.toList());
        for (File file : files) {
            try {
                documents.add(new XWPFDocument(new FileInputStream(file)));
            } catch (IOException ex) {
                LOGGER.warn("Cannot load file {}", ex);
            }

        }
        return documents;
    }
}
