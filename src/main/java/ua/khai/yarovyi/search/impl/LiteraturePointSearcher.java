package ua.khai.yarovyi.search.impl;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import ua.khai.yarovyi.search.EntryPointSearcher;

import java.util.Optional;
import java.util.function.Predicate;

public class LiteraturePointSearcher implements EntryPointSearcher {
    private static final String RU_LITERATURE_EXPR = "ЛИТЕРАТУРА.*";
    private static final String EN_LITERATURE_EXPR = "REFERENCES.*";

    @Override
    public Optional<XWPFParagraph> searchEntryPoint(XWPFDocument document) {
        return document.getParagraphs()
                .stream()
                .filter(
                        ((Predicate<XWPFParagraph>) item -> RU_LITERATURE_EXPR.matches(item.getText()))
                                .or(item -> EN_LITERATURE_EXPR.matches(item.getText()))
                )
                .findFirst();
    }
}
