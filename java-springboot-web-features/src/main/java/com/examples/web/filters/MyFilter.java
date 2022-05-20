package com.examples.web.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Component//将此Filter交给Spring容器管理
//@ServletComponentScan //这里不要再扫描并注册servlet组件，会造成filter注册报错（在Application里扫描就可以了）
@WebFilter(urlPatterns = {"/v2/*"}, filterName = "v2_filter")
@Slf4j//定义之后不用每次都写private  final Logger log = LoggerFactory.getLogger(XXX.class);
@Order(0)//指定过滤器的执行顺序,值越大越靠后执行
public class MyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //response.getWriter().write(String.format("filter blocked %s 3\n", new Object[]{this.demo1.getName()}));
        log.debug("123");

        chain.doFilter(request, response);
    }
}
