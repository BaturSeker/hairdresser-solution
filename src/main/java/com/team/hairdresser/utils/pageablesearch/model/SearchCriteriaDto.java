package com.team.hairdresser.utils.pageablesearch.model;


/**
 * Search filter criteira
 */
public class SearchCriteriaDto {

    /**
     * Field name of the the entity to filter
     */
    private String key;

    /**
     * Filter operation parameter
     * Example: ":" for equality
     *
     * @see SearchOperationEnum
     */
    private String operation;

    /**
     * Filter input
     */
    private Object value;

    /**
     * If between operation is needed, end value is set here
     */
    private Object endValue;

    /**
     * Type of key field of the entity
     */
    private SearchFieldTypeEnum fieldType = SearchFieldTypeEnum.STRING;

    /**
     * Add this criteria as 'OR' if this field is true
     */
    private Boolean or = Boolean.FALSE;

    public SearchCriteriaDto() {
    }

    public SearchCriteriaDto(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SearchCriteriaDto(String key, String operation, Object value, SearchFieldTypeEnum fieldType) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.fieldType = fieldType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getOr() {
        return or;
    }

    public void setOr(Boolean or) {
        this.or = or;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getEndValue() {
        return endValue;
    }

    public void setEndValue(Object endValue) {
        this.endValue = endValue;
    }

    public SearchFieldTypeEnum getFieldType() {
        return fieldType;
    }

    public void setFieldType(SearchFieldTypeEnum fieldType) {
        this.fieldType = fieldType;
    }
}
