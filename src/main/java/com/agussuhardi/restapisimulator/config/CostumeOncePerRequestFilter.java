package com.agussuhardi.restapisimulator.config;

import com.agussuhardi.restapisimulator.repository.RestRepository;
import jdk.swing.interop.SwingInterOpUtils;
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
import java.util.Collections;
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

    log.info("request =>{URI=>{}, Method=>{}}", requestURI, requestMethod);
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
    var rest = optionalRest.get();

    //  1.  validate headers
    Map<String, String> request_headers = new HashMap<>();
    var headerNames = Collections.list(requestWrapper.getHeaderNames());
    for (var headerName : headerNames) {
      request_headers.put(headerName, requestWrapper.getHeader(headerName));
    }

    for (var restHeader : rest.getRequestHeaders().entrySet()) {
      var existHeader = false;
      for (var requestHeader : request_headers.entrySet()) {
        if (restHeader.getKey().equalsIgnoreCase(requestHeader.getKey())
            && restHeader.getValue().equalsIgnoreCase(requestHeader.getValue())) {
          existHeader = true;
          break;
        }
      }

      // fail response by header not valid
      if (!existHeader) {

        responseWrapper.setStatus(rest.getFailResponseCode());
        responseWrapper.setContentType(APPLICATION_JSON_VALUE);
        responseWrapper.getWriter().write(ConvertUtil.mapToJson(rest.getFailResponseBody()));

        for (var header : rest.getRequestHeaders().entrySet()) {
          responseWrapper.addHeader(header.getKey(), header.getValue());
        }

        responseWrapper.copyBodyToResponse();
        filterChain.doFilter(requestWrapper, responseWrapper);
        return;
      }
    }
    //    end validate headers

    // 2.   validate params
    for (var restParam : rest.getRequestParams().entrySet()) {
      var existParam = false;
      for (var requestParam : requestWrapper.getParameterMap().entrySet()) {
        if (restParam.getKey().equalsIgnoreCase(requestParam.getKey())
            && Arrays.equals(restParam.getValue(), requestParam.getValue())) {
          existParam = true;
          break;
        }
      }

      if (!existParam) {

        responseWrapper.setStatus(rest.getFailResponseCode());
        responseWrapper.setContentType(APPLICATION_JSON_VALUE);
        responseWrapper.getWriter().write(ConvertUtil.mapToJson(rest.getFailResponseBody()));

        for (var header : rest.getRequestHeaders().entrySet()) {
          responseWrapper.addHeader(header.getKey(), header.getValue());
        }

        responseWrapper.copyBodyToResponse();
        filterChain.doFilter(requestWrapper, responseWrapper);
        return;
      }
    }

    // 3.   validate body
    if (Arrays.asList(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH).contains(requestMethod)) {

      var requestBody =
          new String(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
      var restRequestBody = ConvertUtil.mapToJson(rest.getRequestBody());
     log.info("request=>{}", requestBody);
     log.info("rest=>{}", restRequestBody);

      if (!requestBody.equals(restRequestBody)) {
        responseWrapper.setStatus(rest.getFailResponseCode());
        responseWrapper.setContentType(APPLICATION_JSON_VALUE);
        responseWrapper.getWriter().write(ConvertUtil.mapToJson(rest.getFailResponseBody()));

        for (var header : rest.getRequestHeaders().entrySet()) {
          responseWrapper.addHeader(header.getKey(), header.getValue());
        }

        responseWrapper.copyBodyToResponse();
        filterChain.doFilter(requestWrapper, responseWrapper);
        return;
      }
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
