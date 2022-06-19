package com.agussuhardi.restapisimulator.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.StringJoiner;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ApiResponse<T> {

  private final long timestamp = System.currentTimeMillis();
  private int status;
  private String error;
  private String message;
  private T data;

  public ApiResponse(HttpStatus httpStatus, T data) {
    this.status = httpStatus.value();
    this.data = data;
  }

  public ResponseEntity<?> entity() {
    return ResponseEntity.status(status).headers(HttpHeaders.EMPTY).body(this);
  }
}
