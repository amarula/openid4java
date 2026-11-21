/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.consumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ehcache.Cache;
import org.ehcache.expiry.ExpiryPolicy;

import java.time.Duration;
import java.util.Date;

/**
 * @author Marius Scurtescu, Johnny Bufu
 */
public class EhcacheNonceVerifier extends AbstractNonceVerifier
{
    private static Log _log = LogFactory.getLog(EhcacheNonceVerifier.class);
    private static final boolean DEBUG = _log.isDebugEnabled();

    private Cache<String, String> _cache;

    public EhcacheNonceVerifier(int maxAge)
    {
        super(maxAge);
    }

    public void setCache(Cache<String, String> cache)
    {
        @SuppressWarnings("unchecked")
        ExpiryPolicy<String, String> expiryPolicy =
                (ExpiryPolicy<String, String>) cache.getRuntimeConfiguration().getExpiryPolicy();

        long timeToLive = getSeconds(expiryPolicy.getExpiryForCreation("key", "value"));
        long timeToIdle = getSeconds(expiryPolicy.getExpiryForAccess("key", () -> "value"));

        if (timeToLive != _maxAgeSeconds)
        {
            throw new IllegalArgumentException("Max Age: " + _maxAgeSeconds + ", same expected for cache, but found: " + timeToLive);
        }

        if (timeToLive != timeToIdle)
        {
            throw new IllegalArgumentException("Cache must have same timeToLive (" + timeToLive + ") as timeToIdle (" + timeToIdle + ")");
        }

        _cache = cache;
    }

    private static long getSeconds(Duration duration)
    {
        if (duration == null)
        {
            throw new IllegalArgumentException("Cache expiry policy must define a duration");
        }

        return duration.getSeconds();
    }

    protected int seen(Date now, String opUrl, String nonce)
    {
        String pair = opUrl + '#' + nonce;

        if (_cache.get(pair) != null)
        {
            _log.error("Possible replay attack! Already seen nonce: " + nonce);
            return SEEN;
        }

        _cache.put(pair, pair);

        if (DEBUG) _log.debug("Nonce verified: " + nonce);

        return OK;
    }
}
