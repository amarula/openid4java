/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Small HTML helpers shared by the HTML discovery parsers.
 */
public final class HtmlUtils
{
    private static final Pattern NON_MARKUP = Pattern.compile(
            "<!--.*?-->|<script\\b[^>]*>.*?</script\\s*>|<style\\b[^>]*>.*?</style\\s*>",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private static final Pattern HEAD_BODY_TAG = Pattern.compile(
            "<\\s*(/?)\\s*(head|body)\\b[^>]*>",
            Pattern.CASE_INSENSITIVE);

    private HtmlUtils()
    {
    }

    /**
     * Counts HEAD start tags that are not inside a BODY element.
     *
     * HTML parsers normalize malformed documents, so the original markup is
     * checked to preserve the "exactly one HEAD" requirement.
     *
     * @param html the HTML source
     * @return the number of top-level HEAD start tags
     */
    public static int countTopLevelHeads(String html)
    {
        if (html == null || html.length() == 0)
        {
            return 0;
        }

        String markup = NON_MARKUP.matcher(html).replaceAll("");
        Matcher matcher = HEAD_BODY_TAG.matcher(markup);
        boolean inBody = false;
        int heads = 0;

        while (matcher.find())
        {
            boolean closing = matcher.group(1).length() > 0;
            String tag = matcher.group(2).toLowerCase(Locale.ROOT);

            if ("body".equals(tag))
            {
                inBody = !closing;
            }
            else if (!closing && !inBody)
            {
                heads++;
            }
        }

        return heads;
    }
}
