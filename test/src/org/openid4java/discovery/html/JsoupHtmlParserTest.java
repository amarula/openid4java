/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.discovery.html;

import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.openid4java.discovery.DiscoveryException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Sutra Zhou
 */
public class JsoupHtmlParserTest extends TestCase
{
    private JsoupHtmlParser parser;

    protected void setUp() throws Exception
    {
        super.setUp();
        parser = new JsoupHtmlParser();
    }

    public void testParseHtml() throws IOException, DiscoveryException
    {
        String htmlData = readResource("identityPage.html");
        HtmlResult result = new HtmlResult();
        parser.parseHtml(htmlData, result);
        assertEquals("http://www.example.com:8080/openidserver/users/myusername",
                result.getDelegate1());
        assertEquals("http://www.example.com:8080/openidserver/openid.server",
                result.getOP1Endpoint().toExternalForm());
    }

    public void testParseHtmlWithXmlNamespace() throws IOException,
            DiscoveryException
    {
        String htmlData = readResource("identityPage-with-xml-namespace.html");
        HtmlResult result = new HtmlResult();
        parser.parseHtml(htmlData, result);
        assertEquals("http://www.example.com:8080/openidserver/users/myusername",
                result.getDelegate1());
        assertEquals("http://www.example.com:8080/openidserver/openid.server",
                result.getOP1Endpoint().toExternalForm());
    }

    public void testParseHtmlXXE() throws Exception
    {
        String htmlData = readResource("identityPageWithExternalEntityReference.html");
        parser.parseHtml(htmlData, new HtmlResult());
        // don't fail trying to read "/path/to/some/file" from the input data
    }

    private static String readResource(String name) throws IOException
    {
        try (InputStream input = JsoupHtmlParserTest.class.getResourceAsStream(name))
        {
            return IOUtils.toString(input, StandardCharsets.UTF_8);
        }
    }
}
