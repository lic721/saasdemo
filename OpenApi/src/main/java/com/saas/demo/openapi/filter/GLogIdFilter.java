package com.saas.demo.openapi.filter;

import com.saas.demo.common.util.LogUtil;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * 跨域过滤器
 *
 * @author jb28755
 * @date 2017/11/13.
 */
public class GLogIdFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            LogUtil.setGLogId(UUID.randomUUID().toString().replace("-", ""));
        } catch (Exception ex) {
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }


}
