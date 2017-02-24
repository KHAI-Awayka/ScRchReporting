package ua.khai.yarovyi.search;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.util.Optional;

@FunctionalInterface
public interface EntryPointSearcher {
    Optional<XWPFParagraph> searchEntryPoint(XWPFDocument document);
}
