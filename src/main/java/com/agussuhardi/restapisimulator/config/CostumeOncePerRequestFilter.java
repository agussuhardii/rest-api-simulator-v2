package com.agussuhardi.restapisimulator.config;

import com.agussuhardi.restapisimulator.repository.RestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class CostumeOncePerRequestFilter extends OncePerRequestFilter {

  private final RestRepository restRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {

    // request
    var requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
    var responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);

    var requestMethod = HttpMethod.resolve(httpServletRequest.getMethod());
    var requestURI = requestWrapper.getRequestURI();
    var optionalRest = restRepository.getUrl(requestMethod, requestURI);
    if (optionalRest.isEmpty()) {

      responseWrapper.setStatus(HttpStatus.NOT_FOUND.value());
      responseWrapper.setContentType(APPLICATION_JSON_VALUE);
      responseWrapper
          .getWriter()
          .write(DefaultResponse.urlNotFound(requestMethod, requestURI, HttpStatus.NOT_FOUND));

      responseWrapper.copyBodyToResponse();
      filterChain.doFilter(requestWrapper, responseWrapper);
      return;
    }

    var requestParams = requestWrapper.getParameterMap();
    Map<String, Object> requestBody = new HashMap<>();

    if (Arrays.asList(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH).contains(requestMethod)) {
      var requestBodyString =
          new String(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
      requestBody = ConvertUtil.jsonToMap(requestBodyString);
    }

    String bodyResponse;

    //    var requestWrapper = new CustomHttpRequestWrapper(httpServletRequest);
    //    Map<String, Object> body = new HashMap<>();
    //    if (Arrays.asList("POST", "PUT").contains(httpServletRequest.getMethod())) {
    //      body = requestWrapper.getBody();
    //    }

    //    get request

    var responseBody = new HashMap<>();
    responseBody.put("url", requestWrapper.getRequestURL());
    String s = requestWrapper.getRequestURI();
    //    System.out.println(s);
    //    if (requestWrapper.getRequestURI().equals(url)) {
    //      responseBody.put("message", "kamu sedang request hello");
    //    }

    //    modify response
    //    ContentCachingResponseWrapper responseWrapper =
    //        new ContentCachingResponseWrapper(httpServletResponse);

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
