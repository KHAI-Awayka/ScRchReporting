package ua.khai.yarovyi.model;

public class Document {
    private long referenceOffset;
    private String filePath;

    public long getReferenceOffset() {
        return referenceOffset;
    }

    public void setReferenceOffset(long referenceOffset) {
        this.referenceOffset = referenceOffset;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "Document{" +
                "referenceOffset=" + referenceOffset +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
