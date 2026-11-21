/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.discovery.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openid4java.OpenIDException;
import org.openid4java.discovery.DiscoveryException;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link HtmlParser} implementation using jsoup.
 *
 * @author Sutra Zhou
 * @since 0.9.4
 */
public class JsoupHtmlParser implements HtmlParser
{
    private static final Log _log = LogFactory.getLog(JsoupHtmlParser.class);
    private static final boolean DEBUG = _log.isDebugEnabled();

    public void parseHtml(String htmlData, HtmlResult result)
            throws DiscoveryException
    {
        if (DEBUG)
            _log.debug("Parsing HTML data:\n" + htmlData);

        Document doc;
        try
        {
            doc = Jsoup.parse(htmlData == null ? "" : htmlData);
        }
        catch (Exception e)
        {
            throw new DiscoveryException("Error parsing HTML message",
                    OpenIDException.DISCOVERY_HTML_PARSE_ERROR, e);
        }

        Element head = doc.head();
        if (head == null)
            throw new DiscoveryException(
                    "HTML response must have exactly one HEAD element.",
                    OpenIDException.DISCOVERY_HTML_PARSE_ERROR);

        Elements linkElements = head.select("link");
        for (Element linkElement : linkElements)
        {
            setResult(linkElement.attr("rel"), linkElement.attr("href"), result);
        }

        if (DEBUG)
            _log.debug("HTML discovery result:\n" + result);
    }

    /**
     * Set the result from <code>rel</code> and <code>href</code> that
     * parsed from node <code>link</code>.
     *
     * @param rel
     *            the <code>rel</code>
     * @param href
     *            the <code>href</code>
     * @param result
     *            the result to set
     * @throws DiscoveryException
     *             if the value has been setted yet, that is to say, find more
     *             than one entries with the same name(attribute value of
     *             <code>rel</code>).
     */
    private void setResult(String rel, String href, HtmlResult result)
            throws DiscoveryException
    {
        List relations = Arrays.asList(rel.split(" "));

        // openid.server
        if (relations.contains("openid.server"))
        {
            if (result.getOP1Endpoint() != null)
                throw new DiscoveryException(
                        "More than one openid.server entries found",
                        OpenIDException.DISCOVERY_HTML_PARSE_ERROR);

            if (DEBUG)
                _log.debug("Found OpenID1 endpoint: " + href);

            result.setEndpoint1(href);
        }

        // openid.delegate
        if (relations.contains("openid.delegate"))
        {
            if (result.getDelegate1() != null)
                throw new DiscoveryException(
                        "More than one openid.delegate entries found",
                        OpenIDException.DISCOVERY_HTML_PARSE_ERROR);

            if (DEBUG)
                _log.debug("Found OpenID1 delegate: " + href);

            result.setDelegate1(href);
        }

        // openid2.provider
        if (relations.contains("openid2.provider"))
        {
            if (result.getOP2Endpoint() != null)
                throw new DiscoveryException(
                        "More than one openid.server entries found",
                        OpenIDException.DISCOVERY_HTML_PARSE_ERROR);

            if (DEBUG)
                _log.debug("Found OpenID2 endpoint: " + href);

            result.setEndpoint2(href);
        }

        // openid2.local_id
        if (relations.contains("openid2.local_id"))
        {
            if (result.getDelegate2() != null)
                throw new DiscoveryException(
                        "More than one openid2.local_id entries found",
                        OpenIDException.DISCOVERY_HTML_PARSE_ERROR);

            if (DEBUG)
                _log.debug("Found OpenID2 localID: " + href);

            result.setDelegate2(href);
        }
    }
}
