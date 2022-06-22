package com.agussuhardi.restapisimulator.web.controller;

import com.agussuhardi.restapisimulator.config.ApiResponse;
import com.agussuhardi.restapisimulator.service.impl.LogsService;
import com.agussuhardi.restapisimulator.service.impl.RestService;
import com.agussuhardi.restapisimulator.vo.LogsQueryVO;
import com.agussuhardi.restapisimulator.vo.RestQueryVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/r")
@AllArgsConstructor
@Slf4j
public class RestWebController {

  private final RestService restService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> list(RestQueryVO vo, Pageable pageable) {
    return new ApiResponse<>(HttpStatus.OK, restService.query(vo, pageable)).entity();
  }
}
