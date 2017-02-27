package ua.khai.yarovyi.reader;

import java.io.Closeable;
import java.util.List;

public interface DocumentReader<T extends Closeable> {
    /**
     * Open file input streams for each string path
     *
     * @param pathList list of paths to files that are needed to be processed
     * @return initialized list of processing files. Files should implement interface Closeable
     */
    List<T> readFiles(List<String> pathList);
}
