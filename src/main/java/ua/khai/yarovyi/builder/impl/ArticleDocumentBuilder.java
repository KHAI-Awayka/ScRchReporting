package ua.khai.yarovyi.builder.impl;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.khai.yarovyi.builder.DocumentBuilder;
import ua.khai.yarovyi.model.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides implementation for compiling several .docx documents in single document
 */
public class ArticleDocumentBuilder implements DocumentBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleDocumentBuilder.class);
    private final String outputPath;

    private int paragraphIndex;
    private int tableIndex;

    /**
     * Construct object of builder and prepare for future processing
     *
     * @param outputPath String parameter for result documents output directory
     */
    public ArticleDocumentBuilder(String outputPath) {
        this.outputPath = outputPath + "/articles.docx";
    }

    /**
     * Provides flow for document building. Supports style and content copy.
     * Tables and images will be implemented in next versions
     *
     * @param documents List of document to process during building
     * @return Initialized list of files for next step building step
     */
    @Override
    public List<Document> buildDocument(List<? extends Document> documents) {
        paragraphIndex = 0;
        tableIndex = 0;
        List<Document> resultList = new ArrayList<>();
        try {
            XWPFDocument resultDocument = new XWPFDocument();
            resultDocument.createStyles().setStyles(CTStyles.Factory.newInstance());
            for (Document sourceDocumentPath : documents) {
                XWPFDocument sourceDocument = new XWPFDocument(new FileInputStream(new File(sourceDocumentPath.getFilePath())));
                copyElements(sourceDocument, resultDocument);
                copyStyles(sourceDocument, resultDocument);
                sourceDocument.close();
                resultList.add(sourceDocumentPath);
            }
            OutputStream out = new FileOutputStream(new File(outputPath));
            resultDocument.write(out);
            out.close();
        } catch (IOException ex) {
            LOGGER.warn("Could not open file {}", ex);
        } catch (InvalidFormatException ex) {
            LOGGER.warn("Could not copy image {}", ex);
        } catch (XmlException ex) {
            LOGGER.warn("Could not copy style {}", ex);
        }
        return resultList;
    }

    /**
     * Copy content elements
     *
     * @throws InvalidFormatException in case of unsupported or incorrect file type
     */
    private void copyElements(XWPFDocument source, XWPFDocument target) throws InvalidFormatException {
        for (IBodyElement e : source.getBodyElements()) {
            if (e instanceof XWPFParagraph) {
                XWPFParagraph p = (XWPFParagraph) e;
                if (!(p.getCTP().getPPr() != null && p.getCTP().getPPr().getSectPr() != null)) {
                    target.createParagraph();
                    target.setParagraph(p, paragraphIndex);
                    paragraphIndex++;
                }
            } else if (e instanceof XWPFTable) {
                XWPFTable t = (XWPFTable) e;
                target.createTable();
                target.setTable(tableIndex, t);
                tableIndex++;
            } else if (e instanceof XWPFPicture) {
                XWPFPicture t = (XWPFPicture) e;
                target.addPictureData(t.getPictureData().getData(), t.getPictureData().getPictureType());
                //TODO improve picture copy
            }
        }
    }

    /**
     * Copy styles
     */
    //Really silly method. Refactor it pls
    private void copyStyles(XWPFDocument source, XWPFDocument target) throws IOException, XmlException {
        CTStyles targetStyle = null;
        try {
            targetStyle = target.getStyle();
            CTStyles sourceStyle = source.getStyle();
            int size1 = targetStyle.getStyleList().size();
            int size2 = sourceStyle.getStyleList().size();
            for (int i = 0; i < size2; i++) {
                targetStyle.addNewStyle();
                targetStyle.setStyleArray(size1 + i, sourceStyle.getStyleList().get(i));
            }
            target.createStyles().setStyles(targetStyle);
        } catch (IOException ex) {
            target.createStyles().setStyles(source.getStyle());
        }

    }

}
