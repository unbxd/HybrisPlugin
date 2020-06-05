package com.unbxd.client.autosuggest;

import com.unbxd.client.autosuggest.exceptions.AutoSuggestException;
import com.unbxd.client.autosuggest.response.AutoSuggestResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 5:32 PM
 *
 * Client class for calling AutoSuggest APIs
 */
public class AutoSuggestClient {

    private static final Logger LOG = Logger.getLogger(AutoSuggestClient.class);

    private static final String __encoding = "UTF-8";

    private String siteKey;
    private String apiKey;
    private boolean secure;

    private String query;
    private int inFieldsCount;
    private int popularProductsCount;
    private int keywordSuggestionsCount;
    private int topQueriesCount;

    private CloseableHttpClient httpClient;


    protected AutoSuggestClient(String siteKey, String apiKey, boolean secure, CloseableHttpClient httpClient) {
        this.siteKey = siteKey;
        this.apiKey = apiKey;
        this.secure = secure;

        this.inFieldsCount = -1;
        this.popularProductsCount = -1;
        this.keywordSuggestionsCount = -1;
        this.topQueriesCount = -1;

        this.httpClient = httpClient;
    }

    private String getAutoSuggestUrl(){
        return (secure ? "https://" : "http://") + "search.unbxdapi.com/" + apiKey + "/" + siteKey + "/autosuggest?wt=json";
    }

    /**
     * Gets autosuggest results for query
     * @param query
     * @return this
     */
    public AutoSuggestClient autosuggest(String query){
        this.query = query;

        return this;
    }

    /**
     * Sets number of in_fields to be returned in results
     * @param inFieldsCount
     * @return this
     */
    public AutoSuggestClient setInFieldsCount(int inFieldsCount) {
        this.inFieldsCount = inFieldsCount;

        return this;
    }

    /**
     * Sets number of popular products to be returned in results
     * @param popularProductsCount
     * @return this
     */
    public AutoSuggestClient setPopularProductsCount(int popularProductsCount) {
        this.popularProductsCount = popularProductsCount;

        return this;
    }

    /**
     * Sets number of keyword suggestions to be returned in results
     * @param keywordSuggestionsCount
     * @return this
     */
    public AutoSuggestClient setKeywordSuggestionsCount(int keywordSuggestionsCount) {
        this.keywordSuggestionsCount = keywordSuggestionsCount;

        return this;
    }

    /**
     * Sets number of popular queries to be returned in results
     * @param topQueriesCount
     * @return this
     */
    public AutoSuggestClient setTopQueriesCount(int topQueriesCount) {
        this.topQueriesCount = topQueriesCount;

        return this;
    }

    private String generateUrl() throws AutoSuggestException {
        try {
            StringBuffer sb = new StringBuffer();

            if(query != null){
                sb.append(this.getAutoSuggestUrl());
                sb.append("&q=" + URLEncoder.encode(query, __encoding));

            }

            if(inFieldsCount != -1){
                sb.append("&inFields.count=" + URLEncoder.encode(inFieldsCount + "", __encoding));
            }

            if(popularProductsCount != -1){
                sb.append("&popularProducts.count=" + URLEncoder.encode(popularProductsCount + "", __encoding));
            }

            if(keywordSuggestionsCount != -1){
                sb.append("&keywordSuggestions.count=" + URLEncoder.encode(keywordSuggestionsCount + "", __encoding));
            }

            if(topQueriesCount != -1){
                sb.append("&topQueries.count=" + URLEncoder.encode(topQueriesCount + "", __encoding));
            }

            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            LOG.error("Encoding error", e);
            throw new AutoSuggestException(e);
        }
    }

    /**
     * Executes Auto Suggest Query.
     *
     * @return {@link AutoSuggestResponse}
     * @throws AutoSuggestException
     */
    public AutoSuggestResponse execute() throws AutoSuggestException {
        String url = this.generateUrl();

        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(get);
            if(response.getStatusLine().getStatusCode() == 200){
                Map<String, Object> responseObject = new ObjectMapper().readValue(new InputStreamReader(response.getEntity().getContent()), Map.class);
                return new AutoSuggestResponse(responseObject);
            } else {
                StringBuffer sb = new StringBuffer();
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }

                String responseText = sb.toString();

                LOG.error(responseText);
                throw new AutoSuggestException(responseText);
            }
        } catch (JsonParseException e) {
            LOG.error(e.getMessage(), e);
            throw new AutoSuggestException(e);
        } catch (JsonMappingException e) {
            LOG.error(e.getMessage(), e);
            throw new AutoSuggestException(e);
        } catch (ClientProtocolException e) {
            LOG.error(e.getMessage(), e);
            throw new AutoSuggestException(e);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new AutoSuggestException(e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

}
