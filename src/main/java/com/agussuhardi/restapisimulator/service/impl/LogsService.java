package com.agussuhardi.restapisimulator.service.impl;

import com.agussuhardi.restapisimulator.entity.Logs;
import com.agussuhardi.restapisimulator.repository.LogsRepository;
import com.agussuhardi.restapisimulator.vo.LogsQueryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogsService {

  private final LogsRepository logsRepository;

  public Page<Logs> query(LogsQueryVO vo, Pageable pageable) {
    log.info("request=>{}", vo);
    return logsRepository.findAllBy(vo.getMethod(), vo.getUri(), pageable);
  }
}
