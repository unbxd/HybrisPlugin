package com.unbxd.client.recommendations.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class RecommendationResults {

    private int _resultsCount;
    private List<RecommendationResult> _results;

    protected RecommendationResults(List<Map<String, Object>> params) {
        this._resultsCount = params.size();

        this._results = new ArrayList<RecommendationResult>();
        for(Map<String, Object> result : params){
            this._results.add(new RecommendationResult(result));
        }
    }

    /**
     * @return Number of results
     */
    public int getResultsCount(){
        return this._resultsCount;
    }

    public RecommendationResult getAt(int i){
        if(i >= _resultsCount)
            return null;

        return this._results.get(i);
    }

    /**
     * @return List of products. Refer {@link RecommendationResult}
     */
    public List<RecommendationResult> getResults(){
        return this._results;
    }

}
