package ua.khai.yarovyi.builder;

import ua.khai.yarovyi.model.Document;

import java.util.List;

/**
 * Provides base interface for document building
 */
@FunctionalInterface
public interface DocumentBuilder {
    /**
     * Provides method for document building.
     *
     * @param documents List of document to process during building
     * @return Initialized list of files for next step building step
     */
    List<Document> buildDocument(List<? extends Document> documents);
}
