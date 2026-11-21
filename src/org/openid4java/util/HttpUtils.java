/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.util;

import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;

import java.util.Iterator;
import java.util.Map;

public final class HttpUtils
{
    private HttpUtils()
    {
        // empty
    }

    public static void dispose(final ClassicHttpResponse response)
    {
        if (response != null)
        {
            dispose(response.getEntity());
        }
    }

    public static void dispose(final HttpEntity entity)
    {
        if (entity != null)
        {
            EntityUtils.consumeQuietly(entity);
        }
    }

    public static void setRequestOptions(HttpUriRequestBase request,
                                         HttpRequestOptions requestOptions)
    {
        setRequestOptions(request, requestOptions, true);
    }

    public static void setRequestOptions(HttpUriRequestBase request,
                                         HttpRequestOptions requestOptions,
                                         boolean redirectsEnabled)
    {
        RequestConfig config = RequestConfig.custom()
                .setMaxRedirects(requestOptions.getMaxRedirects())
                .setCircularRedirectsAllowed(requestOptions.getAllowCircularRedirects())
                .setResponseTimeout(Timeout.ofMilliseconds(requestOptions.getSocketTimeout()))
                .setRedirectsEnabled(redirectsEnabled)
                .build();
        request.setConfig(config);

        Map requestHeaders = requestOptions.getRequestHeaders();
        if (requestHeaders != null)
        {
            Iterator iter = requestHeaders.keySet().iterator();
            String headerName;
            while (iter.hasNext())
            {
                headerName = (String) iter.next();
                request.addHeader(headerName,
                    (String) requestHeaders.get(headerName));
            }
        }
    }
}
