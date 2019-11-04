package com.unbxd.client.feed.response;

import com.unbxd.jalo.UnbxdUploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FeedStatusResponse {

    private int _code;
    private String _status;
    private String _message;
    private String _uploadID;
    private String _fileName;
    private Date _timestamp;
    private List<String> _unknownSchemaFields;
    private List<FeedFieldError> _fieldErrors;
    private int _rowNum;
    private int _colNum;

    public FeedStatusResponse(Map<String, Object> response){
        if(response.get("code") != null)
            _code = (Integer) response.get("code");
        if(response.get("status") != null)
            _status = (String) response.get("status");
        if(response.get("message") != null)
            _message = (String) response.get("message");
        if(response.get("uploadId") != null)
            _uploadID = (String) response.get("uploadId");
        if(response.get("fileName") != null)
            _fileName = (String) response.get("fileName");
        if(response.get("timeStamp") != null)
            _timestamp = new Date((Long)response.get("timeStamp"));
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

    public int getCode(){
        return _code;
    }

    public String getStatus(){
        return _status;
    }

    public String getMessage(){
        return _message;
    }

    public String getUploadID(){
        return _uploadID;
    }

    public String get_fileName() { return _fileName; }

    public Date get_timestamp() { return _timestamp; }

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
        sb.append("_code=").append(_code);
        sb.append("_status=").append(_status);
        sb.append(", _message='").append(_message).append('\'');
        sb.append(", _uploadID='").append(_uploadID).append('\'');
        sb.append(", _timestamp='").append(_timestamp).append('\'');
        sb.append(", _unknownSchemaFields=").append(_unknownSchemaFields);
        sb.append(", _fieldErrors=").append(_fieldErrors);
        sb.append(", _rowNum=").append(_rowNum);
        sb.append(", _colNum=").append(_colNum);
        sb.append('}');
        return sb.toString();
    }
}
