/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.samples;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class HttpServletSupport extends HttpServlet
{
    protected static final long serialVersionUID = 1L;
    protected static Exception lastException;

    protected static int count_;
    protected Log logger_;

    public HttpServletSupport()
    {
        logger_ = LogFactory.getLog(getClass());
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        count_++;
        String name = getClass().getName();
        name = name.substring(name.lastIndexOf('.') + 1);
        String context = name + " call-" + count_ + " : ";

        logger_.info(context + "begin onService");
        try
        {
            onService(req, resp);
        }
        catch (Exception exc)
        {
            lastException = exc;
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        finally
        {
            logger_.info(context + "end onService");
        }
    }

    protected abstract void onService(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
