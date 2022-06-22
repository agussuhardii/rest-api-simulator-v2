package com.agussuhardi.restapisimulator.config;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.StringJoiner;

public class DefaultResponse {

  public static String urlNotFound(HttpMethod method, String uri, HttpStatus httpStatus) {
    return new StringJoiner(", ", "{", "}")
        .add("\"method\": \"" + method + "\"")
        .add("\"uri\": \"" + uri + "\"")
        .add("\"http_status\": \"" + httpStatus + "\"")
        .toString();
  }
}
