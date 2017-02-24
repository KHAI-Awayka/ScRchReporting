package ua.khai.yarovyi.replace.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import ua.khai.yarovyi.replace.NumberReplacer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReferenceNumberReplacer implements NumberReplacer {
    private static final String LITERATURE_REFERENCE = "'\\[[(0-9)+ (,)*]+\\]";
    public static final int TEXT_POS = 0;

    private Pattern pattern = Pattern.compile(LITERATURE_REFERENCE);

    @Override
    public XWPFDocument replace(XWPFDocument document, int value) {
        document.getParagraphs()
                .stream()
                .filter(paragraph -> paragraph.getRuns() != null)
                .forEach(
                        paragraph -> paragraph.getRuns()
                                .forEach(item ->
                                        item.setText(updateReferenceValues(item.getText(TEXT_POS), value), TEXT_POS)));
        return document;
    }

    private String updateReferenceValues(String text, int value) {
        StringBuffer builder = new StringBuffer();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String oldRefValue = matcher.group(1);
            if (StringUtils.isNumeric(oldRefValue)) {
                matcher.appendReplacement(builder, String.valueOf(Integer.parseInt(oldRefValue) + value));
            }
        }
        matcher.appendTail(builder);
        return builder.toString();
    }
}
