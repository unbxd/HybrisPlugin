package com.unbxd.client.autosuggest.response;

import com.unbxd.client.autosuggest.AutoSuggestType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class AutoSuggestResultSection {

    private AutoSuggestType _type;
    private int _resultsCount;
    private List<AutoSuggestResult> _results;

    public AutoSuggestResultSection(AutoSuggestType type) {
        _type = type;

        this._results = new ArrayList<AutoSuggestResult>();
    }

    protected void addResult(Map<String, Object> params){
        this._results.add(new AutoSuggestResult(params));
        this._resultsCount++;
    }

    /**
     * @return {@link AutoSuggestType}
     */
    public AutoSuggestType getType(){
        return this._type;
    }

    /**
     * @return Number of results
     */
    public int getResultsCount(){
        return this._resultsCount;
    }

    public AutoSuggestResult getAt(int i){
        if(i >= _resultsCount)
            return null;

        return this._results.get(i);
    }

    /**
     * @return List of Auto suggest results. Refer {@link AutoSuggestResult}
     */
    public List<AutoSuggestResult> getResults(){
        return this._results;
    }

}
