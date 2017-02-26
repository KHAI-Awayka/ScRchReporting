package ua.khai.yarovyi.replace.impl;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import ua.khai.yarovyi.replace.NumberReplacer;

import java.util.List;
import java.util.function.Predicate;

/**
 * Unused on current phase
 */
public class ParagraphNumberReplacer implements NumberReplacer {
    private static final String THEME_PARAGRAPH = "[A-ZА-Я]{2,}.+";
    private static final String SUB_THEME_PARAGRAPH = "(0-9\\.0-9\\.)*.+";

    @Override
    public XWPFDocument replace(XWPFDocument document, int value) {
        document.getParagraphs()
                .stream()
                .filter(((Predicate<XWPFParagraph>) item -> item.getText().matches(THEME_PARAGRAPH))
                        .or((item -> item.getText().matches(SUB_THEME_PARAGRAPH))))
                .forEach(item -> updateParagraphValues(item, value));
        return document;
    }

    private void updateParagraphValues(XWPFParagraph paragraph, int value) {
        List<XWPFRun> runs = paragraph.getRuns();
        for (int i = runs.size() - 1; i > 0; i--) {
            paragraph.removeRun(i);
        }
        XWPFRun run = runs.get(0);
        run.setText(value + "." + run.getText(0), 0);
    }
}
