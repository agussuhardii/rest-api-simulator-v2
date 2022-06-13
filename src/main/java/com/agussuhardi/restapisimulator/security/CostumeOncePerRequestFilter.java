package com.agussuhardi.restapisimulator.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class CostumeOncePerRequestFilter extends OncePerRequestFilter {

  private final String url = "hello";

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {

    String bodyResponse;

    var requestWrapper = new CustomHttpRequestWrapper(httpServletRequest);
    Map<String, Object> body = new HashMap<>();
    if (Arrays.asList("POST", "PUT").contains(httpServletRequest.getMethod())) {
      body = requestWrapper.getBody();
    }

    //    modify response
    ContentCachingResponseWrapper responseCacheWrapperObject =
        new ContentCachingResponseWrapper(httpServletResponse);


    byte[] responseArray = responseCacheWrapperObject.getContentAsByteArray();
    bodyResponse = new String(responseArray, responseCacheWrapperObject.getCharacterEncoding());

    var map = ConvertUtil.jsonToMap(bodyResponse);
    map.put("timestamp", System.currentTimeMillis());

    bodyResponse = ConvertUtil.objectToJson(map);
    responseCacheWrapperObject.resetBuffer();
    responseCacheWrapperObject.setContentLength(bodyResponse.length());
    responseCacheWrapperObject.getWriter().write(bodyResponse);


    log.info("response=>{}", map);
    responseCacheWrapperObject.copyBodyToResponse();

    filterChain.doFilter(requestWrapper, responseCacheWrapperObject);
  }
}
