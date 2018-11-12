package com.vanderlei.cfp.config.security.cors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class CorsFilter implements Filter {

  @Value("${origin-permitida}")
  private String originPermitida;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    response.setHeader("Access-Control-Allow-Origin", originPermitida);
    response.setHeader("Access-Control-Allow-Credentials", "true");
    log.info("Origem configurada: " + originPermitida);
    log.info("Origem request: " + request.getHeader("Origin"));
    if ("OPTIONS".equals(request.getMethod())
        && originPermitida.equals(request.getHeader("Origin"))) {
      response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
      response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
      response.setHeader("Access-Control-Max-Age", "3600");

      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  @Override
  public void destroy() {}
}
