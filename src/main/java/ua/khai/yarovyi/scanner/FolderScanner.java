package ua.khai.yarovyi.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.khai.yarovyi.exception.NoSuitableDocuments;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FolderScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderScanner.class);
    private static final String DOCX = ".docx";

    public List<String> searchDocuments(String path) {
        return searchDocuments(path, predicate -> true);
    }

    public List<String> searchDocuments(String path, Predicate<String> nameSearchCriteria) {
        File directory = new File(path);
        if (directory.isDirectory()) {
            return Arrays
                    .stream(
                            Optional
                                    .ofNullable(directory.listFiles())
                                    .orElseThrow(IllegalArgumentException::new))
                    .filter(File::isFile)
                    .filter(item -> item.getName().endsWith(DOCX))
                    .filter(item -> nameSearchCriteria.test(item.getName()))
                    .map(File::getAbsolutePath)
                    .collect(Collectors.toList());
        }

        LOGGER.warn("Illegal path : {}", path);
        throw new NoSuitableDocuments("Illegal path : " + path);
    }
}
