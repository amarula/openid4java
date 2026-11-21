/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.consumer;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.ExpiryPolicy;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * @author Marius Scurtescu, Johnny Bufu
 */
public class EhcacheNonceVerifierTest extends AbstractNonceVerifierTest
{
    private CacheManager _cacheManager;
    private int _cacheCount;

    public EhcacheNonceVerifierTest(String name)
    {
        super(name);
    }

    public void setUp() throws Exception
    {
        _cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

        super.setUp();
    }

    public void tearDown() throws Exception
    {
        super.tearDown();

        if (_cacheManager != null)
        {
            _cacheManager.close();
        }
        _cacheManager = null;
    }

    public NonceVerifier createVerifier(int maxAge)
    {
        ExpiryPolicy<String, String> expiryPolicy = new ExpiryPolicy<String, String>()
        {
            public Duration getExpiryForCreation(String key, String value)
            {
                return Duration.ofSeconds(maxAge);
            }

            public Duration getExpiryForAccess(String key, Supplier<? extends String> valueSupplier)
            {
                return Duration.ofSeconds(maxAge);
            }

            public Duration getExpiryForUpdate(String key, Supplier<? extends String> oldValueSupplier, String newValue)
            {
                return Duration.ofSeconds(maxAge);
            }
        };

        CacheConfiguration<String, String> configuration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, String.class,
                        ResourcePoolsBuilder.heap(100))
                .withExpiry(expiryPolicy)
                .build();

        Cache<String, String> cache = _cacheManager.createCache(
                "testCache" + _cacheCount++, configuration);

        EhcacheNonceVerifier nonceVerifier = new EhcacheNonceVerifier(maxAge);
        nonceVerifier.setCache(cache);

        return nonceVerifier;
    }

    public static Test suite()
    {
        return new TestSuite(EhcacheNonceVerifierTest.class);
    }
}
