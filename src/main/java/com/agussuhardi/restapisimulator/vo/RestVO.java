package com.agussuhardi.restapisimulator.vo;

import com.agussuhardi.restapisimulator.config.ConvertUtil;
import com.google.common.base.Strings;
import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Data
public class RestVO implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  private String id;

  @NotNull private String name;

  @NotNull private String uri;

  @NotNull private HttpMethod method;

  @NotNull private String requestHeaders;

  @NotNull private String requestBody;

  @NotNull private String requestParams;

  @NotNull private String successResponseHeaders;

  @NotNull private String successResponseBody;

  @NotNull private String failResponseHeaders;

  @NotNull private String failResponseBody;

  @NotNull private int successResponseCode;

  @NotNull private int failResponseCode;

  @NotNull private Long responseInNanoSecond;

  public UUID getId() {
    if (!Strings.isNullOrEmpty(id)) return UUID.fromString(id);
    return null;
  }

  public HttpStatus getFailResponseCode() {
    return HttpStatus.valueOf(failResponseCode);
  }

  public HttpStatus getSuccessResponseCode() {
    return HttpStatus.valueOf(successResponseCode);
  }

  public Map<String, String> getRequestHeaders() {
    return ConvertUtil.jsonToMapString(requestHeaders);
  }

  public Map<String, Object> getRequestBody() {
    return ConvertUtil.jsonToMap(requestBody);
  }

  public Map<String, String[]> getRequestParams() {
    return ConvertUtil.jsonToMapArray(requestParams);
  }

  public Map<String, Object> getSuccessResponseHeaders() {
    return ConvertUtil.jsonToMap(successResponseHeaders);
  }

  public Map<String, Object> getSuccessResponseBody() {
    return ConvertUtil.jsonToMap(successResponseBody);
  }

  public Map<String, Object> getFailResponseHeaders() {
    return ConvertUtil.jsonToMap(failResponseHeaders);
  }

  public Map<String, Object> getFailResponseBody() {
    return ConvertUtil.jsonToMap(failResponseBody);
  }

  public String getUri() {
    if (Strings.isNullOrEmpty(this.uri)) return uri;
    return uri.trim();
  }
}
