package com.unbxd.client.search.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 11:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class SearchResults {

    private int _resultsCount;
    private List<SearchResult> _results;

    protected SearchResults(List<Map<String, Object>> products){
        this._resultsCount = products.size();

        this._results = new ArrayList<SearchResult>();
        for(Map<String, Object> product : products){
            this._results.add(new SearchResult(product));
        }
    }

    /**
     * @return Number of results
     */
    public int getResultsCount(){
        return _resultsCount;
    }

    public SearchResult getAt(int i){
        if(i >= _resultsCount)
            return null;

        return this._results.get(i);
    }

    /**
     * @return List of products. Refer {@link SearchResult}
     */
    public List<SearchResult> getResults(){
        return _results;
    }

}
