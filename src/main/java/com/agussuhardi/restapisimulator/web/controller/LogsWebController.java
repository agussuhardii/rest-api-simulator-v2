package com.agussuhardi.restapisimulator.web.controller;

import com.agussuhardi.restapisimulator.config.ApiResponse;
import com.agussuhardi.restapisimulator.service.impl.LogsService;
import com.agussuhardi.restapisimulator.vo.LogsQueryVO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/l")
@AllArgsConstructor
public class LogsWebController {

  private final LogsService logsService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> list(LogsQueryVO request, Pageable pageable) {
    return new ApiResponse<>(HttpStatus.OK, logsService.query(request, pageable)).entity();
  }


}
