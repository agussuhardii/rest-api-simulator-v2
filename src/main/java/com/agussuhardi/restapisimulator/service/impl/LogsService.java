package com.agussuhardi.restapisimulator.service.impl;

import com.agussuhardi.restapisimulator.entity.Logs;
import com.agussuhardi.restapisimulator.repository.LogsRepository;
import com.agussuhardi.restapisimulator.vo.LogsQueryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogsService {

  private final LogsRepository logsRepository;

  public Page<Logs> query(LogsQueryVO vO, Pageable pageable) {
//    var method = HttpMethod.resolve(vO.getMethod());
//    System.out.println(method);
    return logsRepository.findAllByMethodAndUri(HttpMethod.GET, "vO.getUri()", pageable);
  }
}
