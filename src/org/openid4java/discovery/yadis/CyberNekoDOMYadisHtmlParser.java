/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.discovery.yadis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.html.dom.HTMLDocumentImpl;
import org.openid4java.OpenIDException;
import org.openid4java.util.OpenID4JavaDOMParser;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLMetaElement;

import javax.xml.transform.TransformerException;

/**
 * A {@link org.openid4java.discovery.yadis.YadisHtmlParser} implementation using the DOMParser of CyberNeko HTML.
 * 
 * @author Sutra Zhou
 * @since 0.9.4
 * @see org.openid4java.util.OpenID4JavaDOMParser
 */
public class CyberNekoDOMYadisHtmlParser implements YadisHtmlParser
{
    private static final Log _log = LogFactory.getLog(CyberNekoDOMYadisHtmlParser.class);
    private static final boolean DEBUG = _log.isDebugEnabled();

    /*
     * (non-Javadoc)
     * 
     * @see org.openid4java.discovery.yadis.YadisParser#getHtmlMeta(java.lang.String)
     */
    public String getHtmlMeta(String input) throws YadisException
    {
        String xrdsLocation = null;

        HTMLDocumentImpl doc = this.parseDocument(input);
        if (DEBUG)
        {
            try
            {
                _log.debug("document:\n" + OpenID4JavaDOMParser.toXmlString(doc));
            } catch (TransformerException e)
            {
                _log.debug("An exception occurs while transforming the document to string in debugging.", e);
            }
        }

        int headCount = countTopLevelHeads(doc);
        if (headCount != 1)
            throw new YadisException(
                    "HTML response must have exactly one HEAD element, "
                            + "found " + headCount,
                    OpenIDException.YADIS_HTMLMETA_INVALID_RESPONSE);

        HTMLHeadElement head = (HTMLHeadElement) doc.getHead();
        NodeList metaElements = head.getElementsByTagName("META");
        if (metaElements == null || metaElements.getLength() == 0)
        {
            if (DEBUG)
                _log.debug("No <meta> element found under <html><head>. " +
                "See Yadis specification, section 6.2.5/1.");
        }
        else
        {
            for (int i = 0, len = metaElements.getLength(); i < len; i++)
            {
                HTMLMetaElement metaElement = (HTMLMetaElement) metaElements.item(i);

                String httpEquiv = metaElement.getHttpEquiv();
                if (YadisResolver.YADIS_XRDS_LOCATION.equalsIgnoreCase(httpEquiv))
                {
                    if (xrdsLocation != null)
                        throw new YadisException(
                            "More than one "
                                + YadisResolver.YADIS_XRDS_LOCATION
                                + "META tags found in HEAD: "
                                + head.toString(),
                            OpenIDException.YADIS_HTMLMETA_INVALID_RESPONSE);

                    xrdsLocation = metaElement.getContent();
                    if (DEBUG)
                        _log.debug("Found " + YadisResolver.YADIS_XRDS_LOCATION
                            + " META tags.");
                }
            }
        }
        return xrdsLocation;
    }

    private static int countTopLevelHeads(HTMLDocumentImpl doc)
    {
        NodeList heads = doc.getElementsByTagName("head");
        int count = 0;
        for (int i = 0; i < heads.getLength(); i++)
        {
            if (!hasBodyAncestor(heads.item(i)))
                count++;
        }
        return count;
    }

    private static boolean hasBodyAncestor(org.w3c.dom.Node node)
    {
        for (org.w3c.dom.Node parent = node.getParentNode();
             parent != null; parent = parent.getParentNode())
        {
            if (parent.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE
                    && "body".equalsIgnoreCase(parent.getNodeName()))
                return true;
        }
        return false;
    }

    private HTMLDocumentImpl parseDocument(String htmlData) throws YadisException
    {
        OpenID4JavaDOMParser parser = new OpenID4JavaDOMParser(false);
        try
        {
            parser.parse(OpenID4JavaDOMParser.createInputSource(htmlData));
        }
        catch (Exception e)
        {
            throw new YadisException("Error parsing HTML message",
                    OpenIDException.YADIS_HTMLMETA_INVALID_RESPONSE, e);
        }

        if (parser.isIgnoredHeadStartElement())
        {
                throw new YadisException("HTML response must have exactly one HEAD element.",
                                OpenIDException.YADIS_HTMLMETA_INVALID_RESPONSE);
        }

        return (HTMLDocumentImpl) parser.getDocument();
    }
}
