package com.agussuhardi.restapisimulator.service.impl;

import com.agussuhardi.restapisimulator.config.exception.CustomException;
import com.agussuhardi.restapisimulator.entity.Rest;
import com.agussuhardi.restapisimulator.repository.RestRepository;
import com.agussuhardi.restapisimulator.vo.RestQueryVO;
import com.agussuhardi.restapisimulator.vo.RestVO;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestService {

  private final RestRepository restRepository;

  public Page<Rest> query(RestQueryVO vo, Pageable pageable) {
    return restRepository.findAllBy(vo.getMethod(), vo.getUri(), pageable);
  }

  public Rest save(RestVO vo) {
    if (vo.getId() == null) {
      var optional = restRepository.getUrl(vo.getMethod(), vo.getUri());
      if (optional.isPresent()) throw new CustomException("Duplicate url", HttpStatus.BAD_REQUEST);
    }
    Rest rest = new Rest();
    BeanUtils.copyProperties(vo, rest);
    return restRepository.save(rest);
  }

  public Rest getById(String id) {
    return restRepository
        .findById(UUID.fromString(id))
        .orElseThrow(() -> new CustomException("Resource not found", HttpStatus.NOT_FOUND));
  }
}
