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

    private final String fileFormat;

    public FolderScanner(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public List<String> searchDocuments(String path) {
        return searchDocuments(path, predicate -> true);
    }

    /**
     * Provides method for file search according file format, that was passed in constructor.
     * Do not provide deep search. Also, can be added additional search criteria.This criteria will be checked
     * after selection by file format.
     * Do not check accessibility of files
     *
     * @param path               base path to folder
     * @param nameSearchCriteria additional criteria of search
     * @return initialized list of absolute paths to suitable files
     */
    public List<String> searchDocuments(String path, Predicate<String> nameSearchCriteria) {
        File directory = new File(path);
        if (directory.isDirectory()) {
            return Arrays
                    .stream(
                            Optional
                                    .ofNullable(directory.listFiles())
                                    .orElseThrow(IllegalArgumentException::new))
                    .filter(File::isFile)
                    .filter(item -> item.getName().endsWith(fileFormat))
                    .filter(item -> nameSearchCriteria.test(item.getName()))
                    .map(File::getAbsolutePath)
                    .collect(Collectors.toList());
        }

        LOGGER.warn("Illegal path : {}", path);
        throw new NoSuitableDocuments("Illegal path : " + path);
    }
}
