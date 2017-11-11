package com.troymaxventures.tapestry.gaeutils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;

/**
 * A filter to patch up GAE issue https://code.google.com/p/googleappengine/issues/detail?id=8201 
 * which prevents Tapestry applications working in the GAE dev app server in versions after 1.7.1
 * 
 * The filter wraps up the original response and only responds in a matter to appease the 
 * org.apache.tapestry5.internal.services.ImmediateActionRenderResponseFilter from throwing up when GAE dev server
 * handles a form submission
 * 
 * @author Alex Kochnev <akochnev@troymaxventures.com>
 */
public class RedirectFixRequestFilter implements RequestFilter {

    @Override
    public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
        return handler.service(request, new RedirectFixerResponse(response));
    }

    class RedirectFixerResponse implements Response {

        private Response origResponse;
        private boolean redirectCalled;

        RedirectFixerResponse(Response origResponse) {
            this.origResponse = origResponse;
            this.redirectCalled = false;
        }

        @Override
        public PrintWriter getPrintWriter(String contentType) throws IOException {
            return origResponse.getPrintWriter(contentType);
        }

        @Override
        public OutputStream getOutputStream(String contentType) throws IOException {
            return origResponse.getOutputStream(contentType);
        }

        @Override
        public void sendRedirect(String URL) throws IOException {
            this.redirectCalled = true;
            origResponse.sendRedirect(URL);
        }

        @Override
        public void sendRedirect(Link link) throws IOException {
            this.redirectCalled = true;
            origResponse.sendRedirect(link);
        }

        @Override
        public void setStatus(int sc) {
            origResponse.setStatus(sc);
        }

        @Override
        public void sendError(int sc, String message) throws IOException {
            origResponse.sendError(sc, message);
        }

        @Override
        public void setContentLength(int length) {
            origResponse.setContentLength(length);
        }

        @Override
        public void setDateHeader(String name, long date) {
            origResponse.setDateHeader(name, date);
        }

        @Override
        public void setHeader(String name, String value) {
            origResponse.setHeader(name, value);
        }

        @Override
        public void setIntHeader(String name, int value) {
            origResponse.setIntHeader(name, value);
        }

        @Override
        public String encodeURL(String URL) {
            return origResponse.encodeURL(URL);
        }

        @Override
        public String encodeRedirectURL(String URL) {
            return origResponse.encodeRedirectURL(URL);
        }

        @Override
        public boolean isCommitted() {
            return this.redirectCalled || origResponse.isCommitted();
        }

        @Override
        public void disableCompression() {
            origResponse.disableCompression();
        }
    }
}