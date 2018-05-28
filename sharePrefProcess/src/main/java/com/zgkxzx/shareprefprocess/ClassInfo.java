package com.zgkxzx.shareprefprocess;

public class ClassInfo {
    private String value;
    private String fieldName;
    private String typeName;

    public ClassInfo(String value, String fieldName, String typeName) {
        this.value = value;
        this.fieldName = fieldName;
        this.typeName = typeName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
