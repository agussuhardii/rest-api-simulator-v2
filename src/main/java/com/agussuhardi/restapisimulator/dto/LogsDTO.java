package com.agussuhardi.restapisimulator.dto;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LogsDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;

    private String uri;

    private String method;

    private String headers;

    private String params;

    private String body;

    private BigDecimal createdAt;

    private BigDecimal updatedAt;

}
