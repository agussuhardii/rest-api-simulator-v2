package com.agussuhardi.restapisimulator.config;

import com.agussuhardi.restapisimulator.entity.Logs;
import com.agussuhardi.restapisimulator.entity.Rest;
import com.agussuhardi.restapisimulator.repository.LogsRepository;
import com.agussuhardi.restapisimulator.repository.RestRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
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

  private final LogsRepository logsRepository;

  @SneakyThrows
  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain) {

    var requestWrapper = new CustomHttpRequestWrapper(httpServletRequest);
    var responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);

    var requestMethod = HttpMethod.resolve(httpServletRequest.getMethod());
    var requestURI = requestWrapper.getRequestURI();

    //    headers
    Map<String, String> request_headers = new HashMap<>();
    var headerNames = Collections.list(requestWrapper.getHeaderNames());
    for (var headerName : headerNames) {
      request_headers.put(headerName, requestWrapper.getHeader(headerName));
    }

    var logs =
        Logs.builder()
            .uri(requestURI)
            .method(requestMethod)
            .headers(request_headers)
            .params(requestWrapper.getParameterMap())
            .build();
    if (Arrays.asList(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH).contains(requestMethod)) {
      var requestBody = requestWrapper.getJsonBody();
      if (requestBody != null) logs.setBody(ConvertUtil.jsonToMap(requestBody));
    }

    logsRepository.save(logs);

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
    Thread.sleep(rest.getResponseInNanoSecond());

    //  1.  validate headers
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
        this.failResponse(requestWrapper, responseWrapper, filterChain, rest);
        log.error("Invalid headers");
        return;
      }
    }

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
        this.failResponse(requestWrapper, responseWrapper, filterChain, rest);
        log.error("invalid params");
        return;
      }
    }

    // 3.   validate body
    if (Arrays.asList(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH).contains(requestMethod)) {
      var requestBody = requestWrapper.getJsonBody();
      if (!rest.getRequestBody().equals(ConvertUtil.jsonToMap(requestBody))) {
        this.failResponse(requestWrapper, responseWrapper, filterChain, rest);
        log.error("invalid body");
        return;
      }
    }

    responseWrapper.setStatus(rest.getSuccessResponseCode());
    responseWrapper.setContentType(APPLICATION_JSON_VALUE);
    responseWrapper.getWriter().write(ConvertUtil.mapToJson(rest.getSuccessResponseBody()));
    responseWrapper.copyBodyToResponse();

    filterChain.doFilter(requestWrapper, responseWrapper);
  }

  private void failResponse(
      CustomHttpRequestWrapper requestWrapper,
      ContentCachingResponseWrapper responseWrapper,
      FilterChain filterChain,
      Rest rest)
      throws IOException, ServletException {
    responseWrapper.setStatus(rest.getFailResponseCode());
    responseWrapper.setContentType(APPLICATION_JSON_VALUE);
    responseWrapper.getWriter().write(ConvertUtil.mapToJson(rest.getFailResponseBody()));

    for (var header : rest.getRequestHeaders().entrySet()) {
      responseWrapper.addHeader(header.getKey(), header.getValue());
    }

    responseWrapper.copyBodyToResponse();
    filterChain.doFilter(requestWrapper, responseWrapper);
  }
}
