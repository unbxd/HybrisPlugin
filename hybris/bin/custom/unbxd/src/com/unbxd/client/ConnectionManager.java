package com.unbxd.client;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class ConnectionManager {

    private static final int DEFAULT_MAX_HTTP_CONNECTIONS = 10;
    private static final int MAX_TOTAL_HTTP = 20;

    private static PoolingHttpClientConnectionManager __connectionManager;

    public static PoolingHttpClientConnectionManager getConnectionManager() {
        if(__connectionManager == null){
            synchronized (ConnectionManager.class){
                if(__connectionManager == null){

                    Registry registry = RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", SSLConnectionSocketFactory.getSocketFactory())
                            .build();

                    __connectionManager = new PoolingHttpClientConnectionManager(registry);

                    __connectionManager.setMaxTotal(MAX_TOTAL_HTTP);
                    __connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_HTTP_CONNECTIONS);
                }
            }
        }

        return __connectionManager;
    }
}
