package com.agussuhardi.restapisimulator.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Configuration
@Slf4j
public class CostumeOncePerRequestFilter extends OncePerRequestFilter {

  private final String url = "/hello";

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {

    String bodyResponse;

    //    var requestWrapper = new CustomHttpRequestWrapper(httpServletRequest);
    //    Map<String, Object> body = new HashMap<>();
    //    if (Arrays.asList("POST", "PUT").contains(httpServletRequest.getMethod())) {
    //      body = requestWrapper.getBody();
    //    }

    //    get request
    var requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);

    var responseBody = new HashMap<>();
    responseBody.put("url", requestWrapper.getRequestURL());
    String s = requestWrapper.getRequestURI();
    System.out.println(s);
    if (requestWrapper.getRequestURI().equals(url)) {
      responseBody.put("message", "kamu sedang request hello");
    }

    //    modify response
    ContentCachingResponseWrapper responseWrapper =
        new ContentCachingResponseWrapper(httpServletResponse);

    byte[] responseArray = responseWrapper.getContentAsByteArray();
    bodyResponse = new String(responseArray, responseWrapper.getCharacterEncoding());

    //        var map = ConvertUtil.jsonToMap(bodyResponse);
    //    map.put("timestamp", System.currentTimeMillis());
    bodyResponse = ConvertUtil.objectToJson(responseBody);
    responseWrapper.resetBuffer();
    responseWrapper.setContentLength(bodyResponse.length());
    responseWrapper.getWriter().write(bodyResponse);

    //    log.info("response=>{}", map);
    responseWrapper.copyBodyToResponse();

    filterChain.doFilter(requestWrapper, responseWrapper);
  }
}
