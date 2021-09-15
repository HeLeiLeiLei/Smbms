package com.hl.filter;

import com.hl.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class sysFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest servletRequest= (HttpServletRequest) req;
        HttpServletResponse servletResponse = (HttpServletResponse) resp;
        Object attribute = servletRequest.getSession().getAttribute(Constants.USER_SESSION);
        if(attribute == null){
            servletResponse.sendRedirect(((HttpServletRequest) req).getContextPath()+"/login.jsp");
        }
        filterChain.doFilter(req,resp);
    }

    public void destroy() {

    }
}
