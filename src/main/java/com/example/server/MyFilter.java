package com.example.server;

import static com.example.shared.Constants.CID_HEADER;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {

        System.out.println("myFilter called");

        final ServletRequest modRequest;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpReq = (HttpServletRequest) request;
            String cid = httpReq.getHeader(CID_HEADER);
            if (cid != null) {
                modRequest = new MyHttpServletRequest(httpReq, cid);
            } else {
                modRequest = request;
            }
        } else {
            modRequest = request;
        }
        chain.doFilter(modRequest, response);
    }

    @Override
    public void destroy() {
    }
}

class MyHttpServletRequest extends HttpServletRequestWrapper {

    private static final String CID_PARAM = "cid";

    private final String cid;

    public MyHttpServletRequest(HttpServletRequest request, String cid) {
        super(request);

        if (cid == null || cid.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.cid = cid;
    }

    @Override
    public String getParameter(String name) {
        if (CID_PARAM.equals(name)) {
            System.out.println("return cid: " + cid);
            return cid;
        }
        return super.getParameter(name);
    }
}
