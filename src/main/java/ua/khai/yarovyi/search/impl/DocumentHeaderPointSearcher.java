package ua.khai.yarovyi.search.impl;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import ua.khai.yarovyi.search.EntryPointSearcher;

import java.util.Optional;

public class DocumentHeaderPointSearcher implements EntryPointSearcher {

    private static final String DOCUMENT_HEADER_REGEX = "(0-9\\.\\s[A-ZА-Я])+";


    @Override
    public Optional<XWPFParagraph> searchEntryPoint(XWPFDocument document) {
        return document.getParagraphs()
                .stream()
                .filter(item -> DOCUMENT_HEADER_REGEX.matches(item.getText()))
                .findFirst();
    }
}
