package com.chaos.web.filter;

import com.alibaba.fastjson2.JSON;
import com.chaos.common.vo.AjaxResult;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * XSS过滤器
 */
@Slf4j
@Component
public class XssFilter implements Filter, Ordered {

    @Value("${xss.enabled:true}")
    private Boolean enabled;

    @Value("${xss.excludes:}")
    private String excludes;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    // XSS检测正则
    private static final Pattern[] XSS_PATTERNS = {
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onerror(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onclick(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 检查是否启用
        if (!enabled) {
            chain.doFilter(request, response);
            return;
        }

        // 检查是否在排除列表中
        String requestURI = httpRequest.getRequestURI();
        if (isExcluded(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        // 检查请求参数是否包含XSS内容
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(httpRequest);
        if (hasXssContent(xssRequest)) {
            log.warn("检测到XSS攻击: {}", requestURI);
            httpResponse.setContentType("application/json;charset=UTF-8");
            PrintWriter out = httpResponse.getWriter();
            out.write(JSON.toJSONString(AjaxResult.fail(500, "请求中包含非法字符")));
            out.flush();
            return;
        }

        chain.doFilter(xssRequest, response);
    }

    /**
     * 检查是否在排除列表中
     */
    private boolean isExcluded(String requestURI) {
        if (excludes == null || excludes.isEmpty()) {
            return false;
        }
        List<String> excludeUrls = Arrays.asList(excludes.split(","));
        for (String pattern : excludeUrls) {
            if (antPathMatcher.match(pattern.trim(), requestURI)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否包含XSS内容
     */
    private boolean hasXssContent(HttpServletRequest request) {
        // 检查请求参数
        var parameterMap = request.getParameterMap();
        for (String[] values : parameterMap.values()) {
            for (String value : values) {
                if (containsXss(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检测字符串是否包含XSS
     */
    private boolean containsXss(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        for (Pattern pattern : XSS_PATTERNS) {
            if (pattern.matcher(value).find()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    /**
     * XSS 请求包装器
     */
    private static class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

        public XssHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            return cleanXss(value);
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) {
                return null;
            }
            int count = values.length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++) {
                encodedValues[i] = cleanXss(values[i]);
            }
            return encodedValues;
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            return cleanXss(value);
        }

        /**
         * 清理XSS内容
         */
        private String cleanXss(String value) {
            if (value == null || value.isEmpty()) {
                return value;
            }
            // HTML转义
            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
            value = value.replaceAll("'", "&#39;");
            value = value.replaceAll("\"", "&quot;");
            return value;
        }
    }
}
