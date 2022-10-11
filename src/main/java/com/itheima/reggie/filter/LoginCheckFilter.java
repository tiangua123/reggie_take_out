package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest servletRequest1 = (HttpServletRequest) servletRequest;
        HttpServletResponse servletResponse1 = (HttpServletResponse) servletResponse;

        String requestURI = servletRequest1.getRequestURI();
        log.info("本次请求的地址是{}",requestURI);
        String[] uris=new String[]{
                "/backend/**",
                "/front/**",
                "/employee/login",
                "/employee/logout",
                "/common/**",
                "/user/**"
        };

        boolean check = check(uris, requestURI);
        if(check){
            log.info("本次请求不需要处理{}",requestURI);
            filterChain.doFilter(servletRequest1,servletResponse1);
            return;
        }
        if(servletRequest1.getSession().getAttribute("employee")!=null){
            log.info("用户已登录，id为{}",servletRequest1.getSession().getAttribute("employee"));

            Long empid=(Long)servletRequest1.getSession().getAttribute("employee");

            BaseContext.setCurrentId(empid);
            filterChain.doFilter(servletRequest1,servletResponse1);
            return;
        }

        if(servletRequest1.getSession().getAttribute("user")!=null){
            log.info("用户已登录，id为{}",servletRequest1.getSession().getAttribute("user"));

            Long userid=(Long)servletRequest1.getSession().getAttribute("user");

            BaseContext.setCurrentId(userid);
            filterChain.doFilter(servletRequest1,servletResponse1);
            return;
        }

        servletResponse1.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        log.info("用户未登录");
        return;
    }

    public boolean check(String[] uris,String requestURI){
        for (String s :uris) {
            if(PATH_MATCHER.match(s,requestURI)){
                return true;
            }
        }
        return false;
    }
}
