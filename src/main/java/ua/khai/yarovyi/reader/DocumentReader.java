package ua.khai.yarovyi.reader;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentReader.class);

    List<XWPFDocument> readFiles(List<String> pathList) {
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
