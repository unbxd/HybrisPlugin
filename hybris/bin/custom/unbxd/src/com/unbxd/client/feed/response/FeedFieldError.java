package com.unbxd.client.feed.response;

import com.unbxd.client.feed.DataType;

import java.util.List;
import java.util.Map;

public class FeedFieldError {

    private String _fieldName;
    private Object _fieldValue;
    private DataType _dataType;
    private boolean _multivalued;
    private int _errorCode;
    private String _message;
    private int _rowNum;
    private int _colNum;

    protected FeedFieldError(Map<String, Object> params) {
        this._fieldName = (String) params.get("fieldName");
        this._fieldValue = params.get("fieldValue");
        this._dataType = DataType.fromString((String) params.get("dataType"));
        this._multivalued = Boolean.parseBoolean((String) params.get("multiValue"));
        this._errorCode = (Integer) params.get("errorCode");
        this._message = (String) params.get("message");

        if(params.get("rowNum") != null)
            this._rowNum = Integer.parseInt((String) params.get("rowNum"));

        if(params.get("colNum") != null)
            this._colNum = Integer.parseInt((String) params.get("colNum"));
    }

    public String getFieldName() {
        return _fieldName;
    }

    public Object getFieldValue() {
        return _fieldValue;
    }

    public DataType getDataType() {
        return _dataType;
    }

    public boolean isMultivalued() {
        return _multivalued;
    }

    public int getErrorCode() {
        return _errorCode;
    }

    public String getMessage() {
        return _message;
    }

    public int getRowNum() {
        return _rowNum;
    }

    public int getColNum() {
        return _colNum;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FeedFieldError{");
        sb.append("_fieldName='").append(_fieldName).append('\'');
        sb.append(", _fieldValue=").append(_fieldValue);
        sb.append(", _dataType=").append(_dataType);
        sb.append(", _multivalued=").append(_multivalued);
        sb.append(", _errorCode=").append(_errorCode);
        sb.append(", _message='").append(_message).append('\'');
        sb.append(", _rowNum=").append(_rowNum);
        sb.append(", _colNum=").append(_colNum);
        sb.append('}');
        return sb.toString();
    }
}
