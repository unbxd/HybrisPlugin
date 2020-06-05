package com.unbxd.client.recommendations.response;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 6:17 PM
 *
 * Corresponds to recommendations API response
 */
public class RecommendationResponse {

    private int _statusCode;
    private int _errorCode;
    private String _message;
    private int _queryTime;
    private int _totalResultsCount;
    private RecommendationResults _results;

    public RecommendationResponse(Map<String, Object> params) {
        if(params.get("error") != null){
            Map<String, Object> error = (Map<String, Object>) params.get("error");
            this._errorCode = (Integer) error.get("code");
            this._message = (String) error.get("message");
        }else{
            this._message = "OK";

            this._statusCode = (Integer) params.get("status");
            this._queryTime = (Integer) params.get("queryTime");

            this._totalResultsCount = (Integer) params.get("count");

            if(params.containsKey("Recommendations")){
                this._results = new RecommendationResults((List<Map<String, Object>>) params.get("Recommendations"));
            }
        }
    }

    /**
     * @return Status Code. 200 if OK.
     */
    public int getStatusCode(){
        return this._statusCode;
    }

    /**
     * @return Error code in case of an error.
     */
    public int getErrorCode(){
        return this._errorCode;
    }

    /**
     * @return OK if successfull. Error message otherwise
     */
    public String getMessage(){
        return this._message;
    }

    /**
     * @return Time taken to query results in milliseconds
     */
    public int getQueryTime(){
        return this._queryTime;
    }

    /**
     * @return Number of results in the response.
     */
    public int getTotalResultsCount(){
        return this._totalResultsCount;
    }

    /**
     * @return Results. Refer {@link RecommendationResults}
     */
    public RecommendationResults getResults(){
        return this._results;
    }

}
