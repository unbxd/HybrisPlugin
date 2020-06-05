package com.unbxd.client.search.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 10:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class SearchResponse {

    private int _statusCode;
    private int _errorCode;
    private String _message;
    private int _queryTime;
    private int _totalResultsCount;
    private SearchResults _results;
    private BucketResults _buckets;
    private Facets _facets;
    private Stats _stats;
    private List<String> _spellCorrections;
    private Banners _banner;
    private RedirectData redirect;

    public SearchResponse(Map<String, Object> params){
        if(params.containsKey("error")){
            Map<String, Object> error = (Map<String, Object>) params.get("error");
            this._errorCode = (Integer) error.get("code");
            this._message = (String) error.get("msg");
        }else{
            this._message = "OK";

            Map<String, Object> metaData = (Map<String, Object>) params.get("searchMetaData");

            this._statusCode = (Integer) metaData.get("status");

            if(metaData.containsKey("queryTime")) {
                this._queryTime = (Integer) metaData.get("queryTime");
            }

            if(params.containsKey("response")){
                Map<String, Object> response = (Map<String, Object>) params.get("response");
                this._totalResultsCount = (Integer) response.get("numberOfProducts");
                this._results = new SearchResults((List<Map<String, Object>>) response.get("products"));
            }

            if(params.containsKey("buckets")){
                Map<String, Object> response = (Map<String, Object>) params.get("buckets");
                this._totalResultsCount = (Integer) response.get("totalProducts");
                this._buckets = new BucketResults(response);
            }

            if(params.containsKey("facets")){
                Map<String, Object> facets = (Map<String, Object>) params.get("facets");
                this._facets = new Facets(facets);
            }

            if(params.containsKey("banner")){
                Map<String, Object> banners = (Map<String, Object>) params.get("banner");
                this._banner = new Banners(banners);
            }

            if(params.containsKey("stats")){
                Map<String, Object> stats = (Map<String, Object>) params.get("stats");
                this._stats = new Stats(stats);
            }

            if(params.containsKey("didYouMean")){
                this._spellCorrections = new ArrayList<String>();
                List<Map<String, Object>> dym = (List<Map<String, Object>>) params.get("didYouMean");
                for(Map<String, Object> suggestion : dym){
                    _spellCorrections.add((String) suggestion.get("suggestion"));
                }
            }

            if(params.containsKey("redirect")) {
                Map<String, Object> redirect_params = (Map<String, Object>) params.get("redirect");
                if(redirect_params.containsKey("type") && redirect_params.containsKey("value")) {
                    this.redirect = new RedirectData((String) redirect_params.get("type"), (String) redirect_params.get("value"));
                }
            }
        }
    }

    /**
     * @return  Status Code. 200 if OK.
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
     * @return Total number of results found.
     */
    public int getTotalResultsCount(){
        return this._totalResultsCount;
    }

    /**
     * @return Results. Refer {@link SearchResults}
     */
    public SearchResults getResults(){
        return this._results;
    }

    /**
     * @return Facets. Refer {@link Facets}
     */
    public Facets getFacets(){
        return this._facets;
    }

    /**
     * @return Stats. Refer {@link Stats}
     */
    public Stats getStats(){
        return this._stats;
    }

    /**
     * @return List of spell corrections in the order of relevance
     */
    public List<String> getSpellCorrections(){
        return this._spellCorrections;
    }

    /**
      * @return Stats. Refer {@link Banner}
     */
    public List<Banner> getBanners() {
        return _banner.getBanner();
    }

    public ArrayList<String> getAppliedCategory() { return _banner.getCategories();}


    /**
     * @return Bucketed Response. Refer {@link BucketResults}
     */

    public BucketResults getBuckets(){
        return this._buckets;
    }

    public RedirectData getRedirect() {
        return this.redirect;
    }
}
