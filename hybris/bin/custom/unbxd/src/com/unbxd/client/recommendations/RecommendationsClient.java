package com.unbxd.client.recommendations;

import com.unbxd.client.recommendations.exceptions.RecommendationsException;
import com.unbxd.client.recommendations.response.RecommendationResponse;
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
 * Time: 6:09 PM
 *
 * Client class for calling Recommendation APIs.
 *
 */
public class RecommendationsClient {

    private static final Logger LOG = Logger.getLogger(RecommendationsClient.class);

    private static final String __encoding = "UTF-8";

    private enum RecommenderBoxType {
        ALSO_VIEWED,
        ALSO_BOUGHT,
        RECENTLY_VIEWED,
        RECOMMENDED_FOR_YOU,
        MORE_LIKE_THESE,
        TOP_SELLERS,
        CATEGORY_TOP_SELLERS,
        BRAND_TOP_SELLERS,
        PDP_TOP_SELLERS,
        CART_RECOMMEND
    }

    private String siteKey;
    private String apiKey;
    private boolean secure;

    private RecommenderBoxType _boxType;
    private String uid;
    private String ip;
    private String uniqueId;
    private String category;
    private String brand;

    private CloseableHttpClient httpClient;

    public RecommendationsClient(String siteKey, String apiKey, boolean secure, CloseableHttpClient httpClient) {
        this.siteKey = siteKey;
        this.apiKey = apiKey;
        this.secure = secure;
        this.httpClient = httpClient;
    }

    private String getRecommendationUrl(){
        return (secure ? "https://" : "http://") + "recommendations.unbxdapi.com/v1.0/" + apiKey + "/" + siteKey + "/";
    }

    /**
     * Get Recently viewed items for the user : uid
     * @param uid value of the cookie : "unbxd.userId"
     * @return this
     */
    public RecommendationsClient getRecentlyViewed(String uid,String ip){
        this._boxType = RecommenderBoxType.RECENTLY_VIEWED;
        this.uid = uid;
        this.ip = ip;

        return this;
    }

    /**
     * Get products recommended for user : uid
     * @param uid value of the cookie : "unbxd.userId"
     * @param ip IP address if the user for localization of results
     * @return this
     */
    public RecommendationsClient getRecommendedForYou(String uid, String ip){
        this._boxType = RecommenderBoxType.RECOMMENDED_FOR_YOU;
        this.uid = uid;
        this.ip = ip;

        return this;
    }

    /**
     * Get More products like product : uniqueId
     * @param uniqueId Unique Id of the product
     * @param uid value of the cookie : "unbxd.userId"
     * @return this
     */
    public RecommendationsClient getMoreLikeThis(String uniqueId, String uid,String ip){
        this._boxType = RecommenderBoxType.MORE_LIKE_THESE;
        this.uid = uid;
        this.uniqueId = uniqueId;
        this.ip = ip;

        return this;
    }

    /**
     * Get products which were also viewed by users who viewed the product : uniqueId
     * @param uniqueId Unique Id of the product
     * @param uid value of the cookie : "unbxd.userId"
     * @return this
     */
    public RecommendationsClient getAlsoViewed(String uniqueId, String uid,String ip){
        this._boxType = RecommenderBoxType.ALSO_VIEWED;
        this.uid = uid;
        this.uniqueId = uniqueId;
        this.ip = ip;

        return this;
    }

    /**
     * Get products which were also bought by users who bought the product : uniqueId
     * @param uniqueId Unique Id of the product
     * @param uid value of the cookie : "unbxd.userId"
     * @return this
     */
    public RecommendationsClient getAlsoBought(String uniqueId, String uid, String ip){
        this._boxType = RecommenderBoxType.ALSO_BOUGHT;
        this.uid = uid;
        this.uniqueId = uniqueId;
        this.ip = ip;

        return this;
    }

    /**
     * Get Top Selling products
     * @param uid value of the cookie : "unbxd.userId"
     * @param ip IP address if the user for localization of results
     * @return this
     */
    public RecommendationsClient getTopSellers(String uid, String ip){
        this._boxType = RecommenderBoxType.TOP_SELLERS;
        this.uid = uid;
        this.ip = ip;

        return this;
    }

    /**
     * Get Top Selling products within this category
     * @param category name of the category
     * @param uid value of the cookie : "unbxd.userId"
     * @param ip IP address if the user for localization of results
     * @return this
     */
    public RecommendationsClient getCategoryTopSellers(String category, String uid, String ip){
        this._boxType = RecommenderBoxType.CATEGORY_TOP_SELLERS;
        this.uid = uid;
        this.ip = ip;
        this.category = category;

        return this;
    }

