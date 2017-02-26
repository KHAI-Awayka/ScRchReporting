package ua.khai.yarovyi;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import ua.khai.yarovyi.builder.ReferenceDocumentBuilder;
import ua.khai.yarovyi.model.ReferenceDocument;
import ua.khai.yarovyi.scanner.FolderScanner;

import java.util.List;
import java.util.stream.Collectors;

public class App {

    private static final String BASE_PATH = "/home/awayka/Documents/ScRchFiles/items/";
    private static final String REFERANCE_START = "[\\x00\\x0B\\x0C\\x0E-\\x1F]+[0-9]+\\..+$";

    public static void main(String[] args) throws OpenXML4JException {
        FolderScanner folderScanner = new FolderScanner();
        List<ReferenceDocument> referenceDocuments = prepareReferenceDocuments(folderScanner.searchDocuments(BASE_PATH, item -> item.contains(".ref")));

        new ReferenceDocumentBuilder()
                .buildReferenceDocument(referenceDocuments).forEach(System.out::println);

    }

    private static List<ReferenceDocument> prepareReferenceDocuments(List<String> pathList) {
        return pathList.stream()
                .map(ReferenceDocument::new)
                .collect(Collectors.toList());
    }
}
