package ua.khai.yarovyi.replace.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import ua.khai.yarovyi.replace.NumberReplacer;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReferenceNumberReplacer implements NumberReplacer {
    private static final int TEXT_POS = 0;
    private static final String LITERATURE_REFERENCE = "\\[(([0-9])+(,)*)+\\]";
    private Pattern pattern = Pattern.compile(LITERATURE_REFERENCE);

    @Override
    public XWPFDocument replace(XWPFDocument document, long value) {
        document.getParagraphs()
                .stream()
                .filter(paragraph -> paragraph.getRuns() != null && paragraph.getRuns().size() > 0)
                .filter(paragraph -> paragraph.getText() != null)
                .forEach(
                        paragraph -> changeText(paragraph, paragraph.getText(), value));
        return document;
    }

    public void changeText(XWPFParagraph p, String text, long value) {
        List<XWPFRun> runs = p.getRuns();
        for (int i = runs.size(); i > 0; i--) {
            p.removeRun(i);
        }
        XWPFRun run = runs.get(0);
        run.setText(updateReferenceValues(text, value), 0);
    }

    private String updateReferenceValues(String text, long value) {
        StringBuffer builder = new StringBuffer();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String oldRefValue = matcher.group(2);
            if (StringUtils.isNumeric(oldRefValue)) {
                matcher.appendReplacement(builder, "[" + String.valueOf(Integer.parseInt(oldRefValue) + value) + "]");
            }
        }
        matcher.appendTail(builder);
        return builder.toString();
    }
}
