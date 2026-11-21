/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.discovery.yadis;

import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.openid4java.discovery.html.JsoupHtmlParserTest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Sutra Zhou
 */
public class JsoupYadisHtmlParserTest extends TestCase
{
    private JsoupYadisHtmlParser parser;

    protected void setUp() throws Exception
    {
        super.setUp();
        parser = new JsoupYadisHtmlParser();
    }

    public final void testGetHtmlMetaIssue83() throws IOException, YadisException
    {
        String htmlData = readResource("issue83.html");
        String s = parser.getHtmlMeta(htmlData);
        assertEquals("http://edevil.livejournal.com/data/yadis", s);
    }

    public void testParseHtmlMetaXXE() throws Exception
    {
        try (InputStream input = JsoupHtmlParserTest.class.getResourceAsStream(
                "identityPageWithExternalEntityReference.html"))
        {
            parser.getHtmlMeta(IOUtils.toString(input, StandardCharsets.UTF_8));
        }
        // don't fail trying to read "/path/to/some/file" from the input data
    }

    private static String readResource(String name) throws IOException
    {
        try (InputStream input = JsoupYadisHtmlParserTest.class.getResourceAsStream(name))
        {
            return IOUtils.toString(input, StandardCharsets.UTF_8);
        }
    }
}
