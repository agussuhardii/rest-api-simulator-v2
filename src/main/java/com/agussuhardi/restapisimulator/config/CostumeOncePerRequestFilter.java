package com.agussuhardi.restapisimulator.config;

import com.agussuhardi.restapisimulator.entity.Logs;
import com.agussuhardi.restapisimulator.entity.Rest;
import com.agussuhardi.restapisimulator.repository.LogsRepository;
import com.agussuhardi.restapisimulator.repository.RestRepository;
import com.github.javafaker.Faker;
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
import java.util.*;

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

    if (httpServletRequest.getRequestURI().equals("/") // dashboard
        || httpServletRequest.getRequestURI().equals("/l") // list logs api
        || httpServletRequest.getRequestURI().equals("/l/v") // get logs api
        || httpServletRequest.getRequestURI().equals("/r") // list rest api
        || httpServletRequest.getRequestURI().equals("/r/a") // add form rest
        || httpServletRequest.getRequestURI().equals("/favicon.ico") // default
    ) {
      filterChain.doFilter(httpServletRequest, httpServletResponse);
      return;
    }

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
            .body(new HashMap<>())
            .responseCode(0)
            .build();
    if (Arrays.asList(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH).contains(requestMethod)) {
      var requestBody = requestWrapper.getJsonBody();
      if (requestBody != null) logs.setBody(ConvertUtil.jsonToMap(requestBody));
    }

    logs = logsRepository.save(logs);

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
      logs.setResponseCode(HttpStatus.NOT_FOUND.value());
      logsRepository.save(logs);
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
        this.failResponse(requestWrapper, responseWrapper, filterChain, rest, logs);
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
        this.failResponse(requestWrapper, responseWrapper, filterChain, rest, logs);
        log.error("invalid params");
        return;
      }
    }

    // 3.   validate body
    if (!rest.getRequestBody().isEmpty())
      if (Arrays.asList(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH)
          .contains(requestMethod)) {
        var requestBody = requestWrapper.getJsonBody();
        if (!rest.getRequestBody().equals(ConvertUtil.jsonToMap(requestBody))) {
          this.failResponse(requestWrapper, responseWrapper, filterChain, rest, logs);
          log.error("invalid body");
          return;
        }
      }

    logs.setResponseCode(responseWrapper.getStatus());
    logsRepository.save(logs);

    responseWrapper.setStatus(rest.getSuccessResponseCode().value());
    responseWrapper.setContentType(APPLICATION_JSON_VALUE);

    var res = rest.getSuccessResponseBody();
    for (var map : res.entrySet()) {
      Faker faker = new Faker(new Locale("in-ID"));
      if (map.getValue() == null) break;
      if (map.getValue().getClass().equals(String.class)) {
        var firstTwo = firstTwo(map.getValue().toString());
        if (firstTwo.equals("**")) {
          map.setValue(faker.number().digits(16));
        }
      }
    }

    responseWrapper.getWriter().write(ConvertUtil.mapToJson(rest.getSuccessResponseBody()));
    responseWrapper.copyBodyToResponse();

    filterChain.doFilter(requestWrapper, responseWrapper);
  }

  public String firstTwo(String str) {
    return str.length() < 2 ? str : str.substring(0, 2);
  }

  private void failResponse(
      CustomHttpRequestWrapper requestWrapper,
      ContentCachingResponseWrapper responseWrapper,
      FilterChain filterChain,
      Rest rest,
      Logs logs)
      throws IOException, ServletException {
    responseWrapper.setStatus(rest.getFailResponseCode().value());
    responseWrapper.setContentType(APPLICATION_JSON_VALUE);
    responseWrapper.getWriter().write(ConvertUtil.mapToJson(rest.getFailResponseBody()));

    for (var header : rest.getRequestHeaders().entrySet()) {
      responseWrapper.addHeader(header.getKey(), header.getValue());
    }

    logs.setResponseCode(responseWrapper.getStatus());
    logsRepository.save(logs);

    responseWrapper.copyBodyToResponse();
    filterChain.doFilter(requestWrapper, responseWrapper);
  }
}
