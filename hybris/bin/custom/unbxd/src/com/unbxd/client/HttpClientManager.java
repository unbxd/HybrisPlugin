package com.unbxd.client;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientManager {

    private static final int CONNECTION_TIMEOUT = 30 * 1000;

    private static CloseableHttpClient _httpClient;

    private static CloseableHttpClient init(PoolingHttpClientConnectionManager poolingHCCManager) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(CONNECTION_TIMEOUT)
                .setRedirectsEnabled(false)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoKeepAlive(true)
                .setSoTimeout(CONNECTION_TIMEOUT).build();

        _httpClient = HttpClients.custom()
                .setConnectionManager(poolingHCCManager)
                .setDefaultRequestConfig(requestConfig)
                .disableRedirectHandling()
                .setDefaultSocketConfig(socketConfig)
                .setUserAgent("Unbxd/1.0.0").build();

        return _httpClient;
    }

    public static CloseableHttpClient getHttpClient(PoolingHttpClientConnectionManager cm) {
        if (cm == null) {
            cm = ConnectionManager.getConnectionManager();
        }

        if(_httpClient == null){
            synchronized (HttpClientManager.class){
                if(_httpClient == null){
                    return init(cm);
                }
            }
        }

        return _httpClient;
    }
}
