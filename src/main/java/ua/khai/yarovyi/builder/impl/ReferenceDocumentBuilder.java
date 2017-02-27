package ua.khai.yarovyi.builder.impl;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.khai.yarovyi.builder.DocumentBuilder;
import ua.khai.yarovyi.model.Document;
import ua.khai.yarovyi.model.ReferenceDocument;
import ua.khai.yarovyi.wrapper.CTLvlWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides implementation for compiling several numbering lists to single .docx
 */
public class ReferenceDocumentBuilder implements DocumentBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceDocumentBuilder.class);
    private final String outputDirectory;

    private int step;
    private CTAbstractNum ctAbstractNum;

    /**
     * Base configuration for creating reference list as numbering in single .docx document
     * Result document will be stored according to outputDirectory + refs.docx
     *
     * @param outputDirectory String parameter for result documents output directory
     */
    public ReferenceDocumentBuilder(String outputDirectory) {
        this.outputDirectory = outputDirectory + "/refs.docx";
        step = 1;
        ctAbstractNum = CTAbstractNum.Factory.newInstance();
        ctAbstractNum.setAbstractNumId(BigInteger.ONE);
    }

    /**
     * Creates refs.docx file in specified in constructor output directory
     * Currently style copy is unsupported. Provides only creating numbering list
     *
     * @param documents List of document to process during building
     * @return Initialized list of files for next step building step
     */
    public List<Document> buildDocument(List<? extends Document> documents) {
        List<ReferenceDocument> referenceDocuments = (List<ReferenceDocument>) documents;
        LinkedList<Document> resultList = new LinkedList<>();
        XWPFDocument resultDocument = new XWPFDocument();
        resultDocument.createNumbering();
        resultDocument.getNumbering().addAbstractNum(new XWPFAbstractNum(ctAbstractNum));
        resultDocument.getNumbering().addNum(BigInteger.ONE);


        referenceDocuments.forEach(item -> processSingleDocument(item.getReferanceDocument(), resultDocument));


        for (ReferenceDocument doc : referenceDocuments) {
            if (resultList.size() == 0) {
                resultList.add(prepareDocument(doc, 0));
                continue;
            }
            resultList.add(prepareDocument(doc, resultList.getLast().getReferenceOffset()));
        }
        try {
            resultDocument.write(new FileOutputStream(new File(outputDirectory)));
            resultDocument.close();
            referenceDocuments.forEach(item -> {
                try {
                    item.getReferanceDocument().close();
                } catch (IOException e) {
                    LOGGER.warn("Could not close file stream {}", e);
                }
            });

        } catch (IOException e) {
            LOGGER.warn("Could not create file at {}", outputDirectory);
        }
        return resultList;
    }


    private void processSingleDocument(XWPFDocument document, XWPFDocument resultDocument) {
        document.getParagraphs()
                .stream()
                .filter(item -> "decimal".equals(item.getNumFmt()))
                .forEach(item -> processSingleParagraph(resultDocument, item.getText()));
    }

    private void processSingleParagraph(XWPFDocument document, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFNumbering numbering = document.getNumbering();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setNumID(addListStyle(document, numbering));
        XWPFRun run = paragraph.createRun();
        run.setText(text);
    }

    private BigInteger addListStyle(XWPFDocument doc, XWPFNumbering numbering) {
        CTLvlWrapper.prepareCtlvl(numbering.getAbstractNum(BigInteger.ONE), step++);
        return BigInteger.ONE;
    }

    private Document prepareDocument(ReferenceDocument referenceDocument, long previousOffset) {
        Document document = new Document();
        document.setReferenceOffset(previousOffset + getReferenceCount(referenceDocument.getReferanceDocument()));
        document.setFilePath(referenceDocument.getFilePath().replace(".ref", ""));
        return document;
    }

    private long getReferenceCount(XWPFDocument xwpfDocument) {
        return xwpfDocument
                .getParagraphs()
                .stream().filter(item -> "decimal".equals(item.getNumFmt()))
                .count();
    }
}
