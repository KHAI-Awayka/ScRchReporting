package ua.khai.yarovyi.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.khai.yarovyi.exception.NoSuitableDocuments;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FolderScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderScanner.class);

    List<String> searchDocuments(String path) {
        File directory = new File(path);

        if (directory.isDirectory() && directory.listFiles() != null) {
            return Arrays.stream(directory.listFiles()).filter(File::isFile).filter(item -> item.getName().endsWith(".docx")).map(File::getAbsolutePath).collect(Collectors.toList());
        }
        LOGGER.warn("No Suitable documents at path : {}", path);
        throw new NoSuitableDocuments("No Suitable documents at path : " + path);
    }
}
