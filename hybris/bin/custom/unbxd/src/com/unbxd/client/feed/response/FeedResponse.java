package com.unbxd.client.feed.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FeedResponse {

    private int _statusCode;
    private String _message;
    private String _uploadID;
    private String _fileName;
    private List<String> _unknownSchemaFields;
    private List<FeedFieldError> _fieldErrors;
    private int _rowNum;
    private int _colNum;

    public FeedResponse(Map<String, Object> response){
        _statusCode = (Integer) response.get("status");
        _message = (String) response.get("message");
        _uploadID = (String) response.get("uploadId");
        _fileName = (String) response.get("fileName");
        if(response.containsKey("unknownSchemaFields")) {
            _unknownSchemaFields = (List<String>) response.get("unknownSchemaFields");
        }
        if(response.containsKey("fieldErrors")){
            List<Map<String, Object>> fieldErrors = (List<Map<String, Object>>) response.get("fieldErrors");
            _fieldErrors = new ArrayList<FeedFieldError>();
            for(Map<String, Object> error : fieldErrors){
                _fieldErrors.add(new FeedFieldError(error));
            }
        }

        if(response.get("rowNum") != null)
            this._rowNum = Integer.parseInt((String) response.get("rowNum"));

        if(response.get("colNum") != null)
            this._colNum = Integer.parseInt((String) response.get("colNum"));
    }

    public int getStatusCode(){
        return _statusCode;
    }

    public String getMessage(){
        return _message;
    }

    public String getUploadID(){
        return _uploadID;
    }

    public List<FeedFieldError> getFieldErrors(){
        return _fieldErrors;
    }

    public List<String> getUnknownSchemaFields(){
        return _unknownSchemaFields;
    }

    public int getRowNum() {
        return _rowNum;
    }

    public int getColNum() {
        return _colNum;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FeedResponse{");
        sb.append("_statusCode=").append(_statusCode);
        sb.append(", _message='").append(_message).append('\'');
        sb.append(", _uploadID='").append(_uploadID).append('\'');
        sb.append(", _unknownSchemaFields=").append(_unknownSchemaFields);
        sb.append(", _fieldErrors=").append(_fieldErrors);
        sb.append(", _rowNum=").append(_rowNum);
        sb.append(", _colNum=").append(_colNum);
        sb.append('}');
        return sb.toString();
    }
}