    /**
     * Get Top Selling products within this brand
     * @param brand name of the brand
     * @param uid value of the cookie : "unbxd.userId"
     * @param ip IP address if the user for localization of results
     * @return this
     */
    public RecommendationsClient getBrandTopSellers(String brand, String uid, String ip){
        this._boxType = RecommenderBoxType.BRAND_TOP_SELLERS;
        this.uid = uid;
        this.ip = ip;
        this.brand = brand;

        return this;
    }

    /**
     * Get Top Selling products among products similar to this product
     * @param uniqueId Unique Id of the product
     * @param uid value of the cookie : "unbxd.userId"
     * @param ip IP address if the user for localization of results
     * @return this
     */
    public RecommendationsClient getPDPTopSellers(String uniqueId, String uid, String ip){
        this._boxType = RecommenderBoxType.PDP_TOP_SELLERS;
        this.uid = uid;
        this.ip = ip;
        this.uniqueId = uniqueId;

        return this;
    }

    /**
     * Get recommendations based on the products added in cart by the user : uid
     * @param uid value of the cookie : "unbxd.userId"
     * @param ip IP address if the user for localization of results
     * @return this
     */
    public RecommendationsClient getCartRecommendations(String uid, String ip){
        this._boxType = RecommenderBoxType.CART_RECOMMEND;
        this.uid = uid;
        this.ip = ip;

        return this;
    }

    private String generateUrl() throws RecommendationsException {
        try {
            StringBuffer sb = new StringBuffer();

            if(_boxType != null){
                sb.append(this.getRecommendationUrl());

                if(_boxType.equals(RecommenderBoxType.ALSO_VIEWED)){
                    sb.append("also-viewed/" + URLEncoder.encode(uniqueId, __encoding) + "?format=json");
                }else if(_boxType.equals(RecommenderBoxType.ALSO_BOUGHT)){
                    sb.append("also-bought/" + URLEncoder.encode(uniqueId, __encoding) + "?format=json");
                }else if(_boxType.equals(RecommenderBoxType.RECENTLY_VIEWED)){
                    sb.append("recently-viewed/" + URLEncoder.encode(uid, __encoding) + "?format=json");
                }else if(_boxType.equals(RecommenderBoxType.RECOMMENDED_FOR_YOU)){
                    sb.append("recommend/" + URLEncoder.encode(uid, __encoding) + "?format=json");
                }else if(_boxType.equals(RecommenderBoxType.MORE_LIKE_THESE)){
                    sb.append("more-like-these/" + URLEncoder.encode(uniqueId, __encoding) + "?format=json");
                }else if(_boxType.equals(RecommenderBoxType.TOP_SELLERS)){
                    sb.append("top-sellers/" + "?format=json");
                }else if(_boxType.equals(RecommenderBoxType.CATEGORY_TOP_SELLERS)){
                    sb.append("category-top-sellers/" + URLEncoder.encode(category, __encoding) + "?format=json");
                }else if(_boxType.equals(RecommenderBoxType.BRAND_TOP_SELLERS)){
                    sb.append("brand-top-sellers/" + URLEncoder.encode(brand, __encoding) + "?format=json");
                }else if(_boxType.equals(RecommenderBoxType.PDP_TOP_SELLERS)){
                    sb.append("pdp-top-sellers/" + URLEncoder.encode(uniqueId, __encoding) + "?format=json");
                }else if(_boxType.equals(RecommenderBoxType.CART_RECOMMEND)){
                    sb.append("cart-recommend/" + URLEncoder.encode(uid, __encoding) + "?format=json");
                }

                if(uid != null)
                    sb.append("&uid=" + URLEncoder.encode(uid, __encoding));

                if(ip != null)
                    sb.append("&ip=" + URLEncoder.encode(ip, __encoding));
            }else{
                throw new RecommendationsException("Couldn't determine which recommendation widget to call.");
            }

            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            LOG.error("Encoding error", e);
            throw new RecommendationsException(e);
        }
    }

    /**
     * Executes a recommendations call
     * @return {@link RecommendationResponse}
     * @throws RecommendationsException
     */
    public RecommendationResponse execute() throws RecommendationsException {

        String url = this.generateUrl();

        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(get);
            if(response.getStatusLine().getStatusCode() == 200){
                Map<String, Object> responseObject = new ObjectMapper().readValue(new InputStreamReader(response.getEntity().getContent()), Map.class);
                return new RecommendationResponse(responseObject);
            } else {
                StringBuffer sb = new StringBuffer();
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }

                String responseText = sb.toString();

                LOG.error(responseText);
                throw new RecommendationsException(responseText);
            }
        } catch (JsonParseException e) {
            LOG.error(e.getMessage(), e);
            throw new RecommendationsException(e);
        } catch (JsonMappingException e) {
            LOG.error(e.getMessage(), e);
            throw new RecommendationsException(e);
        } catch (ClientProtocolException e) {
            LOG.error(e.getMessage(), e);
            throw new RecommendationsException(e);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new RecommendationsException(e);
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
