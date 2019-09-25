package com.unbxd.client.feed;

import java.io.Serializable;

public class FeedField implements Serializable {

    private String name;
    private Object value = null;
    private boolean multiValued;
    private boolean autoSuggest;
    private DataType dataType;

    public FeedField(String name, DataType dataType, boolean multiValued, boolean autoSuggest)
    {
        this.name = name;
        this.multiValued = multiValued;
        this.dataType = dataType;
        this.autoSuggest = autoSuggest;
    }



    /**
     * @return the value for this field. If the field has multiple values, this
     * will be a collection.
     */
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMultiValued() {
        return multiValued;
    }

    public DataType getDataType() {
        return dataType;
    }

    public boolean isAutoSuggest() {
        return autoSuggest;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FeedInputField{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value=").append(value);
        sb.append(", multiValued=").append(multiValued);
        sb.append(", dataType=").append(dataType);
        sb.append('}');
        return sb.toString();
    }
}