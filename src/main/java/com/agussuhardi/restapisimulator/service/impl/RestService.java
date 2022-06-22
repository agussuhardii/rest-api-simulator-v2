package com.agussuhardi.restapisimulator.service.impl;

import com.agussuhardi.restapisimulator.entity.Rest;
import com.agussuhardi.restapisimulator.repository.RestRepository;
import com.agussuhardi.restapisimulator.vo.RestQueryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestService {

  private final RestRepository restRepository;

  public Page<Rest> query(RestQueryVO vo, Pageable pageable) {
    log.info(vo.getMethod());
    log.info(vo.getUri());
    return restRepository.findAllBy(vo.getMethod(), vo.getUri(), pageable);
  }
}
