package com.unbxd.client.search.response;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class BucketResult {

    private int _totalResultsCount;
    private SearchResults _results;

    protected BucketResult(Map<String, Object> params){
        this._totalResultsCount = (Integer) params.get("numberOfProducts");
        this._results = new SearchResults((List<Map<String, Object>>) params.get("products"));
    }

    /**
     * @return Total number of results found.
     */
    public int getTotalResultsCount(){
        return this._totalResultsCount;
    }

    /**
     * @return Results in this bucket. Refer {@link SearchResults}
     */
    public SearchResults getResults(){
        return this._results;
    }

}
