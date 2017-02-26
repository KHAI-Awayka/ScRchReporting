package ua.khai.yarovyi.model;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReferenceDocument extends Document {
    private XWPFDocument referanceDocument;

    public ReferenceDocument(String referenceDocumentPath) {
        this.setFilePath(referenceDocumentPath);
        try {
            this.referanceDocument = new XWPFDocument(new FileInputStream(new File(referenceDocumentPath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public XWPFDocument getReferanceDocument() {
        return referanceDocument;
    }

    public void setReferanceDocument(XWPFDocument referanceDocument) {
        this.referanceDocument = referanceDocument;
    }

}
