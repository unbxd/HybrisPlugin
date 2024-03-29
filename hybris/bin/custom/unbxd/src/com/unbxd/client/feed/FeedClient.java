package com.unbxd.client.feed;

import com.unbxd.client.ConnectionManager;
import com.unbxd.client.feed.exceptions.FeedInputException;
import com.unbxd.client.feed.exceptions.FeedStatusException;
import com.unbxd.client.feed.exceptions.FeedUploadException;
import com.unbxd.client.feed.response.FeedResponse;
import com.unbxd.client.feed.response.FeedStatusResponse;

import de.hybris.platform.util.Config;
import net.minidev.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FeedClient {

    private static final Logger LOG = Logger.getLogger(FeedClient.class);

    private String siteKey;
    private String secretKey;
    private String apiKey;
    private String domain;

    private List<FeedField> _fields;
    private Map<String, FeedProduct> _addedDocs;
    private Map<String, FeedProduct> _updatedDocs;
    private Set<String> _deletedDocs;

    protected FeedClient(String siteKey, String secretKey, String apiKey,String domain) {
        this.siteKey = siteKey;
        this.secretKey = secretKey;
        this.apiKey = apiKey;
        this.domain = sanitize(domain);

        _fields = new ArrayList<FeedField>();
        _addedDocs = new HashMap<String, FeedProduct>();
        _updatedDocs = new HashMap<String, FeedProduct>();
        _deletedDocs = new HashSet<String>();

    }
    
    private String sanitize(String domain) {
    	if (domain != null && domain.endsWith("/")) {
    		return domain.substring(0, domain.lastIndexOf("/"));
    	}
    	return domain;
    }

    public List<FeedField> get_fields() {
        return _fields;
    }

    public String getFeedUrl() {
        return domain+"/api/" + siteKey + "/upload/catalog/";
    }

    public String getFeedStatusUrl() {
        return domain+"/api/" + siteKey + "/catalog/status";
    }

    public String getDeltaFeedStatusUrl() {
        return domain+"/api/" + siteKey + "/catalog/delta/status";
    }

    public String getFeedStatusUrlForUploadId(String uploadId) {
        return domain+"/api/" + siteKey + "/catalog/" + uploadId + "/status";
    }

    public String getDeltaFeedStatusUrlForUploadId(String uploadId) {
        return domain+"/api/" + siteKey + "/catalog/delta/" + uploadId + "/status";
    }

    /**
     * Adds schema for a field. Schema needs to be added only once.
     *
     * @param fieldName   Name of the field. Following rules apply for field names.
     *                    <ul>
     *                    <li>Should be alphnumeric</li>
     *                    <li>Can contain hyphens and underscores</li>
     *                    <li>Can not start and end with -- or __</li>
     *                    <li>Can not start with numbers</li>
     *                    </ul>
     * @param datatype    Datatype of the field. Refer {@link String}
     * @param multivalued True for allowing multiple values for each document
     * @param autosuggest True to include field in autosuggest response
     * @return this
     */
    public FeedClient addSchema(String fieldName, String datatype, boolean multivalued, boolean autosuggest) {
        _fields.add(new FeedField(fieldName, datatype, multivalued, autosuggest));
        return this;
    }

    /**
     * Adds schema for a field. Schema needs to be added only once.
     *
     * @param fieldName Name of the field. Following rules apply for field names.
     *                  <ul>
     *                  <li>Should be alphnumeric</li>
     *                  <li>Can contain hyphens and underscores</li>
     *                  <li>Can not start and end with -- or __</li>
     *                  <li>Can not start with numbers</li>
     *                  </ul>
     * @param datatype  Datatype of the field. Refer {@link String}
     * @return this
     */
    public FeedClient addSchema(String fieldName, String datatype) {
        _fields.add(new FeedField(fieldName, datatype, false, false));
        return this;
    }

    /**
     * Adds a product to the field. If a product with the same uniqueId is found to be already present the product will be overwritten
     *
     * @param product
     * @return this
     */
    public FeedClient addProduct(FeedProduct product) {
        _addedDocs.put(product.getUniqueId(), product);

        return this;
    }

    /**
     * Adds a list of products to the field. If a product with the same uniqueId is found to be already present the product will be overwritten
     *
     * @param products
     * @return this
     */
    public FeedClient addProducts(List<FeedProduct> products) {
        for (FeedProduct product : products) {
            this.addProduct(product);
        }

        return this;
    }

    /**
     * Add a variant to a product.
     *
     * @param parentUniqueId    Unique Id of the parent product
     * @param variantAttributes Attributes of the variant
     * @return this
     * @throws FeedInputException
     */
    public FeedClient addVariant(String parentUniqueId, Map<String, Object> variantAttributes) throws FeedInputException {
        if (!_addedDocs.containsKey(parentUniqueId)) {
            throw new FeedInputException("Parent product needs to be added");
        }

        _addedDocs.get(parentUniqueId).addVariant(variantAttributes);

        return this;
    }

    /**
     * Add variants to a product.
     *
     * @param parentUniqueId Unique Id of the parent product
     * @param variants       List of attributes of the variants
     * @return this
     * @throws FeedInputException
     */
    public FeedClient addVariants(String parentUniqueId, List<Map<String, Object>> variants) throws FeedInputException {
        for (Map<String, Object> variant : variants) {
            this.addVariant(parentUniqueId, variant);
        }

        return this;
    }

    /**
     * Upserts a product.
     *
     * @param productDelta Delta of product attributes. uniqueId is mandatory
     * @return this
     */
    public FeedClient updateProduct(FeedProduct productDelta) {
        _updatedDocs.put(productDelta.getUniqueId(), productDelta);

        return this;
    }

    /**
     * Upserts products.
     *
     * @param productsDeltas Deltas of products attributes. uniqueId is mandatory
     * @return this
     */
    public FeedClient updateProducts(List<FeedProduct> productsDeltas) {
        for (FeedProduct product : productsDeltas) {
            this.updateProduct(product);
        }

        return this;
    }

    /**
     * Deletes a product with given uniqueId
     *
     * @param uniqueId
     * @return this
     */
    public FeedClient deleteProduct(String uniqueId) {
        _deletedDocs.add(uniqueId);

        return this;
    }

    /**
     * Deletes products with given uniqueIds
     *
     * @param uniqueIds
     * @return this
     */
    public FeedClient deleteProducts(List<String> uniqueIds) {
        _deletedDocs.addAll(uniqueIds);

        return this;
    }

    /**
     * Uploads the feed to Unbxd platform. The feed is zipped before being pushed.
     * Please be realistic and don't push millions of products in a single call. Try to confine it to a few thousands. 10K would be a good number
     *
     * @param isFullImport If true it will clear the old feed entirely and upload new products. Please use with care.
     * @return {@link FeedResponse}
     * @throws FeedUploadException
     */
    public FeedResponse push(boolean isFullImport) throws FeedUploadException {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionTimeToLive(1, TimeUnit.MINUTES).setConnectionManager(ConnectionManager.getConnectionManager()).build();
        HttpClientBuilder builder = HttpClientBuilder.create();
        HttpClient httpClient1 = builder.build();

        //Document doc = new FeedFile(_fields, _addedDocs.values(), _updatedDocs.values(), _deletedDocs, _taxonomyNodes, _taxonomyMappings).getDoc();

        JSONObject json = new JsonFeedFile(_fields, _addedDocs.values(), _updatedDocs.values(), _deletedDocs).getJson();

        File file = null;
        try {
            long t = new Date().getTime();

            file = File.createTempFile(siteKey, ".json");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(json.toJSONString().getBytes());

            } finally {
                if (fos != null)
                    fos.close();
            }

            LOG.debug("Stored at : " + file.getAbsolutePath());

            String url = getFeedUrl();

            //ToDo Delta + incremental
            if (isFullImport) {
                url += "full";
            } else {
                url += "delta";
            }

            HttpPost post = new HttpPost(url);
            post.addHeader("Authorization", this.secretKey);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.addPart("file", new FileBody(file));
            entity.addTextBody("variantID","variantId");
            post.setEntity(entity.build());

            HttpResponse response = httpClient1.execute(post);

            t = new Date().getTime() - t;
            LOG.debug("Took : " + t + " millisecs");

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK || response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> map = mapper.readValue(new InputStreamReader(response.getEntity().getContent()), Map.class);
                    map.put("statusCode", response.getStatusLine().getStatusCode());
                    httpClient.close();
                    return new FeedResponse(map);

                } catch (Exception e) {
                    LOG.error("Failed to parse response", e);
                    httpClient.close();
                    throw new FeedUploadException(e);
                }
            } else {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }

                String responseText = sb.toString();

                LOG.error(responseText);
                httpClient.close();
                throw new FeedUploadException(responseText);
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new FeedUploadException(e);
        } finally {
            if (null != file && Config.getBoolean("unbxd.feed.deletefile", true)) {
                file.delete();
            }
        }
    }

    /**
     * get status of the upload feed from Unbxd platform.

     * @param count coint of last N status to be retrieved.
     * @param isDelta If true it will get last N delta upload status else it will get last N full upload
     * @return {@link List<FeedStatusResponse>}
     * @throws FeedStatusException
     */
    public List<FeedStatusResponse> getStatus(int count, boolean isDelta) throws FeedStatusException {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionTimeToLive(1, TimeUnit.MINUTES).setConnectionManager(ConnectionManager.getConnectionManager()).build();
        HttpClientBuilder builder = HttpClientBuilder.create();
        HttpClient httpClient1 = builder.build();

        try {
            long t = new Date().getTime();
            String url = getFeedStatusUrl();

            if (isDelta) {
                url = getDeltaFeedStatusUrl();
            }

            if(count > 1) {
                url += "?count=" + count;
            } else {
                url += "?count=1";
            }

            HttpGet get = new HttpGet(url);
            get.addHeader("Authorization", this.secretKey);
            HttpResponse response = httpClient1.execute(get);

            t = new Date().getTime() - t;
            LOG.debug("Took : " + t + " millisecs");

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    List<Map<String, Object>> map = mapper.readValue(new InputStreamReader(response.getEntity().getContent()), List.class);
                    List<FeedStatusResponse> result = map.stream().map(m -> new FeedStatusResponse(m)).collect(Collectors.toList());
                    httpClient.close();
                    return result;

                } catch (Exception e) {
                    LOG.error("Failed to parse response", e);
                    httpClient.close();
                    throw new FeedStatusException(e);
                }
            } else {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }

                String responseText = sb.toString();

                LOG.error(responseText);
                httpClient.close();
                throw new FeedStatusException(responseText);
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new FeedStatusException(e);
        }
    }

    /**
     * get status of the upload feed from Unbxd platform.

     * @param uploadId upload id of feed.
     * @return {@link FeedStatusResponse}
     * @throws FeedStatusException
     */
    public FeedStatusResponse getStatus(String uploadId, boolean isDelta) throws FeedStatusException {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionTimeToLive(1, TimeUnit.MINUTES).setConnectionManager(ConnectionManager.getConnectionManager()).build();
        HttpClientBuilder builder = HttpClientBuilder.create();
        HttpClient httpClient1 = builder.build();

        try {
            long t = new Date().getTime();
            String url = getFeedStatusUrlForUploadId(uploadId);

            if (isDelta) {
                url = getDeltaFeedStatusUrlForUploadId(uploadId);
            }

            HttpGet get = new HttpGet(url);
            get.addHeader("Authorization", this.secretKey);
            HttpResponse response = httpClient1.execute(get);

            t = new Date().getTime() - t;
            LOG.debug("Took : " + t + " millisecs");

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> map = mapper.readValue(new InputStreamReader(response.getEntity().getContent()), Map.class);
                    httpClient.close();
                    return new FeedStatusResponse(map);

                } catch (Exception e) {
                    LOG.error("Failed to parse response", e);
                    httpClient.close();
                    throw new FeedStatusException(e);
                }
            } else {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }

                String responseText = sb.toString();

                LOG.error(responseText);
                httpClient.close();
                throw new FeedStatusException(responseText);
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new FeedStatusException(e);
        }
    }

    /**
     * get status of the full upload feed from Unbxd platform.

     * @param uploadId upload id of feed.
     * @return {@link FeedStatusResponse}
     * @throws FeedStatusException
     */
    public FeedStatusResponse getFullStatus(String uploadId) throws FeedStatusException {
        return getStatus(uploadId, false);
    }

    /**
     * get status of the delta upload feed from Unbxd platform.

     * @param uploadId upload id of feed.
     * @return {@link FeedStatusResponse}
     * @throws FeedStatusException
     */
    public FeedStatusResponse getDeltaStatus(String uploadId) throws FeedStatusException {
        return getStatus(uploadId, true);
    }


}
