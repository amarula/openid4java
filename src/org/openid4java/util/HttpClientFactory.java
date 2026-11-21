/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

/*
 * Created on Mar 5, 2007
 */
package org.openid4java.util;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * This class handles all HTTPClient connections for the
 * org.openid4java packages.
 *
 * @author Kevin
 */
public class HttpClientFactory
{
    private HttpClientFactory() {}

    /**
     * proxy properties for HTTPClient calls
     */
    private static ProxyProperties proxyProperties = null;

    private static boolean multiThreadedHttpClient = true;

    public static ProxyProperties getProxyProperties()
    {
        return proxyProperties;
    }

    public static void setProxyProperties(ProxyProperties proxyProperties)
    {
        HttpClientFactory.proxyProperties = proxyProperties;
    }

    public static boolean isMultiThreadedHttpClient() {
        return multiThreadedHttpClient;
    }

    /**
     * Configures the type of HttpClient's constructed by the factory.
     *
     * @param multiThreadedHttpClient if true, a pooling connection manager is
     *                                constructed; otherwise a single-connection
     *                                manager is used.
     */
    public static void setMultiThreadedHttpClient(boolean multiThreadedHttpClient) {
        HttpClientFactory.multiThreadedHttpClient = multiThreadedHttpClient;
    }

    public static CloseableHttpClient getInstance(int maxRedirects,
            Boolean allowCircularRedirects,
            int connTimeout, int socketTimeout,
            String cookiePolicy)
    {
        return getInstance(maxRedirects, allowCircularRedirects, connTimeout,
                socketTimeout, cookiePolicy, null, null);
    }

    public static CloseableHttpClient getInstance(int maxRedirects,
                                         Boolean allowCircularRedirects,
                                         int connTimeout, int socketTimeout,
                                         String cookiePolicy, SSLContext sslContext,
                                         HostnameVerifier hostnameVerifier)
    {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();

        registryBuilder.register("http", PlainConnectionSocketFactory.getSocketFactory());

        SSLConnectionSocketFactory sslSocketFactory;
        if (sslContext == null)
        {
            if (hostnameVerifier == null)
            {
                sslSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
            }
            else
            {
                sslSocketFactory = new SSLConnectionSocketFactory(
                        (SSLSocketFactory) SSLSocketFactory.getDefault(),
                        hostnameVerifier);
            }
        }
        else if (hostnameVerifier == null)
        {
            sslSocketFactory = new SSLConnectionSocketFactory(sslContext);
        }
        else
        {
            sslSocketFactory = new SSLConnectionSocketFactory(
                    sslContext, hostnameVerifier);
        }
        registryBuilder.register("https", sslSocketFactory);

        Registry<ConnectionSocketFactory> registry = registryBuilder.build();

        HttpClientConnectionManager connManager;
        if (multiThreadedHttpClient)
            connManager = new PoolingHttpClientConnectionManager(registry);
        else
            connManager = new BasicHttpClientConnectionManager(registry);

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setMaxRedirects(maxRedirects)
                .setCircularRedirectsAllowed(Boolean.TRUE.equals(allowCircularRedirects))
                .setConnectTimeout(Timeout.ofMilliseconds(connTimeout))
                .setResponseTimeout(Timeout.ofMilliseconds(socketTimeout));

        if (cookiePolicy != null)
        {
            requestConfigBuilder.setCookieSpec(cookiePolicy);
        }

        HttpClientBuilder clientBuilder = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfigBuilder.build());

        if (cookiePolicy == null)
        {
            clientBuilder.disableCookieManagement();
        }

        if (proxyProperties != null)
        {
            HttpHost proxy = new HttpHost(
                    proxyProperties.getProxyHostName(),
                    proxyProperties.getProxyPort());

            clientBuilder.setProxy(proxy);

            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            AuthScope authScope = new AuthScope(
                    proxyProperties.getProxyHostName(),
                    proxyProperties.getProxyPort());
            credentialsProvider.setCredentials(authScope,
                    proxyProperties.getCredentials());
            clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        }

        return clientBuilder.build();
    }
}
