/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.discovery.yadis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openid4java.OpenIDException;
import org.openid4java.util.HtmlUtils;

/**
 * A {@link YadisHtmlParser} implementation using jsoup.
 *
 * @author Sutra Zhou
 * @since 0.9.4
 */
public class JsoupYadisHtmlParser implements YadisHtmlParser
{
    private static final Log _log = LogFactory.getLog(JsoupYadisHtmlParser.class);
    private static final boolean DEBUG = _log.isDebugEnabled();

    public String getHtmlMeta(String input) throws YadisException
    {
        if (DEBUG)
            _log.debug("Parsing HTML data:\n" + input);

        int headCount = HtmlUtils.countTopLevelHeads(input);
        if (headCount != 1)
            throw new YadisException(
                    "HTML response must have exactly one HEAD element, "
                            + "found " + headCount,
                    OpenIDException.YADIS_HTMLMETA_INVALID_RESPONSE);

        Document doc;
        try
        {
            doc = Jsoup.parse(input == null ? "" : input);
        }
        catch (Exception e)
        {
            throw new YadisException("Error parsing HTML message",
                    OpenIDException.YADIS_HTMLMETA_INVALID_RESPONSE, e);
        }

        Element head = doc.head();
        if (head == null)
            throw new YadisException(
                    "HTML response must have exactly one HEAD element.",
                    OpenIDException.YADIS_HTMLMETA_INVALID_RESPONSE);

        String xrdsLocation = null;
        Elements metaElements = head.select("meta");

        for (Element metaElement : metaElements)
        {
            String httpEquiv = metaElement.attr("http-equiv");
            if (YadisResolver.YADIS_XRDS_LOCATION.equalsIgnoreCase(httpEquiv))
            {
                if (xrdsLocation != null)
                    throw new YadisException(
                            "More than one "
                                    + YadisResolver.YADIS_XRDS_LOCATION
                                    + " META tags found in HEAD: "
                                    + head,
                            OpenIDException.YADIS_HTMLMETA_INVALID_RESPONSE);

                xrdsLocation = metaElement.hasAttr("content")
                        ? metaElement.attr("content") : null;
                if (DEBUG)
                    _log.debug("Found " + YadisResolver.YADIS_XRDS_LOCATION
                            + " META tags.");
            }
        }

        return xrdsLocation;
    }
}
