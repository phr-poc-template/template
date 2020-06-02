package jp.co.tis.phr.core.division;

public enum RecordType {

    MEDICAL_EXAM("MedicalExam"),
    DEVICE("Device");

    private String recordType;

    RecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getRecordType() {
        return recordType;
    }
}
