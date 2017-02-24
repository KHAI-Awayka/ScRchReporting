package ua.khai.yarovyi.replace;


import org.apache.poi.xwpf.usermodel.XWPFDocument;

@FunctionalInterface
public interface NumberReplacer {
    XWPFDocument replace(XWPFDocument document, int value);
}
