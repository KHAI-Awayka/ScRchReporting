package ua.khai.yarovyi;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.khai.yarovyi.builder.impl.ArticleDocumentBuilder;
import ua.khai.yarovyi.builder.impl.ReferenceDocumentBuilder;
import ua.khai.yarovyi.configuration.BaseConfiguration;
import ua.khai.yarovyi.model.Document;
import ua.khai.yarovyi.model.ReferenceDocument;
import ua.khai.yarovyi.replace.impl.ReferenceNumberReplacer;
import ua.khai.yarovyi.scanner.FolderScanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private String basePath;
    private String outputPath;

    public static void main(String[] args) throws IOException {
        Map<String, String> initProperties = BaseConfiguration.init();
        new App(initProperties);
    }

    public App(Map<String, String> properties) {
        FolderScanner folderScanner = new FolderScanner(properties.getOrDefault("file_format", ".docx"));
        basePath = properties.get("input_files_path");
        outputPath = properties.get("output_path");
        List<ReferenceDocument> referenceDocuments = prepareReferenceDocuments(
                folderScanner.searchDocuments(basePath, item -> item.contains(".ref")));

        List<Document> articleDocuments = new ReferenceDocumentBuilder(outputPath)
                .buildDocument(referenceDocuments);
        updateReferences(articleDocuments);
        List<Document> readyDocuments = new ArticleDocumentBuilder(outputPath).buildDocument(articleDocuments);
    }

    private static List<ReferenceDocument> prepareReferenceDocuments(List<String> pathList) {
        return pathList.stream()
                .map(ReferenceDocument::new)
                .collect(Collectors.toList());
    }

    private static void updateReferences(List<? extends Document> documents) {
        Iterator<? extends Document> iterator = documents.iterator();

        if (iterator.hasNext()) {
            Document prevDocument = iterator.next();
            while (iterator.hasNext()) {
                Document currentDocument = iterator.next();
                ReferenceNumberReplacer replacer = new ReferenceNumberReplacer();
                XWPFDocument xwpfDocument = new XWPFDocument();
                try (FileInputStream fileInputStream = new FileInputStream(new File(currentDocument.getFilePath()))) {
                    xwpfDocument = new XWPFDocument(fileInputStream);
                    replacer.replace(xwpfDocument, prevDocument.getReferenceOffset());
                    prevDocument = currentDocument;
                } catch (IOException e) {
                    LOGGER.warn("Could not update references", e);
                }
                try (FileOutputStream outputStream = new FileOutputStream(new File(currentDocument.getFilePath()))) {
                    xwpfDocument.write(outputStream);
                } catch (IOException e) {
                    LOGGER.warn("Could not update references", e);
                }
            }
        }
    }
}
