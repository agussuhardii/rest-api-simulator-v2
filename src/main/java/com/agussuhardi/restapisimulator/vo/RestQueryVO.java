package com.agussuhardi.restapisimulator.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RestQueryVO implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  private String method;
  private String uri;
}
