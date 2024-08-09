package com.aeonbank.library.filter;

import com.aeonbank.library.common.MdcUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MdcFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        MdcUtil.setTraceId();
        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            MdcUtil.clearTraceId();
        }
    }

}
